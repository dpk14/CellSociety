package cells;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SugarPatch extends Cell implements Comparator<Cell> {

  //  public static final String DATA_TYPE = "FishCell";
    //public static final List<String> DATA_FIELDS = List.of("reproductionTime");
    private int sugarGrowBackRate;
    private int getSugarGrowBackInterval;
    private boolean hasAgent;
    private SugarAgent myAgent;
    private int myTracker;
    private int mySugar;
    private final int MAX_SUGAR=4;
    private int myRow;
    private int myColumn;

    public SugarPatch(int row, int column, int sugar, int rate, int interval, boolean agent) {
        super(row, column, Color.WHITE);
        myRow=row;
        myColumn=column;
        sugarGrowBackRate=rate;
        getSugarGrowBackInterval=interval;
        mySugar=sugar;
        hasAgent=agent;
        myTracker=0;
        Random rand=new Random();
        if(hasAgent) myAgent=new SugarAgent(this, rand.nextInt(20)+5, rand.nextInt(5)+1, rand.nextInt(3)+1);
        setColor();
    }

    private void setColor() {
        Paint p;
        if (mySugar==1) { p = Color.WHITESMOKE; }
        else if (mySugar==2) { p=Color.LIGHTCORAL; }
        else if (mySugar==3) { p = Color.ORANGE; }
        else if (mySugar==4) { p = Color.RED; }
        else { p = Color.BLACK; }
        myColor = p;
    }

    private void setState(int sugar){
        mySugar=sugar;
        setColor();
    }

    public SugarPatch copyPatch(){
        SugarPatch copy=new SugarPatch(myRow, myColumn, mySugar, sugarGrowBackRate, getSugarGrowBackInterval, false);
        copy.setAgent(myAgent);
        copy.setMyTracker(myTracker);
        return copy;
    }

    public SugarPatch updateState(){
        int prevSugar=mySugar;
        myTracker++;
        if (myTracker==getSugarGrowBackInterval) {
            myTracker=0;
            mySugar+=sugarGrowBackRate;
            if (mySugar>MAX_SUGAR) mySugar=4;
        }
        if(hasAgent) {
            myAgent.metabolize();
            if (myAgent.isDead()) {
                hasAgent = false;
                myAgent = null;
            }
        }
        return copyPatch();
    }

    public void moveAgent(Cell newPatch){
        SugarAgent agent=myAgent;
        hasAgent=false;
        myAgent=null;
        agent.addSugar(((SugarPatch) newPatch).getSugar());
        ((SugarPatch) newPatch).setAgent(agent);
        ((SugarPatch) newPatch).eatSugar();
    }

    public SugarAgent getAgent(){
        return myAgent;
    }

    public boolean hasAgent(){
        return hasAgent;
    }

    public void setAgent(SugarAgent agent){
        hasAgent=true;
        myAgent=agent;
    }

    public int getSugar(){
        return mySugar;
    }

    private void eatSugar(){
        mySugar=0;
    }

    public void setMyTracker(int tracker){
        myTracker=tracker;
    }

    @Override
    public int compare(Cell a, Cell b){
        return ((SugarPatch) a).getSugar()-((SugarPatch) b).getSugar();
    }

}
