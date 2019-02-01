package simulations;

import cells.Cell;
import cells.StateChangeCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Simulation {
    //https://stackoverflow.com/questions/46820750/java-how-to-choose-what-subclass-to-construct
    protected Cell[][] myGrid;
    protected List<Cell> myCellList = new ArrayList<Cell>();

    public abstract Map<String, String> getMyDataValues();

    public abstract List<String> getDataFields();

    public abstract String getDataType();

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

    protected Cell[][] getNewGrid(List<Cell> list){
        Cell[][] newGrid = myGrid;
        for(Cell cell : list){
            newGrid[cell.getRow()][cell.getColumn()] = cell;
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

    protected List<Cell> getTypedNeighbors(Cell cell, String type) {
        List<Cell> neighbors = getNeighbors(cell);
        List<Cell> specificNeighbors=new ArrayList<Cell>();
        for(Cell neighbor: neighbors){
            if (((StateChangeCell) cell).getState().equals(type)) specificNeighbors.add(neighbor);
        }
        return specificNeighbors;
    }

    /**
     * Michael made this for debugging purposes.
     * Returns grid at start of initialization just so I can check we have the right grid to begin with
     * @return
     */
    public Cell[][] getMyGrid() {
        return myGrid;
    }
}
