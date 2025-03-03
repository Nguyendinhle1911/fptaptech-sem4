package org.example.mvcjsplibrary.bean;

import lombok.Data;

@Data
public class BookBean {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String publicationYear;
    public BookBean() {}
}
