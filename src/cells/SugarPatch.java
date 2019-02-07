package cells;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SugarPatch extends Cell implements Comparator<Cell> {

    public static final String DATA_TYPE = "FishCell";
    public static final List<String> DATA_FIELDS = List.of("reproductionTime");
    private int sugarGrowBackRate;
    private int getSugarGrowBackInterval;
    private boolean hasAgent;
    private SugarAgent myAgent;
    private int myTracker;
    private int mySugar;
    private final int MAX_SUGAR=4;

    public SugarPatch(int row, int column, int sugar, int rate, int interval, boolean agent) {
        super(row, column, Color.WHITE);
        sugarGrowBackRate=rate;
        getSugarGrowBackInterval=interval;
        mySugar=sugar;
        hasAgent=agent;
        myTracker=0;
        Random rand=new Random();
        if(hasAgent) myAgent=new SugarAgent(this, rand.nextInt(20)+5, rand.nextInt(5)+1, rand.nextInt(3)+1);
    }

    public int updateState(){
        myTracker++;
        if (myTracker==getSugarGrowBackInterval) {
            myTracker=0;
            mySugar+=sugarGrowBackRate;
            if (mySugar>MAX_SUGAR) mySugar=4;
        }
        myAgent.metabolize();
        if (myAgent.isDead()) {
            hasAgent=false;
            myAgent=null;
        }
        return mySugar;
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

    @Override
    public int compare(Cell a, Cell b){
        return ((SugarPatch) a).getSugar()-((SugarPatch) b).getSugar();
    }

}
