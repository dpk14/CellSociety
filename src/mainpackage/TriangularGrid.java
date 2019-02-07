package mainpackage;

import cells.Cell;

import java.util.ArrayList;
import java.util.List;

public class TriangularGrid extends Grid{

    public TriangularGrid(int rows, int columns, List<Cell> list){
        super(rows, columns, list);
    }

    @Override
    public List<Cell> getImmediateNeighbors(Cell cell){
        List <Cell> neighbors = new ArrayList<>();
        int row = cell.getRow();
        int column = cell.getColumn();
        if(column != 0) neighbors.add(this.getCell(row, column-1));
        if(column != this.getWidth()-1) neighbors.add(this.getCell(row, column+1));
        if (row!=0 && (row+column)%2==0) neighbors.add(this.getCell(row-1,column));
        if (row!=this.getHeight()-1 && (row+column)%2!=0) neighbors.add(this.getCell(row+1,column));
        return neighbors;
    }

    @Override
    public List<Cell> getAllNeighbors(Cell cell){
        List <Cell> neighbors = new ArrayList<>();
        int row = cell.getRow();
        int column = cell.getColumn();
        int placeholder=100;
        int[] rowlist={-1, 1, -1, 1, 0, 0, 1, -1, 0, 0, placeholder, placeholder};
        int[] collist={-1, 1, 0, 0, -1, 1, -1, 1, 2, -2, 2, -2};

        if((row+column)%2==0) {   //depending on triangle orientation (up or down) 2 of the 12 possible neighbors are different
            rowlist[rowlist.length - 1] = -1;
            rowlist[rowlist.length - 2] = -1;
        }
        else{
            rowlist[rowlist.length-1]=1;
            rowlist[rowlist.length-2]=1;
        }
        for(int k=0; k<rowlist.length; k++)
            if(row+rowlist[k]>=0 && column+collist[k]>=0 && row+rowlist[k]<=this.getHeight()-1 && column+collist[k]<=this.getWidth()-1){
                neighbors.add(this.getCell(row+rowlist[k], column+collist[k]));
            }
        return neighbors;
    }
}





























































































































































































































































































































































