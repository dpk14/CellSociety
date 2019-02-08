package mainpackage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

import static javafx.application.Application.launch;

public class Main extends Application {

    public static final String TITLE = "Cellular Automaton Simulation";
    public static final int SIZE = 600;
    public static final Paint BACKGROUND = Color.AZURE;

    private int FRAMES_PER_SECOND = 15;
    private int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;


    private List<RunSimulation> simulations;
    private Stage s;
    private Timeline animation = new Timeline();
    private Scene myScene;
    private Group root;


    private RunSimulation r;

    @Override
    public void start(Stage stage){
        // attach scene to the stage and display it
        myScene = setupGame(SIZE, (int) (SIZE * 1.35), BACKGROUND);
        s = stage;
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();
        attachGameLoop();
    }

    private Scene setupGame(int width, int height, Paint background){
        root = new Group();

        r = new RunSimulation(animation);
        root = r.getNode();

        Scene scene = new Scene(root, width, height, background);

        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
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
        r.stepThru(elapsedTime);
    }

    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT) { }
    }

    public static void main(String[] args){
        launch(args);
    }
}
