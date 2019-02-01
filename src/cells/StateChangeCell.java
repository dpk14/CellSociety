package cells;

public class StateChangeCell extends Cell{
    String myState;

    public StateChangeCell(int row, int column, String state) {
        super(row, column);
        myState=state;
    }

    public void setState(String state){
        myState=state;
    }

    public String getState(){
        return myState;
    }

}
