package mainpackage;

public class SharkCell extends Cell{
    int myTracker;
    int myMaxTrack;
    int myEnergy;

    public SharkCell(int row, int column, int maxtrack) {
        super(row, column);
        this.myTracker=0;
        this.myMaxTrack=maxtrack;
    }

    public boolean reproduce(){
        myTracker++;
        if (myTracker==myMaxTrack) return true;
        return false;
    }
}
