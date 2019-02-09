package simulations;

import cells.Cell;
import cells.LangdonCell;
import cells.StateChangeCell;
import cells.SugarPatch;
import grids.Grid;

import java.util.HashMap;
import java.util.List;

public class LangdonLoopSimulation extends Simulation{

    public HashMap<Cell, String[]> mySplitLocations=new HashMap<Cell, String[]>();

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

    public void updatePosition(Cell cell, String state){
        List<Cell> neighbors=myGrid.getImmediateNeighbors(cell);
        if(((LangdonCell) cell).getState.equals("BLUE")) changeNeighborState(cell, neighbors, "BLACK", "BLUE");
        else if(((LangdonCell) cell).getState.equals("BLACK")) changeNeighborState(cell, neighbors, "CYAN", "BLACK");
        else if (((LangdonCell) cell).getState.equals("CYAN"))
            if(getTypedNeighbors(cell,"BLUE", neighbors).size()>0) changeNeighborState(cell, neighbors, "BLUE", "CYAN");
            else{
                int[] direction=getDirection(cell, "BLACK", neighbors);
                Cell ahead=myGrid.getCell(cell.getRow() + direction[0], cell.getColumn() + direction[1]);
                myCellList.add(new LangdonCell(ahead.getRow(), ahead.getColumn(), "BLUE"));
                ahead=myGrid.getCell(ahead.getRow() + direction[0], ahead.getColumn() + direction[1]);
                if(((LangdonCell) ahead).getState.equals("RED")){ //if there is an already existing wall block in front, the loop has closed

                    String blueLocation=

                    mySplitLocations.add(myGrid.getCell(ahead.getRow() + direction[0], ahead.getColumn() + direction[1]);
                }
                changeNeighborState(cell, neighbors, "BLACK", "RED");
                Go to that blue, get its neighbors.
                If that blue has any red neighbors, change the red neimyGrid.getCell(cell.getRow() + rowDif, cell.getColumn() + colDif)ghbor to a blue
                //when pipe turns in on self 	then mark that red neighbor’s column and row,
                // find the difference. Use this info to mark the cell just ahead, store it as the key of a map with
                // information specifying the direction. If rowDif is positive, then “DOWN” and PINK to be set to the
                // right and white to the left, etc. Turn a Boolean count on. Don’t increment the counter yet tho,
                // increment it only right before each iteration through the loop.
                If black neighbors> 0, change all black neighbors to reds.

            }

    }

    private int[] getDirection(Cell cell, String stateofReferenceCell, List<Cell> neighbors) {
        List<Cell> referenceNeighbors = getTypedNeighbors(cell, stateofReferenceCell, neighbors);
        Cell directionReference = referenceNeighbors.get(0);
        int rowDif = cell.getRow() - directionReference.getRow();
        int colDif = cell.getColumn() - directionReference.getColumn();
        int[] direction={rowDif, colDif};
        return direction;
    }

    private void changeNeighborState(Cell cell, List<Cell> neighbors, String currentState, String newState){
        for(Cell neighbor: neighbors){
            if(((LangdonCell) cell).getState.equals(currentState)){
                myCellList.add(new LangdonCell(neighbor.getRow(), neighbor.getColumn(), newState));
            }
        }
    }
}
