package mainpackage;

import cells.Cell;
import grids.Grid;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import simulations.Simulation;
import java.io.File;
import java.util.*;

public class RunSimulation {

    public static final int btnXPosition = 10;
    public static final int btnYPosition = 520;
    public static final int slidersXPosition = 510;
    private String DATA_FILE = "data/locationConfig/spreadingfire_rectangle_12x12.xml";
    private Timeline animation;
    private Group root = new Group();
    private Group root_grid = new Group();
    private Group root_other = new Group();
    private Group root_graph = new Group();
    private PopulationGraph graph;
    private Visualization newVisual;
    private Simulation currentSimulation;
    private Button myResetButton;
    private Button myStartButton;
    private Button myStopButton;
    private Button myApplyButton;
    private Button myNextIterationButton;
    private Button myLoadFileButton;
    private ToggleGroup myToggleGroup;
    private Map<String, Slider> mySliders;
    private CheckBox myGridOnCheckBox;
    private Stage s;
    private FileChooser fileChooser;
    private boolean onInitialGrid = true;
    private boolean startedAnimation = false;

    RunSimulation(Timeline a) {
        animation = a;
    }

    public Group getNode() {
        setupSimulation();
        createUIComponents();
        root.getChildren().add(root_other);
        root.getChildren().add(root_grid);
        root.getChildren().add(root_graph);
        return root;
    }

    private void openFile(File f) {
        DATA_FILE = f.getAbsolutePath();
        root_other.getChildren().clear();
        root_grid.getChildren().clear();
        root_graph.getChildren().clear();
        root.getChildren().clear();
        setupSimulation();
        createUIComponents();
        root.getChildren().add(root_other);
        root.getChildren().add(root_grid);
        root.getChildren().add(root_graph);
    }

    private void replaceSimulation(Simulation sim){
        root_other.getChildren().clear();
        root_grid.getChildren().clear();
        root.getChildren().clear();
        onInitialGrid = true;
        currentSimulation = sim;
        Grid initialGrid = currentSimulation.getMyGrid();
        newVisual = new Visualization(initialGrid.getHeight(), initialGrid.getWidth());
        root_grid.getChildren().add(newVisual.getRootNode(initialGrid));
        createUIComponents();
        root.getChildren().add(root_other);
        root.getChildren().add(root_grid);
        root.getChildren().add(root_graph);
    }

    private void setupSimulation() {
        onInitialGrid = true;
        try {
            currentSimulation = new XMLParser("simType").getSimulation(new File(DATA_FILE));
        }
        catch(RuntimeException e){
            System.out.println("Staying on current simulation since specified file is invalid.");
        }
        Grid initialGrid = currentSimulation.getMyGrid();
        newVisual = new Visualization(initialGrid.getHeight(), initialGrid.getWidth());
        root_grid.getChildren().add(newVisual.getRootNode(initialGrid));
        root_graph.getChildren().clear();
        graph = new PopulationGraph(initialGrid.getMapOfCellCount());
        root_graph.getChildren().add(graph.getGraphRootNode());
    }

    private void createUIComponents() {
        // add other components (i.e. not grid)
        mySliders = createMySliders(currentSimulation, root_other);
        myLoadFileButton = createButton("Load simulation (.xml)", slidersXPosition,0,false);
        myNextIterationButton = createButton(">", btnXPosition, btnYPosition, false);
        myResetButton = createButton("Reset", btnXPosition + 40, btnYPosition, false);
        myStartButton = createButton("Start", btnXPosition + 100, btnYPosition, true);
        myStopButton = createButton("Stop", btnXPosition + 160, btnYPosition, true);


        // TODO wrap this in a method
        myGridOnCheckBox = new CheckBox();
        Label gridLabel = new Label("Grid:");
        gridLabel.setLayoutY(myLoadFileButton.getLayoutY());
        gridLabel.setLayoutX(myLoadFileButton.getLayoutX() + 200);
        myGridOnCheckBox.setLayoutX(gridLabel.getLayoutX() + 30);
        myGridOnCheckBox.setLayoutY(myLoadFileButton.getLayoutY());
        myGridOnCheckBox.setSelected(true);

        // TODO wrap this in a method
        myToggleGroup = new ToggleGroup();
        int x = 580;
        int y = 480;
        RadioButton squareButton = createRadioButton(x, y, "RectangularGrid");
        RadioButton triangleButton = createRadioButton(x, y + 20, "TriangularGrid");
        RadioButton hexagonButton = createRadioButton(x, y + 40,"HexagonalGrid");
        root_other.getChildren().addAll(myLoadFileButton, myNextIterationButton,
                myResetButton, myApplyButton, myStartButton,
                myStopButton, myGridOnCheckBox, gridLabel,
                squareButton, triangleButton, hexagonButton);
        setButtonHandlers();
    }

