package com.example.librarySystem.controller;

import com.example.librarySystem.model.entity.BookReturn;
import com.example.librarySystem.util.DBConnection;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.concurrent.TimeUnit;


public class BookReturnController {
    @FXML
    public AnchorPane Return_root;
    @FXML
    public TextField text_borrow_date;
    @FXML
    public TextField text_fine;
    @FXML
    public DatePicker text_return_date;
    @FXML
    public TableView<BookReturn> return_tbl;
    @FXML
    public ComboBox combo_borrow_id;
    private Connection connection;

    public void initialize() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

        return_tbl.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        return_tbl.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        return_tbl.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("returnedDate"));
        return_tbl.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("fine"));

        try {
            connection = DBConnection.getInstance().getConnection();
            ObservableList<BookReturn> returns = return_tbl.getItems();

            String sql = "SELECT * from return_detail";
            PreparedStatement pstm = connection.prepareStatement(sql);
            ResultSet rst = pstm.executeQuery();
            while (rst.next()) {
                float fine = Float.parseFloat(rst.getString(4));
                returns.add(new BookReturn(rst.getString(1), rst.getString(2), rst.getString(3), fine));
            }
            return_tbl.setItems(returns);

            combo_borrow_id.getItems().clear();
            ObservableList cmbBorrow = combo_borrow_id.getItems();
            String sql2 = "select borrowid from borrow_detail";
            PreparedStatement pstm1 = connection.prepareStatement(sql2);
            ResultSet rst1 = pstm1.executeQuery();

            while (rst1.next()) {
                cmbBorrow.add(rst1.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        combo_borrow_id.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (combo_borrow_id.getSelectionModel().getSelectedItem() == null) {
                    return;
                }
                try {
                    String sql = "select date from borrow_detail where borrowid =?";
                    PreparedStatement pstm = connection.prepareStatement(sql);
                    pstm.setString(1, (String) combo_borrow_id.getSelectionModel().getSelectedItem());
                    ResultSet rst = pstm.executeQuery();
                    if (rst.next()) {
                        text_borrow_date.setText(rst.getString(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        text_return_date.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {

                if (text_return_date.getValue() == null) {
                    return;
                }

                LocalDate returned = text_return_date.getValue();
                LocalDate borrowed = LocalDate.parse(text_borrow_date.getText());

                Date date1 = Date.valueOf(borrowed);
                Date date2 = Date.valueOf(returned);

                long diff = date2.getTime() - date1.getTime();

                System.out.println(TimeUnit.MILLISECONDS.toDays(diff));
                int dateCount = (int) TimeUnit.MILLISECONDS.toDays(diff);
                float fine = 0;

                if (dateCount > 14) {
                    fine = dateCount * 15;
                }
                text_fine.setText(Float.toString(fine));
            }
        });
    }

    //btn new action
    public void button_new(ActionEvent actionEvent) {
        text_fine.clear();
        text_borrow_date.setPromptText("borrow date");
        combo_borrow_id.getSelectionModel().clearSelection();
        text_return_date.setPromptText("Returned date");
    }

    //btn add action
    public void button_add_inventory(ActionEvent actionEvent) throws SQLException {
        if (combo_borrow_id.getSelectionModel().isEmpty() ||
                text_borrow_date.getText().isEmpty() ||
                text_return_date.getValue() == null ||
                text_fine.getText().isEmpty()
        ) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please fill details.",
                    ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            System.out.println("Fill Your empty fields!");
            return;
        }

        String borrowId = (String) combo_borrow_id.getSelectionModel().getSelectedItem();
        String sql = "INSERT INTO return_detail VALUES (?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, (String) combo_borrow_id.getSelectionModel().getSelectedItem());
        pstm.setString(2, text_borrow_date.getText());
        pstm.setString(3, text_return_date.getValue().toString());
        pstm.setString(4, text_fine.getText());
        int affectedRows = pstm.executeUpdate();

        if (affectedRows > 0) {
            System.out.println("successful");
            String sql4 = "Update book_detail SET status=? where id=?";
            PreparedStatement pstm2 = connection.prepareStatement(sql4);

            String sql3 = "select bookid from borrow_detail where borrowid=?";
            PreparedStatement pstm3 = connection.prepareStatement(sql3);
            pstm3.setString(1, (String) combo_borrow_id.getSelectionModel().getSelectedItem());
            ResultSet rst3 = pstm3.executeQuery();
            String id = null;

            if (rst3.next()) {
                id = rst3.getString(1);
            }
            pstm2.setString(1, "Available");
            pstm2.setString(2, id);
            int affected = pstm2.executeUpdate();
            if (affected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Status updated.",
                        ButtonType.OK);
                Optional<ButtonType> buttonType = alert.showAndWait();
            }
        } else {
            System.out.println("Something went wrong");
        }
        try {
            return_tbl.getItems().clear();
            initialize();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection.close();
        combo_borrow_id.getItems().remove(borrowId);
        combo_borrow_id.getSelectionModel().clearSelection();
        text_borrow_date.clear();
        text_fine.clear();
        text_return_date.getEditor().clear();
    }

    public void image_back(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(new File("E:\\javaCode\\Project\\LibrarySystem\\LibrarySystem\\src\\main\\resources\\com\\example\\librarySystem\\HomeFormView.fxml").toURL());
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) this.Return_root.getScene().getWindow();
        primaryStage.setScene(scene);

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();
    }
}
