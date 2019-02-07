package mainpackage;

import cells.Cell;
import cells.StateChangeCell;

import java.util.ArrayList;
import java.util.List;

public abstract class Grid {
    protected Cell[][] myCellArray;

    public Grid(int rows, int columns, List<Cell> list){
        myCellArray = getNewGrid(rows, columns, list);
    }

    public List<Cell> getImmediateNeighbors(Cell cell){
      return new ArrayList<Cell>();
    }

    public abstract List<Cell> getAllNeighbors(Cell cell /*, Cell[][] myGrid*/);

    public Cell[][] getNewGrid(int rows, int columns, List<Cell> list){
        Cell[][] newGrid = new Cell[rows][columns];
        for(Cell cell : list){
            newGrid[cell.getRow()][cell.getColumn()] = cell;
        }
        return newGrid;
    }

    public void updateGrid(List<Cell> list){
        for(Cell cell : list){
            myCellArray[cell.getRow()][cell.getColumn()] = cell;
        }
    }

//    public List<Cell> getTypedNeighbors(Cell cell, String type) {
//        List<Cell> neighbors = getNeighbors(cell);
//        List<Cell> specificNeighbors=new ArrayList<Cell>();
//        for(Cell neighbor: neighbors){
//            if (((StateChangeCell) neighbor).getState().equals(type)) specificNeighbors.add(neighbor);
//        }
//        return specificNeighbors;
//    }

    public Cell getCell(int row, int column){
        return myCellArray[row][column];
    }

    public int getHeight(){
        return myCellArray.length;
    }

    public int getWidth(){
        return myCellArray[0].length;
    }



}
