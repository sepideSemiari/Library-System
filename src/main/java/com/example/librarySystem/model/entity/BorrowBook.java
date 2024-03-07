package com.example.librarySystem.model.entity;


import java.security.Timestamp;

public class BorrowBook {
    private String borrowId;
    private Timestamp date;
    private String memberId;
    private String bookId;

    public BorrowBook(String borrowId, Timestamp date, String memberId, String bookId) {
        this.borrowId = borrowId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.date=date;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setIssueId(String issueId) {
        this.borrowId = borrowId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "BookIssue{" +
                "issueId='" + borrowId + '\'' +
                ", date='" + date + '\'' +
                ", memberId='" + memberId + '\'' +
                ", bookId='" + bookId + '\'' +
                '}';
    }
}
