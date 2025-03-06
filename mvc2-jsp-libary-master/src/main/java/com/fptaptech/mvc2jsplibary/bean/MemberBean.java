package com.fptaptech.mvc2jsplibary.bean;

import lombok.Data;

@Data
public class MemberBean {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private int totalBorrowedBooks; // Thêm để theo dõi số sách đang mượn

    public MemberBean() {
    }
}