package com.app.Hospital.Management.System.Controllers;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Hospital.Management.System.dto.RegistrationResponse;
import com.app.Hospital.Management.System.entities.PatientProfile;
import com.app.Hospital.Management.System.entities.User;
import com.app.Hospital.Management.System.repositories.PatientProfileRepository;
import com.app.Hospital.Management.System.repositories.UserRepository;
import com.app.Hospital.Management.System.utils.JwtUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
@Validated
@RestController
@RequestMapping("/api/register")
public class UserController {
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	 @Autowired
	 private JwtUtil jwtUtil;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PatientProfileRepository patientRepo;
	@Transactional
	@PostMapping("/")
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
	
	@PostMapping("/check")
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
	
	
}
