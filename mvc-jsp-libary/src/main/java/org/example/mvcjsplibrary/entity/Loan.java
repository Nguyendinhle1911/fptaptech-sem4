package org.example.mvcjsplibrary.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "loans")
@Data

public class Loan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Temporal(TemporalType.DATE)
    private Date loadDate;

    @Temporal(TemporalType.DATE)
    private Date returnDate;
}
