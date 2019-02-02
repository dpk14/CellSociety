package mainpackage;

import cells.Cell;
import cells.StateChangeCell;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import simulations.*;
import simulations.Simulation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RunSimulation extends Application {
    public static final String DATA_FILE = "data/initial_percolation1.xml";


    public static final String TITLE = "";
    public static final int SIZE = 600;


    public static final Paint BACKGROUND = Color.AZURE;

    private Timeline animation;
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
    private Button myApplyButton;
    private Button myNextIterationButton;
    private Button myLoadFileButton;
    private Map<String, Slider> mySliders;
    private Slider slider1;
    private Slider slider2;
    private Slider slider3;
    private Label label1;
    private Label label2;
    private Label label3;

    private Stage s;

    private FileChooser fileChooser;

    // state fields
    private boolean onInitialGrid = true;
    private boolean startedSliding = false;
    private boolean startedAnimation = false;

    private int FRAMES_PER_SECOND = 15;
    private int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;


    @Override
    public void start(Stage stage){
        // attach scene to the stage and display it
        myScene = setupGame(SIZE, (int) (SIZE * 1.2), BACKGROUND);
        s = stage;
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();

//        fileChooser = new FileChooser();
//        fileChooser.setTitle("Open Resource File");
//        fileChooser.showOpenDialog(stage);

        // attach "game loop" to timeline to play it
        FileChooser fileChooser = new FileChooser();
        myLoadFileButton = new Button("Load simulation (.xml)");
        myLoadFileButton.setLayoutX(50);
        myLoadFileButton.setLayoutY(510);
        myLoadFileButton.setDisable(false);
        //myLoadFileButton = new Button("Load simulation (.xml)");
        myLoadFileButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(s);
            System.out.println(selectedFile.toString());
        });
        root_other.getChildren().add(myLoadFileButton);




        attachGameLoop();
    }

    private void attachGameLoop() {
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene setupGame(int width, int height, Paint background){
        root = new Group();
        Scene scene = new Scene(root, width, height, background);



        /**
         * Segregation testing
         * **/
        setupSimulation();
        createUIComponents();



        root.getChildren().add(root_other);
        root.getChildren().add(root_grid);

        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }



    private void setupSimulation() {
        onInitialGrid = true;
        currentSimulation = new XMLParser("simType").getSimulation(new File(DATA_FILE));
        Cell[][] initialGrid = currentSimulation.getMyGrid();

        System.out.println(initialGrid[0][0] instanceof Cell);
        newVisual = new Visualization(initialGrid.length, initialGrid[0].length, 1.0);
        root_grid.getChildren().add(newVisual.getRootNode(initialGrid));
    }

    private void createUIComponents() {
        // add other components (i.e. not grid) MICHAEL: I WILL MOVE THIS TO VISUALIZATION I THINK
        mySliders = new HashMap<>();
        label1 = new Label("Speed");
        label1.setLayoutX(200);
        label1.setLayoutY(550);
        root_other.getChildren().add(label1);
        int sliderCounter = 4;

        System.out.println(currentSimulation.getMyDataValues().size());
        for(int k = 0; k < currentSimulation.getMyDataValues().size() - 4; k++){
            String currentField = currentSimulation.getDataFields().get(sliderCounter);
            double value = Double.parseDouble(currentSimulation.getMyDataValues().get(currentField));
            System.out.println(value);
            Slider slider = createSlider(30, 550 + k*20, 0, 100, value);
            mySliders.put(currentField, slider);
            root_other.getChildren().add(slider);
            sliderCounter++;
        }

//        slider1 = createSlider(30, 550, 1, 100, 15);
//        slider1.setDisable(false);
//        root_other.getChildren().add(slider1);
//
//        slider2 = createSlider(30, 580, 0, 100, 50);
//        slider2.setDisable(true);
//        root_other.getChildren().add(slider2);
//
//        slider3 = createSlider(30, 610, 0, 1, 0);
//        slider3.setDisable(true);
//        root_other.getChildren().add(slider3);


        myNextIterationButton = new Button(">");

        myNextIterationButton.setLayoutX(400);
        myNextIterationButton.setLayoutY(510);
        myNextIterationButton.setDisable(false);
        root_other.getChildren().add(myNextIterationButton);

        myResetButton = new Button("Reset");
        myResetButton.setLayoutX(450);
        myResetButton.setLayoutY(580);
        myResetButton.setDisable(false);
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

        myApplyButton = new Button("Apply");
        myApplyButton.setLayoutX(30);
        myApplyButton.setLayoutY(640);
        myApplyButton.setDisable(false);
        root_other.getChildren().add(myApplyButton);



        myNextIterationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onInitialGrid = false;
                renderNextIteration();
            }
        });
        myResetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setupSimulation();
            }
        });
        myStartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startedAnimation = true;
                onInitialGrid = false;
            }
        });
        myStopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startedAnimation = false;
                onInitialGrid = false;
            }
        });

        myApplyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //System.out.println(animation.getCycleDuration().toSeconds());
                animation.setRate(animation.getCycleDuration().toSeconds() * mySliders.get("speed").getValue());
            }
        });


    }

    private Button createButton(String text, int x, int y, boolean setDisable){
        Button button = new Button();
        button = new Button(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setDisable(setDisable);
        return button;
    }

    private Slider createSlider(int x, int y, double min, double max, double value){
        Slider slider = new Slider(0,1,1);
        slider.setLayoutX(x);
        slider.setLayoutY(y);
        slider.setMin(min);
        slider.setMax(max);
        slider.setMajorTickUnit((max - min)/2);
        slider.setValue(value);
        slider.setShowTickLabels(true);
        return slider;
    }

    private void renderNextIteration() {
        // render next iteration
        root_grid.getChildren().clear();
        Node n = newVisual.getRootNode(currentSimulation.updateGrid());
        root_grid.getChildren().add(n);
    }

    private void step(double elapsedTime){
        // update grid
        // receive a Node from visualization class
        //System.out.println("stepping");
        myNextIterationButton.setDisable(startedAnimation);
        myResetButton.setDisable(onInitialGrid);
        myStartButton.setDisable(startedAnimation);
        myStopButton.setDisable(!startedAnimation);
        mySliders.get("speed").setDisable(startedAnimation);
        myApplyButton.setDisable(startedAnimation);

        if (startedAnimation) renderNextIteration();


        //Node n = newVisual.getRootNode()
    }

    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT) {
            renderNextIteration();
        }
    }

    public static void main(String[] args){
        launch(args);
    }

    //create an interface of key and mouse inputs, which toggles a call to mainpackage.Simulation.initializeGrid

}
