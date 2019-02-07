package simulations;

import cells.Cell;
import cells.EmptyCell;
import cells.FishCell;
import cells.SharkCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import cells.Cell;
import cells.StateChangeCell;
import grids.Grid;


import java.util.*;

public class WatorWorldSimulation extends Simulation {
    private int myStartEnergy;
    private int myEnergyGain;
    private int mySharkReprodMax;
    private int myFishReprodMax;
    public static final Paint COLOR_AGENT_RED = Color.RED;


    public static final String DATA_TYPE = "WatorWorldSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "cellShape", "gridShape", "speed", "startEnergy",
            "sharkReproductionMax", "fishReproductionMax", "energyGain", "fishRate", "sharkRate");

    public WatorWorldSimulation(Map<String, String> dataValues, List<Cell> cells){
        super(dataValues, cells);
        myStartEnergy=Integer.parseInt(dataValues.get("startEnergy"));
        myEnergyGain=Integer.parseInt(dataValues.get("energyGain"));
        mySharkReprodMax=Integer.parseInt(dataValues.get("sharkReproductionMax"));
        myFishReprodMax=Integer.parseInt(dataValues.get("fishReproductionMax"));
        mySliderInfo.put("speed", dataValues.get("speed"));
        mySliderInfo.put("startEnergy", dataValues.get("startEnergy"));
        mySliderInfo.put("energyGain", dataValues.get("energyGain"));
        mySliderInfo.put("sharkReproductionMax", dataValues.get("sharkReproductionMax"));
        mySliderInfo.put("fishReproductionMax", dataValues.get("fishReproductionMax"));
    }

    @Override
    public Grid advanceSimulation() {
        initializeCellList();
        Collections.shuffle(myCellList);
        List<Cell> randomizedList=new ArrayList<Cell>(myCellList);
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

    public void fishMover(Cell fish, int currentRow, int currentCol) {
            ArrayList<Cell> emptyNeighbors=new ArrayList<Cell>();
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
        ArrayList<Cell> emptyNeighbors=new ArrayList<Cell>();
        ArrayList<Cell> fishNeighbors=new ArrayList<Cell>();
        ArrayList<Cell> availableNeighbors;

        List<Cell> neighbors = myGrid.getImmediateNeighbors(shark);
        neighbors=removeTakenSpots(neighbors);
        for (Cell neighbor : neighbors) {
            if (neighbor instanceof EmptyCell) emptyNeighbors.add(neighbor);
            else if (neighbor instanceof FishCell) fishNeighbors.add(neighbor);
        }
        if(fishNeighbors.size()>0) availableNeighbors=new ArrayList<Cell>(fishNeighbors);
        else availableNeighbors=new ArrayList<Cell>(emptyNeighbors);
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
        List<Cell> reducedNeighbors=new ArrayList<Cell>();
        for(Cell neighbor: neighbors){
            for (Cell taken: myTakenSpots)
                if(!(neighbor.getColumn()==taken.getColumn() && neighbor.getRow()==taken.getRow())){
                    reducedNeighbors.add(neighbor);
                }
        }
        return reducedNeighbors;
    }

    @Override
    public List<String> getDataFields(){
        return DATA_FIELDS;
    }

    @Override
    public String getDataType(){
        return DATA_TYPE;
    }

    @Override
    public void updateParameters(Map<String, String> map) {
        double start= Double.parseDouble(map.get("startEnergy"));
        myStartEnergy = (int) Math.floor(start);
        double gain= Double.parseDouble(map.get("energyGain"));
        myEnergyGain = (int) Math.floor(gain);
        double sharkMax=Double.parseDouble(map.get("sharkReproductionMax"));
        mySharkReprodMax = (int) Math.floor(sharkMax);
        double fishMax=Double.parseDouble(map.get("fishReproductionMax"));
        mySharkReprodMax = (int) Math.floor(fishMax);
        myDataValues = map;
    }

    @Override
    public void setupGrid(){
        double fishRate = Double.parseDouble(myDataValues.get("fishRate"));
        double sharkRate = Double.parseDouble(myDataValues.get("sharkRate"));
        // TODO create randomized grid and set to myGrid
    }
}
