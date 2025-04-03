package org.example.user.controller;

import org.example.user.dto.JwtResponse;
import org.example.user.dto.LoginRequest;
import org.example.user.entity.User;
import org.example.user.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginService loginService;


    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(loginService.login(loginRequest.getUsername(), loginRequest.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(loginService.register(loginRequest.getUsername(), loginRequest.getPassword(),loginRequest.getRole()));
    }

    @PutMapping("/role/{username}")
    public ResponseEntity<?> changeUserRoleToAdmin(@PathVariable String username) {
        try {
            User updatedUser = loginService.changeUserRoleToAdmin(username);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
