package com.fptaptech.securityp1.service;

import com.fptaptech.securityp1.model.Role;
import com.fptaptech.securityp1.model.User;
import com.fptaptech.securityp1.repository.RoleRepository;
import com.fptaptech.securityp1.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomOAuth2UserService(UserRepository userRepository,
                                   RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Try to get email first
        String email = (String) attributes.get("email");

        // Fallback if email not available (e.g., Facebook without email permission)
        if (email == null || email.isBlank()) {
            email = (String) attributes.get("name"); // Tạm dùng name nếu cần
            if (email == null || email.isBlank()) {
                throw new RuntimeException("Không xác định được thông tin người dùng từ OAuth2 provider.");
            }
        }

        // Check if user exists
        User user = userRepository.findByUsername(email);
        if (user == null) {
            // Create ROLE_USER if not exists
            Role roleUser = roleRepository.findByName("ROLE_USER");
            if (roleUser == null) {
                roleUser = new Role();
                roleUser.setName("ROLE_USER");
                roleRepository.save(roleUser);
            }

            // Create user
            user = new User();
            user.setUsername(email);
            user.setPassword(UUID.randomUUID().toString()); // Avoid hardcode
            user.setRoles(new HashSet<>(Collections.singleton(roleUser)));
            userRepository.save(user);
        }

        // Convert roles to authorities
        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        // Return OAuth2 user with actual authorities and attributes
        return new DefaultOAuth2User(authorities, attributes, "email");
    }
}
