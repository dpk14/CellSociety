package cells;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;

public class FishCell extends Cell {

    public static final String FISH_IMAGE = "WatorWorldImages/fish.gif";
    public static final Paint COLOR_FISH = Color.ORANGE;
    public static final String DATA_TYPE = "FishCell";
    public static final List<String> DATA_FIELDS = List.of("maxTrack");

    private int myTracker;
    private int myMaxTrack;


    public FishCell(int row, int column, int maxtrack) {
        super(row, column, COLOR_FISH);
        this.myTracker=0;
        this.myMaxTrack=maxtrack;
    }

    public FishCell(List<String> dataValues) {
        super(Integer.parseInt(dataValues.get(0)), Integer.parseInt(dataValues.get(1)), COLOR_FISH);
        this.myTracker=0;
        this.myMaxTrack=Integer.parseInt(dataValues.get(2));
    }

    public boolean canReproduce(){
        if (this.myTracker==this.myMaxTrack) return true;
        return false;
    }

    public void setMyTracker(int tracker){ this.myTracker = tracker; }

    public void updateTracker(){ this.myTracker++; }

    public int getMyTracker(){ return this.myTracker; }

}
