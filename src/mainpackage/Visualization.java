package mainpackage;

import javafx.scene.Group;
import javafx.scene.Node;

public class Visualization {
    private int numRows = 10;
    private int numCols = 10;
    private double cellWidth = 15.0;
    private double cellHeight = 15.0;
    private double simSpeed = 1.0;

    public Visualization(int numRows, int numCols, double cellWidth, double cellHeight, double simSpeed) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.simSpeed = simSpeed;
    }

    public Node getRootNode (Cell[][] currentGrid) {
        Group root = new Group();
        for (int i = 0; i < currentGrid.length; i++) {
            for (int j = 0; j < currentGrid[i].length; j++) {

            }
        }
        return root;
    }







}
