package simulations;

import cells.Cell;

import java.util.List;

public class GameOfLifeSimulation extends Simulation{
    public static final String DATA_TYPE = "GameOfLifeSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "speed");
    public GameOfLifeSimulation(int numRows, int numCols){
        super(numRows, numCols);
    }

    public GameOfLifeSimulation(List<String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
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
