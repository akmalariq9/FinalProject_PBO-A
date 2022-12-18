package sample;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
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
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

public class PlayGameController implements Initializable {

    @FXML
    private Label AfterPlayText;

    @FXML
    private ImageView AreYouReady;

    @FXML
    private Button StartButton2;

    @FXML
    private Label SumScore;
    @FXML
    private Label lvlind;
    @FXML
    private Label ScoreText;

    @FXML
    private Label Lifes;

    @FXML
    private ImageView paddle;

    @FXML
    private AnchorPane scene;

    @FXML
    private Circle circle;

    @FXML
    private ImageView red_amogus;

    @FXML
    private ImageView cyan_amogus;

    @FXML
    private Rectangle bottomZone;

    @FXML
    private Button startButton;
    
    @FXML
    private Button next;

    @FXML
    private Label tamat;

    @FXML
    private Label EnglishIndicatorLabel;

    Robot robot = new Robot();
    private ArrayList<Rectangle> bricks = new ArrayList<>();
    private ImageView[] playerLifes;

    private String urlLife = "/resources/heart.png";
    double width;
    double height;
    double deltaX = 1;
    double deltaY = 3;
    int initscore = 0;
    int initlifes = 2;
    int lvl = 1;
    //1 Frame evey 10 millis, which means 100 FPS
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
    	@Override
        public void handle(ActionEvent actionEvent){
            movePaddle();
            checkCollisionPaddle(paddle);
            circle.setLayoutX(circle.getLayoutX() + deltaX);
            circle.setLayoutY(circle.getLayoutY() + deltaY);

            if(!bricks.isEmpty()){
                bricks.removeIf(brick -> checkCollisionBrick(brick));
            } else {
                AfterPlayText.setText("Congratulations!");
                timeline.stop();
                next.setVisible(true);
            }
            checkCollisionScene(scene);
            if(circle.getBoundsInParent().intersects(bottomZone.getBoundsInParent()))
            {
                removeLife();
                circle.setLayoutX(450);
                circle.setLayoutY(45);
            }
        }
    }));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeline.setCycleCount(Animation.INDEFINITE);
        next.setVisible(false);
        amogusMoveRotate(cyan_amogus);
        amogusMoveRotate(red_amogus);
    }

    @FXML
    void startGameButtonAction(ActionEvent event) {
        startButton.setVisible(false);
        StartButton2.setVisible(false);
        AreYouReady.setVisible(false);
        createGameElements(urlLife);
        amogusMoveTranslation(red_amogus, 10000, 850); // red = 1000 ; cyan = 2000
        amogusMoveRotate(red_amogus);
        amogusMoveTranslation(cyan_amogus, 10000, -850);
        amogusMoveRotate(cyan_amogus);
        startGame();
        initscore = 0;
        initlifes = 2;
        lvlind.setText(String.format("%d",lvl));
        ScoreText.setText(String.format("%d", initscore));
    }


    @FXML
    void nextLvl(ActionEvent event) {
        height = height +50;
        lvl = lvl+1;
        deltaX=deltaX+3;
        deltaY=deltaY+3;
    	createBricks(height,width);
    	timeline.play();
        next.setVisible(false);
        AfterPlayText.setVisible(false);
    }

    public void startGame(){
    	width = 860;
    	height = 100;
        createBricks(height,width);
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
            initscore += 50;
            ScoreText.setText(String.format("%d", initscore));
            lvlind.setText(String.format("%d", lvl));

            return true;
        }
        lvlind.setText(String.format("%d", lvl));
        return false;
    }

    public void createBricks(double tinggi, double lebar){

        int spaceCheck = 1;

        for (double i = tinggi; i > 0 ; i = i - 50) {
            for (double j = lebar; j > 0 ; j = j - 25) {
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
        double paddleWidth = paddle.getFitWidth();

        if(xPos >= sceneXPos + (paddleWidth/2) && xPos <= (sceneXPos + scene.getWidth()) - (paddleWidth/2)){
            paddle.setLayoutX(xPos - sceneXPos - (paddleWidth/2));
        } else if (xPos < sceneXPos + (paddleWidth/2)){
            paddle.setLayoutX(0);
        } else if (xPos > (sceneXPos + scene.getWidth()) - (paddleWidth/2)){
            paddle.setLayoutX(scene.getWidth() - paddleWidth);
        }
    }

    public void checkCollisionPaddle(ImageView paddle2){

        if(circle.getBoundsInParent().intersects(paddle2.getBoundsInParent())){

            boolean rightBorder = circle.getLayoutX() >= ((paddle2.getLayoutX() + paddle.getFitWidth()) - circle.getRadius());
            boolean leftBorder = circle.getLayoutX() <= (paddle2.getLayoutX() + circle.getRadius());
            boolean bottomBorder = circle.getLayoutY() >= ((paddle2.getLayoutY() + paddle.getFitHeight()) - circle.getRadius());
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
            AfterPlayText.setText("Game Over :(");
            SumScore.setText(String.format("Your Score : %d", initscore));
            bricks.forEach(brick -> scene.getChildren().remove(brick));
            bricks.clear();
            StartButton2.setVisible(true);
            
            lvl = 1;
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
    
    @FXML
    void StartButton2Action(ActionEvent event) {
        lvl = 1;
    	initlifes = 2;
        createGameElements(urlLife);
        startGame();
        StartButton2.setVisible(false);
        AfterPlayText.setText("");
        SumScore.setText("");
    }

    private void createGameElements(String urlLife) {
		initlifes = 2;
		playerLifes = new ImageView[3];
		
		for(int i = 0; i < playerLifes.length; i++) {
			playerLifes[i] = new ImageView(urlLife);
            playerLifes[i].setScaleX(0.025);
            playerLifes[i].setScaleY(0.025);
			playerLifes[i].setLayoutX(-955 + (i * 60));
			playerLifes[i].setLayoutY(-655);
			scene.getChildren().add(playerLifes[i]);	
		}
    }
    
    private void removeLife() {
		scene.getChildren().remove(playerLifes[initlifes]);
		initlifes -= 1;
		if(initlifes < 0) {
			timeline.stop();
            AfterPlayText.setText("Game Over :(");
            SumScore.setText(String.format("Your Score : %d", initscore));
            bricks.forEach(brick -> scene.getChildren().remove(brick));
            bricks.clear();
            StartButton2.setVisible(true);
            lvl = 1;
            initscore = 0;
            ScoreText.setText("");
            Lifes.setText("");
            deltaX = -1;
            deltaY = -3;
            circle.setLayoutX(300);
            circle.setLayoutY(300);
		}
	}

    public void amogusMoveTranslation(ImageView amogus, double speed, double x) {
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(amogus);
        translate.setDuration(Duration.millis(speed));
        translate.setCycleCount(TranslateTransition.INDEFINITE);
        translate.setByX(x);
        translate.setByY(-150);
        translate.setAutoReverse(true);
        translate.play();
    }


    public void amogusMoveRotate(ImageView amogus) {
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(amogus);
        rotate.setDuration(Duration.millis(5000));
        rotate.setCycleCount(TranslateTransition.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setByAngle(360);
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.play();
    }
}
