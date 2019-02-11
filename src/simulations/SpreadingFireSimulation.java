package simulations;

import cells.AgentCell;
import cells.Cell;
import cells.EmptyCell;
import cells.StateChangeCell;
import grids.Grid;

import java.util.*;

public class SpreadingFireSimulation extends Simulation{
    public static final String DATA_TYPE = "SpreadingFireSimulation";
//    public static final List<String> DATA_FIELDS = List.of(
//            "title", "author", "rows", "columns", "cellShape", "gridShape", "speed", "spreadRate", "growthRate", "lightningRate",
//            "treeRate", "burningRate");

    private double myProbCatch;
    private double myProbLightning;
    private double myProbGrow;

    public SpreadingFireSimulation(Map<String, String> dataValues, List<Cell> cells) { // pass in list of strings representing rows, columns, sat threshold
        super(dataValues, cells);
        setValues();
        setupSliderInfo();
    }

    public SpreadingFireSimulation(Map<String, String> dataValues){
        super(dataValues);
        setValues();
        setupSliderInfo();
    }

    @Override
    public Grid advanceSimulation(){
        String state;
        myCellList.clear();
        for(int i = 0; i < myGrid.getHeight(); i++){ // i = row number
            for(int j = 0; j < myGrid.getWidth(); j++){ // j = column number
                Cell cell = myGrid.getCell(i,j);
                state=((StateChangeCell) cell).getState();
                if(((StateChangeCell) cell).getState().equals("BURNING")) myCellList.add(new StateChangeCell(i, j, "EMPTY"));
                else myCellList.add(new StateChangeCell(i, j, randomizeState(cell, state)));
            }
        }
        myGrid.updateGrid(myCellList);
        return myGrid;
    }

    @Override
    protected void setupSliderInfo() {
        super.setupSliderInfo();
        mySliderInfo.put("spreadRate", myDataValues.get("spreadRate"));
        mySliderInfo.put("growthRate", myDataValues.get("growthRate"));
        mySliderInfo.put("lightningRate", myDataValues.get("lightningRate"));
        addSliderInfo("treeRate");
        addSliderInfo("burningRate");
    }

    private String randomizeState(Cell cell, String state){
        double rand=Math.random();
        if(state.equals("TREE")){
            List<Cell> neighbors=myGrid.getImmediateNeighbors(cell);
            int firecount=getTypedNeighbors(cell, "BURNING", neighbors).size();
            if(firecount!=0){
                if (rand/firecount<myProbCatch) return "BURNING";
                }
            else if (rand<myProbCatch*myProbLightning) return "BURNING";
        }
        else if (rand<myProbGrow) return "TREE";
        return state;
    }

    @Override
    public void updateParameters() {
        super.updateParameters();
        setValues();
    }

    @Override
    protected Grid setupGridByProb(){
        int rows = (int) Double.parseDouble(myDataValues.get("rows"));
        int cols = (int) Double.parseDouble(myDataValues.get("columns"));
        double treeRate = Double.parseDouble(myDataValues.get("treeRate"));
        double burningRate = Double.parseDouble(myDataValues.get("burningRate"));
        List<Cell> cells = new ArrayList<>();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                Cell cell;
                if(evaluateOdds(treeRate)){
                    cell = new StateChangeCell(i, j, "TREE");
                }
                else if(evaluateOdds(burningRate)){
                    cell = new StateChangeCell(i, j, "BURNING");
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
    protected Grid setupGridByQuota() {
        int rows = (int) Double.parseDouble(myDataValues.get("rows"));
        int cols = (int) Double.parseDouble(myDataValues.get("columns"));
        int treeRate = (int) Double.parseDouble(myDataValues.get("treeRate"));
        int burningRate = (int) Double.parseDouble(myDataValues.get("burningRate"));
        List<String> states = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        for (int k = 0; k < treeRate; k++) {
            states.add("TREE");
        }
        for (int k = 0; k < burningRate; k++) {
            states.add("BURNING");
        }
        for (int k = 0; k < (rows * cols - burningRate - treeRate + 1); k++) {
            states.add("EMPTY");
        }
        Collections.shuffle(states);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String state = states.remove(0);
                if (state.equals("TREE") || state.equals("BURNING") || state.equals("EMPTY")) {
                    cells.add(new StateChangeCell(i, j, state));
                } else {
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

    private void setValues(){
        myProbCatch = Double.parseDouble(myDataValues.get("spreadRate"));
        myProbGrow=Double.parseDouble(myDataValues.get("growthRate"));
        myProbLightning=Double.parseDouble(myDataValues.get("lightningRate"));
    }

    @Override
    public void createQueueOfCellChoices () {
        myCellChoices = new LinkedList<>();

        Cell c1 = new StateChangeCell(-1,-1, "TREE");
        Cell c2 = new StateChangeCell(-1,-1,"BURNING");
        Cell c3 = new StateChangeCell(-1,-1, "EMPTY");


        myCellChoices.add(c1);
        myCellChoices.add(c2);
        myCellChoices.add(c3);
    }
}
