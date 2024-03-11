package com.example.librarySystem.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowBook  {
    private  String borrowId;
    private String date;
    private String memberId;
    private String bookId;

    @Override
    public String toString() {
        return "BorrowBook{" +
                "borrowId='" + borrowId + '\'' +
                ", date='" + date + '\'' +
                ", memberId='" + memberId + '\'' +
                ", bookId='" + bookId + '\'' +
                '}';
    }
}
