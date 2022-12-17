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
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Label congrats;

    @FXML
    private Label ScoreText;

    @FXML
    private Label Lifes;

    @FXML
    private Rectangle paddle;

    @FXML
    private AnchorPane scene;

    @FXML
    private Circle circle;

    //@FXML
    //private Rectangle paddle;

    @FXML
    private Rectangle bottomZone;

    @FXML
    private Button startButton;

    @FXML
    private Label EnglishIndicatorLabel;

    //@FXML
    //private Text score;

    Robot robot = new Robot();
    //private int scoreCounter = 0;
    private ArrayList<Rectangle> bricks = new ArrayList<>();

    double deltaX = -1;
    double deltaY = -3;
    int initscore = 0;
    int initlifes = 3;

    //1 Frame evey 10 millis, which means 100 FPS
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            movePaddle();
            checkCollisionPaddle(paddle);
            circle.setLayoutX(circle.getLayoutX() + deltaX);
            circle.setLayoutY(circle.getLayoutY() + deltaY);

            if(!bricks.isEmpty()){
                bricks.removeIf(brick -> checkCollisionBrick(brick));
            } else {
                congrats.setText("Congratulations!");
                timeline.stop();
                startButton.setVisible(true);
            }
            checkCollisionScene(scene);
            if(circle.getBoundsInParent().intersects(bottomZone.getBoundsInParent()))
            {
                if(initlifes == 0){
                    checkCollisionBottomZone();
                }
                else{
                    initlifes -= 1;
                    Lifes.setText(String.format("%d", initlifes));
                    circle.setLayoutX(300);
                    circle.setLayoutY(27);
                }
            }
        }
    }));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    @FXML
    void startGameButtonAction(ActionEvent event) {
        startButton.setVisible(false);
        congrats.setText("");
        startGame();
        initscore = 0;
        initlifes = 3;
        ScoreText.setText(String.format("%d", initscore));
        Lifes.setText(String.format("%d", initlifes));
    }

    public void startGame(){
        createBricks();
        timeline.play();
    }

    public void checkCollisionScene(Node node){
        Bounds bounds = node.getBoundsInLocal();
        boolean rightBorder = circle.getLayoutX() >= (bounds.getMaxX() - circle.getRadius());
        boolean leftBorder = circle.getLayoutX() <= (bounds.getMinX() + circle.getRadius());
        boolean bottomBorder = circle.getLayoutY() >= (bounds.getMaxY() - circle.getRadius());
        boolean topBorder = circle.getLayoutY() <= (bounds.getMinY() + circle.getRadius());

        if (rightBorder || leftBorder) {
            deltaX *= -1;
        }
        if (bottomBorder || topBorder) {
            deltaY *= -1;
        }
    }

    public boolean checkCollisionBrick(Rectangle brick){
        if(circle.getBoundsInParent().intersects(brick.getBoundsInParent())){
            boolean rightBorder = circle.getLayoutX() >= ((brick.getX() + brick.getWidth()) - circle.getRadius());
            boolean leftBorder = circle.getLayoutX() <= (brick.getX() + circle.getRadius());
            boolean bottomBorder = circle.getLayoutY() >= ((brick.getY() + brick.getHeight()) - circle.getRadius());
            boolean topBorder = circle.getLayoutY() <= (brick.getY() + circle.getRadius());

            if (rightBorder || leftBorder) {
                deltaX *= -1;
            }
            if (bottomBorder || topBorder) {
                deltaY *= -1;
            }
            scene.getChildren().remove(brick);
            initscore += 1;
            ScoreText.setText(String.format("%d", initscore));
            return true;
        }
        return false;
    }

    public void createBricks(){
        double width = 560;
        double height = 200;

        int spaceCheck = 1;

        for (double i = height; i > 0 ; i = i - 500) {
            for (double j = width; j > 0 ; j = j - 150) {
                if(spaceCheck % 2 == 0){
                    Rectangle rectangle = new Rectangle(j,i,30,30);
                    rectangle.setFill(Color.ORANGE);
                    scene.getChildren().add(rectangle);
                    bricks.add(rectangle);
                }
                spaceCheck++;
            }
        }
    }

    public void movePaddle(){
        Bounds bounds = scene.localToScreen(scene.getBoundsInLocal());
        double sceneXPos = bounds.getMinX();

        double xPos = robot.getMouseX();
        double paddleWidth = paddle.getWidth();

        if(xPos >= sceneXPos + (paddleWidth/2) && xPos <= (sceneXPos + scene.getWidth()) - (paddleWidth/2)){
            paddle.setLayoutX(xPos - sceneXPos - (paddleWidth/2));
        } else if (xPos < sceneXPos + (paddleWidth/2)){
            paddle.setLayoutX(0);
        } else if (xPos > (sceneXPos + scene.getWidth()) - (paddleWidth/2)){
            paddle.setLayoutX(scene.getWidth() - paddleWidth);
        }
    }

    public void checkCollisionPaddle(Rectangle paddle2){

        if(circle.getBoundsInParent().intersects(paddle2.getBoundsInParent())){

            boolean rightBorder = circle.getLayoutX() >= ((paddle2.getLayoutX() + paddle.getWidth()) - circle.getRadius());
            boolean leftBorder = circle.getLayoutX() <= (paddle2.getLayoutX() + circle.getRadius());
            boolean bottomBorder = circle.getLayoutY() >= ((paddle2.getLayoutY() + paddle.getHeight()) - circle.getRadius());
            boolean topBorder = circle.getLayoutY() <= (paddle2.getLayoutY() + circle.getRadius());

            if (rightBorder || leftBorder) {
                deltaX *= -1;
            }
            if (bottomBorder || topBorder) {
                deltaY *= -1;
            }
        }
    }

    public void checkCollisionBottomZone(){
        if(circle.getBoundsInParent().intersects(bottomZone.getBoundsInParent())){
            timeline.stop();
            bricks.forEach(brick -> scene.getChildren().remove(brick));
            bricks.clear();
            startButton.setVisible(true);
            initscore = 0;
            ScoreText.setText("");
            Lifes.setText("");
            deltaX = -1;
            deltaY = -3;
            circle.setLayoutX(300);
            circle.setLayoutY(300);
            // System.out.println("Game over!");
        }
    }
}
