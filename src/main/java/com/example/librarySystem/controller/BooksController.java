package com.example.librarySystem.controller;


import com.example.librarySystem.model.entity.Book;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class BooksController {
    @FXML
    public TextField text_book_id;
    @FXML
    public TextField text_book_title;
    @FXML
    public TextField text_book_author;
    @FXML
    public TextField text_book_status;
    @FXML
    public TableView<Book> table_book;
    @FXML
    public AnchorPane book_root;
    @FXML
    public ImageView image_back;
    @FXML
    public Button btn_add;
    @FXML
    public Button btn_new;
    @FXML
    public Button btn_delete;
    private Connection connection;


    //JDBC
    private PreparedStatement selectAll;
    private PreparedStatement selectedId;
    private PreparedStatement newIdQuery;
    private PreparedStatement addToTable;
    private PreparedStatement updateQuery;
    private PreparedStatement deleteQuery;

    public void initialize() throws ClassNotFoundException {
        //disable id field
        text_book_id.setDisable(true);
        //load table
        table_book.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        table_book.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("title"));
        table_book.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("author"));
        table_book.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("status"));
        Class.forName("org.postgresql.Driver");
        try {
            connection = DBConnection.getInstance().getConnection();
            selectAll = connection.prepareStatement("SELECT * FROM book_detail");
            updateQuery = connection.prepareStatement("update book_detail set title=? ,author=?,status=? where id=?");
            selectedId = connection.prepareStatement("select * from book_detail where id=?");
            addToTable = connection.prepareStatement("insert into book_detail values (?,?,?,?)");
            newIdQuery = connection.prepareStatement("select id from book_detail");
            deleteQuery = connection.prepareStatement("delete from book_detail where id=?");
            ObservableList<Book> membersBook = table_book.getItems();
            ResultSet resultSet = selectAll.executeQuery();
            while (resultSet.next()) {
                System.out.println("load");
                membersBook.add(new Book(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                ));
            }
            table_book.setItems(membersBook);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table_book.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
            @Override
            public void changed(ObservableValue<? extends Book> observableValue, Book oldValue, Book newValue) {
                Book selectedItem = table_book.getSelectionModel().getSelectedItem();
                try {
                    connection = null;

                    try {
                        selectedId.setString(1, selectedItem.getId());
                        ResultSet resultSet = selectedId.executeQuery();
                        if (resultSet.next()) {
                            text_book_id.setText(resultSet.getString(1));
                            text_book_title.setText(resultSet.getString(2));
                            text_book_author.setText(resultSet.getString(3));
                            text_book_status.setText(resultSet.getString(4));
                            text_book_id.setDisable(true);
                            btn_add.setText("update");
                        }
                        btn_add.setText("update");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (NullPointerException e) {
                    return;
                }
            }
        });
    }

    public void button_new(ActionEvent actionEvent) throws SQLException {
        btn_add.setText("Add");
        text_book_status.setText("Available");
        text_book_status.setDisable(true);
        text_book_id.setDisable(false);
        text_book_author.clear();
        text_book_title.clear();
        text_book_title.requestFocus();
        ResultSet resultSet = newIdQuery.executeQuery();

        String ids = null;
        int maxId = 0;
        while (resultSet.next()) {
            ids = resultSet.getString(1);
            int id = Integer.parseInt(ids.replace("B", ""));
            if (id > maxId) {
                maxId = id;
            }
        }
        maxId = maxId + 1;
        String id = "";
        if (maxId < 10) {
            id = "B00" + maxId;
        } else if (maxId < 100) {
            id = "B0" + maxId;
        } else {
            id = "B" + maxId;
        }
        text_book_id.setText(id);

    }

    public void button_Add(ActionEvent actionEvent) throws SQLException {
        ObservableList<Book> books = FXCollections.observableList(DB.books);

        if (text_book_id.getText().isEmpty() ||
                text_book_title.getText().isEmpty() ||
                text_book_author.getText().isEmpty()
        ) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "please fill your details", ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            return;
        }

        //regex
        if (!(text_book_title.getText().matches("^\\b([A-Za-z.]+\\s?)+$") && text_book_author.getText().matches("^\\b([A-Za-z.]+\\s?)+$"))) {
            new Alert(Alert.AlertType.ERROR, "Enter valid Name").show();

            return;
        }
        if (btn_add.getText().equals("Add")) {
            addToTable.setString(1, text_book_id.getText());
            addToTable.setString(2, text_book_title.getText());
            addToTable.setString(3, text_book_author.getText());
            addToTable.setString(4, text_book_status.getText());

            int rowValues=addToTable.executeUpdate();
            if (rowValues>0){
                System.out.println("data insert successful");
            }
            else {
                System.out.println("Error");
            }
        }else {
            if (btn_add.getText().equals("update")){
                for (int i=0;i<books.size();i++){
                    if (text_book_id.getText().equals(books.get(i).getId())){
                        updateQuery.setString(1,text_book_title.getText());
                        updateQuery.setString(2,text_book_author.getText());
                        updateQuery.setString(3,text_book_status.getText());
                        updateQuery.setString(4,text_book_id.getText());
                        int rowValue=updateQuery.executeUpdate();
                        if (rowValue>0){
                            Alert alert=new Alert(Alert.AlertType.INFORMATION,"record updated!",ButtonType.OK);
                            Optional<ButtonType> buttonType=alert.showAndWait();
                        }
                        else {
                            Alert alert=new Alert(Alert.AlertType.ERROR,"update error!",ButtonType.OK);
                            Optional<ButtonType> buttonType=alert.showAndWait();
                        }
                    }
                }
                table_book.setItems(books);
            }

        }
        try{
            table_book.getItems().clear();
            initialize();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void button_delete(ActionEvent actionEvent) throws SQLException {
        Book selectedItem=table_book.getSelectionModel().getSelectedItem();
        if (table_book.getSelectionModel().isEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR,"please select a book",ButtonType.OK);
            Optional<ButtonType> buttonType=alert.showAndWait();
            return;
        }
        else {
            deleteQuery.setString(1,selectedItem.getId());
            int affected=deleteQuery.executeUpdate();
            if (affected>0){
                Alert alert=new Alert(Alert.AlertType.INFORMATION,"record deleted",ButtonType.OK);
                Optional<ButtonType>buttonType=alert.showAndWait();
            }
        }
        try {
            table_book.getItems().clear();
            initialize();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void image_back(MouseEvent mouseEvent) throws IOException {

            Parent root = FXMLLoader.load(new File("E:\\javaCode\\Project\\LibrarySystem\\LibrarySystem\\src\\main\\resources\\com\\example\\librarySystem\\HomeFormView.fxml").toURL());
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) this.book_root.getScene().getWindow();
            primaryStage.setScene(scene);

            TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
            tt.setFromX(-scene.getWidth());
            tt.setToX(0);
            tt.play();
        }

    }

