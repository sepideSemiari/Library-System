package com.example.librarymanagementsystem.model.entity;

public class BookReturn {
    private String id;
    private String issuedDate;
    private String returnedDate;
    private float fine;

    public BookReturn(String id, String issuedDate, String returnedDate, float fine) {
        this.id = id;
        this.issuedDate = issuedDate;
        this.returnedDate = returnedDate;
        this.fine = fine;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
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
        return "BookReturn{" +
                "id='" + id + '\'' +
                ", issuedDate='" + issuedDate + '\'' +
                ", returnedDate='" + returnedDate + '\'' +
                ", fine=" + fine +
                '}';
    }
}
