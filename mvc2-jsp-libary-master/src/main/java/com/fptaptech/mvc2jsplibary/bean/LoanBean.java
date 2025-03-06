package com.fptaptech.mvc2jsplibary.bean;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanBean {
    private Long id;
    private Long bookId;
    private Long memberId;
    private String bookTitle;
    private String memberName;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private int overdueDays;

    public LoanBean() {
    }
}