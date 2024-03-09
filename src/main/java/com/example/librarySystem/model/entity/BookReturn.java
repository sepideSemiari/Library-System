package com.example.librarySystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookReturn {
    private int id;
    private String borrowedDate;
    private String returnedDate;
    private float fine;

    @Override
    public String toString() {
        return "BookReturn{" +
                "id=" + id +
                ", borrowedDate='" + borrowedDate + '\'' +
                ", returnedDate='" + returnedDate + '\'' +
                ", fine=" + fine +
                '}';
    }
}
