package mainpackage;

import grids.Grid;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import simulations.Simulation;

import java.io.File;
import java.util.*;

public class RunSimulation {

    public static final int btnXPosition = 10;
    public static final int btnYPosition = 520;
    public static final int slidersXPosition = 510;

    private String DATA_FILE = "data/locationConfig/spreadingfire_hexagon_36x36.xml";
    private Timeline animation;
    private Group root = new Group();
    private Group root_grid = new Group();
    private Group root_other = new Group();
    private Group root_graph = new Group();

    private Visualization newVisual;
    private Simulation currentSimulation;

    // UI components
    private Button myResetButton;
    private Button myStartButton;
    private Button myStopButton;
    private Button myApplyButton;
    private Button myNextIterationButton;
    private Button myLoadFileButton;
    private Button myNewWindowButton;
    private Map<String, Slider> mySliders;

    private Stage s;

    private FileChooser fileChooser;

    // state fields
    private boolean onInitialGrid = true;
    private boolean startedSliding = false;
    private boolean startedAnimation = false;

    RunSimulation(Timeline a) {
        animation = a;
    }

    public Group getNode() {
        setupSimulation();
        createUIComponents();


        createGraph();

        root.getChildren().add(root_other);
        root.getChildren().add(root_grid);
        root.getChildren().add(root_graph);
        return root;
    }

    private void createGraph() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        final LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Portfolio 1");
        series1.getData().add(new XYChart.Data("Jan", 23));
        series1.getData().add(new XYChart.Data("Feb", 14));
        series1.getData().add(new XYChart.Data("Mar", 15));
        series1.getData().add(new XYChart.Data("Apr", 24));
        series1.getData().add(new XYChart.Data("May", 34));
        series1.getData().add(new XYChart.Data("Jun", 36));
        series1.getData().add(new XYChart.Data("Jul", 22));
        series1.getData().add(new XYChart.Data("Aug", 45));
        series1.getData().add(new XYChart.Data("Sep", 43));
        series1.getData().add(new XYChart.Data("Oct", 17));
        series1.getData().add(new XYChart.Data("Nov", 29));
        series1.getData().add(new XYChart.Data("Dec", 25));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Portfolio 2");
        series2.getData().add(new XYChart.Data("Jan", 33));
        series2.getData().add(new XYChart.Data("Feb", 34));
        series2.getData().add(new XYChart.Data("Mar", 25));
        series2.getData().add(new XYChart.Data("Apr", 44));
        series2.getData().add(new XYChart.Data("May", 39));
        series2.getData().add(new XYChart.Data("Jun", 16));
        series2.getData().add(new XYChart.Data("Jul", 55));
        series2.getData().add(new XYChart.Data("Aug", 54));
        series2.getData().add(new XYChart.Data("Sep", 48));
        series2.getData().add(new XYChart.Data("Oct", 27));
        series2.getData().add(new XYChart.Data("Nov", 37));
        series2.getData().add(new XYChart.Data("Dec", 29));

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Portfolio 3");
        series3.getData().add(new XYChart.Data("Jan", 44));
        series3.getData().add(new XYChart.Data("Feb", 35));
        series3.getData().add(new XYChart.Data("Mar", 36));
        series3.getData().add(new XYChart.Data("Apr", 33));
        series3.getData().add(new XYChart.Data("May", 31));
        series3.getData().add(new XYChart.Data("Jun", 26));
        series3.getData().add(new XYChart.Data("Jul", 22));
        series3.getData().add(new XYChart.Data("Aug", 25));
        series3.getData().add(new XYChart.Data("Sep", 43));
        series3.getData().add(new XYChart.Data("Oct", 44));
        series3.getData().add(new XYChart.Data("Nov", 45));
        series3.getData().add(new XYChart.Data("Dec", 44));

        lineChart.getData().addAll(series1, series2, series3);
        lineChart.setLayoutX(0);
        lineChart.setLayoutY(540);
        lineChart.setMaxWidth(600);
        lineChart.setMaxHeight(250);
        root_graph.getChildren().add(lineChart);
    }

    private void openFile(File f) {
        DATA_FILE = f.getAbsolutePath();
        root_other.getChildren().clear();
        root_grid.getChildren().clear();
        root.getChildren().clear();
        setupSimulation();
        createUIComponents();
        root.getChildren().add(root_other);
        root.getChildren().add(root_grid);
        root.getChildren().add(root_graph);
    }

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
        myLoadFileButton = createButton("Load simulation (.xml)", slidersXPosition,0,false);
        myNextIterationButton = createButton(">", btnXPosition, btnYPosition, false);
        myResetButton = createButton("Reset", btnXPosition + 40, btnYPosition, false);
        myStartButton = createButton("Start", btnXPosition + 100, btnYPosition, true);
        myStopButton = createButton("Stop", btnXPosition + 160, btnYPosition, true);

        root_other.getChildren().addAll(myLoadFileButton, myNextIterationButton,
                myResetButton, myApplyButton, myStartButton, myNewWindowButton, myStopButton);
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
        myNewWindowButton.setOnAction(event -> {

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
        int k = 1;
        for(String currentField : sim.getMySliderInfo().keySet()){
            //if(sim.getMyDataValues().get(currentField).equals("")) continue;
            double value = Double.parseDouble(sim.getMyDataValues().get(currentField));
            Slider slider = createSlider(slidersXPosition, k*40, Simulation.Bounds.valueOf(currentField).getMin(),
                    Simulation.Bounds.valueOf(currentField).getMax(), value);
            sliderMap.put(currentField, slider);
            Label label = new Label(currentField);
            label.setLayoutX(slidersXPosition + 150);
            label.setLayoutY(k*40);
            root.getChildren().addAll(label, slider);
            k++;
        }
        myApplyButton = createButton("Apply", slidersXPosition, 40*k, false);
        k++;
        myNewWindowButton = createButton("New Window", slidersXPosition, 40*k, false);
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
    //create an interface of key and mouse inputs, which toggles a call to mainpackage.Simulation.initializeGrid

}
