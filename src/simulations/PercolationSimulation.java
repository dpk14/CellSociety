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

    @Override
    public Cell[][] updateGrid(){
        String state;
        myCellList.clear();
        Queue<Cell> qu=openOne();
        while(qu.size()!=0)
            if(((StateChangeCell) cell).getState().equals("OPEN")) ((StateChangeCell) cell).setState(fill(cell, state));
            myCellList.add(cell);
        }
        myGrid = getNewGrid(this.myCellList);
        return myGrid;
    }

    private Queue<Cell> openOne(){
        List<Cell> revisedList=new ArrayList<>();
        Queue<Cell> qu=new LinkedList<Cell>();

        for(int i = 0; i < myGrid.length; i++) { // i = row number
            for (int j = 0; j < myGrid[0].length; j++) { // j = column number
                revisedList.add(myGrid[i][j]);
            }
        }
        Collections.shuffle(revisedList);
        for(Cell cell:revisedList){
            if (((StateChangeCell) cell).getState().equals("CLOSED")) {
                ((StateChangeCell) cell).setState("OPEN");
                if (cell.getRow()==0 || getTypedNeighbors(cell, "FULL").size()!=0) {
                    ((StateChangeCell) cell).setState("FULL");
                    qu.add(cell);
                }
                break;
            }
        }
        return qu;
    }

    private String fill(Cell cell, String state){
        List<Cell> neighbors=getNeighbors(cell);
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

    protected List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = super.getNeighbors(cell);
        List<Cell> fullNeighbors=new ArrayList<Cell>();
        for(Cell neighbor: fullNeighbors){
            if (((StateChangeCell) cell).getState().equals("FULL")) burnNeighbors.add(neighbor);
        }
        return burnNeighbors;
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
