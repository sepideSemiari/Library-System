package com.example.librarySystem.model.entity;

public class BookReturn {
    private String id;
    private String borrowedDate;
    private String returnedDate;
    private float fine;

    public BookReturn(String id, String borrowedDate, String returnedDate, float fine) {
        this.id = id;
        this.borrowedDate = borrowedDate;
        this.returnedDate = returnedDate;
        this.fine = fine;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public void setIssuedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public String getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(String returnedDate) {
        this.returnedDate = returnedDate;
    }

    public float getFine() {
        return fine;
    }

    public void setFine(float fine) {
        this.fine = fine;
    }

    @Override
    public String toString() {
        return "BookReturn{" + "id='" + id + '\'' + ", issuedDate='" + borrowedDate + '\'' + ", returnedDate='" + returnedDate + '\'' + ", fine=" + fine + '}';
    }
}
