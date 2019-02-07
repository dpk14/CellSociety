package cells;

public class SugarAgent {
    private int mySugar;
    private int myVision;
    private int myMetabolism;

    SugarAgent(Cell cell, int sugar, int vision, int metabolism ){
        sugar=mySugar;
        vision=myVision;
        metabolism=myMetabolism;
    }

    public boolean isDead(){
        if (mySugar<=0) return true;
        else return false;
    }

    public void metabolize(){
        mySugar-=myMetabolism;
    }

}
