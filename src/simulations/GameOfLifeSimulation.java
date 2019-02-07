package simulations;

import cells.Cell;
import cells.StateChangeCell;
import grids.Grid;

import java.util.List;
import java.util.Map;

public class GameOfLifeSimulation extends Simulation{
    public static final String DATA_TYPE = "GameOfLifeSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "cellShape", "gridShape", "rows", "columns", "speed", "populatedRate");
    private Map<String, String> myDataValues;

    public GameOfLifeSimulation(Map<String, String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(dataValues, cells);
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
    public void setupGrid(){
        double populatedRate = Double.parseDouble(myDataValues.get("populatedRate"));
        // TODO create randomized grid and set to myGrid
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
    public Map<String, String> getMyDataValues(){
        return myDataValues;
    }

    @Override
    public void updateParameters(Map<String, String> map){
        myDataValues = map;
    }
}
