package sample;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LevelController {

    @FXML
    private Button Easy;

    @FXML
    private Button Hard;

    @FXML
    private Button Medium;

    @FXML
    private AnchorPane scene;

    @FXML
    void EasyButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Easy.fxml"));
        Stage window = (Stage)(((Node) event.getSource()).getScene().getWindow());
        window.setTitle("Final Project_Brick Breaker!");
        window.setScene(new Scene(root, 900, 600));
        window.show();
    }

    @FXML
    void MediumButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Medium.fxml"));
        Stage window = (Stage)(((Node) event.getSource()).getScene().getWindow());
        window.setTitle("Final Project_Brick Breaker!");
        window.setScene(new Scene(root, 900, 600));
        window.show();
    }

    @FXML
    void hardButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Hard.fxml"));
        Stage window = (Stage)(((Node) event.getSource()).getScene().getWindow());
        window.setTitle("Final Project_Brick Breaker!");
        window.setScene(new Scene(root, 900, 600));
        window.show();

    }

}
