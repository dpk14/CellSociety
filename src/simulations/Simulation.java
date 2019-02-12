package simulations;

import cells.AgentCell;
import cells.Cell;
import cells.StateChangeCell;
import cells.SugarPatch;
import grids.Grid;
import grids.HexagonalGrid;
import grids.RectangularGrid;
import grids.TriangularGrid;

import java.util.*;

public abstract class Simulation {
    protected Grid myGrid;
    protected List<Cell> myCellList = new ArrayList<Cell>();
    protected ArrayList<Cell> myTakenSpots=new ArrayList<>();
    protected Map<String, String> myDataValues;
    protected Map<String, String> mySliderInfo;
    protected List<String> mySpecialSliders;

    protected Queue<Cell> myCellChoices;

    protected static final int ROW_DEFAULT = 36;
    protected static final int COL_DEFAULT = 36;


    public enum Bounds{
        rows(1, 100),
        columns(1,100),
        speed (1, 30),
        satisfaction (0, 1),
        spreadRate(0,1),
        growthRate(0,1),
        lightningRate(0,1),
        startEnergy(1,100),
        sharkReproductionMax(1,100),
        fishReproductionMax(1,100),
        energyGain(1,10),
        populatedRate(0,1),
        openRate(0,1),
        blueRate(0,1),
        redRate(0,1),
        treeRate(0,1),
        burningRate(0,1),
        fishRate(0,1),
        sharkRate(0,1),
        agentRate(0,1),
        sugarGrowthRate(1,4),
        growthInterval(1,10);

        private double min;
        private double max;

        Bounds(double min, double max){
            this.min = min;
            this.max = max;
        }

        public double getMin(){ return min; }
        public double getMax(){ return max; }
    }

    public Simulation(Map<String, String> dataValues, List<Cell> cells){
        myDataValues = dataValues;
        int numRows = Integer.parseInt(dataValues.get("rows"));
        int numCols = Integer.parseInt(dataValues.get("columns"));
        myGrid = createGrid(dataValues.get("gridShape"), numRows, numCols, cells);
    }

    public Simulation(Map<String, String> dataValues){
        myDataValues = dataValues;
        setupGrid(dataValues.get("generatorType"));
    }

    public static Simulation createNewSimulation(String simType, Map<String, String> dataValues, List<Cell> cells){
        switch (simType) {
            case SegregationSimulation.DATA_TYPE:
                return new SegregationSimulation(dataValues, cells);
            case WatorWorldSimulation.DATA_TYPE:
                return new WatorWorldSimulation(dataValues, cells);
            case PercolationSimulation.DATA_TYPE:
                return new PercolationSimulation(dataValues, cells);
            case SpreadingFireSimulation.DATA_TYPE:
                return new SpreadingFireSimulation(dataValues, cells);
            case GameOfLifeSimulation.DATA_TYPE:
                return new GameOfLifeSimulation(dataValues, cells);
        }
        System.out.println("XML file contains invalid or unspecified SimulationType.");
        throw new RuntimeException("not any kind of Simulation");
    }

