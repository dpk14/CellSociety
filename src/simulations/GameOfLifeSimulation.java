package simulations;

import cells.Cell;
import cells.StateChangeCell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameOfLifeSimulation extends Simulation{
    public static final String DATA_TYPE = "GameOfLifeSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "speed");
    private Map<String, String> myDataValues;

    public GameOfLifeSimulation(int numRows, int numCols){
        super(numRows, numCols);
        myDataValues = new HashMap<>();
    }

    public GameOfLifeSimulation(Map<String, String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(Integer.parseInt(dataValues.get("rows")), Integer.parseInt(dataValues.get("columns")));
        myGrid = getNewGrid(cells);
        myDataValues = dataValues;
    }

    @Override
    public Cell[][] updateGrid(){
        String state;
        myCellList.clear();
        for(int i = 0; i < myGrid.length; i++){ // i = row number
            for(int j = 0; j < myGrid[0].length; j++){ // j = column number
                Cell cell = myGrid[i][j];
                state=((StateChangeCell) cell).getState();
                ((StateChangeCell) cell).setState(editState(cell, state));
                myCellList.add(cell);
            }
        }
        myGrid = getNewGrid(this.myCellList);
        return myGrid;
    }

    private String editState(Cell cell, String state){
        List<Cell> neighbors=getNeighbors(cell);
        if (state.equals("POPULATED") && (neighbors.size()<=1 || neighbors.size()>=4)) return "EMPTY";
        else if (state.equals("EMPTY") && neighbors.size()==3) return "POPULATED";
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
    public Map<String, String> getMyDataValues(){
        return myDataValues;
    }
}