    private RadioButton createRadioButton (int x, int y, String text) {
        RadioButton r = new RadioButton(text);
        r.setLayoutX(x);
        r.setLayoutY(y);
        r.setUserData(text);
        r.setToggleGroup(myToggleGroup);
        // TODO set the right one to true...
        if(currentSimulation.getMyDataValues().get("gridShape").equals(text)) {
            r.setSelected(true);
        }
        return r;
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
        });
        myLoadFileButton.setOnAction(e -> {
            fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(s);
            if (selectedFile != null
                    && selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1).equals("xml")) {
                openFile(selectedFile);
            }
        });

        myGridOnCheckBox.setOnAction(e -> {
            if (myGridOnCheckBox.isSelected()) {
                newVisual.turnBorderOn();
            }
            else {
                newVisual.turnBorderOff();
            }
            refreshGridView();
        });
    }

    private void refreshGridView() {
        // incorporates changes when the simulation is not applied, e.g. grid on/off, clicks, etc.
        root_grid.getChildren().clear();
        Grid g = currentSimulation.getMyGrid();
        g.updateGrid(currentSimulation.getMyCellList());
        Map<Paint, Integer> m = g.getMapOfCellCount();
        graph.addPoint(m);
        Node n = newVisual.getRootNode(g);
        root_grid.getChildren().add(n);
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
            double value = Double.parseDouble(sim.getMySliderInfo().get(currentField));
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

        return sliderMap;
    }

    private void carryOutApply(Simulation sim){
        Map<String, String> dataValues = sim.getMyDataValues();
        boolean shouldReplace = false;
        for(String s : mySliders.keySet()){
            // if one of the "foundational" sliders is edited, will need to create new simulation
            if(sim.getMySpecialSliders().contains(s) && mySliders.get(s).getValue() != Double.parseDouble(dataValues.get(s))){
                shouldReplace = true;
            }
            dataValues.put(s, Double.toString(mySliders.get(s).getValue()));
        }
        sim.updateParameters();
        shouldReplace = shouldReplace || updateGridShape();
        if(shouldReplace) {
            dataValues.put("generatorType", "probability");
            System.out.println("from dataValues: " + dataValues.get("gridShape"));
            System.out.println("from myDataValues: " + currentSimulation.getMyDataValues().get("gridShape"));
            currentSimulation = Simulation.createNewSimulation(currentSimulation.getSimType(), dataValues);
            replaceSimulation(currentSimulation);
        }
    }

    private boolean updateGridShape(){
        if(!myToggleGroup.getSelectedToggle().getUserData().equals(currentSimulation.getMyDataValues().get("gridShape"))){
            currentSimulation.getMyDataValues().put("gridShape", myToggleGroup.getSelectedToggle().getUserData().toString());
            //System.out.println( myToggleGroup.getSelectedToggle().getUserData().toString());
            return true;
        }
        return false;
    }

    private void renderNextIteration() {
        // render next iteration
        root_grid.getChildren().clear();
        Grid g = currentSimulation.advanceSimulation();
        Map<Paint, Integer> m = g.getMapOfCellCount();
        graph.addPoint(m);
        Node n = newVisual.getRootNode(g);
        root_grid.getChildren().add(n);
    }

    public void renderNextIterationFromClick(double x, double y) {
        int[] location = newVisual.findLocOfShapeClicked(x, y);
        if (location == null) return;
        Cell oldCell = currentSimulation.getMyGrid().getCell(location[0], location[1]);
        Cell nextCell = currentSimulation.getNextCell(oldCell);
//        System.out.println("OLD: " + oldCell.getRow() + "|" + oldCell.getColumn());
//        System.out.println("NEW: " + nextCell.getMyColor());
        currentSimulation.getMyGrid().replaceCellOnWithNew(oldCell.getRow(), oldCell.getColumn(), nextCell);
        nextCell.swapPosition(oldCell);
        oldCell.setNegativePosition();
        currentSimulation.createQueueOfCellChoices();
        refreshGridView();
    }

    public void stepThru(double elapsedTime){
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

}
