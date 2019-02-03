package simulations;

import cells.Cell;
import cells.EmptyCell;
import cells.StateChangeCell;

import java.util.*;

public class PercolationSimulation extends Simulation{
    public static final String DATA_TYPE = "PercolationSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "speed");
    private Map<String, String> myDataValues;
    public PercolationSimulation(int numRows, int numCols){
        super(numRows, numCols);
        myDataValues = new HashMap<>();
    }

    public PercolationSimulation(Map<String, String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(Integer.parseInt(dataValues.get("rows")), Integer.parseInt(dataValues.get("columns")));
        myGrid = getNewGrid(cells);
        myDataValues = dataValues;
    }

    @Override
    public Cell[][] updateGrid(){
        if (percolates()) return myGrid;
        Cell cell = new EmptyCell(0, 0);
        List<Cell> openNeighbors;
        myCellList.clear();
        Queue<Cell> qu = openOne();
        while (qu.size() > 0) {
            cell = qu.remove();
            if (getTypedNeighbors(cell, "FULL").size() > 0 || cell.getRow() == 0) {
                ((StateChangeCell) cell).setState("FULL");
                myCellList.add(cell);
                openNeighbors = getTypedNeighbors(cell, "OPEN");
                for (Cell open : openNeighbors) qu.add(open);
            }
        }
        myGrid = getNewGrid(this.myCellList);
        return myGrid;
    }

    private Queue<Cell> openOne(){
        Queue<Cell> qu=new LinkedList<Cell>();
        Cell cell=new StateChangeCell(0, 0, "OPEN");
        Random rand=new Random();

        while(!((StateChangeCell) cell).getState().equals("CLOSED")) {
            cell=myGrid[rand.nextInt(myGrid.length)][rand.nextInt(myGrid[0].length)];
        }

        ((StateChangeCell) cell).setState("OPEN");
        qu.add(cell);
        return qu;
    }

    private boolean percolates(){
        for(int k=0; k<myGrid[0].length; k++) {
            Cell cell=myGrid[myGrid.length - 1][k];
            if (((StateChangeCell) cell).getState().equals("FULL")) return true;
        }
        return false;
        }

    @Override
    public String getDataType(){
        return DATA_TYPE;
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
    public void updateParameters(Map<String, String> map){
        myDataValues = map;
    }
}
