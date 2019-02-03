package mainpackage;

import cells.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
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
import simulations.Simulation;

import java.io.File;
import java.util.*;

public class RunSimulation extends Application {
//    public static final String DATA_FILE = "data/initial_gameoflife1.xml";
//    public static final String DATA_FILE = "data/initial_gameoflife2.xml";
//    public static final String DATA_FILE = "data/initial_spreadingfire1.xml";
    public static final String DATA_FILE = "data/initial_spreadingfire2.xml";
//    public static final String DATA_FILE = "data/initial_percolation1.xml";
//    public static final String DATA_FILE = "data/initial_percolation2.xml";
//    public static final String DATA_FILE = "data/initial_segregation1.xml";
//    public static final String DATA_FILE = "data/initial_segregation2.xml";
//    public static final String DATA_FILE = "data/initial_watorworld1.xml";

    public static final String TITLE = "Cellular Automaton Simulation";
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
        myScene = setupGame(SIZE, (int) (SIZE * 1.35), BACKGROUND);
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
        myLoadFileButton.setLayoutX(30);
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
        mySliders = createMySliders(currentSimulation, root_other);
        myNextIterationButton = createButton(">", 450, 550, false);
        myResetButton = createButton("Reset", 450, 580, false);
        myStartButton = createButton("Start", 450, 610, true);
        myStopButton = createButton("Stop", 450, 640, true);;
        root_other.getChildren().addAll(myNextIterationButton, myResetButton, myApplyButton, myStartButton, myStopButton);
        setButtonHandlers();
    }

    private void setButtonHandlers(){
        myNextIterationButton.setOnAction(event -> {
            onInitialGrid = false;
            renderNextIteration();
        });
        myResetButton.setOnAction(event -> setupSimulation());
        myStartButton.setOnAction(event -> {
            startedAnimation = true;
            onInitialGrid = false;
        });
        myStopButton.setOnAction(event -> {
            startedAnimation = false;
            onInitialGrid = false;
        });
        myApplyButton.setOnAction(event -> {
            //System.out.println(animation.getCycleDuration().toSeconds());
            animation.setRate(animation.getCycleDuration().toSeconds() * mySliders.get("speed").getValue());
            carryOutApply(currentSimulation);
        });
    }

    private Button createButton(String text, double x, double y, boolean setDisable){
        Button button = new Button(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setDisable(setDisable);
        return button;
    }

    private Slider createSlider(double x, double y, double min, double max, double value){
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

    /**
     * Given a simulation, a map of sliders is created matching the simulation's data fields. The apply button is also
     * initialized and located under the last slider
     * @param sim - the current simulation
     * @param root - the root that the sliders will be attached to
     * @return
     */
    private HashMap<String, Slider> createMySliders(Simulation sim, Group root){
        HashMap<String, Slider> sliderMap = new LinkedHashMap<>();
        double applyButtonY = 0;
        int sliderCounter = 4; // first important data field (comes after title, author, rows, columns...)
        for(int k = 0; k < sim.getMyDataValues().size() - 4; k++){
            String currentField = sim.getDataFields().get(sliderCounter);
            double value = Double.parseDouble(sim.getMyDataValues().get(currentField));
            Slider slider = createSlider(30, 550 + k*40, Simulation.Bounds.valueOf(currentField).getMin(),
                    Simulation.Bounds.valueOf(currentField).getMax(), value);
            sliderMap.put(currentField, slider);
            Label label = new Label(currentField);
            label.setLayoutX(180);
            label.setLayoutY(550 + k*40);
            root.getChildren().addAll(label, slider);
            if(k == sim.getMyDataValues().size() - 5) applyButtonY = 550 + ++k*40;
            sliderCounter++;
        }
        myApplyButton = createButton("Apply", 30, applyButtonY, false);
        return sliderMap;
    }

    private void carryOutApply(Simulation sim){
        Map<String, String> map = sim.getMyDataValues();
        for(String s : map.keySet()){
            System.out.println(s);
            if(mySliders.containsKey(s)) map.put(s, Double.toString(mySliders.get(s).getValue()));
        }
        sim.updateParameters(map);
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
