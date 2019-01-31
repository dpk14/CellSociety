package mainpackage;

import java.util.*;

public class WatorWorldSimulation extends Simulation{

    public WatorWorldSimulation(int numRows, int numCols){
        super(numRows,numCols);
        setupSimulation();
    }

    @Override
    public Cell[][] updateGrid() {
        myCellList.clear();
        for(int i = 0; i < myGrid.length; i++){ // i = row number
            for(int j = 0; j < myGrid[0].length; j++){ // j = column number
                Cell cell = myGrid[i][j];
                List<Cell> neighbors=new ArrayList<Cell>();
                List<Cell> fishNeighbors=new ArrayList<Cell>();
                List<Cell> emptyNeighbors=new ArrayList<Cell>();
                if(cell instanceof EmptyCell){ // if cell is EmptyCell
                    myEmptyCells.add(cell);
                }
                else if(cell instanceof FishCell){
                    neighbors=getNeighbors(cell);
                    for (Cell neighbor: neighbors) {
                        if (neighbor instanceof EmptyCell) emptyNeighbors.add(neighbor);
                    }
                    Cell newlocation=move(emptyNeighbors, cell);
                    swap(newlocation, cell);
                    if(reproduce) myGrid[i][j]=new FishCell(i, j, myMaxTrack);
                    if reproduce, remove cell at current index, insert new fish one instead

                    If shark:

                }
                else{ // add satisfied cells to list already to be added first
                    myCellList.add(cell);
                }
            }
            System.out.println();
        }
        return new Cell[0][];
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
    public void setupSimulation() {

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
