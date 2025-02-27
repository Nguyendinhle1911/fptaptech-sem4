package org.example.mvc2jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity

public class account {
    @Id
    private int id;

    private double amount;
}
