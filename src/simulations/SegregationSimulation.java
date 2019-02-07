package simulations;

import cells.AgentCell;
import cells.Cell;
import cells.EmptyCell;
import grids.Grid;

import java.util.*;

public class SegregationSimulation extends Simulation {
    public static final String DATA_TYPE = "SegregationSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "cellShape", "gridShape", "rows", "columns", "speed", "satisfaction", "redRate", "blueRate");

    public double mySatisfactionThreshold; // between 0 & 1
    private double myBluePercentage; // between 0 & 1, percentage made up by first Agent
    private double myRedPercentage; // between 0 & 1

    private List<Cell> myEmptyCells = new ArrayList<>();
    private List<Cell> myCellsToMove = new ArrayList<>();

    /**
     * Constructor needed to initialize from XML
     */

    public SegregationSimulation(Map<String, String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(dataValues, cells);
        mySatisfactionThreshold = Double.parseDouble(dataValues.get("satisfaction"));
        //myBluePercentage = Double.parseDouble(dataValues.get("blueRate"));
        //myRedPercentage = Double.parseDouble(dataValues.get("redRate"));
    }


    @Override
    public Grid advanceSimulation(){
        myEmptyCells.clear();
        myCellsToMove.clear();
        myCellList.clear();
        checkAndSortCells(myGrid);
        Collections.shuffle(myCellsToMove); // randomize order in which unsatisfied agents are moved
        Collections.shuffle(myEmptyCells); // randomize where unsatisfied agents will go
        for(Cell c : myCellsToMove){
            if (myEmptyCells.size() == 0) { break;} // if no space for empty cell to move, then don't move it
            Cell empty = myEmptyCells.remove(0);
            c.swapPosition(empty);
            myCellList.add(c);
            myCellList.add(empty);
        }
        myCellList.addAll(myEmptyCells);
        myCellList.addAll(myCellsToMove);
        myGrid.updateGrid(myCellList);
        return myGrid;
    }

    private void checkAndSortCells(Grid grid){
        for(int i = 0; i < grid.getHeight(); i++){ // i = row number
            for(int j = 0; j < grid.getWidth(); j++){ // j = column number
                Cell cell = grid.getCell(i, j);
                if(cell instanceof EmptyCell){ // if cell is EmptyCell
                    myEmptyCells.add(cell);
                }
                else if(cell instanceof AgentCell && ((AgentCell) cell).calculatePercentage(grid.getAllNeighbors(cell)) < mySatisfactionThreshold){
                    myCellsToMove.add(cell);
                }
                else{ // add satisfied cells to list already to be added first
                    myCellList.add(cell);
                }
            }
        }
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
    public Map<String, String> getMyDataValues(){
        return myDataValues;
    }

    @Override
    public void updateParameters(Map<String, String> map){
        mySatisfactionThreshold = Double.parseDouble(map.get("satisfaction"));
        myDataValues = map;
    }

    @Override
    public void setupGrid(){
        double redRate = Double.parseDouble(myDataValues.get("redRate"));
        double blueRate = Double.parseDouble(myDataValues.get("blueRate"));
        // TODO create randomized grid and set to myGrid
    }
}
