package mainpackage;

import grids.Grid;
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
import java.sql.Time;
import java.util.*;

public class RunSimulation {
    private String DATA_FILE = "data/locationConfig/segregation_hexagon_36x36.xml";
//    public static final String TITLE = "Cellular Automaton Simulation";
//    public static final int SIZE = 600;
//    public static final Paint BACKGROUND = Color.AZURE;

    private Timeline animation;
    private Scene myScene;
    private Group root = new Group();
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
//    private int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
//    private double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;


//    @Override
//    public void start(Stage stage){
//        // attach scene to the stage and display it
//        myScene = setupGame(SIZE, (int) (SIZE * 1.35), BACKGROUND);
//        s = stage;
//        stage.setScene(myScene);
//        stage.setTitle(TITLE);
//        stage.show();
//        attachGameLoop();
//    }

    RunSimulation(Timeline a) {
        animation = a;
    }

    public Group getNode() {
        setupSimulation();
        createUIComponents();
        root.getChildren().add(root_other);
        root.getChildren().add(root_grid);
        return root;
    }


    private void openFile(File f) {
        DATA_FILE = f.getAbsolutePath();
        root_other.getChildren().clear();
        root.getChildren().clear();
        setupSimulation();
        createUIComponents();
        root.getChildren().add(root_other);
        root.getChildren().add(root_grid);

    }

//    private void attachGameLoop() {
//        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
//        animation = new Timeline();
//        animation.setCycleCount(Timeline.INDEFINITE);
//        animation.getKeyFrames().add(frame);
//        animation.setRate(animation.getCycleDuration().toSeconds() * mySliders.get("speed").getValue());
//        animation.play();
//    }

//    private Scene setupGame(int width, int height, Paint background){
//        root = new Group();
//        Scene scene = new Scene(root, width, height, background);
//
//        setupSimulation();
//        createUIComponents();
//        root.getChildren().add(root_other);
//        root.getChildren().add(root_grid);
//        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
//        return scene;
//    }

    private void setupSimulation() {
        onInitialGrid = true;
        currentSimulation = new XMLParser("simType").getSimulation(new File(DATA_FILE));
        Grid initialGrid = currentSimulation.getMyGrid();
        newVisual = new Visualization(initialGrid.getHeight(), initialGrid.getWidth(), 1.0);
        root_grid.getChildren().add(newVisual.getRootNode(initialGrid));
    }

    private void createUIComponents() {
        // add other components (i.e. not grid)
        mySliders = createMySliders(currentSimulation, root_other);
        myLoadFileButton = createButton("Load simulation (.xml)", 50,510,false);
        myNextIterationButton = createButton(">", 450, 550, false);
        myResetButton = createButton("Reset", 450, 580, false);
        myStartButton = createButton("Start", 450, 610, true);
        myStopButton = createButton("Stop", 450, 640, true);;
        root_other.getChildren().addAll(myLoadFileButton, myNextIterationButton, myResetButton, myApplyButton, myStartButton, myStopButton);
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
            animation.setRate(animation.getCycleDuration().toSeconds() * mySliders.get("speed").getValue());
            carryOutApply(currentSimulation);
            //must update parameters
        });
        myLoadFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(s);
            if (selectedFile != null
                    && selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1).equals("xml")) {
                openFile(selectedFile);
            }
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
        int k = 0;
        for(String currentField : sim.getMySliderInfo().keySet()){
            //if(sim.getMyDataValues().get(currentField).equals("")) continue;
            double value = Double.parseDouble(sim.getMyDataValues().get(currentField));
            Slider slider = createSlider(30, 550 + k*40, Simulation.Bounds.valueOf(currentField).getMin(),
                    Simulation.Bounds.valueOf(currentField).getMax(), value);
            sliderMap.put(currentField, slider);
            Label label = new Label(currentField);
            label.setLayoutX(180);
            label.setLayoutY(550 + k*40);
            root.getChildren().addAll(label, slider);
            k++;
        }
        myApplyButton = createButton("Apply", 30, 550 + 40*k, false);
        return sliderMap;
    }

    private void carryOutApply(Simulation sim){
        Map<String, String> map = sim.getMyDataValues();
        for(String s : map.keySet()){
            if(mySliders.containsKey(s)) map.put(s, Double.toString(mySliders.get(s).getValue()));
        }
        sim.updateParameters(map);
    }

    private void renderNextIteration() {
        // render next iteration
        root_grid.getChildren().clear();
        Node n = newVisual.getRootNode(currentSimulation.advanceSimulation());
        root_grid.getChildren().add(n);
    }

    public void stepThru(double elapsedTime){
        // update grid
        // receive a Node from visualization class
        myNextIterationButton.setDisable(startedAnimation);
        myResetButton.setDisable(onInitialGrid);
        myStartButton.setDisable(startedAnimation);
        myStopButton.setDisable(!startedAnimation);

        for (String s : mySliders.keySet()) {
            mySliders.get(s).setDisable(startedAnimation);
        }

        myApplyButton.setDisable(startedAnimation);
        if (startedAnimation) renderNextIteration();
    }

//    private void handleKeyInput (KeyCode code) {
//        if (code == KeyCode.RIGHT) {
//            renderNextIteration();
//        }
//    }

    //create an interface of key and mouse inputs, which toggles a call to mainpackage.Simulation.initializeGrid

}
