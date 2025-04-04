package org.example.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    @Email(message = "Username must be a valid email address")
    private String username;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;
}
