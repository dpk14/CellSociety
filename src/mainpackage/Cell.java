package mainpackage;


public class Cell {
    private int myRow;
    private int myColumn;
    // ImageView?

    public Cell(int row, int column){
        myRow = row;
        myColumn = column;
    }


    // Michael: I think we should delete this; i moved it to the simulation class
//    public swapPosition(Cell cell){
//        int tempRow = cell.myRow;
//        int tempCol = cell.myColumn;
//        cell.myRow = this.myRow;
//        cell.myColumn = this.myColumn;
//        this.myRow = tempRow;
//        this.myColumn = tempCol;
//        return cell;
//    }


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
