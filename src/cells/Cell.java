package cells;

import javafx.scene.paint.Paint;



public class Cell {
    private int myRow;
    private int myColumn;
    protected Paint myColor;

    public Cell(int row, int column, Paint c){
        myRow = row;
        myColumn = column;
        myColor = c;
    }

    public void swapPosition(Cell cell){
        int tempRow = cell.myRow;
        int tempCol = cell.myColumn;
        cell.myRow = this.myRow;
        cell.myColumn = this.myColumn;
        this.myRow = tempRow;
        this.myColumn = tempCol;
    }


    public int getRow(){
        return myRow;
    }

    public int getColumn(){
        return myColumn;
    }

    public Paint getMyColor() {
        return myColor;
    }

}
