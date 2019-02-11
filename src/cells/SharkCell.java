package cells;

import cells.Cell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;
import java.util.Map;

public class SharkCell extends Cell {
    public static final String DATA_TYPE = "SharkCell";
//    public static final List<String> DATA_FIELDS = List.of("reproductionTime", "energy", "energyGain");
    public static final Paint COLOR_SHARK = Color.GREY;
        private int myTurnsSurvived;
        private int myReproductionTime;
        private int myEnergy;
        private int myEnergyGain;

        public SharkCell(int row, int column, int reproductionTime, int energy, int energyGain) {
        super(row, column, COLOR_SHARK);
        this.myTurnsSurvived=0;
        this.myReproductionTime=reproductionTime;
        this.myEnergy=energy;
        this.myEnergyGain=energyGain;
    }

    public SharkCell(Map<String, String> dataValues){
        super(Integer.parseInt(dataValues.get("row")), Integer.parseInt(dataValues.get("column")), COLOR_SHARK);
        this.myTurnsSurvived=0;
        this.myReproductionTime=Integer.parseInt(dataValues.get("reproductionTime"));
        this.myEnergy=Integer.parseInt(dataValues.get("energy"));
        this.myEnergyGain=Integer.parseInt(dataValues.get("energyGain"));
    }

    public boolean canReproduce(){
        if (this.myTurnsSurvived==this.myReproductionTime) return true;
        return false;
    }

    public void updateEnergy(){
        this.myEnergy+=this.myEnergyGain;
    }

    public void decrementEnergy(){
        myEnergy--;
    }

    public void setMyTurnsSurvived(int tracker){ this.myTurnsSurvived = tracker; }

    public void updateMyTurnsSurvived(){ this.myTurnsSurvived++; }

    public int getMyEnergy(){return this.myEnergy;}
}
