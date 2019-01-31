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

    public boolean canReproduce(){
        this.myTracker++;
        if (myTracker==myMaxTrack) return true;
        return false;
    }

    public void setMyTracker(int tracker){ myTracker = tracker; }

    public int getMyTracker(){ return myTracker; }

}
