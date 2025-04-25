package com.app.Hospital.Management.System.Controllers;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Hospital.Management.System.dto.RegistrationResponse;
import com.app.Hospital.Management.System.entities.PatientProfile;
import com.app.Hospital.Management.System.entities.User;
import com.app.Hospital.Management.System.repositories.PatientProfileRepository;
import com.app.Hospital.Management.System.repositories.UserRepository;
import com.app.Hospital.Management.System.utils.JwtUtil;
import com.app.Hospital.Management.System.dto.PasswordUpdateRequest;
import com.app.Hospital.Management.System.Services.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
@Validated
@RestController
@RequestMapping("/api")
public class UserController {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
    private PatientProfileRepository patientProfileRepository;
	@Autowired
	private UserService userService;

    @Autowired
    private UserRepository userRepository;
	 @Autowired
	 private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PatientProfileRepository patientRepo;
	@Transactional
	@PostMapping("/register/")
	public ResponseEntity<?>  register(@Valid @RequestBody User user)
	{ 
		if (userRepo.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Duplicate entry for the field email", HttpStatus.BAD_REQUEST);
        }
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User savedUser=userRepo.save(user);
		System.out.println(savedUser);
		String token=jwtUtil.generateToken(savedUser.getId(), savedUser.getEmail());
		
		RegistrationResponse response = new RegistrationResponse(savedUser.getId(), token);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
		
	}
	
	@PostMapping("/register/check")
	public ResponseEntity<?> checkUser(@RequestBody User user) {
	    Optional<User> foundUser = userRepo.findByEmail(user.getEmail());
	    if (foundUser.isPresent()) {
	    	User existingUser = foundUser.get();
	        
	        // Verify if the password matches (add password validation logic if necessary)
	    	if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
	            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
	        }

	        // Generate a JWT token
	        String token = jwtUtil.generateToken(existingUser.getId(), existingUser.getEmail());

	        // Return the userId and token
	        return ResponseEntity.ok(new RegistrationResponse(existingUser.getId(), token));
	}
	    return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
	}



	@GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPatientDetails(@PathVariable Long patientId) {
        // Fetch the PatientProfile
        Optional<PatientProfile> patientProfileOptional = patientProfileRepository.findById(patientId);

        if (patientProfileOptional.isPresent()) {
            PatientProfile patientProfile = patientProfileOptional.get();

            // Fetch the User associated with the PatientProfile
            Optional<User> userOptional = userRepository.findById(patientId); // Assuming patientId matches userId
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Combine PatientProfile and User data
                HashMap<String, Object> response = new HashMap<>();
                response.put("patientId", patientProfile.getPatientId());
                response.put("name", patientProfile.getName());
                response.put("dateOfBirth", patientProfile.getDateOfBirth());
                response.put("contactDetails", patientProfile.getContactDetails());
                response.put("email", user.getEmail()); // Add email from User entity
                response.put("password", user.getPassword()); 

				    return ResponseEntity.ok(response);
            } else {
                return new ResponseEntity<>("User not found for the given patient ID", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
    }

	@PutMapping("/users/{userId}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long userId, @Valid @RequestBody PasswordUpdateRequest request) {
        try {
            userService.updateUserPassword(userId, request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok("Password updated successfully");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update password", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	// @PutMapping("/{userId}/password")
    // public ResponseEntity<?> updatePassword(@PathVariable Long userId, @Valid @RequestBody PasswordUpdateRequest request) {
    //     try {
    //         userService.updateUserPassword(userId, request.getCurrentPassword(), request.getNewPassword());
    //         return ResponseEntity.ok("Password updated successfully");
    //     } catch (IllegalArgumentException e) {
    //         return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED); // Or another appropriate status
    //     } catch (NoSuchElementException e) {
    //         return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>("Failed to update password", HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }
	
	
}
