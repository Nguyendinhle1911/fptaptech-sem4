package org.example.mvcjsplibrary.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "books")
@Data
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String publicationYear;
    private int availStatus;
    private int totalBook;

}
