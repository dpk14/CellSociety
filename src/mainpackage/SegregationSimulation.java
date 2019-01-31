package mainpackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// EDITED BY JORGE
public class SegregationSimulation extends Simulation {
    public static final String DATA_TYPE = "SegregationSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "speed", "satisfaction");

    public double mySatisfactionThreshold; // between 0 & 1
    private double myRacePercentage; // between 0 & 1, percentage made up by first Agent
    private double myEmptyPercentage; // between 0 & 1
// comment by jorge
    private List<Cell> myEmptyCells = new ArrayList<Cell>();
    private List<Cell> cellsToMove = new ArrayList<Cell>();;

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
    }

    /**
     * Constructor needed to initialize from XML
     */
    public SegregationSimulation(List<String> dataValues, List<String> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(Integer.parseInt(dataValues.get(2)), Integer.parseInt(dataValues.get(3)));
        this.mySatisfactionThreshold = Double.parseDouble(dataValues.get(5));
    }

    @Override
    public Cell[][] updateGrid(){
        myEmptyCells.clear();
        cellsToMove.clear();
        myCellList.clear();
        for(int i = 0; i < myGrid.length; i++){ // i = row number
            for(int j = 0; j < myGrid[0].length; j++){ // j = column number
                Cell cell = myGrid[i][j];
                if(cell instanceof EmptyCell){ // if cell is EmptyCell
                    myEmptyCells.add(cell);
                }
                else if(cell instanceof AgentCell && ((AgentCell) cell).calculatePercentage(getNeighbors(cell)) < mySatisfactionThreshold){
                    cellsToMove.add(cell);
                }
                else{ // add satisfied cells to list already to be added first
                    myCellList.add(cell);
                }
            }
            //System.out.println();
        }
        Collections.shuffle(myEmptyCells); // randomize where unsatisfied agents will go
        for(Cell c : cellsToMove){
            // if there's no space for an empty cell to move, then we don't move it
            if (myEmptyCells.size() == 0) {
                break;
            }
            Cell empty = myEmptyCells.get(0);
            myEmptyCells.remove(0);
            Cell[] tmp = swapTwoCells(c, empty);
            myCellList.add(tmp[0]);
            myCellList.add(tmp[1]);
        }
        myCellList.addAll(myEmptyCells);
        myCellList.addAll(cellsToMove);
        myGrid = getNewGrid(this.myCellList);
        return myGrid;
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

}
