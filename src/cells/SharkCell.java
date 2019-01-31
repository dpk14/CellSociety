package cells;

import cells.Cell;

public class SharkCell extends Cell {
    private int myTracker;
    private int myMaxTrack;
    private int myEnergy;
    private int myEnergyGain;

    public SharkCell(int row, int column, int maxtrack, int energy, int energyGain) {
        super(row, column);
        this.myTracker=0;
        this.myMaxTrack=maxtrack;
        this.myEnergy=energy;
        this.myEnergyGain=energyGain;
    }

    public boolean canReproduce(){
        this.myTracker++;
        if (myTracker==myMaxTrack) return true;
        return false;
    }

    public void updateEnergy(){
        this.myEnergy+=myEnergyGain;
    }

    public void decrementEnergy(){
        this.myEnergy--;
    }

    public void setMyTracker(int tracker){ myTracker = tracker; }

    public int getMyTracker(){ return myTracker; }

    public int getMyEnergy(){return myEnergy;}
}
