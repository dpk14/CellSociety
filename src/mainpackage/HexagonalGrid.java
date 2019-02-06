package mainpackage;

import cells.Cell;

import java.util.ArrayList;
import java.util.List;

public class HexagonalGrid extends Grid{

    @Override
    protected List<Cell> getImmediateNeighbors(Cell cell, Cell[][] myGrid){
        List <Cell> neighbors = new ArrayList<>();
        int row = cell.getRow();
        int column = cell.getColumn();
        if(row!=0) {
            neighbors.add(myGrid[row - 1][column]);
            if (row % 2 != 0 && column != myGrid[0].length - 1) neighbors.add(myGrid[row - 1][column + 1]);  // check top neighbor
            if (row % 2 == 0 && column != 0) neighbors.add(myGrid[row - 1][column - 1]);
        }
        if(row != myGrid.length-1) {
            neighbors.add(myGrid[row + 1][column]);
            if (row % 2 != 0 && column != myGrid[0].length - 1) neighbors.add(myGrid[row + 1][column + 1]);  // check top neighbor
            if (row % 2 == 0 && column != 0) neighbors.add(myGrid[row + 1][column - 1]);
        }
        if(column != 0) neighbors.add(myGrid[row][column-1]);
        if(column != myGrid[0].length-1) neighbors.add(myGrid[row][column+1]);
        return neighbors;
    }

}