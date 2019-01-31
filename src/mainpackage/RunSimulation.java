package mainpackage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RunSimulation extends Application {
    public static final String TITLE = "";
    public static final int SIZE = 600;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;




    private Scene myScene;
    private Group root;

    private Visualization newVisual;


    @Override
    public void start(Stage stage){
        // attach scene to the stage and display it
        myScene = setupGame(SIZE, (int) (SIZE * 1.1), BACKGROUND);

        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();

        // attach "game loop" to timeline to play it
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene setupGame(int width, int height, Paint background){
        root = new Group();
        Scene scene = new Scene(root, width, height, background);

        newVisual = new Visualization(10,10,30,30,1);
        Cell[][] initialGrid = newVisual.getInitialGrid("SEGREGATION");
        Node currentIterationView = newVisual.getRootNode(initialGrid);


        root.getChildren().add(currentIterationView);

        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }
    private void step(double elapsedTime){
        // update grid
        // receive a Node from visualization class

        //Node n = newVisual.getRootNode()
    }

    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT) {
            root.getChildren().clear();
            Node n = newVisual.getRootNode(newVisual.getCurrentSimType().updateGrid());
            root.getChildren().add(n);
        }
    }

    public static void main(String[] args){
        launch(args);


        // For debugging purposes, just use a for loop for now




        //


    }

    //create an interface of key and mouse inputs, which toggles a call to mainpackage.Simulation.initializeGrid

}
