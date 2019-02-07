package simulations;

import cells.Cell;
import cells.StateChangeCell;
import mainpackage.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Simulation {
    protected Grid myGrid;
    protected List<Cell> myCellList = new ArrayList<Cell>();

    /*
    public Simulation(int numRows, int numCols){
        myGrid = new Grid(numRows, numCols, );
    }
    */

    public static enum Bounds{
        rows(1, 30),
        columns(1,100),
        speed (1, 30),
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

    /**
     *
     */
    public abstract void setupGrid();

//    public Simulation(int numRows, int numCols){
//        myGrid = new Cell[numRows][numCols];
//    }

    /**
     * Updates and returns myGrid by updating the cell's positions according to the simulation's rules and then
     * returning the result of getNewGrid(myCellList). This should be called by the RunSimulation class once within the
     * step function.
     * @return updated myGrid
     */
    public abstract Grid advanceSimulation();

    /**
     * Returns grid at start of initialization just so I can check we have the right grid to begin with
     * @return
     */
    public Grid getMyGrid() {
        return myGrid;
    }

    public List<Cell> getTypedNeighbors(Cell cell, String type, List<Cell> neighbors) {
        List<Cell> specificNeighbors=new ArrayList<Cell>();
        for(Cell neighbor: neighbors){
            if (((StateChangeCell) neighbor).getState().equals(type)) specificNeighbors.add(neighbor);
        }
        return specificNeighbors;
    }


}
