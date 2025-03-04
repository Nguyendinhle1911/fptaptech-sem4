package com.fptaptech.mvc2jsplibary.bean;

import lombok.Data;

@Data
public class BookBean {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private int publicationYear;
    private int availStatus;
    private int totalBook;
    public BookBean() {
    }
}
