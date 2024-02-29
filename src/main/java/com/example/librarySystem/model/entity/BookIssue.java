package com.example.librarymanagementsystem.model.entity;

import java.sql.Timestamp;

public class BookIssue {
    private String issueId;
    private Timestamp date;
    private String memberId;
    private String bookId;

    public BookIssue(String issueId, Timestamp date, String memberId, String bookId) {
        this.issueId = issueId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.date=date;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
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
                "issueId='" + issueId + '\'' +
                ", date='" + date + '\'' +
                ", memberId='" + memberId + '\'' +
                ", bookId='" + bookId + '\'' +
                '}';
    }
}
