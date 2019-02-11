package simulations;

import cells.*;
import grids.Grid;

import java.util.*;

public class LangdonLoopSimulation extends Simulation{

    private HashMap<Cell, Cell[]> mySplitLocations=new HashMap<Cell, Cell[]>();
    private List<Cell> myPurpleCells=new ArrayList<Cell>();
    public static final String DATA_TYPE = "LangdonLoopSimulation";
    public LangdonLoopSimulation(Map<String, String> dataValues, List<Cell> cells) {
        super(dataValues, cells);
        setupSliderInfo();
        createQueueOfCellChoices();
    }

    public LangdonLoopSimulation(Map<String, String> dataValues) {
        super(dataValues);
        setupSliderInfo();
        createQueueOfCellChoices();
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
            for(Cell key: mySplitLocations.keySet()){
                ((LangdonCell) key).incrementCounter();
                if(((LangdonCell) key).getCounter()==3){
                    Cell[] cells=mySplitLocations.get(key);
                    for(Cell cell: cells) myGrid.setCell(cell, cell.getRow(), cell.getColumn());
                }
            }
            for (Cell purpleCell: myPurpleCells) movePurpleCell(purpleCell);
        return myGrid;
    }

    private void movePurpleCell(Cell purpleCell){
        int direction[]=((LangdonCell) purpleCell).getDirection();
        if(((LangdonCell) purpleCell).getCounter()==0) {
            myGrid.setCell(new LangdonCell(purpleCell.getRow()-direction[1], purpleCell.getColumn()+direction[0], "BLACK"));
            myGrid.setCell(new LangdonCell(purpleCell.getRow(), purpleCell.getColumn(), "RED"));
        }
        else if(((LangdonCell) purpleCell).getCounter()==1) {
            myGrid.setCell(new LangdonCell(purpleCell.getRow()-direction[1], purpleCell.getColumn()+direction[0], "BLACK"));
            myGrid.setCell(new LangdonCell(purpleCell.getRow()+direction[1], purpleCell.getColumn()-direction[0], "BLACK"));
            myGrid.setCell(new LangdonCell(purpleCell.getRow(), purpleCell.getColumn(), "BLACK"));
        }
        else if(((LangdonCell) purpleCell).getCounter()==2) {
            direction=new int[]{direction[1], -direction[0]}; //rotates 90 degrees clockwise
        }
        else if(((LangdonCell) purpleCell).getCounter()>2){
            myGrid.setCell(new LangdonCell(purpleCell.getRow(), purpleCell.getColumn(), "RED"));
        }
        myGrid.setCell(purpleCell, purpleCell.getRow()+direction[0], purpleCell.getColumn()+direction[1]);
        ((LangdonCell) purpleCell).incrementCounter();
    }



    private void updatePosition(Cell cell, String state) {
        List<Cell> neighbors = myGrid.getImmediateNeighbors(cell);
        int[] direction;
        if (((LangdonCell) cell).getState().equals("BLUE")) changeNeighborState(cell, neighbors, "BLACK", "BLUE");
        else if (((LangdonCell) cell).getState().equals("BLACK")) {
            changeNeighborState(cell, neighbors, "CYAN", "BLACK");
            changeNeighborState(cell, neighbors, "WHITE", "BLACK");
        }
        else if (((LangdonCell) cell).getState().equals("PURPLE") && !myPurpleCells.contains(cell)) myPurpleCells.add(cell);
        else if (((LangdonCell) cell).getState().equals("WHITE")) changeNeighborState(cell, neighbors, "BLUE", "WHITE");
        else if (((LangdonCell) cell).getState().equals("CYAN")) {
            if (getTypedNeighbors(cell, "BLUE", neighbors).size() > 0)
                changeNeighborState(cell, neighbors, "BLUE", "CYAN");
            else if (getTypedNeighbors(cell, "PINK", neighbors).size() > 0);
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
        LangdonCell[] splitterCells = new LangdonCell[3]; //{blue location, pink location, white location}
        if (direction[0] > 0) {
            splitterCells[0] = new LangdonCell(row - 1, col, "BLUE");
            splitterCells[1] = new LangdonCell(row, col - 1, "PINK");
            splitterCells[2] = new LangdonCell(row, col + 1, "WHITE");
        } else if (direction[0] < 0) {
            splitterCells[0] = new LangdonCell(row + 1, col, "BLUE");
            splitterCells[1] = new LangdonCell(row, col + 1, "PINK");
            splitterCells[2] = new LangdonCell(row, col - 1, "WHITE");
        } else if (direction[1] < 0) {
            splitterCells[0] = new LangdonCell(row, col + 1, "BLUE");
            splitterCells[1] = new LangdonCell(row - 1, col, "PINK");
            splitterCells[2] = new LangdonCell(row + 1, col, "WHITE");
        } else {
            splitterCells[0] = new LangdonCell(row, col - 1, "BLUE");
            splitterCells[1] = new LangdonCell(row + 1, col, "PINK");
            splitterCells[2] = new LangdonCell(row - 1, col, "WHITE");
        }
        int[] purpleDirection={direction[1], -direction[0]}; //90 degrees clockwise
        splitterCells[1].setDirection(purpleDirection);
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

    private List<Cell> changeNeighborState(Cell cell, List<Cell> neighbors, String stateToChange, String newState){
        List<Cell> changedNeighbors=new ArrayList<Cell>();
        for(Cell neighbor: neighbors){
            if(((LangdonCell) cell).getState().equals(stateToChange)){
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


    @Override
    public void createQueueOfCellChoices () {
        myCellChoices = new LinkedList<>();
        // TODO
    }
}
