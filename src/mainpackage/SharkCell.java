package mainpackage;

public class SharkCell extends Cell{
    int myTracker;
    int myMaxTrack;
    int myEnergy;

    public SharkCell(int row, int column, int maxtrack, int maxenergy) {
        super(row, column);
        this.myTracker=0;
        this.myMaxTrack=maxtrack;
        this.myEnergy=maxenergy;
    }

    public boolean reproduce(){
        this.myTracker++;
        if (myTracker==myMaxTrack) return true;
        return false;
    }

    public void updateEnergy(){
        this.myEnergy++;
    }

    public void decrementEnergy(){
        this.myEnergy--;
    }


}
