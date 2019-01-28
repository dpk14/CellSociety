public class SegregationSimulation extends Simulation {

    private double mySatisfactionThreshold; // between 0 & 1
    private double myRacePercentage; // between 0 & 1, percentage made up by first Agent
    private double myEmptyPercentage; // between 0 & 1

    public SegregationSimulation(int numRows, int numCols, double mySatisfactionThreshold, double myRacePercentage, double myEmptyPercentage){
        super(numRows,numCols);
        this.mySatisfactionThreshold = mySatisfactionThreshold;
        this.myRacePercentage = myRacePercentage;
        this.myEmptyPercentage = myEmptyPercentage;
    }

    
}
