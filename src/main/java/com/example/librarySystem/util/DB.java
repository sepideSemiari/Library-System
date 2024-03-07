package com.example.librarySystem.util;



import com.example.librarySystem.model.entity.Book;
import com.example.librarySystem.model.entity.BorrowBook;
import com.example.librarySystem.model.entity.BookReturn;
import com.example.librarySystem.model.entity.Member;

import java.util.ArrayList;

public class DB {
    public static ArrayList<Member> members =new ArrayList<>();
    public static ArrayList<Book> books=new ArrayList<>();
    public  static ArrayList<BorrowBook> issuedBook=new ArrayList<>();
    public static ArrayList<BookReturn> returnedBook=new ArrayList<>();

}
