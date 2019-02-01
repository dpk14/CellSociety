package cells;


public class Cell {
    private int myRow;
    private int myColumn;
    // ImageView?

    public Cell(int row, int column){
        myRow = row;
        myColumn = column;
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


}
