package cells;

public class SugarAgent {
    private int mySugar;
    private int myVision;
    private int myMetabolism;

    SugarAgent(Cell cell, int sugar, int vision, int metabolism ){
        mySugar=sugar;
        myVision=vision;
        myMetabolism=metabolism;
    }

    SugarAgent(SugarAgent another) {
        this.mySugar = another.mySugar;
        this.myVision = another.myVision;
        this.myMetabolism = another.myMetabolism;
    }

    public boolean isDead(){
        if (mySugar<=0) return true;
        else return false;
    }

    public void metabolize(){
        mySugar-=myMetabolism;
    }

    public int getMyVision(){
        return myVision;
    }

    public void addSugar(int sugar){
        mySugar+=sugar;
    }
}
