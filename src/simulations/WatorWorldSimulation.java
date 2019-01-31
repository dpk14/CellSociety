package simulations;

import cells.Cell;
import cells.EmptyCell;
import cells.FishCell;
import cells.SharkCell;

import java.util.*;

public class WatorWorldSimulation extends Simulation {
    private int myStartEnergy;
    private int myEnergyGain;
    private int mySharkReprodMax;
    private int myFishReprodMax;

    public static final String DATA_TYPE = "WatorWorldSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "speed", "satisfaction", "start energy",
            "shark reproduction max", "fish reproduction max", "energy gain");

    public WatorWorldSimulation(int numRows, int numCols, int startEnergy, int energyGain, int sharkReproductionMax, int fishReproductionMax){
        super(numRows,numCols);
        myStartEnergy=startEnergy;
        myEnergyGain=energyGain;
        mySharkReprodMax=sharkReproductionMax;
        myFishReprodMax=fishReproductionMax;
        setupSimulation();
    }

    public WatorWorldSimulation(List<String> dataValues, List<Cell> cells){
        super(Integer.parseInt(dataValues.get(2)), Integer.parseInt(dataValues.get(3)));
        myStartEnergy=Integer.parseInt(dataValues.get(4));
        myEnergyGain=Integer.parseInt(dataValues.get(5));
        mySharkReprodMax=Integer.parseInt(dataValues.get(6));
        myFishReprodMax=Integer.parseInt(dataValues.get(7));
        myGrid = getNewGrid(cells);
    }

    @Override
    public Cell[][] updateGrid() {
        myCellList.clear();
        for(int i = 0; i < myGrid.length; i++){ // i = row number
            for(int j = 0; j < myGrid[0].length; j++){ // j = column number
                Cell cell = myGrid[i][j];
                List<Cell> neighbors=new ArrayList<Cell>();
                ArrayList<Cell> fishNeighbors=new ArrayList<Cell>();
                ArrayList<Cell> emptyNeighbors=new ArrayList<Cell>();
                Cell otherCell;
                if(cell instanceof FishCell) {
                    neighbors = getNeighbors(cell);
                    for (Cell neighbor : neighbors) {
                        if (neighbor instanceof EmptyCell) emptyNeighbors.add(neighbor);
                    }
                    otherCell = move(emptyNeighbors, cell);
                    cell.swapPosition(otherCell);
                    myCellList.add(cell);
                    if (((FishCell) cell).canReproduce() && cell!=otherCell) {
                        ((FishCell) cell).setMyTracker(0);
                        myCellList.add(new FishCell(i, j, myFishReprodMax));
                    }
                    else myCellList.add(otherCell);
                    emptyNeighbors.clear();
                }
                else if(cell instanceof SharkCell) {
                    neighbors = getNeighbors(cell);
                    for (Cell neighbor : neighbors) {
                        if (neighbor instanceof EmptyCell) emptyNeighbors.add(neighbor);
                        else if (neighbor instanceof FishCell) fishNeighbors.add(neighbor);
                    }
                    if(fishNeighbors.size()>0) otherCell=move(fishNeighbors, cell);
                    else otherCell=move(emptyNeighbors, cell);
                    cell.swapPosition(otherCell);
                    if (((SharkCell) cell).canReproduce() && cell!=otherCell) {
                        ((SharkCell) cell).setMyTracker(0);
                        if (otherCell instanceof FishCell) ((SharkCell) cell).updateEnergy();
                        myCellList.add(new SharkCell(i, j, mySharkReprodMax, myStartEnergy, myEnergyGain));
                    }
                    else if (otherCell instanceof FishCell) {
                        myCellList.add(new EmptyCell(i, j));
                        ((SharkCell) cell).updateEnergy();
                    }
                    else myCellList.add(otherCell);

                    ((SharkCell) cell).decrementEnergy();
                    if(((SharkCell) cell).getMyEnergy()==0) myCellList.add(new EmptyCell(cell.getRow(), cell.getColumn()));
                    else myCellList.add(cell);
                    emptyNeighbors.clear();
                    fishNeighbors.clear();
                }

            }
            System.out.println();
        }
        myGrid = getNewGrid(this.myCellList);
        return myGrid;
    }

    @Override
    protected Cell[][] getNewGrid(List<Cell> list){
        Cell[][] newGrid = super.getNewGrid(list);
        for(int i = 0; i < myGrid.length; i++) { // i = row number
            for (int j = 0; j < myGrid[0].length; j++) { // j = column number
            if (myGrid[i][j]==null) myGrid[i][j]=new EmptyCell(i, j);
            }
        }
        return newGrid;
    }

    private Cell move(ArrayList<Cell> movable_spots, Cell current){
        Cell newLocation;
        if (movable_spots.size()==0) return current;
        else {
            Random rand = new Random();
            newLocation = movable_spots.get(rand.nextInt(movable_spots.size()));
        }
        return newLocation;
    }

    @Override
    public void setupSimulation(){
        for (int i = 0; i < myGrid.length; i++) {
            for (int j = 0; j < myGrid[i].length; j++) {
                if (i == 3) {
                    myGrid[i][j] = new EmptyCell(i,j);
                } else if (i % 2 == 0) {
                    myGrid[i][j] = new SharkCell(i, j, 7, 9, 2);
                } else {
                    myGrid[i][j] = new FishCell(i, j, 7);
                }
            }
        }
    }
    @Override
    protected List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = super.getNeighbors(cell);
        int row = cell.getRow();
        int column = cell.getColumn();
        if(row==0 || row==myGrid.length-1){
            neighbors.add(myGrid[Math.abs(row-myGrid.length-1)][column]);
        }
        if(column==0 || column==myGrid[0].length-1){
            neighbors.add(myGrid[row][Math.abs(column-myGrid.length-1)]);;
        }
        return neighbors;
    }

    @Override
    public List<String> getDataFields(){
        return DATA_FIELDS;
    }

    @Override
    public String getDataType(){
        return DATA_TYPE;
    }
}
