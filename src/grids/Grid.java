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

    public List<Cell> getVisibleNeighbors(Cell cell, int vision){
        Queue<Cell> qu=new LinkedList<Cell>();
        HashMap<Cell, Integer> neighborMap=new HashMap<Cell, Integer>();
        List<Cell> bestNeighbors=new ArrayList<Cell>();
        int distanceOut=0;
        int highestSugar=0;
        int distOfHighestSugar=0;
        int sugar;
        neighborMap.put(cell, distanceOut);
        qu.add(cell);
        while(qu.size()!=0){
            Cell current=qu.remove();
            distanceOut=neighborMap.get(current);
            if(distanceOut>vision) break; //if neighbor lies outside vision, don't consider it, get out of loop
            sugar=((SugarPatch) current).getSugar();
            if((distanceOut>distOfHighestSugar && sugar>highestSugar) || (sugar>=highestSugar && distanceOut==distOfHighestSugar)) {
                highestSugar=sugar;
                distOfHighestSugar=distanceOut;
                bestNeighbors.add(current);
            }
            List<Cell> neighbors=getImmediateNeighbors(current);
            for(Cell neighbor: neighbors){
                if(!neighborMap.containsKey(neighbor) && highestSugar<MAX_SUGAR) {
                    //^if the sugariest possible sugar patch has already been found, don't bother to look for ones further out
                    neighborMap.put(neighbor, distanceOut + 1);
                    qu.add(neighbor);
                }
            }
        }
        return bestNeighbors;
    }

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
