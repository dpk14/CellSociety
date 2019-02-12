package cells;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;

public class LangdonCell extends Cell{
    private String myState;
    private int myCounter;
    private int[] myDirection;

    public static final String DATA_TYPE = "LangdonCell";
    public static final List<String> DATA_FIELDS = List.of("state");

    public LangdonCell(int row, int column, String state) {
        super(row, column, Color.WHITE);
        myState=state;
        myCounter=0;
        setColor();
    }

    private void setColor() {
        Paint p;
        if (myState=="BLUE") { p=Color.BLUE; }
        else if (myState=="RED") { p = Color.RED; }
        else if (myState=="GREEN") { p = Color.GREEN; }
        else if (myState=="YELLOW") { p=Color.YELLOW; }
        else if (myState=="PINK") { p = Color.PINK; }
        else if (myState=="WHITE") { p = Color.WHITESMOKE; }
        else if (myState=="CYAN") { p = Color.CYAN; }
        else p=Color.BLACK;
        myColor = p;
    }

    private void setState(String state){
        myState=state;
        setColor();
    }
    public String getState(){
        return myState;
    }

    public void incrementCounter(){
        myCounter++;
    }
    public int getCounter(){
        return myCounter;
    }
    public void setCounter(int counter){
        myCounter=counter;
    }

    public void setDirection(int[] direction){
        myDirection=direction;
    }
    public int[] getDirection(){
        return myDirection;
    }
}
