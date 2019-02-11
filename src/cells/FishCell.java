package cells;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.Map;

public class FishCell extends Cell {

    public static final Paint COLOR_FISH = Color.ORANGE;
    public static final String DATA_TYPE = "FishCell";
//    public static final List<String> DATA_FIELDS = List.of("reproductionTime");
    private int myTurnsSurvived;
    private int myReproductionTime;

    public FishCell(int row, int column, int reproductionTime) {
        super(row, column, COLOR_FISH);
        this.myTurnsSurvived=0;
        this.myReproductionTime=reproductionTime;
    }

    public FishCell(Map<String, String> dataValues){
        super(Integer.parseInt(dataValues.get("row")), Integer.parseInt(dataValues.get("column")), COLOR_FISH);
        this.myTurnsSurvived=0;
        this.myReproductionTime=Integer.parseInt(dataValues.get("reproductionTime"));
    }

    public boolean canReproduce(){
        if (this.myTurnsSurvived==this.myReproductionTime) return true;
        return false;
    }

    public void setMyTurnsSurvived(int tracker){ this.myTurnsSurvived = tracker; }

    public void updateMyTurnsSurvived(){ this.myTurnsSurvived++; }
}
