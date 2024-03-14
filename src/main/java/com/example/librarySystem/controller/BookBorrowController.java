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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
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
    private ObservableList<BorrowBook> borrowBooks;


    public void initialize() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");


        borrow_book_table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        borrow_book_table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("date"));
        borrow_book_table.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("memberId"));
        borrow_book_table.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("bookId"));


        try {
            connection = DBConnection.getInstance().getConnection();
            ObservableList<BorrowBook> issue = borrow_book_table.getItems();

            selectAll = connection.prepareStatement("SELECT * FROM borrow_detail");
            selectMemberId = connection.prepareStatement("select name from member_detail where id=?");
            selectBookDetails = connection.prepareStatement("select title,status from book_detail where id=?");
            table = connection.prepareStatement("INSERT INTO borrow_detail values(?,?,?,?)");
            delete = connection.prepareStatement("DELETE FROM book_detail WHERE id=?");
            ResultSet rst = selectAll.executeQuery();

            while (rst.next()) {
                System.out.println("load");
                issue.add(new BorrowBook(rst.getString(1),
                        rst.getString(2),
                        rst.getString(3),
                        rst.getString(4)));
            }

            borrow_book_table.setItems(issue);
            member_borrow_id.getItems().clear();
            ObservableList cmbmembers = member_borrow_id.getItems();
            String sql2 = "select id from member_detail";
            PreparedStatement pstm1 = connection.prepareStatement(sql2);
            ResultSet rst1 = pstm1.executeQuery();

            while (rst1.next()) {
                cmbmembers.add(rst1.getString(1));
            }

            book_id.getItems().clear();
            ObservableList cmbbooks = book_id.getItems();
            String sql3 = "select id from book_detail";
            PreparedStatement pstm2 = connection.prepareStatement(sql3);
            ResultSet rst2 = pstm2.executeQuery();
            while (rst2.next()) {
                cmbbooks.add(rst2.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        member_borrow_id.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                if (member_borrow_id.getSelectionModel().getSelectedItem() != null) {
                    Object selectedItem = member_borrow_id.getSelectionModel().getSelectedItem();


                    if (selectedItem.equals(null) || member_borrow_id.getSelectionModel().isEmpty()) {
                        return;
                    }
                    try {
                        selectMemberId.setString(1, selectedItem.toString());
                        ResultSet rst = selectMemberId.executeQuery();

                        if (rst.next()) {
                            text_name.setText(rst.getString(1));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        book_id.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (book_id.getSelectionModel().getSelectedItem() != null) {
                    Object selectedItem = book_id.getSelectionModel().getSelectedItem();

                    try {
                        text_title.clear();
                        selectBookDetails.setString(1, selectedItem.toString());
                        ResultSet rst = selectBookDetails.executeQuery();

                        if (rst.next()) {
                            if (rst.getString(2).equals("Available")) {
                                text_title.setText(rst.getString(1));
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR,
                                        "This book isn't available!",
                                        ButtonType.OK);
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

        String sql = "select borrowid from borrow_detail";
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

    public void add_Action(ActionEvent actionEvent) throws SQLException {

        ObservableList<BorrowBook> BorrowBooks = FXCollections.observableList(DB.borrowBooks);
        ObservableList<Book> books = FXCollections.observableList(DB.books);

        if (text_borrow_id.getText().isEmpty() ||
                book_id.getSelectionModel().getSelectedItem().equals(null) ||
                member_borrow_id.getSelectionModel().getSelectedItem().equals(null)
                || text_borrow_date.getValue().toString().equals(null)) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please fill your details.",
                    ButtonType.OK);
            alert.showAndWait();
            return;
        } else {
            String memberId = (String) member_borrow_id.getSelectionModel().getSelectedItem();
            String bookId = (String) book_id.getSelectionModel().getSelectedItem();
            LocalDate localDate=text_borrow_date.getValue();
            BorrowBooks.add(new BorrowBook(text_borrow_id.getText(), localDate.toString(), memberId, bookId));

            try {
                table.setString(1, text_borrow_id.getText());
                table.setDate(2, Date.valueOf(text_borrow_date.getValue().toString()));
                table.setString(3, (String) member_borrow_id.getSelectionModel().getSelectedItem());
                table.setString(4, (String) book_id.getSelectionModel().getSelectedItem());
                int affectedRows = table.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Data insertion successful");
                    String sql2 = "Update book_detail SET status=? where id=?";
                    PreparedStatement pstm2 = connection.prepareStatement(sql2);
                    String id = (String) book_id.getSelectionModel().getSelectedItem();
                    pstm2.setString(1, "Unavailable");
                    pstm2.setString(2, id);
                    int affected = pstm2.executeUpdate();

                    if (affected > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                                "Status updated.",
                                ButtonType.OK);
                        Optional<ButtonType> buttonType = alert.showAndWait();
                    } else {
                        System.out.println("ERROR");
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
                preparedStatement.setString(1, "Available");
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
