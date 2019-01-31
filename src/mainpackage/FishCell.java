package mainpackage;

public class FishCell extends Cell{
    int myTracker;
    int myMaxTrack;

    public FishCell(int row, int column, int maxtrack) {
        super(row, column);
        this.myTracker=0;
    }

    public boolean reproduce(maxtrack){
        myTracker++;
        if (myTracker==myMaxTrack) return true;
        return false;
    }
}
