package simulations;

import cells.*;
import grids.Grid;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.*;
import java.util.List;


public class SugarScapeSimulation extends Simulation{
    private final int MAX_SUGAR=4;
    private Grid nextGrid;
    public static final String DATA_TYPE = "SugarScapeSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "cellShape", "gridShape", "speed", "startEnergy",
            "sharkReproductionMax", "fishReproductionMax", "energyGain", "fishRate", "sharkRate");

    public Grid advanceSimulation(){
        initializeCellList();
        Collections.shuffle(myCellList);
        List<Cell> randomizedList=new ArrayList<Cell>(myCellList);
        myCellList.clear();
        myTakenSpots.clear();
        for(Cell cell: randomizedList){
                if (((SugarPatch) cell).hasAgent()){
                    myTakenSpots.add(cell);
                    checkAgent(cell, ((SugarPatch) cell).getAgent(), cell.getRow(), cell.getColumn());
                }
            }
    }

    private Comparator<Cell> SugarComparator=new Comparator<Cell>(){
        @Override
        public int compare(Cell a, Cell b){
            return ((SugarPatch) a).getSugar()-((SugarPatch) b).getSugar();
        }
    };

private void checkAgent(Cell patch, SugarAgent agent, int currentRow, int currentCol) {
    List<Cell> goodNeighbors = myGrid.getVisibleNeighbors(patch, agent.getMyVision());
    List<Cell> bestNeighbors = new ArrayList<Cell>();

    Collections.sort(goodNeighbors, SugarComparator);

    Cell goodNeighbor=bestNeighbors.get(0);
    int maxSugar=((SugarPatch) goodNeighbor).getSugar();
    for (Cell neighbor : goodNeighbors) {
        if (((SugarPatch) neighbor).getSugar()<maxSugar) break;
        bestNeighbors.add(neighbor);
    }
    int
    Cell otherPatch = move(bestNeighbors, patch);
    Cell newLocation=new Cell(otherPatch.getRow(), otherPatch.getColumn(), Color.WHITE);
    if(!(newLocation.getRow()==currentRow && newLocation.getColumn()==currentCol)){
        moveAgent(patch, otherPatch);
        myTakenSpots.add(newLocation);
        myCellList.add(fish);
        if (((FishCell) fish).canReproduce()) {
            ((FishCell) fish).setMyTurnsSurvived(0);
            myCellList.add(new FishCell(currentRow, currentCol, myFishReprodMax));
        }
        else myCellList.add(new EmptyCell(currentRow, currentCol));
    }
}

    public List<Cell> getVisibleNeighbors(Cell cell, int vision){
        Queue<Cell> qu=new LinkedList<Cell>();
        HashMap<Cell, Integer> neighborMap=new HashMap<Cell, Integer>();
        List<Cell> bestNeighbors=new ArrayList<Cell>();
        int distanceOut=0;
        neighborMap.put(cell, distanceOut);
        qu.add(cell);
        int highestSugar=0;
        int sugar;
        while(qu.size()!=0){
            Cell current=qu.remove();
            distanceOut=neighborMap.get(current);
            if(distanceOut>vision) break; //if neighbor lies outside vision, don't consider it, get out of loop
            sugar=((SugarPatch) current).getSugar();
            if(sugar>=highestSugar){
                if (sugar>highestSugar) highestSugar=sugar;
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
    public List<String> getDataFields(){
        return DATA_FIELDS;
    }
}