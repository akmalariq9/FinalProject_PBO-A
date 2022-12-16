package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private AnchorPane scene;

    @FXML
    private Circle circle;

    @FXML
    private Rectangle paddle;

    @FXML
    private Rectangle bottomZone;

    @FXML
    private Button startButton;

    @FXML
    private Text score;

    Robot robot = new Robot();
    private int scoreCounter = 0;
    private ArrayList<Rectangle> bricks = new ArrayList<>();

    double deltaX = -1;
    double deltaY = -3;