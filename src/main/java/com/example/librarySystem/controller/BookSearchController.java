package com.example.librarySystem.controller;

import com.example.librarySystem.model.entity.Book;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
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
        table_book.getColumns().get(0).setCellValueFactory(new
               PropertyValueFactory<>("id") );
        table_book.getColumns().get(1).setCellValueFactory(new
                PropertyValueFactory<>("title"));
        table_book.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("author"));
        table_book.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("status"));
        try {
            connection=DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","12345");
            ObservableList<Book> books=table_book.getItems();
            String sql="select * from book_detail";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                System.out.println("initialize");
                books.add(new Book(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
            }
            table_book.setItems(books);
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        book_search.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

                String searchText = book_search.getText();
                try {
                    table_book.getItems().clear();

                    try {
                        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345");
                        String sql="select * from book_detail where id like ? or title like ? or author like ?";
                        PreparedStatement preparedStatement=connection.prepareStatement(sql);
                        String like="%" +searchText+"%";
                        preparedStatement.setString(1,like);
                        preparedStatement.setString(2,like);
                        preparedStatement.setString(3,like);
                        ResultSet resultSet=preparedStatement.executeQuery();
                        ObservableList observableList=table_book.getItems();
                        observableList.clear();
                        while (resultSet.next()){
                            observableList.add(new Book(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
                        }
                        connection.close();
                    }
                    catch (SQLException e){
                        e.printStackTrace();
                    }

                }
                catch (NullPointerException n){
                    return;
                }
            }
        });
    }

    public void image_back(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(new File("E:\\javaCode\\Project\\LibrarySystem\\LibrarySystem\\src\\main\\resources\\com\\example\\librarySystem\\HomeFormView.fxml").toURL());
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) this.search_root.getScene().getWindow();
        primaryStage.setScene(scene);

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();
    }
}
