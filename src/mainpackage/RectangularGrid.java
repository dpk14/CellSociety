package mainpackage;

import cells.Cell;

import java.util.ArrayList;
import java.util.List;

public class RectangularGrid extends Grid{

    public RectangularGrid(int rows, int columns, List<Cell> list){
        super(rows, columns, list);
    }

    @Override
    public List<Cell> getImmediateNeighbors(Cell cell){
        List <Cell> neighbors = new ArrayList<>();
        int row = cell.getRow();
        int column = cell.getColumn();
        if(row != 0){ // check top neighbor
            neighbors.add(this.getCell(row-1,column));
        }
        if(row != this.getHeight()-1){ // check bottom neighbor
            neighbors.add(this.getCell(row+1, column));
        }
        if(column != 0){ // check left neighbor
            neighbors.add(this.getCell(row,column-1));
        }
        if(column != this.getWidth()-1){ // check right neighbor
            neighbors.add(this.getCell(row, column+1));
        }
        return neighbors;
    }

    @Override
    public List<Cell> getAllNeighbors(Cell cell){
        List<Cell> neighbors = getImmediateNeighbors(cell);
        int row = cell.getRow();
        int column = cell.getColumn();
        if(row != 0 && column != 0){ //check top-left neighbor
            neighbors.add(this.getCell(row-1,column-1));
        }
        if(row != 0 && column != this.getWidth()-1){ //check top-right neighbor
            neighbors.add(this.getCell(row-1,column+1));
        }
        if(row != this.getHeight()-1 && column != 0){ //check bottom-left neighbor
            neighbors.add(this.getCell(row+1,column-1));
        }
        if(row != this.getHeight()-1 && column != this.getWidth()-1){ //check bottom-right neighbor
            neighbors.add(this.getCell(row+1,column+1));
        }
        return neighbors;
    }
}
