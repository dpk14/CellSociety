package simulations;

import cells.Cell;
import cells.StateChangeCell;
import cells.Cell;
import cells.StateChangeCell;
import grids.Grid;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpreadingFireSimulation extends Simulation{
    public static final String DATA_TYPE = "SpreadingFireSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "speed", "spreadRate", "growthRate", "lightningRate",
            "treeRate", "burningRate");
    private Map<String, String> myDataValues;

    private double myProbCatch;
    private double myProbLightning;
    private double myProbGrow;

    public SpreadingFireSimulation(Map<String, String> dataValues, List<Cell> cells) { // pass in list of strings representing rows, columns, sat threshold
        super(dataValues, cells);
        myProbCatch = Double.parseDouble(dataValues.get("spreadRate"));
        myProbGrow=Double.parseDouble(dataValues.get("growthRate"));
        myProbLightning=Double.parseDouble(dataValues.get("lightningRate"));
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
    public Map<String, String> getMyDataValues(){
        return myDataValues;
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
    public List<String> getDataFields(){
        return DATA_FIELDS;
    }

    @Override
    public String getDataType(){
        return DATA_TYPE;
    }

    @Override
    public void updateParameters(Map<String, String> map) {
        myProbCatch = Double.parseDouble(map.get("spreadRate"));
        myProbGrow = Double.parseDouble(map.get("growthRate"));
        myProbLightning = Double.parseDouble(map.get("lightningRate"));
        myDataValues = map;
    }

    @Override
    public void setupGrid(){
        double treeRate = Double.parseDouble(myDataValues.get("treeRate"));
        double burningRate = Double.parseDouble(myDataValues.get("burningRate"));
        // TODO create randomized grid and set to myGrid
    }

}
