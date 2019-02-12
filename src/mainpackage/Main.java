package mainpackage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;

public class Main extends Application {

    public static final String TITLE = "Cellular Automaton Simulation";
    public static final int WIDTH = 810;
    public static final int HEIGHT = 810;
    public static final int WINDOW_BUTTON_X = 700;
    public static final int WINDOW_BUTTON_Y = 700;
    public static final int NEW_WINDOW_OFFSET = 40;
    public static final Paint BACKGROUND = Color.AZURE;
    private int FRAMES_PER_SECOND = 15;
    private int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private Map<Scene, RunSimulation> scenes = new HashMap<>();
    private Timeline animation = new Timeline();
    private Group root;
    private Stage startStage;

    /**
     * Starts simulation and initializes the the first scene
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage){
        setupGame(WIDTH, HEIGHT, BACKGROUND);
        for (Scene s : scenes.keySet()) {
            Stage stage = new Stage();
            startStage = stage;
            stage.setScene(s);
            stage.setTitle(TITLE);
            stage.show();
            attachGameLoop();
        }
    }

    private Scene setupGame(int width, int height, Paint background){
        root = new Group();
        RunSimulation r = new RunSimulation(animation);
        root = r.getNode();
        Button newWinButton = createButton("New Window", WINDOW_BUTTON_X,WINDOW_BUTTON_Y);
        newWinButton.setOnAction(event -> createAndInitializeNewWindow());
        root.getChildren().add(newWinButton);
        Scene scene = new Scene(root, width, height, background);
        scenes.put(scene, r);
        scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY(), r));
        return scene;
    }

    private void createAndInitializeNewWindow() {
        Scene newScene = setupGame(WIDTH, HEIGHT, BACKGROUND);
        Stage stage = new Stage();
        stage.setScene(newScene);
        stage.setX(startStage.getX() + scenes.size() * NEW_WINDOW_OFFSET);
        stage.setY(startStage.getY() + scenes.size() * NEW_WINDOW_OFFSET);
        stage.setTitle(TITLE);
        stage.show();
        attachGameLoop();
    }

    private void attachGameLoop() {
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void step(double elapsedTime){
        for (Map.Entry<Scene, RunSimulation> entry :  scenes.entrySet()) {
            RunSimulation y = entry.getValue();
            y.stepThru();
        }
    }

    private void handleMouseInput (double x, double y, RunSimulation s) {
        s.renderNextIterationFromClick(x, y);
    }

    private Button createButton(String text, double x, double y){
        Button button = new Button(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        return button;
    }

    /**
     * Main
     * @param args
     */
    public static void main(String[] args){
        launch(args);
    }
}
