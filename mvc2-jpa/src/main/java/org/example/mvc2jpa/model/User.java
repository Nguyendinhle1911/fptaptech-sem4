package org.example.mvc2jpa.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "Users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;


    @Column (name = "Username", nullable = false)
    private String name ;


    @Column(name = "emaill", nullable = false)
    private String email ;

    //@OneToMany
    //private List<account> account = new ArrayList<account>();

    public User(String username, String email) {
            this.name = username; // Sửa để gán đúng giá trị
            this.email = email;

    }

    public User() {

    }
}
