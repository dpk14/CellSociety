package simulations;

import cells.Cell;
import cells.StateChangeCell;
import grids.*;

import java.util.*;

public abstract class Simulation {
    protected Grid myGrid;
    protected List<Cell> myCellList = new ArrayList<Cell>();
    protected Map<String, String> myDataValues;
    protected Map<String, String> mySliderInfo;
    public enum Bounds{
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

    public Simulation(Map<String, String> dataValues, List<Cell> cells){
        myDataValues = dataValues;
        mySliderInfo = new HashMap<>();
        int numRows = Integer.parseInt(dataValues.get("rows"));
        int numCols = Integer.parseInt(dataValues.get("columns"));
        myGrid = createGrid(myDataValues.get("gridShape"), numRows, numCols, cells);
    }

    public Simulation(Map<String, String> dataValues){
        myDataValues = dataValues;
        mySliderInfo = new HashMap<>();
        int numRows = Integer.parseInt(dataValues.get("rows"));
        int numCols = Integer.parseInt(dataValues.get("columns"));
        setupGrid();
    }

    /**
     * Returns myDataValues, which is defined within each subclass. The instance variable myDataValues is a map with
     * strings representing a simulation's data fields as the keys and the corresponding numbers as the values (as
     * Strings). This is used to initialize the sliders to be on the values specified within the XML files.
     * @return Map<String, String> myDataValues
     */
    public Map<String, String> getMyDataValues(){
        return myDataValues;
    }

    public Map<String, String> getMySliderInfo(){
        return mySliderInfo;
    }

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

    //public abstract void changeCell();

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

    protected abstract void setupSliderInfo();

    protected boolean evaluateOdds(double probability){
        double rand = Math.random();
        return (rand <= probability);
    }

    protected Grid createGrid(String gridShape, int numRows, int numCols, List<Cell> cells){
        Grid grid;
        switch(gridShape){
            case "RectangularGrid":
                System.out.println("RECT");
                grid = new RectangularGrid(numRows, numCols, cells);
                break;
            case "TriangularGrid":
                System.out.println("TRI");
                grid = new TriangularGrid(numRows, numCols, cells);
                break;
            case "HexagonalGrid":
                System.out.println("HEX");
                grid = new HexagonalGrid(numRows, numCols, cells);
                break;
            default:
                throw new IllegalStateException();
        }
        return grid;
    }

}
