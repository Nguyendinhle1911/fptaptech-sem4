package org.example.mvcjsplibrary.entity;


import jakarta.persistence.*;
import lombok.Data;

import javax.management.relation.Role;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String fullName;

    public enum Role {
        LIBRARIAN, ADMIN, READER
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Loan> loans;

}
