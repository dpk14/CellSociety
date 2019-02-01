package simulations;

import cells.AgentCell;
import cells.Cell;
import cells.EmptyCell;
import cells.StateChangeCell;

import java.util.*;

public class PercolationSimulation extends Simulation{
    public static final String DATA_TYPE = "PercolationSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "speed");

    public PercolationSimulation(int numRows, int numCols){
        super(numRows, numCols);
    }

    public PercolationSimulation(List<String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(Integer.parseInt(dataValues.get(2)), Integer.parseInt(dataValues.get(3)));
        myGrid = getNewGrid(cells);
    }
/*
    @Override
    public Cell[][] updateGrid(){
        String state;
        Cell cell=new Cell(0, 0);
        List<Cell> openNeighbors;
        myCellList.clear();
        Queue<Cell> qu=openOne();
        while(qu.size()>0){
            cell=qu.remove();
            if(getTypedNeighbors(cell, "FULL").size()>0){
                ((StateChangeCell) cell).setState("FULL");
                myCellList.add(cell);
                openNeighbors=getTypedNeighbors(cell, "OPEN");
                for(Cell open: openNeighbors) qu.add(open);
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

    @Override
    public void setupSimulation(){
        for (int i = 0; i < myGrid.length; i++) {
            for (int j = 0; j < myGrid[i].length; j++) {
                if (i == 3) {
                    myGrid[i][j] = new EmptyCell(i,j);
                } else if (i % 2 == 0) {
                    myGrid[i][j] = new AgentCell(i,j,"BLUE");
                } else {
                    myGrid[i][j] = new AgentCell(i,j,"RED");
                }
            }
        }
    }
*/

    @Override
    public Cell[][] updateGrid() {
        return new Cell[0][];
    }

@Override
public void setupSimulation() {

}
    @Override
    public String getDataType(){
        return DATA_TYPE;
    }

@Override
public List<String> getDataFields(){
    return DATA_FIELDS;
}
}
