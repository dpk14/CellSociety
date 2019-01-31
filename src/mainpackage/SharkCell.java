package mainpackage;

public class SharkCell extends Cell{
    int myTracker;
    int myMaxTrack;
    int myEnergy;
    int myEnergyGain;

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
}
