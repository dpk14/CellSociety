package mainpackage;

import cells.Cell;
import grids.Grid;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import simulations.Simulation;

import java.sql.Time;
import java.util.*;

import static javafx.application.Application.launch;

public class Main extends Application {

    public static final String TITLE = "Cellular Automaton Simulation";
    public static final int WIDTH = 810;
    public static final int HEIGHT = 810;
    public static final Paint BACKGROUND = Color.AZURE;

    private int FRAMES_PER_SECOND = 15;
    private int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;


    private Map<Scene, RunSimulation> scenes = new HashMap<>();
    private Timeline animation = new Timeline();
    private Group root;

    @Override
    public void start(Stage primaryStage){
        // attach scene to the stage and display it
        Scene myScene = setupGame(WIDTH, HEIGHT, BACKGROUND);
        Scene myScene2 = setupGame(WIDTH, HEIGHT, BACKGROUND);
//        Scene myScene3 = setupGame(WIDTH, HEIGHT, BACKGROUND);

        for (Scene s : scenes.keySet()) {
            Stage s2 = new Stage();
            s2.setScene(s);
            s2.setTitle(TITLE);
            s2.show();
            attachGameLoop();
        }
    }

    private Scene setupGame(int width, int height, Paint background){
        root = new Group();

        RunSimulation r = new RunSimulation(animation);
        root = r.getNode();

        Scene scene = new Scene(root, width, height, background);
        scenes.put(scene, r);
        scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY(), r));
        return scene;
    }

    private void attachGameLoop() {
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        //animation.setRate(animation.getCycleDuration().toSeconds() * mySliders.get("speed").getValue());
        animation.play();
    }

    private void step(double elapsedTime){
        for (Map.Entry<Scene, RunSimulation> entry :  scenes.entrySet()) {
            RunSimulation y = entry.getValue();
            y.stepThru(elapsedTime);
        }
    }

    private void handleMouseInput (double x, double y, RunSimulation s) {
        //for (Map.Entry<Scene, RunSimulation> entry : scenes.entrySet()) {
            //RunSimulation s = entry.getValue();
        s.renderNextIterationFromClick(x, y);


//            for (int i = 0; i < g.getHeight(); i++) {
//                for (int j = 0; j < g.getWidth(); j++) {
//                    Cell c = g.getCell(i,j);
//
////                    System.out.println(c.getMyColor());
//                }
//            }
        //}
    }

    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT) { }
    }

    public static void main(String[] args){
        launch(args);
    }
}
