package simulations;

import cells.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpreadingFireSimulation extends Simulation{
    public static final String DATA_TYPE = "SpreadingFireSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "speed", "spread rate", "growth rate", "lightning rate");
    private Map<String, String> myDataValues;
    public SpreadingFireSimulation(int numRows, int numCols){
        super(numRows, numCols);
        myDataValues = new HashMap<>();
    }

    public SpreadingFireSimulation(Map<String, String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(Integer.parseInt(dataValues.get("rows")), Integer.parseInt(dataValues.get("columns")));
        myGrid = getNewGrid(cells);
        myDataValues = dataValues;
    }

    @Override
    public List<String> getDataFields(){
        return DATA_FIELDS;
    }

    @Override
    public Map<String, String> getMyDataValues(){
        return myDataValues;
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

}
