package mainpackage;

import java.util.*;

public class WatorWorldSimulation extends Simulation{
    int myStartEnergy;
    int mySharkReprodMax;
    int myFishReprodMax;
    int myEnergyGain;

    public WatorWorldSimulation(int numRows, int numCols, int startEnergy, int energyGain, int sharkReproductionMax, int fishReproductionMax){
        super(numRows,numCols);b
        mySharkReprodMax=sharkReproductionMax;
        myFishReprodMax=fishReproductionMax;
        myStartEnergy=startEnergy;
        myEnergyGain=energyGain;
        setupSimulation();
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
                        ((FishCell) cell).myTracker=0;
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
                        ((SharkCell) cell).myTracker=0;
                        if (otherCell instanceof FishCell) ((SharkCell) cell).updateEnergy();
                        myCellList.add(new SharkCell(i, j, mySharkReprodMax, myStartEnergy, myEnergyGain));
                    }
                    else if (otherCell instanceof FishCell) {
                        myCellList.add(new EmptyCell(i, j));
                        ((SharkCell) cell).updateEnergy();
                    }
                    else myCellList.add(otherCell);

                    ((SharkCell) cell).decrementEnergy();
                    if(((SharkCell) cell).myEnergy==0) myCellList.add(new EmptyCell(cell.getRow(), cell.getColumn()));
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
}
