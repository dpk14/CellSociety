package simulations;

import cells.AgentCell;
import cells.Cell;
import cells.EmptyCell;
import cells.StateChangeCell;
import grids.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameOfLifeSimulation extends Simulation{
    public static final String DATA_TYPE = "GameOfLifeSimulation";
//    public static final List<String> DATA_FIELDS = List.of(
//            "title", "author", "cellShape", "gridShape", "rows", "columns", "speed", "populatedRate");

    public GameOfLifeSimulation(Map<String, String> dataValues, List<Cell> cells){ // pass in list of strings representing rows, columns, sat threshold
        super(dataValues, cells);
        setupSliderInfo();
    }

    public GameOfLifeSimulation(Map<String, String> dataValues){
        super(dataValues);
        setupSliderInfo();
    }

    @Override
    public Grid advanceSimulation(){
        String state;
        myCellList.clear();
        for(int i = 0; i < myGrid.getHeight(); i++){ // i = row number
            for(int j = 0; j < myGrid.getWidth(); j++){ // j = column number
                Cell cell = myGrid.getCell(i, j);
                state=((StateChangeCell) cell).getState();
                cell=new StateChangeCell(i, j, editState(cell, state));
                myCellList.add(cell);
            }
        }
        myGrid.updateGrid(myCellList);
        return myGrid;
    }

    @Override
    protected void setupSliderInfo() {
        mySliderInfo.put("speed", myDataValues.get("speed"));
    }

    private String editState(Cell cell, String state){
        List<Cell> neighbors=myGrid.getAllNeighbors(cell);
        neighbors=getTypedNeighbors(cell, "POPULATED", neighbors);
        if (state.equals("POPULATED") && (neighbors.size()<=1 || neighbors.size()>=4)) return "EMPTY";
        else if (state.equals("EMPTY") && neighbors.size()==3) return "POPULATED";
        return state;
    }

    @Override
    public void setupGrid(){
        List<Cell> cells = new ArrayList<>();
        int rows = Integer.parseInt(myDataValues.get("rows"));
        int cols = Integer.parseInt(myDataValues.get("columns"));
        double populatedRate = Double.parseDouble(myDataValues.get("populatedRate"));
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                Cell cell;
                if(evaluateOdds(populatedRate)){
                    cell = new StateChangeCell(i, j, "POPULATED");
                }
                else{
                    cell = new StateChangeCell(i, j, "EMPTY");
                }
                cells.add(cell);
            }
        }
        myGrid = createGrid(myDataValues.get("gridShape"), rows, cols, cells);
    }

    @Override
    public String getDataType(){
        return DATA_TYPE;
    }

    @Override
    public void updateParameters(Map<String, String> map){
        myDataValues = map;
    }
}
