package grids;

import cells.Cell;
import cells.EmptyCell;
import cells.StateChangeCell;
import cells.SugarPatch;

import java.util.*;

public class Grid {
    private Cell[][] myCellArray;
    private int myRows;
    private int myColumns;

    public Grid(int rows, int columns, List<Cell> list){
        myCellArray = getNewArray(rows, columns, list);
        myRows=rows;
        myColumns=columns;
    }

    public Grid(int rows, int columns){
        myCellArray = new Cell[rows][columns];
        myRows=rows;
        myColumns=columns;
    }

    public List<Cell> getImmediateNeighbors(Cell cell){
      return new ArrayList<Cell>();
    }

    public List<Cell> getAllNeighbors(Cell cell) {
        return new ArrayList<Cell>();
    };

    public Cell[][] getNewArray(int rows, int columns, List<Cell> list){
        Cell[][] newArray = new Cell[rows][columns];
        for(Cell cell : list){
            newArray[cell.getRow()][cell.getColumn()] = cell;
        }
        return newArray;
    }

    public void updateGrid(List<Cell> list){
        for(Cell cell : list){
            myCellArray[cell.getRow()][cell.getColumn()] = cell;
        }
    }

    public List<Cell> fillWithEmpty(List<Cell> myCellList){
        for(int i = 0; i < myRows; i++) { // i = row number
            for (int j = 0; j < myColumns; j++) { // j = column number
                if (myCellArray[i][j]==null) {
                    Cell empty=new EmptyCell(i, j);
                    myCellArray[i][j]=empty;
                    myCellList.add(empty);
                }
            }
        }
        return myCellList;
    }

    public Cell getCell(int row, int column){
        return myCellArray[row][column];
    }

    public int getHeight(){
        return myRows;
    }

    public int getWidth(){
        return myColumns;
    }

    public Cell[][] getMyCellArray(){
        return myCellArray;
    }

}
