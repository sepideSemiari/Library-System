package com.example.librarySystem.controller;

import com.example.librarySystem.model.entity.Book;
import com.example.librarySystem.model.entity.BorrowBook;
import com.example.librarySystem.util.DB;
import com.example.librarySystem.util.DBConnection;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


public class BookBorrowController {
    @FXML
    public TextField text_borrow_id;
    @FXML
    public TextField text_name;
    @FXML
    public TextField text_title;
    @FXML
    public DatePicker text_borrow_date;
    @FXML
    public ComboBox member_borrow_id;
    @FXML
    public ComboBox book_id;
    @FXML
    public TableView<BorrowBook> borrow_book_table;
    @FXML
    public AnchorPane borrow_book_root;
    private Connection connection;

    //JDBC
    private PreparedStatement selectAll;
    private PreparedStatement selectMemberId;
    private PreparedStatement selectBookDetails;
    private PreparedStatement table;
    private PreparedStatement delete;

    public void initialize() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

        borrow_book_table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        borrow_book_table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("date"));
        borrow_book_table.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("memberId"));
        borrow_book_table.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("bookId"));

        try {
            connection = DBConnection.getInstance().getConnection();
            ObservableList<BorrowBook> borrowBooks = borrow_book_table.getItems();
            selectAll = connection.prepareStatement("select * from borrow_detail");
            selectMemberId = connection.prepareStatement("select name from member_detail where id=?");
            selectBookDetails = connection.prepareStatement("select title,status from book_detail where id=?");
            table = connection.prepareStatement("insert into borrow_detail values (?,?,?,?)");
            delete = connection.prepareStatement("delete from borrow_detail where borrowId=?");
            ResultSet resultSet = selectAll.executeQuery();
            while (resultSet.next()) {
                System.out.println("load");
                borrowBooks.add(new BorrowBook(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)));


            }
            borrow_book_table.setItems(borrowBooks);
            member_borrow_id.getItems().clear();
            ObservableList membersBorrowId = member_borrow_id.getItems();
            String sql = "select id from  member_detail";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                membersBorrowId.add(result.getString(1));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        member_borrow_id.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue
                                        observableValue, Object oldValue, Object newValue) {
                if (member_borrow_id.getSelectionModel().getSelectedItem() != null) {
                    Object selectedItem = member_borrow_id.getSelectionModel().getSelectedItem();

                    if (selectedItem.equals(null) || member_borrow_id.getSelectionModel().isEmpty()) {
                        return;
                    }
                    try {
                        selectMemberId.setString(1, selectedItem.toString());
                        ResultSet resultSet = selectMemberId.executeQuery();
                        if (resultSet.next()) {
                            text_name.setText(resultSet.getString(1));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        book_id.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if (book_id.getSelectionModel().getSelectedItem() != null) {
                    Object selectedIem = book_id.getSelectionModel().getSelectedItem();
                    try {
                        text_title.clear();
                        selectBookDetails.setString(1, selectedIem.toString());
                        ResultSet resultSet = selectBookDetails.executeQuery();
                        if (resultSet.next()) {
                            if (resultSet.getString(2).equals("Available")) {
                                text_title.setText(resultSet.getString(1));
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR, "this book isn't available!", ButtonType.OK);
                                Optional<ButtonType> buttonType = alert.showAndWait();
                            }
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }


    public void new_action(ActionEvent actionEvent) throws SQLException {
        text_title.clear();
        text_name.clear();
        member_borrow_id.getSelectionModel().clearSelection();
        book_id.getSelectionModel().clearSelection();
        text_borrow_date.setPromptText("borrow date");

        String sql = "select borrowId from borrow_detail";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        String ids = null;
        int maxId = 0;

        while (resultSet.next()) {
            ids = resultSet.getString(1);
            int id = Integer.parseInt(ids.replace("I", ""));
            if (id > maxId) {
                maxId = id;
            }
        }
        maxId = maxId + 1;
        String id = "";
        if (maxId < 10) {
            id = "I00" + maxId;
        } else if (maxId < 100) {
            id = "I0" + maxId;
        } else {
            id = "I" + maxId;
        }
        text_borrow_id.setText(id);
    }

    public void add_Action(ActionEvent actionEvent) {
        ObservableList<BorrowBook> borrowBooks = FXCollections.observableList(DB.borrowBooks);
        ObservableList<Book> books = FXCollections.observableList(DB.books);
        if (text_borrow_id.getText().isEmpty() || book_id.getSelectionModel().getSelectedItem().equals(null) || member_borrow_id.getSelectionModel().getSelectedItem().equals(null) || text_borrow_date.getValue().toString().equals(null)) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "please fill your details", ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            return;
        } else {
            String memberId = (String) member_borrow_id.getSelectionModel().getSelectedItem();
            String bookId = (String) book_id.getSelectionModel().getSelectedItem();
            borrowBooks.add(new BorrowBook(text_borrow_id.getText(), text_borrow_date.getValue().toString(), memberId, bookId));
            try {
                table.setString(1, text_borrow_id.getText());
                table.setString(2, text_borrow_date.getValue().toString());
                table.setString(3, (String) member_borrow_id.getSelectionModel().getSelectedItem());
                table.setString(4, (String) book_id.getSelectionModel().getSelectedItem());
                int rowsValue = table.executeUpdate();
                if (rowsValue > 0) {
                    System.out.println("data insertion successful");
                    String sql = "update book_detail set status=? where id=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    String id = (String) book_id.getSelectionModel().getSelectedItem();
                    preparedStatement.setString(1, "Unavailable");
                    preparedStatement.setString(2, id);
                    int affected = preparedStatement.executeUpdate();
                    if (affected > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "status updated", ButtonType.OK);
                        Optional<ButtonType> buttonType = alert.showAndWait();
                    } else {
                        System.out.println("Error");
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            borrow_book_table.getItems().clear();
            initialize();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void delete_Action(ActionEvent actionEvent) {
        BorrowBook selectItem = borrow_book_table.getSelectionModel().getSelectedItem();
        if (borrow_book_table.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "please select a row", ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            return;

        } else {
            try {
                delete.setString(1, selectItem.getBorrowId());
                delete.executeUpdate();

                String sql = "update book_detail set status=? where id =?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                String id = (String) book_id.getSelectionModel().getSelectedItem();
                preparedStatement.setString(1, "available");
                preparedStatement.setString(2, id);
                preparedStatement.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "record deleted!", ButtonType.OK);
                Optional<ButtonType> buttonType = alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            borrow_book_table.getItems().clear();
            initialize();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void image_back(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(new File("E:\\javaCode\\Project\\LibrarySystem\\LibrarySystem\\src\\main\\resources\\com\\example\\librarySystem\\HomeFormView.fxml").toURL());
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) this.borrow_book_root.getScene().getWindow();
        primaryStage.setScene(scene);

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();
    }
}
