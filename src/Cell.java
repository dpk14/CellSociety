public class Cell {
    private int myRow;
    private int myColumn;
    // ImageView?

    Cell(int row, int column){
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



}
