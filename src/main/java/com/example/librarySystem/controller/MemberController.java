package com.example.librarySystem.controller;

import com.example.librarySystem.model.entity.Member;
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
import javafx.scene.Scene;
import javafx.scene.Parent;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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


public class MemberController {
    @FXML
    public AnchorPane root;
    @FXML
    public TextField member_id;
    @FXML
    public TextField member_name;
    @FXML
    public TextField member_address;
    @FXML
    public TextField member_number;
    @FXML
    public TableView<Member> member_table;
    @FXML
    public ImageView image_back;
    @FXML
    public Button btn_new;
    @FXML
    public Button btn_add;
    @FXML
    public Button btn_delete;
    //JDBC
    private Connection connection;
    private PreparedStatement selectAll;
    private PreparedStatement newIdQuery;
    private PreparedStatement addToTable;
    private PreparedStatement updateQuery;
    private PreparedStatement deleteQuery;
    private PreparedStatement selectMemberId;

    @FXML
    public void initialize() throws ClassNotFoundException {
        member_id.setDisable(true);


        //set column
        member_table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        member_table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        member_table.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        member_table.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("concat"));

        Class.forName("org.postgresql.Driver");
        try {
            connection = DBConnection.getInstance().getConnection();
            ObservableList<Member> members = member_table.getItems();
            selectAll = connection.prepareStatement("select * from member_detail");
            selectMemberId = connection.prepareStatement("select * from member_detail where  id=?");
            newIdQuery = connection.prepareStatement("select id from member_detail");
            addToTable = connection.prepareStatement("insert into member_detail values (?,?,?,?)");
            updateQuery = connection.prepareStatement("update member_detail set name=? ,address=?,contact=? where id=?");
            deleteQuery = connection.prepareStatement("delete from member_detail where id=?");
            ResultSet resultSet = selectAll.executeQuery();
            while (resultSet.next()) {
                System.out.println("load");
                members.add(new Member(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                ));
            }
            member_table.setItems(members);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        member_table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Member>() {
            @Override
            public void changed(ObservableValue<? extends Member> observableValue, Member oldValue, Member newValue) {
                Member selectedItem = member_table.getSelectionModel().getSelectedItem();
                try {
                    connection = null;
                    try {
                        selectMemberId.setString(1, selectedItem.getId());
                        ResultSet rst = selectMemberId.executeQuery();
                        if (rst.next()) {
                            member_id.setText(rst.getString(1));
                            member_name.setText(rst.getString(2));
                            member_address.setText(rst.getString(3));
                            member_number.setText(rst.getString(4));
                            member_id.setDisable(true);
                            btn_add.setText("Update");
                        }
                        btn_add.setText("Update");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (NullPointerException e) {
                    return;
                }
            }

        });

    }

    public void btn_new(ActionEvent actionEvent) throws SQLException {
        btn_add.setText("Add");
        member_id.setDisable(false);
        member_name.clear();
        member_address.clear();
        member_number.clear();


        ResultSet resultSet = newIdQuery.executeQuery();

        String ids = null;
        int maxId = 0;

        while (resultSet.next()) {
            ids = resultSet.getString(1);
            int id = Integer.parseInt(ids.replace("M", ""));
            if (id > maxId) {
                maxId = id;
            }
        }
        maxId = maxId + 1;
        String id = " ";
        if (maxId < 10) {
            id = "M00" + maxId;
        } else if (maxId < 100) {
            id = "M0" + maxId;
        } else {
            id = "M" + maxId;
        }
        member_id.setText(id);


    }


    public void btn_add(ActionEvent actionEvent) throws SQLException {
        ObservableList<Member> members = FXCollections.observableList(DB.members);
        if (member_id.getText().isEmpty() ||
                member_name.getText().isEmpty() ||
                member_address.getText().isEmpty() ||
                member_number.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "please fill your  details.", ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            return;
        }
        if (!(member_name.getText().matches("^\\b([A-Za-z.]+\\s?)+$") && member_address.getText().matches("^\\b[A-Za-z0-9/,\\s]+.$") && member_number.getText().matches("\\d{11}"))) {
            new Alert(Alert.AlertType.ERROR, "Entered details invalid").show();
            return;
        }

        //save
        if (btn_add.getText().equals("Add")) {
            addToTable.setString(1, member_id.getText());
            addToTable.setString(2, member_name.getText());
            addToTable.setString(3, member_address.getText());
            addToTable.setString(4, member_number.getText());
            int enteredRows = addToTable.executeUpdate();

            if (enteredRows > 0) {
                System.out.println("data load successful");
            } else {
                System.out.println("something is wrong");
            }
        } else {
            if (btn_add.getText().equals("Update")) {
                for (int i = 0; i < members.size(); i++) {
                    if (member_id.getText().equals(members.get(i).getId())) {
                        try {
                            updateQuery.setString(1, member_name.getText());
                            updateQuery.setString(2, member_address.getText());
                            updateQuery.setString(3, member_number.getText());
                            updateQuery.setString(4, member_id.getText());
                            int affected = updateQuery.executeUpdate();
                            if (affected > 0) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Record updated!!", ButtonType.OK);

                                Optional<ButtonType> buttonType = alert.showAndWait();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR, "update error!", ButtonType.OK);
                                Optional<ButtonType> buttonType = alert.showAndWait();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            member_table.setItems(members);
        }
        try {
            member_table.getItems().clear();
            initialize();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void btn_delete(ActionEvent actionEvent) throws SQLException {
        Member selectedItem = member_table.getSelectionModel().getSelectedItem();
        if (member_table.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "please select a member", ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            return;
        } else {
            deleteQuery.setString(1, selectedItem.getId());
            int values = deleteQuery.executeUpdate();
            if (values > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "record deleted!", ButtonType.OK);
                Optional<ButtonType> buttonType = alert.showAndWait();
            }

        }
        try {
            member_table.getItems().clear();
            initialize();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void image_back(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(new File("E:\\javaCode\\Project\\LibrarySystem\\LibrarySystem\\src\\main\\resources\\com\\example\\librarySystem\\view\\HomeFormView.fxml").toURL());
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(scene);

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();
    }


}
