package simulations;

import cells.AgentCell;
import cells.Cell;
import cells.EmptyCell;

import java.util.*;

public class SegregationSimulation extends Simulation {
    public static final String DATA_TYPE = "SegregationSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "speed", "satisfaction");

    public double mySatisfactionThreshold; // between 0 & 1
    private double myRacePercentage; // between 0 & 1, percentage made up by first Agent
    private double myEmptyPercentage; // between 0 & 1

    private Map<String, String> myDataValues;
    private List<Cell> myEmptyCells = new ArrayList<Cell>();
    private List<Cell> myCellsToMove = new ArrayList<Cell>();;

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
        setupSimulation();
        myDataValues = new HashMap<>();
    }

    /**
     * Constructor needed to initialize from XML
     */
    public SegregationSimulation(Map<String, String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(Integer.parseInt(dataValues.get("rows")), Integer.parseInt(dataValues.get("columns")));
        this.mySatisfactionThreshold = Double.parseDouble(dataValues.get("satisfaction"));
        myGrid = getNewGrid(cells);
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
    /**
     * Overrides the superclass's method. It takes the result of the superclass's method while adding the corner
     * neighbors. A key assumption is that each row in the grid has the same number of entries.
     * @return ArrayList<Cell> neighbors
     */
    protected List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = super.getNeighbors(cell);
        int row = cell.getRow();
        int column = cell.getColumn();
        if(row != 0 && column != 0){ //check top-left neighbor
            neighbors.add(myGrid[row-1][column-1]);
        }
        if(row != 0 && column != myGrid[0].length-1){ //check top-right neighbor
            neighbors.add(myGrid[row-1][column+1]);
        }
        if(row != myGrid.length-1 && column != 0){ //check bottom-left neighbor
            neighbors.add(myGrid[row+1][column-1]);
        }
        if(row != myGrid.length-1 && column != myGrid[0].length-1){ //check bottom-right neighbor
            neighbors.add(myGrid[row+1][column+1]);
        }
        return neighbors;
    }

    @Override
    public void setupSimulation(){
        for (int i = 0; i < myGrid.length; i++) {
            for (int j = 0; j < myGrid[i].length; j++) {
                if (i == 3) {
                    myGrid[i][j] = new EmptyCell(i,j);
                } else if (i % 2 == 0) {
                    myGrid[i][j] = new AgentCell(i,j,"BLUE");
                } else {
                    myGrid[i][j] = new AgentCell(i,j,"RED");
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
}
