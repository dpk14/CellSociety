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
    private ArrayList<Cell> myTakenSpots=new ArrayList<>();

    public static final String DATA_TYPE = "WatorWorldSimulation";
    public static final List<String> DATA_FIELDS = List.of(
            "title", "author", "rows", "columns", "speed", "startEnergy",
            "sharkReproductionMax", "fishReproductionMax", "energyGain");
    private Map<String, String> myDataValues;
    public WatorWorldSimulation(int numRows, int numCols, int startEnergy, int energyGain, int sharkReproductionMax, int fishReproductionMax){
        super(numRows,numCols);
        myStartEnergy=startEnergy;
        myEnergyGain=energyGain;
        mySharkReprodMax=sharkReproductionMax;
        myFishReprodMax=fishReproductionMax;
        myDataValues = new HashMap<>();
    }

    public WatorWorldSimulation(Map<String, String> dataValues, List<Cell> cells){
        super(Integer.parseInt(dataValues.get("rows")), Integer.parseInt(dataValues.get("columns")));
        myStartEnergy=Integer.parseInt(dataValues.get("startEnergy"));
        myEnergyGain=Integer.parseInt(dataValues.get("energyGain"));
        mySharkReprodMax=Integer.parseInt(dataValues.get("sharkReproductionMax"));
        myFishReprodMax=Integer.parseInt(dataValues.get("fishReproductionMax"));
        myGrid = getNewGrid(cells);
        myDataValues = dataValues;
    }

    private ArrayList<Cell> randomizeCellVisitation(){
        ArrayList<Cell> randomizedList=new ArrayList<Cell>();
        for(int i = 0; i < myGrid.length; i++) { // i = row number
            for (int j = 0; j < myGrid[0].length; j++) { // j = column number
                randomizedList.add(myGrid[i][j]);
            }
        }
        Collections.shuffle(randomizedList);
        return randomizedList;
    }

    @Override
    protected Cell[][] getNewGrid(List<Cell> list){
        Cell[][] newGrid = super.getNewGrid(list);
        for(int i = 0; i < newGrid.length; i++) { // i = row number
            for (int j = 0; j < newGrid[0].length; j++) { // j = column number
                if (newGrid[i][j]==null) newGrid[i][j]=new EmptyCell(i, j);
            }
        }
        return newGrid;
    }

    @Override
    public Cell[][] updateGrid() {
        myCellList.clear();
        myTakenSpots.clear();
        ArrayList<Cell> randomizedList=randomizeCellVisitation();
        for(Cell cell: randomizedList){
            if(cell instanceof FishCell) {
                ((FishCell) cell).updateTracker();
                fishMover(cell, new Cell(cell.getRow(), cell.getColumn()), cell.getRow(), cell.getColumn());
            }
            else if(cell instanceof SharkCell) {
                ((SharkCell) cell).updateTracker();
                ((SharkCell) cell).decrementEnergy();
                sharkMover(cell, new Cell(cell.getRow(), cell.getColumn()), cell.getRow(), cell.getColumn());
            }
            System.out.println();
        }
        myGrid = getNewGrid(this.myCellList);
        return myGrid;
    }

    public void fishMover(Cell fish, Cell currentLocation, int currentRow, int currentCol) {
            ArrayList<Cell> emptyNeighbors=new ArrayList<Cell>();
            List<Cell> neighbors = getNeighbors(fish);

            for (Cell neighbor : neighbors) {
                if (neighbor instanceof EmptyCell) emptyNeighbors.add(neighbor);
            }
            emptyNeighbors.removeAll(myTakenSpots);
            Cell otherCell = move(emptyNeighbors, fish);
            Cell newLocation=new Cell(otherCell.getRow(), otherCell.getColumn());
            myTakenSpots.add(newLocation);
            fish.swapPosition(otherCell);
            myCellList.add(fish);
            if (((FishCell) fish).canReproduce() && (newLocation.getRow()!=currentRow || newLocation.getColumn()!=currentCol)) {
                ((FishCell) fish).setMyTracker(0);
                myCellList.add(new FishCell(currentRow, currentCol, myFishReprodMax));
                myTakenSpots.add(currentLocation);
            }
            else myCellList.add(otherCell);
    }

    public void sharkMover(Cell shark, Cell currentLocation, int currentRow, int currentCol) {
        ArrayList<Cell> emptyNeighbors=new ArrayList<Cell>();
        ArrayList<Cell> fishNeighbors=new ArrayList<Cell>();
        ArrayList<Cell> availableNeighbors;

        List<Cell> neighbors = getNeighbors(shark);
        for (Cell neighbor : neighbors) {
            if (neighbor instanceof EmptyCell) emptyNeighbors.add(neighbor);
            else if (neighbor instanceof FishCell) fishNeighbors.add(neighbor);
        }
        if(fishNeighbors.size()>0) availableNeighbors=new ArrayList<Cell>(fishNeighbors);
        else availableNeighbors=new ArrayList<Cell>(emptyNeighbors);
        Cell otherCell=move(availableNeighbors, shark);
        Cell newLocation=new Cell(otherCell.getRow(), otherCell.getColumn());
        shark.swapPosition(otherCell);
        if (((SharkCell) shark).canReproduce() && (newLocation.getRow()!=currentRow || newLocation.getColumn()!=currentCol)) {
            ((SharkCell) shark).setMyTracker(0);
            if (otherCell instanceof FishCell) ((SharkCell) shark).updateEnergy();
            myCellList.add(new SharkCell(currentRow, currentCol, mySharkReprodMax, myStartEnergy, myEnergyGain));
            myTakenSpots.add(currentLocation);
        }
        else if (otherCell instanceof FishCell) {
            myCellList.add(new EmptyCell(currentRow, currentCol));
            ((SharkCell) shark).updateEnergy();
        }
        else myCellList.add(otherCell);

        if(((SharkCell) shark).getMyEnergy()==0) myCellList.add(new EmptyCell(newLocation.getRow(), newLocation.getColumn()));
        else {
            myTakenSpots.add(newLocation);
            myCellList.add(shark);
        }
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
    protected List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = super.getNeighbors(cell);
        int row = cell.getRow();
        int column = cell.getColumn();
        if(row==0 || row==myGrid.length-1){
            neighbors.add(myGrid[Math.abs(row-(myGrid.length-1))][column]); //at grid edges, neighbors are also on opposite edge of grid
        }
        if(column==0 || column==myGrid[0].length-1){
            neighbors.add(myGrid[row][Math.abs(column-(myGrid.length-1))]);; //at grid edges, neighbors are also on opposite edge of grid
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

    @Override
    public Map<String, String> getMyDataValues(){
        return myDataValues;
    }
}
