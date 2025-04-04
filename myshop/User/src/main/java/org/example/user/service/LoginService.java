package org.example.user.service;

import org.example.user.dto.JwtResponse;
import org.example.user.entity.User;
import org.example.user.repository.UserRepository;
import org.example.user.security.JwtUtil;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public LoginService(AuthenticationManager authenticationManager,
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public JwtResponse login(String username, String password) {
        // Authentication sẽ tự động validate email qua UserDetailsService
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user);
        return new JwtResponse(token);
    }

    public User register(String username, String password, String role) {
        // Chuyển username về chữ thường để đồng nhất
        String normalizedUsername = username.toLowerCase();

        // Check if username already exists
        if (userRepository.findByUsername(normalizedUsername).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

        // Validate and normalize role
        String normalizedRole = validateAndNormalizeRole(role);

        // Create new user
        User newUser = new User();
        newUser.setUsername(normalizedUsername);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(normalizedRole);

        return userRepository.save(newUser);
    }

    private String validateAndNormalizeRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return "USER"; // Default role nếu không cung cấp
        }

        String upperCaseRole = role.toUpperCase();
        if (!upperCaseRole.equals("USER") && !upperCaseRole.equals("ADMIN")) {
            throw new IllegalArgumentException("Invalid role. Must be 'USER' or 'ADMIN'");
        }

        return upperCaseRole;
    }

    @Transactional
    public User changeUserRoleToAdmin(String username) {
        User user = userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("User is already an admin");
        }

        user.setRole("ADMIN");
        return userRepository.save(user);
    }
}