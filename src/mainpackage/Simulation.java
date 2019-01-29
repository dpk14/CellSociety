package mainpackage;

import java.util.ArrayList;
import java.util.List;

public abstract class Simulation {
    protected Cell[][] myGrid;
    protected List<Cell> myCellList;

    public Simulation(int numRows, int numCols){
        myGrid = new Cell[numRows][numCols];
    }

    /**
     * Updates and returns myGrid by updating the cell's positions according to the simulation's rules and then
     * returning the result of getNewGrid(myCellList). This should be called by the RunSimulation class once within the
     * step function.
     * @return updated myGrid
     */
    public abstract Cell[][] updateGrid();

    /**
     * Populates myGrid according to XML file. (Possibly?)
     */
    public abstract void setupSimulation();

    protected Cell[][] getNewGrid(List<Cell> list){
        Cell[][] newGrid = new Cell[myGrid.length][myGrid[0].length];
        for(Cell cell : list){
            newGrid[cell.getRow()][ cell.getRow()] = cell;
        }
        return newGrid;
    }

    /**
     * Returns an ArrayList of cells that neighbor the given cell. In this case, it is implemented taking neighbros only
     * to mean the cells immediately above, below, or next to the given cell. Depending on what "neighbors" means, may
     * be overridden. A key assumption is that each row in the grid has the same number of entries.
     *
     * @param cell - cell whose neighbors will be checked.
     * @return ArrayList<Cell> neighbors
     */
    protected List<Cell> getNeighbors(Cell cell){
        List <Cell> neighbors = new ArrayList<>();
        int row = cell.getRow();
        int column = cell.getColumn();
        if(row != 0){ // check top neighbor
            neighbors.add(myGrid[row-1][column]);
        }
        if(row != myGrid.length-1){ // check bottom neighbor
            neighbors.add(myGrid[row+1][column]);
        }
        if(column != 0){ // check left neighbor
            neighbors.add(myGrid[row][column-1]);
        }
        if(column != myGrid[0].length-1){ // check right neighbor
            neighbors.add(myGrid[row][column+1]);
        }
        return neighbors;
    }

//    private removeCell(cell toberemoved){
//    }
}
