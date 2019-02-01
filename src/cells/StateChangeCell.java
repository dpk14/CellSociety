package cells;

import java.util.List;

public class StateChangeCell extends Cell{
    String myState;

    public static final String DATA_TYPE = "StateChangeCell";
    public static final List<String> DATA_FIELDS = List.of("state");

    public StateChangeCell(int row, int column, String state) {
        super(row, column);
        myState=state;
    }

    public StateChangeCell(List<String> dataValues){
        super(Integer.parseInt(dataValues.get(0)), Integer.parseInt(dataValues.get(1)));
        myState = dataValues.get(3);
    }

    public void setState(String state){
        myState=state;
    }

    public String getState(){
        return myState;
    }


}
