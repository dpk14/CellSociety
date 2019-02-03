package simulations;

import cells.AgentCell;
import cells.Cell;
import cells.EmptyCell;
import cells.StateChangeCell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpreadingFireSimulation extends Simulation{
    public static final String DATA_TYPE = "SpreadingFireSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "speed", "spreadRate", "growthRate", "lightningRate");
    private Map<String, String> myDataValues;

    private double myProbCatch;
    private double myProbLightning;
    private double myProbGrow;

    public SpreadingFireSimulation(int numRows, int numCols){
        super(numRows, numCols);
        myDataValues = new HashMap<>();
    }

    public SpreadingFireSimulation(Map<String, String> dataValues, List<Cell> cells) { // pass in list of strings representing rows, columns, sat threshold
        super(Integer.parseInt(dataValues.get("rows")), Integer.parseInt(dataValues.get("columns")));
        myProbCatch = Double.parseDouble(dataValues.get("spreadRate"));
        myProbGrow=Double.parseDouble(dataValues.get("growthRate"));
        myProbLightning=Double.parseDouble(dataValues.get("lightningRate"));
        myGrid = getNewGrid(cells);
        myDataValues = dataValues;
    }

    public SpreadingFireSimulation(int numRows, int numCols, double probCatch, double probLightning, double probGrow){
        super(numRows,numCols);
        myProbCatch=probCatch;
        myProbLightning=probLightning;
        myProbGrow=probGrow;
        //setupSimulation();
    }

    public SpreadingFireSimulation(List<String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(Integer.parseInt(dataValues.get(2)), Integer.parseInt(dataValues.get(3)));
        myGrid = getNewGrid(cells);
        myDataValues = new HashMap<>();
    }


    @Override
    public Cell[][] updateGrid(){
        String state;
        myCellList.clear();
        for(int i = 0; i < myGrid.length; i++){ // i = row number
            for(int j = 0; j < myGrid[0].length; j++){ // j = column number
                Cell cell = myGrid[i][j];
                state=((StateChangeCell) cell).getState();
                if(((StateChangeCell) cell).getState().equals("BURNING")) ((StateChangeCell) cell).setState("EMPTY");
                else ((StateChangeCell) cell).setState(randomizeState(cell, state));
                myCellList.add(cell);
            }
        }
        myGrid = getNewGrid(this.myCellList);
        return myGrid;
    }

    @Override
    public Map<String, String> getMyDataValues(){
        return myDataValues;
    }

    private String randomizeState(Cell cell, String state){
        double rand=Math.random();

        if(state.equals("TREE")){
            int firecount=getTypedNeighbors(cell, "BURNING").size();
            if((firecount!=0 && rand/firecount<myProbCatch) || rand<myProbCatch*myProbLightning) return "BURNING";
        }
        else if (rand<myProbGrow) return "TREE";
        return state;
    }

    @Override
    public List<String> getDataFields(){
        return DATA_FIELDS;
    }

    @Override
    public String getDataType(){
        return DATA_TYPE;
    }

}
