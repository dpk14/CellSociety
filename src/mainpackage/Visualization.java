package mainpackage;

import cells.*;
import grids.Grid;
import grids.HexagonalGrid;
import grids.RectangularGrid;
import grids.TriangularGrid;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
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

    public Node getRootNode (Grid currentGrid) {
        Group root = new Group();
        if(currentGrid instanceof RectangularGrid){
            root = renderSquareGrid(currentGrid.getMyCellArray(), root);
        }
        else if(currentGrid instanceof TriangularGrid){
            root = renderTriGrid(currentGrid.getMyCellArray(), root);
        }
        else if(currentGrid instanceof HexagonalGrid){
            root = renderHexGrid(currentGrid.getMyCellArray(), root);
        }
        else{
            // MAYBE THROW SOME EXCEPTION HERE JUST IN CASE
        }
        return root;
    }

    private Group renderSquareGrid(Cell[][] currentGrid, Group root) {
        for (int i = 0; i < currentGrid.length; i++) {
            for (int j = 0; j < currentGrid[i].length; j++) {
                Cell c = currentGrid[i][j];
                double currentX = j * cellWidth;
                double currentY = i * cellHeight;
                addRectToRoot(root, currentX, currentY, c.getMyColor());
            }
        }
        return root;
    }

    private Group renderTriGrid(Cell[][] currentGrid, Group root) {
        for (int i = 0; i < currentGrid.length; i++) {
            for (int j = 0; j < currentGrid[i].length - 1; j+=2) {
                Cell c1 = currentGrid[i][j];
                Cell c2 = currentGrid[i][j+1];
                double x1 = (j / 2) * cellWidth;
                double x2 = (j / 2 + 1) * cellWidth;
                double x3 = (j / 2 + 0.5) * cellWidth;
                if (i % 2 != 0) {
                    double y1 = (i + 1) * cellHeight;
                    double y2 = (i + 1) * cellHeight;
                    double y3 = i * cellHeight;
                    addTriToRoot(root, x1, y1, x2, y2, x3, y3, c1.getMyColor());
                    addTriToRoot(root, x3, y3, x3 + cellWidth, y3, x2, y2, c2.getMyColor());
                } else {
                    double y1 = i * cellHeight;
                    double y2 = i * cellHeight;
                    double y3 = (i + 1) * cellHeight;
                    addTriToRoot(root, x1, y1, x2, y2, x3, y3, c1.getMyColor());
                    addTriToRoot(root, x2, y2, x3, y3, x3 + cellWidth, y3, c2.getMyColor());
                }
            }
        }
        return root;
    }


    private Group renderHexGrid (Cell[][] currentGrid, Group root) {
        double l = cellHeight / 2;
        double dy = 0.5 * l;
        double dx = cellWidth / 2;

        for (int i = 0; i < currentGrid.length; i++) {
            for (int j = 0; j < currentGrid[i].length; j++) {
                Cell c1 = currentGrid[i][j];

                if (i % 2 != 0) {
                    addHexToRoot(root,j * cellWidth + dx,i * cellHeight + dy - i * dy,
                            j * cellWidth + dx + dx,i * cellHeight- i * dy,
                            j * cellWidth + 2 * dx + dx, i * cellHeight + dy- i * dy,
                            j * cellWidth + 2 * dx + dx, i * cellHeight + dy + l- i * dy,
                            j * cellWidth + dx + dx, i * cellHeight + cellHeight- i * dy,
                            j * cellWidth + dx, i * cellHeight + dy + l - i * dy, c1.getMyColor());
                }
                else {
                    addHexToRoot(root,j * cellWidth,i * cellHeight + dy - i * dy,
                            j * cellWidth + dx,i * cellHeight- i * dy,
                            j * cellWidth + 2 * dx, i * cellHeight + dy- i * dy,
                            j * cellWidth + 2 * dx, i * cellHeight + dy + l- i * dy,
                            j * cellWidth + dx, i * cellHeight + cellHeight- i * dy,
                            j * cellWidth, i * cellHeight + dy + l- i * dy, c1.getMyColor());
                }
            }
        }
        return root;
    }



    private void addHexToRoot (Group root, double x1, double y1, double x2, double y2, double x3, double y3,
                               double x4, double y4, double x5, double y5, double x6, double y6, Paint p) {
        Polygon hex = new Polygon();
        hex.getPoints().addAll(new Double[]{x1, y1, x2, y2, x3, y3, x4, y4, x5, y5, x6, y6 });
        hex.setFill(p);
        hex.setStroke(COLOR_BLACK);
        hex.setStrokeType(StrokeType.INSIDE);
        root.getChildren().add(hex);
    }

    private void addTriToRoot(Group root, double x1, double y1, double x2, double y2, double x3, double y3, Paint p) {
        Polygon tri = new Polygon();
        tri.getPoints().addAll(new Double[]{x1, y1, x2, y2, x3, y3 });
        tri.setFill(p);
        tri.setStroke(COLOR_BLACK);
        tri.setStrokeType(StrokeType.INSIDE);
        root.getChildren().add(tri);
    }

    private void addRectToRoot(Group root, double currentX, double currentY, Paint p) {
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
