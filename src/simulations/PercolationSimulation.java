package simulations;

import cells.Cell;
import cells.EmptyCell;
import cells.StateChangeCell;
import cells.Cell;
import cells.StateChangeCell;
import grids.Grid;


import java.util.*;

public class PercolationSimulation extends Simulation{
    public static final String DATA_TYPE = "PercolationSimulation";
//    public static final List<String> DATA_FIELDS = List.of(
//            "title", "author", "rows", "columns", "cellShape", "gridShape", "speed", "openRate");

    public PercolationSimulation(Map<String, String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(dataValues, cells);
        setupSliderInfo();
    }

    public PercolationSimulation(Map<String, String> dataValues){
        super(dataValues);
        setupSliderInfo();
    }

    @Override
    public Grid advanceSimulation(){
        if (percolates()) return myGrid;
        Cell cell = new EmptyCell(0, 0);
        List<Cell> neighbors;
        myCellList.clear();
        Queue<Cell> qu = openOne();
        while (qu.size() > 0) {
            cell = qu.remove();
            neighbors=myGrid.getImmediateNeighbors(cell);
            if (getTypedNeighbors(cell, "FULL", neighbors).size() > 0 || cell.getRow() == 0) {
                ((StateChangeCell) cell).setState("FULL");
                myCellList.add(cell);
                neighbors = getTypedNeighbors(cell, "OPEN", neighbors);
                for (Cell open : neighbors) qu.add(open);
            }
        }
        myGrid.updateGrid(myCellList);
        return myGrid;
    }

    @Override
    protected void setupSliderInfo() {
        super.setupSliderInfo();
    }

    private Queue<Cell> openOne(){
        Queue<Cell> qu=new LinkedList<Cell>();
        Cell cell=new StateChangeCell(0, 0, "OPEN");
        Random rand=new Random();

        while(!((StateChangeCell) cell).getState().equals("CLOSED")) {
            cell=myGrid.getCell(rand.nextInt(myGrid.getHeight()), rand.nextInt(myGrid.getWidth()));
        }

        ((StateChangeCell) cell).setState("OPEN");
        qu.add(cell);
        return qu;
    }

    private boolean percolates(){
        for(int k=0; k<myGrid.getWidth(); k++) {
            Cell cell=myGrid.getCell(myGrid.getHeight()-1, k);
            if (((StateChangeCell) cell).getState().equals("FULL")) return true;
        }
        return false;
        }

    @Override
    public void updateParameters(Map<String, String> map){
        super.updateParameters(map);
    }

    @Override
    protected Grid setupGridByProb(){
        int rows = Integer.parseInt(myDataValues.get("rows"));
        int cols = Integer.parseInt(myDataValues.get("columns"));
        double openRate = Double.parseDouble(myDataValues.get("openRate"));
        List<Cell> cells = new ArrayList<>();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                Cell cell;
                if(evaluateOdds(openRate)){
                    cell = new StateChangeCell(i, j, "OPEN");
                }
                else{
                    cell = new StateChangeCell(i, j, "CLOSED");
                }
                cells.add(cell);
            }
        }
        return createGrid(myDataValues.get("gridShape"), rows, cols, cells);
    }

    @Override
    protected Grid setupGridByQuota(){
        int rows = Integer.parseInt(myDataValues.get("rows"));
        int cols = Integer.parseInt(myDataValues.get("columns"));
        int openRate = (int) Double.parseDouble(myDataValues.get("openRate"));
        List<String> states = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        for(int k = 0; k < openRate; k++){
            states.add("OPEN");
        }
        for(int k = 0; k < (rows*cols-openRate+1); k++){
            states.add("CLOSED");
        }
        Collections.shuffle(states);
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                String state = states.remove(0);
                if(state.equals("OPEN") || state.equals("CLOSED")) {
                    cells.add(new StateChangeCell(i, j, state));
                }
                else {
                    throw new RuntimeException("Cell type not allowed in " + DATA_TYPE);
                }
            }
        }
        return createGrid(myDataValues.get("gridShape"), rows, cols, cells);
    }

    @Override
    public String getSimType(){
        return DATA_TYPE;
    }
}
