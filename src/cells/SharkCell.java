package cells;

import cells.Cell;

import java.util.List;

public class SharkCell extends Cell {
    public static final String DATA_TYPE = "SharkCell";
    public static final List<String> DATA_FIELDS = List.of("row", "column", "max track", "energy", "energy gain");
    private int myTurnsSurvived;
    private int myReproductionTime;
    private int myEnergy;
    private int myEnergyGain;

    public SharkCell(int row, int column, int reproductionTime, int energy, int energyGain) {
        super(row, column);
        this.myTurnsSurvived=0;
        this.myReproductionTime=reproductionTime;
        this.myEnergy=energy;
        this.myEnergyGain=energyGain;
    }

    public SharkCell(List<String> dataValues){
        super(Integer.parseInt(dataValues.get(0)), Integer.parseInt(dataValues.get(1)));
        this.myTurnsSurvived= 0;
        this.myReproductionTime= Integer.parseInt(dataValues.get(2));
        this.myEnergy=Integer.parseInt(dataValues.get(3));
        this.myEnergyGain=Integer.parseInt(dataValues.get(4));
    }

    public boolean canReproduce(){
        if (this.myTurnsSurvived==this.myReproductionTime) return true;
        return false;
    }

    public void updateEnergy(){
        this.myEnergy+=this.myEnergyGain;
    }

    public void decrementEnergy(){
        this.myEnergy--;
    }

    public void setMyTurnsSurvived(int tracker){ this.myTurnsSurvived = tracker; }

    public void updateMyTurnsSurvived(){ this.myTurnsSurvived++; }

    public int getMyEnergy(){return this.myEnergy;}
}
