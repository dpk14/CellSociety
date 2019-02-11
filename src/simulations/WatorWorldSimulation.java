package simulations;

import cells.Cell;
import cells.EmptyCell;
import cells.FishCell;
import cells.SharkCell;
import grids.Grid;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WatorWorldSimulation extends Simulation {
    private int myStartEnergy;
    private int myEnergyGain;
    private int mySharkReprodMax;
    private int myFishReprodMax;
    public static final Paint COLOR_AGENT_RED = Color.RED;

    public static final int ENERGY_DEFAULT = 5;
    public static final int ENERGY_GAIN_DEFAULT = 5;
    public static final int SHARK_REPRODMAX_DEFAULT = 15;
    public static final int FISH_REPRODMAX_DEFAULT = 5;
    public static final double SHARK_DEFAULT = .05;
    public static final double FISH_DEFAULT = .3;

    public static final String DATA_TYPE = "WatorWorldSimulation";
//    public static final List<String> DATA_FIELDS = List.of(
//            "title", "author", "rows", "columns", "cellShape", "gridShape", "speed", "startEnergy",
//            "sharkReproductionMax", "fishReproductionMax", "energyGain", "fishRate", "sharkRate");

    public WatorWorldSimulation(Map<String, String> dataValues, List<Cell> cells){
        super(dataValues, cells);
        setValues();
        setupSliderInfo();
    }

    public WatorWorldSimulation(Map<String, String> dataValues){
        super(dataValues);
        setValues();
        setupSliderInfo();
    }

    @Override
    public Grid advanceSimulation() {
        initializeCellList();
        Collections.shuffle(myCellList);
        List<Cell> randomizedList=new ArrayList<>(myCellList);
        myCellList.clear();
        myTakenSpots.clear();
        for(Cell cell: randomizedList) {
            if (cell instanceof SharkCell) {
                myTakenSpots.add(cell);
                ((SharkCell) cell).updateMyTurnsSurvived();
                ((SharkCell) cell).decrementEnergy();
                if (((SharkCell) cell).getMyEnergy() <= 0) myCellList.add(new EmptyCell(cell.getRow(), cell.getColumn()));
                else sharkMover(cell, cell.getRow(), cell.getColumn());
            }
        }
        myGrid.updateGrid(myCellList);
        initializeCellList();
        Collections.shuffle(myCellList);
        randomizedList=new ArrayList<Cell>(myCellList);
        myCellList.clear();
        myTakenSpots.clear();
        for(Cell cell: randomizedList) {
            if(cell instanceof FishCell) {
                myTakenSpots.add(cell);
                ((FishCell) cell).updateMyTurnsSurvived();
                fishMover(cell, cell.getRow(), cell.getColumn());
            }
        }
        myGrid.updateGrid(myCellList);
        return myGrid;
    }

    @Override
    protected void setupSliderInfo() {
        super.setupSliderInfo();
        mySliderInfo.put("startEnergy", myDataValues.get("startEnergy"));
        mySliderInfo.put("energyGain", myDataValues.get("energyGain"));
        mySliderInfo.put("sharkReproductionMax", myDataValues.get("sharkReproductionMax"));
        mySliderInfo.put("fishReproductionMax", myDataValues.get("fishReproductionMax"));
        addSliderInfo("fishRate");
        addSliderInfo("sharkRate");
    }

    public void fishMover(Cell fish, int currentRow, int currentCol) {
            ArrayList<Cell> emptyNeighbors=new ArrayList<>();
            List<Cell> neighbors = myGrid.getImmediateNeighbors(fish);
            neighbors=removeTakenSpots(neighbors);
            for (Cell neighbor : neighbors) {
                if (neighbor instanceof EmptyCell) emptyNeighbors.add(neighbor);
            }
            Cell otherCell = move(emptyNeighbors, fish);
            Cell newLocation=new Cell(otherCell.getRow(), otherCell.getColumn(), COLOR_AGENT_RED);
            if(!(newLocation.getRow()==currentRow && newLocation.getColumn()==currentCol)){
                fish.swapPosition(otherCell);
                myTakenSpots.add(newLocation);
                myCellList.add(fish);
                if (((FishCell) fish).canReproduce()) {
                    ((FishCell) fish).setMyTurnsSurvived(0);
                    myCellList.add(new FishCell(currentRow, currentCol, myFishReprodMax));
                }
                else myCellList.add(new EmptyCell(currentRow, currentCol));
            }
    }

    public void sharkMover(Cell shark, int currentRow, int currentCol) {
        ArrayList<Cell> emptyNeighbors=new ArrayList<>();
        ArrayList<Cell> fishNeighbors=new ArrayList<>();
        ArrayList<Cell> availableNeighbors;

        List<Cell> neighbors = myGrid.getImmediateNeighbors(shark);
        neighbors=removeTakenSpots(neighbors);
        for (Cell neighbor : neighbors) {
            if (neighbor instanceof EmptyCell) emptyNeighbors.add(neighbor);
            else if (neighbor instanceof FishCell) fishNeighbors.add(neighbor);
        }
        if(fishNeighbors.size()>0) availableNeighbors=new ArrayList<>(fishNeighbors);
        else availableNeighbors=new ArrayList<>(emptyNeighbors);
        Cell otherCell=move(availableNeighbors, shark);
        Cell newLocation=new Cell(otherCell.getRow(), otherCell.getColumn(), COLOR_AGENT_RED);
        if(!(newLocation.getRow()==currentRow && newLocation.getColumn()==currentCol)) {
            shark.swapPosition(otherCell);
            if (otherCell instanceof FishCell) ((SharkCell) shark).updateEnergy();
            myTakenSpots.add(newLocation);
            myCellList.add(shark);
            if (((SharkCell) shark).canReproduce()) {
                ((SharkCell) shark).setMyTurnsSurvived(0);
                myCellList.add(new SharkCell(currentRow, currentCol, mySharkReprodMax, myStartEnergy, myEnergyGain));
            }
            else myCellList.add(new EmptyCell(currentRow, currentCol));
        }
    }

    private List<Cell> removeTakenSpots(List<Cell> neighbors){
        List<Cell> reducedNeighbors=new ArrayList<>();
        for(Cell neighbor: neighbors){
            for (Cell taken: myTakenSpots)
                if(!(neighbor.getColumn()==taken.getColumn() && neighbor.getRow()==taken.getRow())){
                    reducedNeighbors.add(neighbor);
                }
        }
        return reducedNeighbors;
    }

    @Override
    public void updateParameters() {
        super.updateParameters();
        setValues();
    }

    @Override
    protected Grid setupGridByProb(){
        int rows = (int) readInValue("rows", ROW_DEFAULT);
        int cols = (int) readInValue("columns", COL_DEFAULT);
        double fishRate = readInValue("fishRate", FISH_DEFAULT);
        double sharkRate = readInValue("sharkRate", SHARK_DEFAULT);
        List<Cell> cells = new ArrayList<>();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                Cell cell;
                if(evaluateOdds(fishRate)){
                    cell = new FishCell(i, j, (int) readInValue("fishReproductionMax", FISH_REPRODMAX_DEFAULT));
                }
                else if(evaluateOdds(sharkRate)){
                    cell = new SharkCell(i, j, (int) readInValue("sharkReproductionMax", SHARK_REPRODMAX_DEFAULT),
                            (int) readInValue("startEnergy", ENERGY_DEFAULT),
                            (int) readInValue("energyGain", ENERGY_GAIN_DEFAULT));
                }
                else{
                    cell = new EmptyCell(i, j);
                }
                cells.add(cell);
            }
        }
        return createGrid(myDataValues.get("gridShape"), rows, cols, cells);
    }

    @Override
    protected Grid setupGridByQuota() {
        int rows = (int) readInValue("rows", ROW_DEFAULT);
        int cols = (int) readInValue("columns", COL_DEFAULT);
        int fishRate = (int) readInValue("fishRate", FISH_DEFAULT*rows*cols);
        int sharkRate = (int) readInValue("sharkRate", SHARK_DEFAULT*rows*cols);
        List<String> states = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        for (int k = 0; k < fishRate; k++) {
            states.add("FISH");
        }
        for (int k = 0; k < sharkRate; k++) {
            states.add("SHARK");
        }
        for (int k = 0; k < (rows * cols - fishRate - sharkRate + 1); k++) {
            states.add("EMPTY");
        }
        Collections.shuffle(states);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String state = states.remove(0);
                if (state.equals("FISH")) {
                    cells.add(new FishCell(i, j, (int) readInValue("fishReproductionMax", FISH_REPRODMAX_DEFAULT)));
                }
                else if (state.equals("SHARK")) {
                    cells.add(new SharkCell(i, j, (int) readInValue("sharkReproductionMax", SHARK_REPRODMAX_DEFAULT),
                            (int) readInValue("startEnergy", ENERGY_DEFAULT), (int) readInValue("energyGain", ENERGY_GAIN_DEFAULT)));
                }
                else if (state.equals("EMPTY")) {
                    cells.add(new EmptyCell(i, j));
                }
                else {
                    throw new RuntimeException("Cell type not allowed in " + DATA_TYPE);
                }
            }
        }
        return createGrid(myDataValues.get("gridShape"), rows, cols, cells);
    }

    private void setValues(){
        myStartEnergy = (int) readInValue("startEnergy", ENERGY_DEFAULT);
        myEnergyGain = (int) readInValue("energyGain", ENERGY_GAIN_DEFAULT);
        mySharkReprodMax = (int) readInValue("sharkReproductionMax", SHARK_REPRODMAX_DEFAULT);
        myFishReprodMax = (int) readInValue("fishReproductionMax", FISH_REPRODMAX_DEFAULT);
    }

    @Override
    public String getSimType(){
        return DATA_TYPE;
    }
}
