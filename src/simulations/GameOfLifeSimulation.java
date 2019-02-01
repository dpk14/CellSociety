package simulations;

import cells.Cell;

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
    public List<String> getDataFields(){
        return DATA_FIELDS;
    }

    @Override
    public String getDataType(){
        return DATA_TYPE;
    }

    @Override
    public Cell[][] updateGrid() {
        return new Cell[0][];
    }

    @Override
    public void setupSimulation() {
    }

    @Override
    public Map<String, String> getMyDataValues(){
        return myDataValues;
    }
}
