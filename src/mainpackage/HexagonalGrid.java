package mainpackage;

import cells.Cell;

import java.util.ArrayList;
import java.util.List;

public class HexagonalGrid extends Grid{

    HexagonalGrid(int rows, int columns, List<Cell> list){
        super(rows, columns, list);
    }

    @Override
    public List<Cell> getImmediateNeighbors(Cell cell){
        List <Cell> neighbors = new ArrayList<>();
        int row = cell.getRow();
        int column = cell.getColumn();
        if(row!=0) {
            neighbors.add(this.getCell(row - 1,column));
            if (row % 2 != 0 && column != this.getWidth()-1) neighbors.add(this.getCell(row - 1,column + 1));  // check top neighbor
            if (row % 2 == 0 && column != 0) neighbors.add(this.getCell(row - 1,column - 1));
        }
        if(row != this.getHeight()-1) {
            neighbors.add(this.getCell(row + 1,column));
            if (row % 2 != 0 && column != this.getWidth()-1) neighbors.add(this.getCell(row + 1,column + 1));  // check top neighbor
            if (row % 2 == 0 && column != 0) neighbors.add(this.getCell(row + 1, column - 1));
        }
        if(column != 0) neighbors.add(this.getCell(row,column-1));
        if(column != this.getWidth()-1) neighbors.add(this.getCell(row, column+1));
        return neighbors;
    }

}