package mainpackage;


public class Cell {
    private int myRow;
    private int myColumn;
    // ImageView?

    public Cell(int row, int column){
        myRow = row;
        myColumn = column;
    }

    public Cell swapPosition(Cell cell){
        int tempRow = cell.myRow;
        int tempCol = cell.myColumn;
        cell.myRow = this.myRow;
        cell.myColumn = this.myColumn;
        this.myRow = tempRow;
        this.myColumn = tempCol;
        return cell;
    }


    public int getRow(){
        return myRow;
    }

    public int getColumn(){
        return myColumn;
    }




}
