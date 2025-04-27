package com.app.Hospital.Management.System.Controllers;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

@RestController
@RequestMapping("/api")
@Validated
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

    @Transactional
    @PostMapping("/register/")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new RegistrationResponse(null, "Duplicate entry for email"));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getId(), savedUser.getEmail());

        return ResponseEntity.ok(new RegistrationResponse(savedUser.getId(), token));
    }

    @PostMapping("/register/check")
    public ResponseEntity<RegistrationResponse> checkUser(@RequestBody User user) {
        User existingUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.status(401).body(new RegistrationResponse(null, "Invalid credentials"));
        }

        String token = jwtUtil.generateToken(existingUser.getId(), existingUser.getEmail());
        System.out.println(token);
        return ResponseEntity.ok(new RegistrationResponse(existingUser.getId(), token));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<HashMap<String, Object>> getPatientDetails(@PathVariable Long patientId) {
        PatientProfile patientProfile = patientProfileRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));

        User user = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("User not found for the given patient ID"));

        HashMap<String, Object> response = new HashMap<>();
        response.put("patientId", patientProfile.getPatientId());
        response.put("name", patientProfile.getName());
        response.put("dateOfBirth", patientProfile.getDateOfBirth());
        response.put("contactDetails", patientProfile.getContactDetails());
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{userId}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long userId, @Valid @RequestBody PasswordUpdateRequest request) {
        userService.updateUserPassword(userId, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok("Password updated successfully.");
    }
}
