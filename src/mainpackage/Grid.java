package mainpackage;

import cells.Cell;

import java.util.ArrayList;
import java.util.List;

public abstract class Grid {

    protected List<Cell> getImmediateNeighbors(Cell cell, Cell[][] myGrid){
      return new ArrayList<Cell>();
    }

    protected List<Cell> getAllNeighbors(Cell cell, Cell[][] myGrid){
        return new ArrayList<Cell>();
    }

}
