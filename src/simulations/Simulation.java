package simulations;

import cells.Cell;
import cells.StateChangeCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Simulation {
    protected Cell[][] myGrid;
    protected List<Cell> myCellList = new ArrayList<Cell>();

    public static enum Bounds{
        rows(1, 100),
        columns(1,100),
        speed (1, 100),
        satisfaction (0, 1),
        spreadRate(0,1),
        growthRate(0,1),
        lightningRate(0,1),
        startEnergy(1,100),
        sharkReproductionMax(1,100),
        fishReproductionMax(1,100),
        energyGain(1,10);

        private double min;
        private double max;

        private Bounds(double min, double max){
            this.min = min;
            this.max = max;
        }

        public double getMin(){ return min; }
        public double getMax(){ return max; }
    }

    /**
     * Returns myDataValues, which is defined within each subclass. The instance variable myDataValues is a map with
     * strings representing a simulation's data fields as the keys and the corresponding numbers as the values (as
     * Strings). This is used to initialize the sliders to be on the values specified within the XML files.
     * @return Map<String, String> myDataValues
     */
    public abstract Map<String, String> getMyDataValues();

    /**
     * Returns myDataFields, which is defined within each subclass. The instance variable myDataFields is a List that
     * contains a simulation's data fields as strings.
     * @return List<String> myDataFields
     */
    public abstract List<String> getDataFields();

    /**
     * Returns a String representing the Simulation subclass. This is used when creating an instance of a Simulation
     * subclass within the XMLParser according to the simulation specified within the XML file being read.
     * @return String data type
     */
    public abstract String getDataType();

    /**
     * Updates the instance variable myDataValues defined within each Simulation subclass to match the given map.
     * It also updates all the parameters within a Simulation to match the values within the given map. This is always
     * called from within carryOutApply. The map passed is always created using the values from mySliders.
     * @param map
     */
    public abstract void updateParameters(Map<String, String> map);

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
            if (((StateChangeCell) neighbor).getState().equals(type)) specificNeighbors.add(neighbor);
        }
        return specificNeighbors;
    }

    /**
     * Returns grid at start of initialization just so I can check we have the right grid to begin with
     * @return
     */
    public Cell[][] getMyGrid() {
        return myGrid;
    }


}
