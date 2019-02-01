package cells;

import java.util.List;

public class FishCell extends Cell {
    public static final String DATA_TYPE = "FishCell";
    public static final List<String> DATA_FIELDS = List.of("row", "column", "max track");
    private int myTracker;
    private int myMaxTrack;

    public FishCell(int row, int column, int maxtrack) {
        super(row, column);
        this.myTracker=0;
        this.myMaxTrack=maxtrack;
    }

    public FishCell(List<String> dataValues) {
        super(Integer.parseInt(dataValues.get(0)), Integer.parseInt(dataValues.get(1)));
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
