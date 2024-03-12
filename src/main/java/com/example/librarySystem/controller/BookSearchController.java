package com.example.librarySystem.controller;

import com.example.librarySystem.model.entity.Book;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.sql.*;


public class BookSearchController {
    @FXML
    public TextField book_search;
    @FXML
    public TableView<Book> table_book;
    @FXML
    public AnchorPane search_root;
    private Connection connection;

    public void initialize() {
    }

    public void image_back(MouseEvent mouseEvent) {
    }
}
