package mainpackage;

import cells.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import simulations.SegregationSimulation;
import simulations.Simulation;

import java.awt.desktop.SystemSleepEvent;

public class Visualization {
    public final double VISUALIZATION_HEIGHT = 500;
    public final double VISUALIZATION_WIDTH = 500;

    private int numRows;
    private int numCols;
    private double cellWidth;
    private double cellHeight;
    private double simSpeed = 1.0;
    private Simulation currentSimType;

    public static final Paint COLOR_AGENT_RED = Color.RED;
    public static final Paint COLOR_AGENT_BLUE = Color.BLUE;
    public static final Paint COLOR_EMPTY = Color.WHITESMOKE;
    public static final Paint COLOR_BLACK = Color.BLACK;
    public static final Paint COLOR_SHARK = Color.GREY;
    public static final Paint COLOR_FISH = Color.ORANGE;


    public Visualization(int numRows, int numCols, double simSpeed) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.simSpeed = simSpeed;
        this.cellHeight = VISUALIZATION_HEIGHT / numRows;
        this.cellWidth = VISUALIZATION_WIDTH / numCols;
    }

    public Simulation getCurrentSimType() {
        return currentSimType;
    }

    public Node getRootNode (Cell[][] currentGrid) {
        Group root = new Group();
        for (int i = 0; i < currentGrid.length; i++) {
            for (int j = 0; j < currentGrid[i].length; j++) {
                Cell c = currentGrid[i][j];
                double currentX = j * cellWidth;
                double currentY = i * cellHeight;
                addImageToRoot(root, currentX, currentY, c.getMyColor());
            }
        }
        return root;
    }

    private void addImageToRoot(Group root, double currentX, double currentY, Paint p) {
        Rectangle rec = new Rectangle();
        rec.setX(currentX);
        rec.setY(currentY);
        rec.setWidth(cellWidth);
        rec.setHeight(cellHeight);
        rec.setFill(p);
        rec.setStroke(COLOR_BLACK);
        rec.setStrokeType(StrokeType.INSIDE);
        root.getChildren().add(rec);
    }
}
