package cells;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.List;

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

    public FishCell(List<String> dataValues) {
        super(Integer.parseInt(dataValues.get(0)), Integer.parseInt(dataValues.get(1)), COLOR_FISH);
        this.myTurnsSurvived=0;
        this.myReproductionTime=Integer.parseInt(dataValues.get(2));
    }

    public boolean canReproduce(){
        if (this.myTurnsSurvived==this.myReproductionTime) return true;
        return false;
    }

    public void setMyTurnsSurvived(int tracker){ this.myTurnsSurvived = tracker; }

    public void updateMyTurnsSurvived(){ this.myTurnsSurvived++; }
}
