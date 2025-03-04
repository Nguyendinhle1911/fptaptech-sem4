package com.fptaptech.mvc2jsplibary.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Member member;

    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

}