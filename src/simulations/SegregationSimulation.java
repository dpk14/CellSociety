package simulations;

import cells.AgentCell;
import cells.Cell;
import cells.EmptyCell;
import cells.StateChangeCell;
import grids.Grid;

import java.util.*;

public class SegregationSimulation extends Simulation {
    public static final String DATA_TYPE = "SegregationSimulation";
//    public static final List<String> DATA_FIELDS = List.of(
//            "title", "author", "cellShape", "gridShape", "rows", "columns", "speed", "satisfaction", "redRate", "blueRate");

    public double mySatisfactionThreshold; // between 0 & 1
//    private double myBluePercentage; // between 0 & 1, percentage made up by first Agent
//    private double myRedPercentage; // between 0 & 1

    private List<Cell> myEmptyCells = new ArrayList<>();
    private List<Cell> myCellsToMove = new ArrayList<>();

    /**
     * Constructor needed to initialize from XML
     */
    public SegregationSimulation(Map<String, String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(dataValues, cells);
        mySatisfactionThreshold = Double.parseDouble(dataValues.get("satisfaction"));
        setupSliderInfo();
        //myBluePercentage = Double.parseDouble(dataValues.get("blueRate"));
        //myRedPercentage = Double.parseDouble(dataValues.get("redRate"));
        createQueueOfCellChoices();
    }

    public SegregationSimulation(Map<String, String> dataValues){
        super(dataValues);
        mySatisfactionThreshold = Double.parseDouble(dataValues.get("satisfaction"));
        setupSliderInfo();
        createQueueOfCellChoices();
    }


    @Override
    public Grid advanceSimulation(){
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
        myGrid.updateGrid(myCellList);
        return myGrid;
    }

    @Override
    protected void setupSliderInfo() {
        super.setupSliderInfo();
        mySliderInfo.put("satisfaction", myDataValues.get("satisfaction"));
        addSliderInfo("blueRate");
        addSliderInfo("redRate");
    }

    @Override
    public void updateParameters() {
        super.updateParameters();
        mySatisfactionThreshold = Double.parseDouble(myDataValues.get("satisfaction"));
    }

    private void checkAndSortCells(Grid grid){
        for(int i = 0; i < grid.getHeight(); i++){ // i = row number
            for(int j = 0; j < grid.getWidth(); j++){ // j = column number
                Cell cell = grid.getCell(i, j);
                if(cell instanceof EmptyCell){ // if cell is EmptyCell
                    myEmptyCells.add(cell);
                }
                else if(cell instanceof AgentCell && ((AgentCell) cell).calculatePercentage(grid.getAllNeighbors(cell)) < mySatisfactionThreshold){
                    myCellsToMove.add(cell);
                }
                else{ // add satisfied cells to list already to be added first
                    myCellList.add(cell);
                }
            }
        }
    }

    @Override
    protected Grid setupGridByProb(){
        int rows = (int) Double.parseDouble(myDataValues.get("rows"));
        int cols = (int) Double.parseDouble(myDataValues.get("columns"));
        double redRate = Double.parseDouble(myDataValues.get("redRate"));
        double blueRate = Double.parseDouble(myDataValues.get("blueRate"));
        List<Cell> cells = new ArrayList<>();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                Cell cell;
                if(evaluateOdds(redRate)){
                    cell = new AgentCell(i, j, "RED");
                }
                else if(evaluateOdds(blueRate)){
                    cell = new AgentCell(i, j, "BLUE");
                }
                else{
                    cell = new EmptyCell(i, j);
                }
                cells.add(cell);
            }
        }
        return createGrid(myDataValues.get("gridShape"), rows, cols, cells);
    }

    @Override
    protected Grid setupGridByQuota(){
        int rows = (int) Double.parseDouble(myDataValues.get("rows"));
        int cols = (int) Double.parseDouble(myDataValues.get("columns"));
        int redRate = (int) Double.parseDouble(myDataValues.get("redRate"));
        int blueRate = (int) Double.parseDouble(myDataValues.get("blueRate"));
        List<String> states = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        for(int k = 0; k < redRate; k++){
            states.add("RED");
        }
        for(int k = 0; k < blueRate; k++){
            states.add("BLUE");
        }
        for(int k = 0; k < (rows*cols-redRate-blueRate+1); k++){
            states.add("EMPTY");
        }
        Collections.shuffle(states);
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                String state = states.remove(0);
                if(state.equals("RED") || state.equals("BLUE")) {
                    cells.add(new AgentCell(i, j, state));
                }
                else if(state.equals("EMPTY")){
                    cells.add(new EmptyCell(i, j));
                }
                else {
                    throw new RuntimeException("Cell type not allowed in " + DATA_TYPE);
                }
            }
        }
        return createGrid(myDataValues.get("gridShape"), rows, cols, cells);
    }

    @Override
    public String getSimType(){
        return DATA_TYPE;
    }


    @Override
    public void createQueueOfCellChoices () {
        myCellChoices = new LinkedList<>();

        Cell c1 = new AgentCell(-1,-1, "RED");
        Cell c2 = new AgentCell(-1,-1,"BLUE");
        Cell c3 = new EmptyCell(-1,-1);


        myCellChoices.add(c1);
        myCellChoices.add(c2);
        myCellChoices.add(c3);
    }

//    @Override
//    public void changeCell() {
//        List<Cell> choices = List.of(new AgentCell(0,0,"BLUE"),
//                new AgentCell(0,0,"BLUE"), new EmptyCell(0,0));
//    }
}
