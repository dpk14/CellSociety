package cells;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;



public class Cell {
    private int myRow;
    private int myColumn;
    protected ImageView myImage;

    public Cell(int row, int column, ImageView m){
        myRow = row;
        myColumn = column;
        myImage = m;
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

    public void setMyRow(int r) {
        myRow = r;
    }

    public void setMyColumn(int c) {
        myColumn = c;
    }

    public ImageView getMyImage() {
        return myImage;
    }

}
