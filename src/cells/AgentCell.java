package cells;


import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;
import java.util.Map;

public class AgentCell extends Cell {
    private String myType;
    public static final Paint COLOR_AGENT_RED = Color.RED;
    public static final Paint COLOR_AGENT_BLUE = Color.BLUE;
    public static final String DATA_TYPE = "AgentCell";
//    public static final List<String> DATA_FIELDS = List.of(
//            "race");

    public AgentCell(int row, int column, String myType){
        super(row, column, COLOR_AGENT_RED);
        this.myType = myType;
        setImage();
    }

    public AgentCell(Map<String, String> dataValues){
        super(Integer.parseInt(dataValues.get("row")), Integer.parseInt(dataValues.get("column")), COLOR_AGENT_RED);
        this.myType = dataValues.get("race");
        setImage();
    }

    private void setImage() {
        if (myType.equals("RED")) { myColor=COLOR_AGENT_RED;}
        else if (myType.equals("BLUE")) { myColor=COLOR_AGENT_BLUE;}
    }

    public String getType(){
        return myType;
    }

    public double calculatePercentage(List<Cell> neighbors){
        int sameType = 0;
        int differentType = 0;
        for(Cell cell : neighbors){
            if (cell instanceof AgentCell && ((AgentCell) cell).getType().equals(this.getType())) {
                sameType++;
            } else if (cell instanceof AgentCell && !((AgentCell) cell).getType().equals(this.getType())) {
                differentType++;
            }

        }
        //System.out.print("| " + (double) sameType/(sameType+differentType));
        return (double) sameType/(sameType+differentType);
    }


}
