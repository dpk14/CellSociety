package mainpackage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class RunSimulation extends Application {
    public static final String DATA_FILE = "data/initial_segregation1.xml";


    public static final String TITLE = "";
    public static final int SIZE = 600;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;


    private Scene myScene;
    private Group root;
    private Group root_grid = new Group();
    private Group root_other = new Group();

    private Visualization newVisual;
    private Simulation currentSimulation;


    // UI components
    private Button myResetButton;
    private Button myStartButton;
    private Button myStopButton;
    private Button myNextIterationButton;
    private Button myLoadFileButton;
    private Slider slider1;
    private Slider slider2;
    private Slider slider3;
    private FileChooser fileChooser;



    @Override
    public void start(Stage stage){
        // attach scene to the stage and display it
        myScene = setupGame(SIZE, (int) (SIZE * 1.2), BACKGROUND);

        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();

//        fileChooser = new FileChooser();
//        fileChooser.setTitle("Open Resource File");
//        fileChooser.showOpenDialog(stage);


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
        createUIComponents();

        currentSimulation = new XMLParser("simType").getSimulation(new File(DATA_FILE));
        System.out.println(((SegregationSimulation) currentSimulation).mySatisfactionThreshold);
        // need to receive speed from xml

        currentSimulation.setupSimulation();
        Cell[][] initialGrid = currentSimulation.getMyGrid();

        newVisual = new Visualization(initialGrid.length,initialGrid[0].length,1.0);
        root_grid.getChildren().add(newVisual.getRootNode(initialGrid));

        root.getChildren().add(root_other);
        root.getChildren().add(root_grid);


        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    private void createUIComponents() {
        // add other components (i.e. not grid) MICHAEL: I WILL MOVE THIS TO VISUALIZATION I THINK

        slider1 = new Slider(0,1,1);
        slider1.setLayoutX(30);
        slider1.setLayoutY(550);
        slider1.setDisable(true);
        root_other.getChildren().add(slider1);

        slider2 = new Slider(0,1,1);
        slider2.setLayoutX(30);
        slider2.setLayoutY(580);
        slider2.setDisable(true);
        root_other.getChildren().add(slider2);

        slider3 = new Slider(0,1,1);
        slider3.setLayoutX(30);
        slider3.setLayoutY(610);
        slider3.setDisable(true);
        root_other.getChildren().add(slider3);

        myLoadFileButton = new Button("Load simulation (.xml)");
        myLoadFileButton.setLayoutX(50);
        myLoadFileButton.setLayoutY(510);
        myLoadFileButton.setDisable(true);
        root_other.getChildren().add(myLoadFileButton);

        myNextIterationButton = new Button(">");
        myNextIterationButton.setLayoutX(400);
        myNextIterationButton.setLayoutY(510);
        myNextIterationButton.setDisable(true);
        root_other.getChildren().add(myNextIterationButton);

        myResetButton = new Button("Reset");
        myResetButton.setLayoutX(450);
        myResetButton.setLayoutY(580);
        myResetButton.setDisable(true);
        root_other.getChildren().add(myResetButton);

        myStartButton = new Button("Start");
        myStartButton.setLayoutX(450);
        myStartButton.setLayoutY(610);
        myStartButton.setDisable(true);
        root_other.getChildren().add(myStartButton);

        myStopButton = new Button("Stop");
        myStopButton.setLayoutX(450);
        myStopButton.setLayoutY(640);
        myStopButton.setDisable(true);
        root_other.getChildren().add(myStopButton);
    }

    private void step(double elapsedTime){
        // update grid
        // receive a Node from visualization class

        //Node n = newVisual.getRootNode()
    }

    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT) {
            root_grid.getChildren().clear();
            Node n = newVisual.getRootNode(currentSimulation.updateGrid());
            root_grid.getChildren().add(n);
        }
    }

    public static void main(String[] args){
        launch(args);


        // For debugging purposes, just use a for loop for now




        //


    }

    //create an interface of key and mouse inputs, which toggles a call to mainpackage.Simulation.initializeGrid

}
