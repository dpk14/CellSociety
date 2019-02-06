package simulations;

import cells.AgentCell;
import cells.Cell;
import cells.EmptyCell;

import java.util.*;

public class SegregationSimulation extends Simulation {
    public static final String DATA_TYPE = "SegregationSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "speed", "satisfaction", "redRate", "blueRate");

    public double mySatisfactionThreshold; // between 0 & 1
    private double myRacePercentage; // between 0 & 1, percentage made up by first Agent
    private double myEmptyPercentage; // between 0 & 1

    private Map<String, String> myDataValues;
    private List<Cell> myEmptyCells = new ArrayList<>();
    private List<Cell> myCellsToMove = new ArrayList<>();

    /**
     *
     * @param numRows - number of rows in desired grid
     * @param numCols - number of columns in desired grid
     * @param mySatisfactionThreshold - if ratio of a cell's neighbors is below this number, then it is "unsatisfied" and must move
     * @param myRacePercentage - ratio of total AgentCells made up by first agent type
     * @param myEmptyPercentage - ratio of total cells that should be empty
     */
    public SegregationSimulation(int numRows, int numCols, double mySatisfactionThreshold, double myRacePercentage, double myEmptyPercentage){
        super(numRows,numCols);
        this.mySatisfactionThreshold = mySatisfactionThreshold;
        this.myRacePercentage = myRacePercentage;
        this.myEmptyPercentage = myEmptyPercentage;
        myDataValues = new LinkedHashMap<>();
    }

    /**
     * Constructor needed to initialize from XML
     */
    public SegregationSimulation(Map<String, String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(Integer.parseInt(dataValues.get("rows")), Integer.parseInt(dataValues.get("columns")));
        this.mySatisfactionThreshold = Double.parseDouble(dataValues.get("satisfaction"));
        myGrid = new Grid(cells);
        myDataValues = dataValues;
    }

    @Override
    public Cell[][] updateGrid(){
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
        myGrid = getNewGrid(this.myCellList);
        return myGrid;
    }

    private void checkAndSortCells(Cell[][] grid){
        for(int i = 0; i < grid.length; i++){ // i = row number
            for(int j = 0; j < grid[0].length; j++){ // j = column number
                Cell cell = grid[i][j];
                if(cell instanceof EmptyCell){ // if cell is EmptyCell
                    myEmptyCells.add(cell);
                }
                else if(cell instanceof AgentCell && ((AgentCell) cell).calculatePercentage(getNeighbors(cell)) < mySatisfactionThreshold){
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
