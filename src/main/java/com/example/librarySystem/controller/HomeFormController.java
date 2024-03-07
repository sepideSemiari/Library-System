package com.example.librarySystem.controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class HomeFormController {

    @FXML
    public AnchorPane root;
    @FXML
    public ImageView member;
    @FXML
    public ImageView books;
    @FXML
    public ImageView issue;
    @FXML
    public ImageView books_return;
    @FXML
    public ImageView books_search;


    //navigate windows
    @FXML
    public void navigate(MouseEvent event) throws IOException {

        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            Parent root = null;

            switch (icon.getId()) {
                case "member":
                    root = FXMLLoader.load(new File("E:\\javaCode\\Project\\LibrarySystem\\LibrarySystem\\src\\main\\resources\\com\\example\\librarySystem\\MembersFormView.fxml").toURI().toURL());
                    break;
                case "books":
                    root = FXMLLoader.load(new File("E:\\javaCode\\Project\\LibrarySystem\\LibrarySystem\\src\\main\\resources\\com\\example\\librarySystem\\BooksFormView.fxml").toURI().toURL());
                    break;
                case "borrow":
                    root = FXMLLoader.load(new File("E:\\javaCode\\Project\\LibrarySystem\\LibrarySystem\\src\\main\\resources\\com\\example\\librarySystem\\BooksBorrowFormView.fxml").toURI().toURL());
                    break;
                case "books_return":
                    root = FXMLLoader.load(new File("E:\\javaCode\\Project\\LibrarySystem\\LibrarySystem\\src\\main\\resources\\com\\example\\librarySystem\\BookReturnFormView.fxml").toURI().toURL());
                    break;
                case "books_search":
                    root = FXMLLoader.load(new File("E:\\javaCode\\Project\\LibrarySystem\\LibrarySystem\\src\\main\\resources\\com\\example\\librarySystem\\BookSearchFormView.fxml").toURI().toURL());
                    break;
            }

            if (root != null) {
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.root.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();

            }
        }
    }


}
