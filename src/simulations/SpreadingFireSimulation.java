package simulations;

import cells.Cell;

import java.util.List;

public class SpreadingFireSimulation extends Simulation{
    public static final String DATA_TYPE = "SpreadingFireSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "speed", "spread rate", "growth rate", "lightning rate");
    public SpreadingFireSimulation(int numRows, int numCols){
        super(numRows, numCols);
    }

    public SpreadingFireSimulation(List<String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(Integer.parseInt(dataValues.get(2)), Integer.parseInt(dataValues.get(3)));
        myGrid = getNewGrid(cells);
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

}
