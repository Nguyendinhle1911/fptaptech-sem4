package com.fptaptech.securityp1;

import com.fptaptech.securityp1.model.Role;
import com.fptaptech.securityp1.model.User;
import com.fptaptech.securityp1.repository.RoleRepository;
import com.fptaptech.securityp1.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@SpringBootApplication
public class SecurityP1Application {

    public static void main(String[] args) {
        SpringApplication.run(SecurityP1Application.class, args);
    }

    @Bean
    @Transactional
    public CommandLineRunner dataInitializer(RoleRepository roleRepository,
                                             UserRepository userRepository,
                                             PasswordEncoder passwordEncoder) {
        return args -> {
            // Tạo hoặc lấy ROLE_USER
            Role roleUser = roleRepository.findByName("USER");
            if (roleUser == null) {
                roleUser = new Role();
                roleUser.setName("USER");
                roleRepository.save(roleUser);
            }

            // Tạo hoặc lấy ROLE_ADMIN
            Role roleAdmin = roleRepository.findByName("ADMIN");
            if (roleAdmin == null) {
                roleAdmin = new Role();
                roleAdmin.setName("ADMIN");
                roleRepository.save(roleAdmin);
            }

            // Tạo user với ROLE_USER nếu chưa tồn tại
            if (userRepository.findByUsername("user") == null) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("password"));
                user.setRoles(Set.of(roleUser));
                userRepository.save(user);
            }

            // Tạo admin với ROLE_ADMIN nếu chưa tồn tại
            if (userRepository.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(roleAdmin));
                userRepository.save(admin);
            }
        };
    }
}