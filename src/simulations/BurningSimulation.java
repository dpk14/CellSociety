//package simulations;
//
//import cells.AgentCell;
//import cells.Cell;
//import cells.EmptyCell;
//import cells.StateChangeCell;
//
//import javax.swing.plaf.nimbus.State;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Random;
//
//public class BurningSimulation extends Simulation {
//    public static final String DATA_TYPE = "SegregationSimulation";
//    public static final List<String> DATA_FIELDS = List.of(
//            "title", "author", "rows", "columns", "speed", "satisfaction");
//
//    private double myProbCatch;
//    private double myProbLightning;
//    private double myProbGrow;
//
//    public BurningSimulation(int numRows, int numCols, double probCatch, double probLightning, double probGrow){
//        super(numRows,numCols);
//        myProbCatch=probCatch;
//        myProbLightning=probLightning;
//        myProbGrow=probGrow;
//        setupSimulation();
//    }
//
//    @Override
//    public Cell[][] updateGrid(){
//        String state;
//        myCellList.clear();
//        for(int i = 0; i < myGrid.length; i++){ // i = row number
//            for(int j = 0; j < myGrid[0].length; j++){ // j = column number
//                Cell cell = myGrid[i][j];
//                state=((StateChangeCell) cell).getState();
//                if(((StateChangeCell) cell).getState().equals("BURN")) ((StateChangeCell) cell).setState("EMPTY");
//                else ((StateChangeCell) cell).setState(randomizeState(cell, state));
//                myCellList.add(cell);
//            }
//        }
//        myGrid = getNewGrid(this.myCellList);
//        return myGrid;
//    }
//
//    private String randomizeState(Cell cell, String state){
//        double rand=Math.random();
//
//        if(state.equals("TREE")){
//            int firecount=getTypedNeighbors(cell, "BURNING").size();
//            if((firecount!=0 && rand/firecount<myProbCatch) || rand<myProbCatch*myProbLightning) return "BURNING";
//        }
//        else if (rand<myProbGrow) return "TREE";
//
//        return state;
//    }
//
//    @Override
//    public void setupSimulation(){
//        for (int i = 0; i < myGrid.length; i++) {
//            for (int j = 0; j < myGrid[i].length; j++) {
//                if (i == 3) {
//                    myGrid[i][j] = new EmptyCell(i,j);
//                } else if (i % 2 == 0) {
//                    myGrid[i][j] = new AgentCell(i,j,"BLUE");
//                } else {
//                    myGrid[i][j] = new AgentCell(i,j,"RED");
//                }
//            }
//        }
//    }
//
//    @Override
//    public List<String> getDataFields(){
//        return DATA_FIELDS;
//    }
//
//    @Override
//    public String getDataType(){
//        return DATA_TYPE;
//    }
//}
