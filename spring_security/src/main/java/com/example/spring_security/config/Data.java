package com.example.spring_security.config;

import com.example.spring_security.entity.UserEntity;
import com.example.spring_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Data implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Kiểm tra và tạo tài khoản user
        if (userRepository.findByUsername("user").isEmpty()) {
            UserEntity user = new UserEntity();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole("ROLE_USER");
            userRepository.save(user);
        }

        // Kiểm tra và tạo tài khoản admin
        if (userRepository.findByUsername("admin").isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
        }

        // Kiểm tra và tạo tài khoản superadmin (tài khoản admin mới)
        if (userRepository.findByUsername("superadmin").isEmpty()) {
            UserEntity superadmin = new UserEntity();
            superadmin.setUsername("superadmin");
            superadmin.setPassword(passwordEncoder.encode("super123"));
            superadmin.setRole("ROLE_ADMIN");
            userRepository.save(superadmin);
        }
    }
}