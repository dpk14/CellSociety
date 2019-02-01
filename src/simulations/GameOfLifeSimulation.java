package simulations;

import cells.AgentCell;
import cells.Cell;
import cells.EmptyCell;
import cells.StateChangeCell;

import java.util.List;

public class GameOfLifeSimulation extends Simulation{
    public static final String DATA_TYPE = "GameOfLifeSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "speed");

    public GameOfLifeSimulation(int numRows, int numCols){
        super(numRows,numCols);
        setupSimulation();
    }

    public GameOfLifeSimulation(List<String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(Integer.parseInt(dataValues.get(2)), Integer.parseInt(dataValues.get(3)));
        myGrid = getNewGrid(cells);
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

    @Override
    public List<String> getDataFields(){
        return DATA_FIELDS;
    }

    @Override
    public String getDataType(){
        return DATA_TYPE;
    }

}