    public static Simulation createNewSimulation(String simType, Map<String, String> dataValues){
        switch (simType) {
            case SegregationSimulation.DATA_TYPE:
                return new SegregationSimulation(dataValues);
            case WatorWorldSimulation.DATA_TYPE:
                return new WatorWorldSimulation(dataValues);
            case PercolationSimulation.DATA_TYPE:
                return new PercolationSimulation(dataValues);
            case SpreadingFireSimulation.DATA_TYPE:
                return new SpreadingFireSimulation(dataValues);
            case GameOfLifeSimulation.DATA_TYPE:
                return new GameOfLifeSimulation(dataValues);
            case SugarScapeSimulation.DATA_TYPE:
                return new SugarScapeSimulation(dataValues);
            case LangdonLoopSimulation.DATA_TYPE:
                return new LangdonLoopSimulation(dataValues);
        }
        System.out.println("XML file contains invalid or unspecified SimulationType.");
        throw new RuntimeException("not any kind of Simulation");
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

    public List<String> getMySpecialSliders(){
        return mySpecialSliders;
    }

    /**
     * Returns grid at start of initialization just so I can check we have the right grid to begin with
     * @return
     */
    public Grid getMyGrid() {
        return myGrid;
    }


    protected boolean evaluateOdds(double probability){
        double rand = Math.random();
        return (rand <= probability);
    }

    protected Grid createGrid(String gridShape, int numRows, int numCols, List<Cell> cells){
        Grid grid;
        switch(gridShape){
            case "RectangularGrid":
                grid = new RectangularGrid(numRows, numCols, cells);
                break;
            case "TriangularGrid":
                grid = new TriangularGrid(numRows, numCols, cells);
                break;
            case "HexagonalGrid":
                grid = new HexagonalGrid(numRows, numCols, cells);
                break;
            default:
                System.out.println("XML file contains invalid or unspecified Grid Type.");
                throw new RuntimeException("No such grid type.");
        }
        return grid;
    }

    /**
     * Updates the instance variable myDataValues defined within each Simulation subclass to match the given map.
     * It also updates all the parameters within a Simulation to match the values within the given map. This is always
     * called from within carryOutApply. The map passed is always created using the values from mySliders.
     * //@param map
     */
    public void updateParameters(){
        for(String s : myDataValues.keySet()){
            if(mySliderInfo.containsKey(s)) {
                mySliderInfo.put(s, myDataValues.get(s));
            }
        }
    }

    protected void setupSliderInfo(){
        mySliderInfo = new LinkedHashMap<>();
        mySpecialSliders = new ArrayList<>();
        mySpecialSliders.add("rows");
        mySpecialSliders.add("columns");
        mySliderInfo.put("rows", myDataValues.get("rows"));
        mySliderInfo.put("columns", myDataValues.get("columns"));
        mySliderInfo.put("speed", myDataValues.get("speed"));
    }

    protected void addSliderInfo(String field){
        if(!myDataValues.containsKey(field)){
            myDataValues.put(field, "0");
        }
        mySliderInfo.put(field, myDataValues.get(field));
        mySpecialSliders.add(field);
    }

    /**
     *
     */
    public void setupGrid(String generationType){
        switch(myDataValues.get("generatorType")){
            case "probability":
                myGrid = setupGridByProb();
                break;
            case "quota":
                myGrid = setupGridByQuota();
                break;
            default:
                throw new RuntimeException("No such generationType");
        }
    }

    protected List<Cell> initializeCellList(boolean randomize){
        List<Cell> list=new ArrayList<Cell>();
        for(int i = 0; i < myGrid.getHeight(); i++) { // i = row number
            for (int j = 0; j < myGrid.getWidth(); j++) { // j = column number
                list.add(myGrid.getCell(i, j));
            }
        }
        if (randomize) Collections.shuffle(list);
        return list;
    }

    /**
     * Updates and returns myGrid by updating the cell's positions according to the simulation's rules and then
     * returning the result of getNewGrid(myCellList). This should be called by the RunSimulation class once within the
     * step function.
     * @return updated myGrid
     */
    public abstract Grid advanceSimulation();


    public abstract void createQueueOfCellChoices();


    protected abstract Grid setupGridByProb();

    protected List<Cell> getTypedNeighbors(Cell cell, String type, List<Cell> neighbors) {
        List<Cell> specificNeighbors=new ArrayList<Cell>();
        for(Cell neighbor: neighbors){
            if (((StateChangeCell) neighbor).getState().equals(type)) specificNeighbors.add(neighbor);
        }
        return specificNeighbors;
    }

    protected Cell move(List<Cell> movable_spots, Cell current){
        Cell newLocation;
        if (movable_spots.size()==0) return current;
        else {
            Random rand = new Random();
            newLocation = movable_spots.get(rand.nextInt(movable_spots.size()));
        }
        return newLocation;
    }

    public Cell getNextCell(Cell current) {
        Queue<Cell> q = myCellChoices;
        int num = q.size();
        try {
            while (num >= 0) {
                Cell candidate = q.poll();
                if ( (!(current instanceof StateChangeCell))
                        && (!(current instanceof AgentCell))
                        && (!(current instanceof SugarPatch))
                        && current.getClass().equals(candidate.getClass())) {
                    q.add(candidate);
                    return q.peek();
                }
                else if (current instanceof StateChangeCell && candidate instanceof StateChangeCell
                        && ((StateChangeCell) candidate).getState().equals(((StateChangeCell) current).getState())) {
                    q.add(candidate);
                    return q.peek();
                }
                else if (current instanceof AgentCell && candidate instanceof AgentCell
                        && ((AgentCell) candidate).getType().equals(((AgentCell) current).getType())) {
                    q.add(candidate);
                    return q.peek();
                }
                else if (current instanceof SugarPatch && candidate instanceof SugarPatch
                    && ((SugarPatch) current).getSugar() == ((SugarPatch) candidate).getSugar()
                    && ((SugarPatch) current).hasAgent() == ((SugarPatch) candidate).hasAgent()) {
                    q.add(candidate);
                    System.out.println("FOUND");
                    return q.peek();
                }
                else {
                    q.add(candidate);
                }
                num--;
            }
        }
        catch(Exception e) {
            System.out.println("Queue not complete");
        }
        return null;
    }

    public List<Cell> getMyCellList() {
        return myCellList;
    }

    protected abstract Grid setupGridByQuota();

    public abstract String getSimType();


    protected double readInValue(String dataField, double defaultValue){
        try{
            return Double.parseDouble(myDataValues.get(dataField));
        }
        catch(NullPointerException e){
            myDataValues.put(dataField, Double.toString(defaultValue));
            System.out.printf("%s not found in XML. Using default value %f instead \n", dataField, defaultValue);
            return defaultValue;
        }
    }

    //public abstract void changeCell();


}
