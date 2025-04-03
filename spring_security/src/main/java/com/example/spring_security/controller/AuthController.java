package com.example.spring_security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Đây là endpoint công khai!";
    }

    @GetMapping("/user")
    public String userEndpoint() {
        return "Chào mừng USER!";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Chào mừng ADMIN!";
    }
}