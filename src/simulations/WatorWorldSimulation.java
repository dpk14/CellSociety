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
                myTakenSpots.add(cell);
                ((FishCell) cell).updateMyTurnsSurvived();
                fishMover(cell, cell.getRow(), cell.getColumn());
            }
            else if(cell instanceof SharkCell) {
                myTakenSpots.add(cell);
                ((SharkCell) cell).updateMyTurnsSurvived();
                ((SharkCell) cell).decrementEnergy();
                if(((SharkCell) cell).getMyEnergy()<=0) myCellList.add(new EmptyCell(cell.getRow(), cell.getColumn()));
                else sharkMover(cell, cell.getRow(), cell.getColumn());
            }
            System.out.println();
        }
        myGrid = getNewGrid(this.myCellList);
        return myGrid;
    }

    public void fishMover(Cell fish, int currentRow, int currentCol) {
            ArrayList<Cell> emptyNeighbors=new ArrayList<Cell>();
            List<Cell> neighbors = getNeighbors(fish);
            neighbors=removeTakenSpots(neighbors);
            for (Cell neighbor : neighbors) {
                if (neighbor instanceof EmptyCell) emptyNeighbors.add(neighbor);
            }
            Cell otherCell = move(emptyNeighbors, fish);
            Cell newLocation=new Cell(otherCell.getRow(), otherCell.getColumn());
            if(!(newLocation.getRow()==currentRow && newLocation.getColumn()==currentCol)){
                fish.swapPosition(otherCell);
                myTakenSpots.add(newLocation);
                myCellList.add(fish);
                if (((FishCell) fish).canReproduce()) {
                    ((FishCell) fish).setMyTurnsSurvived(0);
                    myCellList.add(new FishCell(currentRow, currentCol, myFishReprodMax));
                }
                else myCellList.add(new EmptyCell(currentRow, currentCol));
    }       }

    public void sharkMover(Cell shark, int currentRow, int currentCol) {
        ArrayList<Cell> emptyNeighbors=new ArrayList<Cell>();
        ArrayList<Cell> fishNeighbors=new ArrayList<Cell>();
        ArrayList<Cell> availableNeighbors;

        List<Cell> neighbors = getNeighbors(shark);
        neighbors=removeTakenSpots(neighbors);
        for (Cell neighbor : neighbors) {
            if (neighbor instanceof EmptyCell) emptyNeighbors.add(neighbor);
            else if (neighbor instanceof FishCell) fishNeighbors.add(neighbor);
        }
        if(fishNeighbors.size()>0) availableNeighbors=new ArrayList<Cell>(fishNeighbors);
        else availableNeighbors=new ArrayList<Cell>(emptyNeighbors);
        Cell otherCell=move(availableNeighbors, shark);
        Cell newLocation=new Cell(otherCell.getRow(), otherCell.getColumn());
        if(!(newLocation.getRow()==currentRow && newLocation.getColumn()==currentCol)) {
            shark.swapPosition(otherCell);
            if (otherCell instanceof FishCell) ((SharkCell) shark).updateEnergy();
            myTakenSpots.add(newLocation);
            myCellList.add(shark);
            if (((SharkCell) shark).canReproduce()) {
                ((SharkCell) shark).setMyTurnsSurvived(0);
                myCellList.add(new SharkCell(currentRow, currentCol, mySharkReprodMax, myStartEnergy, myEnergyGain));
            }
            else myCellList.add(new EmptyCell(currentRow, currentCol));
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

    private List<Cell> removeTakenSpots(List<Cell> neighbors){
        List<Cell> reducedNeighbors=new ArrayList<Cell>();
        for(Cell neighbor: neighbors){
            for (Cell taken: myTakenSpots)
                if(!(neighbor.getColumn()==taken.getColumn() && neighbor.getRow()==taken.getRow())){
                    reducedNeighbors.add(neighbor);
                }
        }
        return reducedNeighbors;
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
    public void setupSimulation(){
        int sharknum=3;
        int fishnum=4;
        int emptynum=25;
        ArrayList<String> freqlist=new ArrayList<String>();
        Random rand=new Random();
        Cell cell;
        String cellname;

        for(int k=0; k<sharknum; k++) freqlist.add("SHARK");
        for(int k=0; k<fishnum; k++) freqlist.add("FISH");
        for(int k=0; k<emptynum; k++) freqlist.add("EMPTY");
    for (int i = 0; i < myGrid.length; i++) {
            for (int j = 0; j < myGrid[i].length; j++) {
                cellname=freqlist.get(rand.nextInt(freqlist.size()-1));
                if (cellname.equals("SHARK")){
                    myGrid[i][j] = new SharkCell(i, j, 9, 3, 2);
                }
                else if (cellname.equals("FISH")){
                    myGrid[i][j] = new FishCell(i, j, 9);
                }
                else myGrid[i][j] = new EmptyCell(i, j);
            }
        }
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
