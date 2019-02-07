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
        List<Cell> neighbors=myGrid.getAllNeighbors(cell, myGrid);
        List<Cell> neighbors=myGrid.getTypedNeighbors(cell, "POPULATED");
        if (state.equals("POPULATED") && (neighbors.size()<=1 || neighbors.size()>=4)) return "EMPTY";
        else if (state.equals("EMPTY") && neighbors.size()==3) return "POPULATED";
        return state;
    }

    @Override
    /**
     * Overrides the superclass's method. It takes the result of the superclass's method while adding the corner
     * neighbors. A key assumption is that each row in the grid has the same number of entries.
     * @return ArrayList<Cell> neighbors
     */
    protected List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = super.getNeighbors(cell);
        int row = cell.getRow();
        int column = cell.getColumn();
        if(row != 0 && column != 0){ //check top-left neighbor
            neighbors.add(myGrid[row-1][column-1]);
        }
        if(row != 0 && column != myGrid[0].length-1){ //check top-right neighbor
            neighbors.add(myGrid[row-1][column+1]);
        }
        if(row != myGrid.length-1 && column != 0){ //check bottom-left neighbor
            neighbors.add(myGrid[row+1][column-1]);
        }
        if(row != myGrid.length-1 && column != myGrid[0].length-1){ //check bottom-right neighbor
            neighbors.add(myGrid[row+1][column+1]);
        }
        return neighbors;
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
