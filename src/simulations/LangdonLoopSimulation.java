package simulations;

import cells.Cell;
import cells.LangdonCell;
import cells.StateChangeCell;
import grids.Grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LangdonLoopSimulation extends Simulation{

    public static final String DATA_TYPE = "LangdonLoopSimulation";

    public HashMap<Cell, Cell[]> mySplitLocations=new HashMap<Cell, Cell[]>();

    public LangdonLoopSimulation(Map<String, String> dataValues, List<Cell> cells) {
        super(dataValues, cells);
        setupSliderInfo();

    }

    public LangdonLoopSimulation(Map<String, String> dataValues) {
        super(dataValues);
        setupSliderInfo();
    }


    public Grid advanceSimulation() {
        myCellList.clear();
        String state;
            for(int i = 0; i < myGrid.getHeight(); i++) { // i = row number
                for (int j = 0; j < myGrid.getWidth(); j++) { // j = column number
                    Cell cell = myGrid.getCell(i, j);
                    state = ((StateChangeCell) cell).getState();
                    updatePosition(cell, state);
                }
            }
        myGrid.updateGrid(myCellList);
        return myGrid;
    }

    public void updatePosition(Cell cell, String state) {
        List<Cell> neighbors = myGrid.getImmediateNeighbors(cell);
        int[] direction;
        if (((LangdonCell) cell).getState().equals("BLUE")) changeNeighborState(cell, neighbors, "BLACK", "BLUE");
        else if (((LangdonCell) cell).getState().equals("BLACK")) changeNeighborState(cell, neighbors, "CYAN", "BLACK");
        else if (((LangdonCell) cell).getState().equals("CYAN")) {
            if (getTypedNeighbors(cell, "BLUE", neighbors).size() > 0)
                changeNeighborState(cell, neighbors, "BLUE", "CYAN");
            else {
                direction = getDirection(cell, "BLACK", neighbors);
                Cell ahead1 = myGrid.getCell(cell.getRow() + direction[0], cell.getColumn() + direction[1]);
                myCellList.add(new LangdonCell(ahead1.getRow(), ahead1.getColumn(), "BLUE"));
                Cell ahead2 = myGrid.getCell(ahead1.getRow() + direction[0], ahead1.getColumn() + direction[1]);
                if (((LangdonCell) ahead2).getState().equals("RED")) { //if there is an already existing wall block in front, the loop has closed
                    Cell ahead3 = myGrid.getCell(ahead2.getRow() + direction[0], ahead2.getColumn() + direction[1]);
                    markForSplitting(ahead3, direction);
                }
                changeNeighborState(ahead1, neighbors, "BLACK", "RED");
            }
        }
        else if (((LangdonCell) cell).getState().equals("YELLOW")){
            List<Cell> changedNeighbors=changeNeighborState(cell, neighbors, "GREEN", "BLUE");
            if (changedNeighbors.size()>0){
                Cell greenCell=changedNeighbors.get(0);
                changeNeighborState(greenCell, neighbors, "BLACK", "RED");
            }
            else if (getTypedNeighbors(cell, "BLUE", neighbors).size()==0){
                direction=getDirection(cell, "BLACK", neighbors);
                Cell counterClckwse90 = myGrid.getCell(cell.getRow() - direction[1], cell.getColumn() + direction[0]);
                myCellList.add(new LangdonCell(counterClckwse90.getRow(), counterClckwse90.getColumn(), "GREEN"));
            }
        }
    }

    public void markForSplitting(Cell splitLocation, int[] direction) {
        int row = splitLocation.getRow();
        int col = splitLocation.getColumn();
        Cell[] splitterCells = new Cell[3]; //{blue location, pink location, white location}
        if (direction[0] > 0) {
            splitterCells[0] = new LangdonCell(row - 1, col, "BLUE");
            splitterCells[1] = new LangdonCell(row, col - 1, "PINK");
            splitterCells[2] = new LangdonCell(row, col + 1, "WHITE");
        } else if (direction[0] < 0) {
            splitterCells[0] = new LangdonCell(row + 1, col, "BLUE");
            splitterCells[1] = new LangdonCell(row, col + 1, "PINK");
            splitterCells[2] = new LangdonCell(row, col - 1, "WHITE");
        } else if (direction[1] > 0) {
            splitterCells[0] = new LangdonCell(row, col + 1, "BLUE");
            splitterCells[1] = new LangdonCell(row - 1, col, "PINK");
            splitterCells[2] = new LangdonCell(row + 1, col, "WHITE");
        } else {
            splitterCells[0] = new LangdonCell(row, col - 1, "BLUE");
            splitterCells[1] = new LangdonCell(row + 1, col, "PINK");
            splitterCells[2] = new LangdonCell(row - 1, col, "WHITE");
        }
        mySplitLocations.put(splitLocation, splitterCells);
    }

    private int[] getDirection(Cell cell, String stateofReferenceCell, List<Cell> neighbors) {
        List<Cell> referenceNeighbors = getTypedNeighbors(cell, stateofReferenceCell, neighbors);
        Cell directionReference = referenceNeighbors.get(0);
        int rowDif = cell.getRow() - directionReference.getRow();
        int colDif = cell.getColumn() - directionReference.getColumn();
        int[] direction={rowDif, colDif};
        return direction;
    }

    private List<Cell> changeNeighborState(Cell cell, List<Cell> neighbors, String currentState, String newState){
        List<Cell> changedNeighbors=new ArrayList<Cell>();
        for(Cell neighbor: neighbors){
            if(((LangdonCell) cell).getState().equals(currentState)){
                myCellList.add(new LangdonCell(neighbor.getRow(), neighbor.getColumn(), newState));
                changedNeighbors.add(neighbor);
            }
        }
        return changedNeighbors;
    }

    @Override
    protected Grid setupGridByProb() {
        return null;
    }

    @Override
    protected Grid setupGridByQuota() {
        return null;
    }

    @Override
    public String getSimType() {
        return null;
    }

}
