package simulations;

import cells.Cell;
import cells.StateChangeCell;
import grids.Grid;

import java.util.*;

public class GameOfLifeSimulation extends Simulation{
    public static final String DATA_TYPE = "GameOfLifeSimulation";
//    public static final List<String> DATA_FIELDS = List.of(
//            "title", "author", "cellShape", "gridShape", "rows", "columns", "speed", "populatedRate");

    public static final double POPULATED_RATE_DEAFULT = 0.40;

    public GameOfLifeSimulation(Map<String, String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(dataValues, cells);
        setupSliderInfo();
        createQueueOfCellChoices();
    }

    public GameOfLifeSimulation(Map<String, String> dataValues){
        super(dataValues);
        setupSliderInfo();
        createQueueOfCellChoices();
    }

    @Override
    public Grid advanceSimulation(){
        String state;
        myCellList.clear();
        for(int i = 0; i < myGrid.getHeight(); i++){ // i = row number
            for(int j = 0; j < myGrid.getWidth(); j++){ // j = column number
                Cell cell = myGrid.getCell(i, j);
                state=((StateChangeCell) cell).getState();
                cell=new StateChangeCell(i, j, editState(cell, state));
                myCellList.add(cell);
            }
        }
        myGrid.updateGrid(myCellList);
        return myGrid;
    }

    private String editState(Cell cell, String state){
        List<Cell> neighbors=myGrid.getAllNeighbors(cell);
        neighbors=getTypedNeighbors(cell, "POPULATED", neighbors);
        if (state.equals("POPULATED") && (neighbors.size()<=1 || neighbors.size()>=4)) return "EMPTY";
        else if (state.equals("EMPTY") && neighbors.size()==3) return "POPULATED";
        return state;
    }

    @Override
    protected void setupSliderInfo() {
        super.setupSliderInfo();
        addSliderInfo("populatedRate");
    }

    @Override
    protected Grid setupGridByProb(){
        int rows = (int) readInValue("rows", ROW_DEFAULT);
        int cols = (int) readInValue("columns", COL_DEFAULT);
        double populatedRate = readInValue("populatedRate", POPULATED_RATE_DEAFULT);
        List<Cell> cells = new ArrayList<>();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                Cell cell;
                if(evaluateOdds(populatedRate)){
                    cell = new StateChangeCell(i, j, "POPULATED");
                }
                else{
                    cell = new StateChangeCell(i, j, "EMPTY");
                }
                cells.add(cell);
            }
        }
        return createGrid(myDataValues.get("gridShape"), rows, cols, cells);
    }

    @Override
    protected Grid setupGridByQuota(){
        int rows = (int) readInValue("rows", ROW_DEFAULT);
        int cols = (int) readInValue("columns", COL_DEFAULT);
        int populatedRate = (int) readInValue("populatedRate", POPULATED_RATE_DEAFULT*rows*cols);
        List<String> states = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        for(int k = 0; k < (int) populatedRate; k++){
            states.add("POPULATED");
        }
        for(int k = 0; k < (int) (rows*cols-populatedRate+1); k++){
            states.add("EMPTY");
        }
        Collections.shuffle(states);
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                String state = states.remove(0);
                if(state.equals("POPULATED") || state.equals("EMPTY")) {
                    cells.add(new StateChangeCell(i, j, state));
                }
                else {
                    throw new RuntimeException("Cell type not allowed in " + DATA_TYPE);
                }
            }
        }
        return createGrid(myDataValues.get("gridShape"), rows, cols, cells);
    }

    @Override
    public void createQueueOfCellChoices () {
        myCellChoices = new LinkedList<>();

        Cell c1 = new StateChangeCell(-1,-1, "POPULATED");
        Cell c2 = new StateChangeCell(-1,-1,"EMPTY");

        myCellChoices.add(c1);
        myCellChoices.add(c2);
    }




    @Override
    public String getSimType(){
        return DATA_TYPE;
    }
}
