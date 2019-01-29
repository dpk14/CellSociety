package mainpackage;

import java.util.List;

public class SegregationSimulation extends Simulation {

    private double mySatisfactionThreshold; // between 0 & 1
    private double myRacePercentage; // between 0 & 1, percentage made up by first Agent
    private double myEmptyPercentage; // between 0 & 1

    public SegregationSimulation(int numRows, int numCols, double mySatisfactionThreshold, double myRacePercentage, double myEmptyPercentage){
        super(numRows,numCols);
        this.mySatisfactionThreshold = mySatisfactionThreshold;
        this.myRacePercentage = myRacePercentage;
        this.myEmptyPercentage = myEmptyPercentage;
    }

    @Override
    public Cell[][] updateGrid(){
        for(int i = 0; i < myGrid.length; i++){ // i = row number
            for(int j = 0; j < myGrid[0].length; j++){ // j = column number
                Cell cell = myGrid[i][j];
                List<Cell> neighbors = getNeighbors(cell);
            }
        }
        return getNewGrid(this.myCellList);
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
        if(row != 0 && column != myGrid[0].length){ //check top-right neighbor
            neighbors.add(myGrid[row-1][column+1]);
        }
        if(row != myGrid.length-1 && column != 0){ //check bottom-left neighbor
            neighbors.add(myGrid[row+1][column-1]);
        }
        if(row != myGrid.length-1 && column != myGrid[0].length){ //check bottom-right neighbor
            neighbors.add(myGrid[row+1][column+1]);
        }
        return neighbors;
    }

    public void setupSimulation(){
    }

}
