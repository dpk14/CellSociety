package cells;

import cells.Cell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;

public class SharkCell extends Cell {
    public static final String SHARK_IMAGE = "WatorWorldImages/shark.gif";
    public static final String DATA_TYPE = "SharkCell";
    public static final List<String> DATA_FIELDS = List.of("maxTrack", "energy", "energyGain");
    public static final Paint COLOR_SHARK = Color.GREY;
    private int myTracker;
    private int myMaxTrack;
    private int myEnergy;
    private int myEnergyGain;

    public SharkCell(int row, int column, int maxtrack, int energy, int energyGain) {
        super(row, column, new ImageView(new Image(SHARK_IMAGE)));
        this.myTracker=0;
        this.myMaxTrack=maxtrack;
        this.myEnergy=energy;
        this.myEnergyGain=energyGain;
    }

    public SharkCell(List<String> dataValues){
        super(Integer.parseInt(dataValues.get(0)), Integer.parseInt(dataValues.get(1)), new ImageView(new Image(SHARK_IMAGE)));
        this.myTracker= 0;
        this.myMaxTrack= Integer.parseInt(dataValues.get(2));
        this.myEnergy=Integer.parseInt(dataValues.get(3));
        this.myEnergyGain=Integer.parseInt(dataValues.get(4));
    }

    public boolean canReproduce(){
        if (this.myTracker==this.myMaxTrack) return true;
        return false;
    }

    public void updateEnergy(){
        this.myEnergy+=this.myEnergyGain;
    }

    public void decrementEnergy(){
        this.myEnergy--;
    }

    public void setMyTracker(int tracker){ this.myTracker = tracker; }

    public void updateTracker(){ this.myTracker++; }

    public int getMyTracker(){ return this.myTracker; }

    public int getMyEnergy(){return this.myEnergy;}
}
