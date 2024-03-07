package com.example.librarySystem.controller;

import com.example.librarySystem.model.entity.Member;
import com.example.librarySystem.util.DBConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class MemberController {
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
    public AnchorPane root;
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
        Class.forName("org.postgresql.Driver");
        //set column
        member_table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        member_table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        member_table.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        member_table.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contact"));

        try {
            connection= DBConnection.getInstance().getConnection();
            ObservableList<Member> members=member_table.getItems();
            selectAll=connection.prepareStatement("select  * from member member_detail");
            selectMemberId=connection.prepareStatement("select *from member_detail where id=?");
            newIdQuery=connection.prepareStatement("select id from member_detail ");
            addToTable=connection.prepareStatement("insert into member_detail values(?,?,?,?)");
            updateQuery=connection.prepareStatement("update member_detail set name=? , address=? ,concat=? where id =?");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btn_new(ActionEvent actionEvent) {
    }

    public void btn_add(ActionEvent actionEvent) {
    }

    public void btn_delete(ActionEvent actionEvent) {
    }

    public void image_back(MouseEvent mouseEvent) {
    }
}
