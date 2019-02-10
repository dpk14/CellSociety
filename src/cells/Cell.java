package cells;

import javafx.scene.paint.Paint;

import java.util.List;
import java.util.Map;


public class Cell {
    protected int myRow;
    protected int myColumn;
    protected Paint myColor;

    public Cell(int row, int column, Paint c){
        myRow = row;
        myColumn = column;
        myColor = c;
    }

    public static Cell createNewCell(String cellType, Map<String, String> dataValues){
        switch (cellType) {
            case AgentCell.DATA_TYPE :
                return new AgentCell(dataValues);
            case EmptyCell.DATA_TYPE :
                return new EmptyCell(dataValues);
            case FishCell.DATA_TYPE :
                return new FishCell(dataValues);
            case SharkCell.DATA_TYPE :
                return new SharkCell(dataValues);
            case StateChangeCell.DATA_TYPE :
                return new StateChangeCell(dataValues);
            case LangdonCell.DATA_TYPE :
                ///return new LangdonCell(dataValues);
                return new SugarPatch(dataValues);
            case SugarPatch.DATA_TYPE :
                return new SugarPatch(dataValues);
        }
        throw new RuntimeException("No such kind of Cell.");
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
