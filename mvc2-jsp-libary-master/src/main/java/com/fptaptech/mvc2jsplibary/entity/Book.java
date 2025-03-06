package com.fptaptech.mvc2jsplibary.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String isbn;
    private boolean available = true;

    @OneToMany(mappedBy = "book")
    private List<Loan> loans;

}