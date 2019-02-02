package cells;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;

public class AgentCell extends Cell {
    private String myType;
    public static final String COLOR_AGENT_RED = "SegregationImages/red.gif";
    public static final String COLOR_AGENT_BLUE = "SegregationImages/blue.gif";
    public static final String DATA_TYPE = "AgentCell";
    public static final List<String> DATA_FIELDS = List.of(
            "race");

    public AgentCell(int row, int column, String myType){
        super(row, column, new ImageView(new Image(COLOR_AGENT_RED)));
        this.myType = myType;
        setImage();
    }

    public AgentCell(List<String> dataValues){
        super(Integer.parseInt(dataValues.get(0)), Integer.parseInt(dataValues.get(1)), new ImageView(new Image(COLOR_AGENT_RED)));
        this.myType = dataValues.get(2);
        setImage();
    }

    private void setImage() {
        ImageView p;
        if (myType.equals("RED")) { p = new ImageView(new Image(COLOR_AGENT_RED)); }
        else if (myType.equals("BLUE")) { p = new ImageView(new Image(COLOR_AGENT_BLUE)); }
        else { p = new ImageView(new Image(COLOR_AGENT_RED)); }
        myImage = p;
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
