package simulations;

import cells.Cell;
import cells.SugarAgent;
import cells.SugarPatch;
import grids.Grid;
import javafx.scene.paint.Color;

import java.util.*;


public class SugarScapeSimulation extends Simulation{
    private final int MAX_SUGAR=4;
    public static final String DATA_TYPE = "SugarScapeSimulation";
//    public static final List<String> DATA_FIELDS = List.of(
//            "title", "author", "rows", "columns", "cellShape", "gridShape", "speed", "rate",
//            "interval");

    public static final double AGENT_RATE_DEFAULT = 0.1;
    public static final int GROWTH_RATE_DEFAULT = 4;

    public SugarScapeSimulation(Map<String, String> dataValues, List<Cell> cells) {
        super(dataValues, cells);
        setupSliderInfo();
    }

    public SugarScapeSimulation(Map<String, String> dataValues) {
        super(dataValues);
        setupSliderInfo();
    }

    //sugar growback rate is 1-4, sugar growback Interval is arbitrary
    public Grid advanceSimulation() {
        myTakenSpots.clear();
        myCellList.clear();
        List<Cell> randomizedList=initializeCellList();
        for (Cell currentPatch : randomizedList) {
            if (((SugarPatch) currentPatch).hasAgent()) {
                myTakenSpots.add(currentPatch);
                checkAgent(currentPatch, ((SugarPatch) currentPatch).getAgent(), currentPatch.getRow(), currentPatch.getColumn());
            }
        }
        myGrid.updateGrid(myCellList);
        myCellList=initializeCellList();
        for (Cell currentPatch : myCellList) ((SugarPatch) currentPatch).updateState();
        return myGrid;
    }

    private Comparator<Cell> SugarComparator=new Comparator<Cell>(){
        @Override
        public int compare(Cell a, Cell b){
            return ((SugarPatch) a).getSugar()-((SugarPatch) b).getSugar();
        }
    };

    private void checkAgent(Cell patch, SugarAgent agent, int currentRow, int currentCol) {
        List<Cell> goodNeighbors = getVisibleNeighbors(patch, agent.getMyVision());
        List<Cell> bestNeighbors = new ArrayList<Cell>();
        Collections.sort(goodNeighbors, SugarComparator);
        Cell goodNeighbor=bestNeighbors.get(0);
        int maxSugar=((SugarPatch) goodNeighbor).getSugar();
        for (Cell neighbor : goodNeighbors) {
            if (((SugarPatch) neighbor).getSugar()<maxSugar) break;
            bestNeighbors.add(neighbor);
        }
        Cell otherPatch = move(bestNeighbors, patch);
        Cell newLocation=new Cell(otherPatch.getRow(), otherPatch.getColumn(), Color.WHITE);
        Cell updatedOtherPatch = ((SugarPatch) otherPatch).copyPatch();
        if(!(newLocation.getRow()==currentRow && newLocation.getColumn()==currentCol)){
            Cell updatedCurrentPatch = ((SugarPatch) otherPatch).copyPatch();
            ((SugarPatch) updatedCurrentPatch).moveAgent(updatedOtherPatch);
            myTakenSpots.add(newLocation);
            myCellList.add(updatedOtherPatch);
            myCellList.add(updatedCurrentPatch);
        }
        else myCellList.add(patch);
    }

    public List<Cell> getVisibleNeighbors(Cell cell, int vision){
        Queue<Cell> qu=new LinkedList<Cell>();
        HashMap<Cell, Integer> neighborMap=new HashMap<Cell, Integer>();
        List<Cell> bestNeighbors=new ArrayList<Cell>();
        int distanceOut=0;
        int highestSugar=0;
        int distOfHighestSugar=0;
        int sugar;
        neighborMap.put(cell, distanceOut);
        qu.add(cell);
        while(qu.size()!=0){
            Cell current=qu.remove();
            distanceOut=neighborMap.get(current);
            if(distanceOut>vision) break; //if neighbor lies outside vision, don't consider it, get out of loop
            sugar=((SugarPatch) current).getSugar();
            if((distanceOut>distOfHighestSugar && sugar>highestSugar) || (sugar>=highestSugar && distanceOut==distOfHighestSugar)) {
                highestSugar=sugar;
                distOfHighestSugar=distanceOut;
                bestNeighbors.add(current);
            }
            List<Cell> neighbors=myGrid.getImmediateNeighbors(current);
            for(Cell neighbor: neighbors){
                if(!neighborMap.containsKey(neighbor) && highestSugar<MAX_SUGAR) {
                    //^if the sugariest possible sugar patch has already been found, don't bother to look for ones further out
                    neighborMap.put(neighbor, distanceOut + 1);
                    qu.add(neighbor);
                }
            }
        }
        return bestNeighbors;
    }

    @Override
    protected Grid setupGridByProb() {
        int rows = (int) readInValue("rows", ROW_DEFAULT);
        int cols = (int) readInValue("columns", COL_DEFAULT);
        double agentRate = readInValue("agentRate", AGENT_RATE_DEFAULT);
        List<Cell> cells = new ArrayList<>();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                Cell cell;
                if(evaluateOdds(agentRate)){
                    cell = new SugarPatch(i, j, 0, (int) readInValue("rate", GROWTH_RATE_DEFAULT),
                            (int) readInValue("interval", Math.random()*4+1),  true);
                }
                else {
                    cell = new SugarPatch(i, j, 0, (int) readInValue("rate", GROWTH_RATE_DEFAULT),
                            (int) readInValue("interval", Math.random()*4+1),  false);
                }
                cells.add(cell);
            }
        }
        return createGrid(myDataValues.get("gridShape"), rows, cols, cells);
    }

    @Override
    protected Grid setupGridByQuota() {
        return null;
    }

    @Override
    public String getSimType() {
        return null;
    }
}