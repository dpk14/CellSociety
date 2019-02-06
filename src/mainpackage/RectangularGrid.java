package mainpackage;

import cells.Cell;

import java.util.ArrayList;
import java.util.List;

public class RectangularGrid extends Grid{

    RectangularGrid(int rows, int columns, List<Cell> list){
        super(rows, columns, list);
    }

    @Override
    protected List<Cell> getImmediateNeighbors(Cell cell, Cell[][] myGrid){
        List <Cell> neighbors = new ArrayList<>();
        int row = cell.getRow();
        int column = cell.getColumn();
        if(row != 0){ // check top neighbor
            neighbors.add(myGrid[row-1][column]);
        }
        if(row != myGrid.length-1){ // check bottom neighbor
            neighbors.add(myGrid[row+1][column]);
        }
        if(column != 0){ // check left neighbor
            neighbors.add(myGrid[row][column-1]);
        }
        if(column != myGrid[0].length-1){ // check right neighbor
            neighbors.add(myGrid[row][column+1]);
        }
        return neighbors;
    }

    @Override
    protected List<Cell> getAllNeighbors(Cell cell, Cell[][] myGrid){
        List<Cell> neighbors = getImmediateNeighbors(cell, myGrid);
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
}
