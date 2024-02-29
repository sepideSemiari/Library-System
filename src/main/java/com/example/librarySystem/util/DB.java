package com.example.librarymanagementsystem.util;

import com.example.librarymanagementsystem.model.entity.Book;
import com.example.librarymanagementsystem.model.entity.BookIssue;
import com.example.librarymanagementsystem.model.entity.BookReturn;
import com.example.librarymanagementsystem.model.entity.Member;

import java.util.ArrayList;

public class DB {
    public static ArrayList<Member> members =new ArrayList<>();
    public static ArrayList<Book> books=new ArrayList<>();
    public  static ArrayList<BookIssue> issuedBook=new ArrayList<>();
    public static ArrayList<BookReturn> returnedBook=new ArrayList<>();

}
