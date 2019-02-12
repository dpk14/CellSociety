package simulations;

import cells.*;
import grids.Grid;

import java.util.*;

public class LangdonLoopSimulation extends Simulation{

    private HashMap<Cell, Cell[]> mySplitLocations=new HashMap<Cell, Cell[]>();
    private List<Cell> mypinkCells=new ArrayList<Cell>();
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
                    int row=cell.getRow();
                    int col=cell.getColumn();
                    List<Cell> neighbors = myGrid.getImmediateNeighbors(cell);
                    updatePosition(cell, row, col, state, neighbors);
                }
            }
        myGrid.updateGrid(myCellList);
            for(Cell key: mySplitLocations.keySet()){
                ((LangdonCell) key).incrementCounter();
                if(((LangdonCell) key).getCounter()==2) myGrid.setCell(new LangdonCell(key.getRow(), key.getColumn(), "BLACK"));
                if(((LangdonCell) key).getCounter()==3){ //3 turns after loop is closed, initialize splitter cells
                    Cell[] whiteAndPinkSplitCells=mySplitLocations.get(key);
                    myGrid.setCell(new LangdonCell(key.getRow(), key.getColumn(), "BLACK"));
                    for(Cell cell: whiteAndPinkSplitCells) myGrid.setCell(cell, cell.getRow(), cell.getColumn());
                }
            }
            for (Cell pinkCell: mypinkCells) movepinkCell(pinkCell);
        return myGrid;
    }

    private void updatePosition(Cell cell, int row, int col, String state, List<Cell> neighbors) {
        int[] direction;
        if (state.equals("BLUE") && getTypedNeighbors(cell, "PINK", neighbors).size()==0){
            changeNeighborState(cell, neighbors, "BLACK", "BLUE");
        }
        else if (state.equals("BLACK")) {
            changeNeighborState(cell, neighbors, "CYAN", "BLACK");
            changeNeighborState(cell, neighbors, "WHITE", "BLACK");}
        else if (state.equals("PINK") && !mypinkCells.contains(cell)) mypinkCells.add(cell);
        else if (state.equals("WHITE")) moveWhiteCell(cell, row, col, neighbors);
        else if (state.equals("CYAN") && getTypedNeighbors(cell, "PINK", neighbors).size()==0) {
            if (getTypedNeighbors(cell, "BLUE", neighbors).size() > 0)
                changeNeighborState(cell, neighbors, "BLUE", "CYAN");
            else {
                direction = getDirection(cell, "BLACK", neighbors);
                Cell ahead1 = myGrid.getCell(row + direction[0], col + direction[1]);
                myCellList.add(new LangdonCell(ahead1.getRow(), ahead1.getColumn(), "BLUE"));
                Cell ahead2 = myGrid.getCell(ahead1.getRow() + direction[0], ahead1.getColumn() + direction[1]);
                if (((LangdonCell) ahead2).getState().equals("RED")) { //if there is an already existing wall block in front, the loop has closed
                    Cell ahead3 = myGrid.getCell(ahead2.getRow() + direction[0], ahead2.getColumn() + direction[1]);
                    markForSplitting(ahead3, direction);}
                changeNeighborState(ahead1, neighbors, "BLACK", "RED");
            }
        }
        else if (state.equals("YELLOW") && getTypedNeighbors(cell, "PINK", neighbors).size()==0){
            List<Cell> changedNeighbors=changeNeighborState(cell, neighbors, "GREEN", "BLUE");
            if (changedNeighbors.size()>0){
                Cell greenCell=changedNeighbors.get(0);
                changeNeighborState(greenCell, neighbors, "BLACK", "RED");
            }
            else if (getTypedNeighbors(cell, "BLUE", neighbors).size()==0){
                direction=getDirection(cell, "BLACK", neighbors);
                Cell counterClckwse90 = myGrid.getCell(row - direction[1], col+ direction[0]);
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
        int[] pinkDirection={direction[1], -direction[0]}; //90 degrees clockwise
        int[] whiteDirection={-direction[1], direction[0]}; //90 degrees counter
        splitterCells[1].setDirection(pinkDirection);
        splitterCells[2].setDirection(whiteDirection);
        mySplitLocations.put(splitLocation, splitterCells);
    }

    private void movepinkCell(Cell pinkCell){
        int direction[]=((LangdonCell) pinkCell).getDirection();
        int row=pinkCell.getRow();
        int col=pinkCell.getColumn();
        int counter=((LangdonCell) pinkCell).getCounter();

        if(counter==3) myGrid.setCell(new LangdonCell(row-direction[1], col+direction[0], "BLACK"));
        if (counter==5) direction=new int[]{direction[1], -direction[0]}; //rotates 90 degrees clockwise

        if(counter==4) {
            myGrid.setCell(new LangdonCell(row-direction[1], col+direction[0], "BLACK"));
            myGrid.setCell(new LangdonCell(row+direction[1], col-direction[0], "BLACK"));
            myGrid.setCell(new LangdonCell(row, col, "BLACK"));
        }
        else if(counter>=13) makeNewArm(pinkCell, row, col, direction, counter);
        else if (counter==24) {
            closeArm(pinkCell, row, col, direction);
            return;
        }
        else myGrid.setCell(new LangdonCell(row, col, "RED"));

        myGrid.setCell(pinkCell, row+direction[0], col+direction[1]);
        ((LangdonCell) pinkCell).incrementCounter();
    }

    private int[] makeNewArm(Cell pinkCell, int row, int col, int[] direction, int counter){
        myGrid.setCell(new LangdonCell(row+direction[1], col-direction[0], "BLUE"));
        myGrid.setCell(new LangdonCell(row+(2*direction[1]), col-(2*direction[0]), "RED"));
        if(counter>=15 && counter<20){
            int[] stop={0, 0};
            return stop; //pink cell stops temporarily, waiting at end of new arm to cut away incoming yellows and blacks
        }
        return direction;
    }

    private void closeArm(Cell pinkCell, int row, int col, int[] direction){
        myGrid.setCell(new LangdonCell(row+direction[1], col-direction[0], "RED"));
        myGrid.setCell(new LangdonCell(row, col, "BLACK"));
        mypinkCells.remove(pinkCell);
    }


    private void moveWhiteCell(Cell whiteCell, int row, int col, List<Cell> neighbors){
        ((LangdonCell) whiteCell).incrementCounter();
        int counter=((LangdonCell) whiteCell).getCounter();
        int[] direction=((LangdonCell) whiteCell).getDirection();
        Cell ahead=myGrid.getCell(row+direction[0], col+direction[1]);
        if(counter==11){
            myCellList.add(new LangdonCell(ahead.getRow()+direction[1], ahead.getColumn()-direction[0], "RED"));
            myCellList.add(new LangdonCell(ahead.getRow()-direction[1], ahead.getColumn()+direction[0], "PINK"));
            Cell pinkCell=myCellList.get(myCellList.size()-1);
            ((LangdonCell) pinkCell).setCounter(15); //arm has been completed, now a stopped pink cell is set, which will filter out incoming yellows
        }
        else{
            String aheadState=((LangdonCell) ahead).getState();
            myCellList.add(new LangdonCell(ahead.getRow(), ahead.getColumn(), "WHITE"));
            if (!aheadState.equals("BLUE")) {
                myCellList.add(new LangdonCell(ahead.getRow()+direction[1], ahead.getColumn()-direction[0], "RED"));
                myCellList.add(new LangdonCell(ahead.getRow()-direction[1], ahead.getColumn()+direction[0], "RED"));
            }
        }
        ((LangdonCell) ahead).setCounter(counter);
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
