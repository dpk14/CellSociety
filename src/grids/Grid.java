package grids;

import cells.Cell;
import cells.EmptyCell;
import cells.StateChangeCell;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Grid {
    private Cell[][] myCellArray;
    private int myRows;
    private int myColumns;

    public Grid(int rows, int columns, List<Cell> list){
        myCellArray = getNewArray(rows, columns, list);
        myRows=rows;
        myColumns=columns;
    }

    public abstract List<Cell> getImmediateNeighbors(Cell cell);

    public abstract List<Cell> getAllNeighbors(Cell cell);

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

    public Map<Paint, Integer> getMapOfCellCount() {
        Map<Paint, Integer> m = new HashMap<>();
        for (int i = 0; i < myCellArray.length; i++) {
            for (int j = 0; j < myCellArray[i].length; j++) {
                Paint p = myCellArray[i][j].getMyColor();
                if (m == null || ! m.containsKey(p)) {
                    m.put(p, 1);
                }
                else {
                    m.put(p, m.get(p) + 1);
                }
            }
        }
        return m;
    }


}
