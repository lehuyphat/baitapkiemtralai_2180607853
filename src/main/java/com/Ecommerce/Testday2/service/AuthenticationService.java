package com.Ecommerce.Testday2.service;

import com.Ecommerce.Testday2.exception.InvalidCredentialsException;
import com.Ecommerce.Testday2.exception.UserAlreadyExistsException;
import com.Ecommerce.Testday2.model.Role;
import com.Ecommerce.Testday2.model.User;
import com.Ecommerce.Testday2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void register(User request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("Tài khoản đã tồn tại");
        }
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(Role.USER);

        userRepository.save(newUser);
    }

    public void authenticate(User request) {
        // Log the email and password for debugging purposes (remove in production)
        System.out.println("Authenticating user with email: " + request.getUsername());

        // Check if user exists
        Optional<User> userOptional = userRepository.findByEmail(request.getUsername());
        if (!userOptional.isPresent()) {
            System.out.println("User not found with email: " + request.getUsername());
            throw new InvalidCredentialsException("Invalid email or password");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            // Log the exception for debugging purposes
            System.out.println("Authentication failed: " + e.getMessage());
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

}
