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

public class MainMenuController {

    @FXML
    private Button AboutButton;

    @FXML
    private Button CreditButton;

    @FXML
    private AnchorPane scene;

    @FXML
    void AboutButtonAction(ActionEvent event) {

    }

    @FXML
    void CreditButtonAction(ActionEvent event) {

    }

    @FXML
    void PlayGame(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("PlayGame.fxml"));
        Stage window = (Stage)(((Node) event.getSource()).getScene().getWindow());
        window.setTitle("Final Project_Brick Breaker!");
        window.setScene(new Scene(root, 900, 600));
        window.show();
    }

}
