package com.app.Hospital.Management.System.Services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.Hospital.Management.System.entities.User;
import com.app.Hospital.Management.System.repositories.UserRepository;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void updateUserPassword(Long userId, String currentPassword, String newPassword) {
        logger.info("Attempting to update password for User ID: {}", userId);
        
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            logger.error("User not found with ID: {}", userId);
            throw new NoSuchElementException("User not found");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            logger.warn("Incorrect current password attempt for User ID: {}", userId);
            throw new IllegalArgumentException("Incorrect current password");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
        
        logger.info("Password successfully updated for User ID: {}", userId);
    }
}
