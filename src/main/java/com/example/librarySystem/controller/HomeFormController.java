package controller;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

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
                    root = FXMLLoader.load(this.getClass().getResource("/View/MembersFormView.fxml"));
                    break;
                case "books":
                    root = FXMLLoader.load(this.getClass().getResource("/View/BooksFormView.fxml"));
                    break;
                case "issue":
                    root = FXMLLoader.load(this.getClass().getResource("/View/BookIssueFormView.fxml"));
                    break;
                case "bk_return":
                    root = FXMLLoader.load(this.getClass().getResource("/View/BookReturnFormView.fxml"));
                    break;
                case "bk_search":
                    root = FXMLLoader.load(this.getClass().getResource("/View/BookSearchFormView.fxml"));
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
