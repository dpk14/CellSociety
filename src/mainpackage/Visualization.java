package mainpackage;

import cells.*;
import grids.Grid;
import grids.HexagonalGrid;
import grids.RectangularGrid;
import grids.TriangularGrid;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import simulations.Simulation;

import java.util.List;


public class Visualization {
    public final double VISUALIZATION_HEIGHT = 500;
    public final double VISUALIZATION_WIDTH = 500;
    private double cellWidth;
    private double cellHeight;
    public static final Paint COLOR_BLACK = Color.BLACK;
    private Shape[][] shapes;
    private boolean borderOn = true;

    public Visualization(int numRows, int numCols) {
        this.cellHeight = VISUALIZATION_HEIGHT / numRows;
        this.cellWidth = VISUALIZATION_WIDTH / numCols;
        shapes = new Shape[numRows][numCols];
    }

    /**
     * Renders a Grid object with shapes and locations on screen
     * @param currentGrid
     * @return
     */
    public Node getRootNode (Grid currentGrid) {

        Group root = new Group();
        if(currentGrid instanceof RectangularGrid){
            root = renderSquareGrid(currentGrid, root);
        }
        else if(currentGrid instanceof TriangularGrid){
            root = renderTriGrid(currentGrid, root);
        }
        else if(currentGrid instanceof HexagonalGrid){
            root = renderHexGrid(currentGrid, root);
        }
        else{
            // TODO
            // MAYBE THROW SOME EXCEPTION HERE JUST IN CASE
        }
        return root;
    }

    public int[] findLocOfShapeClicked (double x, double y) {
        int[] out;
        for (int i = 0; i < shapes.length; i++) {
            for (int j = 0; j < shapes[0].length; j++) {
                Shape curr = shapes[i][j];
                if (curr.contains(x,y)) {
                    out = new int[2];
                    out[0] = i;
                    out[1] = j;
                    return out;
                }
            }
        }
        return null;
    }

    private Group renderSquareGrid(Grid currentGrid, Group root) {
        for (int i = 0; i < currentGrid.getHeight(); i++) {
            for (int j = 0; j < currentGrid.getWidth(); j++) {
                Cell c = currentGrid.getCell(i, j);
                double currentX = j * cellWidth;
                double currentY = i * cellHeight;
                shapes[i][j] = addRectToRoot(root, currentX, currentY, c.getMyColor());
            }
        }
        return root;
    }

    private Group renderTriGrid(Grid currentGrid, Group root) {
        for (int i = 0; i < currentGrid.getHeight(); i++) {
            for (int j = 0; j < currentGrid.getWidth() - 1; j+=2) {
                Cell c1 = currentGrid.getCell(i, j);
                Cell c2 = currentGrid.getCell(i, j+1);
                double x1 = (j / 2) * cellWidth;
                double x2 = (j / 2 + 1) * cellWidth;
                double x3 = (j / 2 + 0.5) * cellWidth;
                if (i % 2 != 0) {
                    double y1 = (i + 1) * cellHeight;
                    double y2 = (i + 1) * cellHeight;
                    double y3 = i * cellHeight;
                    shapes[i][j] = addTriToRoot(root, x1, y1, x2, y2, x3, y3, c1.getMyColor());
                    shapes[i][j+1] = addTriToRoot(root, x3, y3, x3 + cellWidth, y3, x2, y2, c2.getMyColor());
                } else {
                    double y1 = i * cellHeight;
                    double y2 = i * cellHeight;
                    double y3 = (i + 1) * cellHeight;
                    shapes[i][j] = addTriToRoot(root, x1, y1, x2, y2, x3, y3, c1.getMyColor());
                    shapes[i][j+1] =  addTriToRoot(root, x2, y2, x3, y3, x3 + cellWidth, y3, c2.getMyColor());
                }
            }
        }
        return root;
    }

    private Group renderHexGrid (Grid currentGrid, Group root) {
        double l = cellHeight / 2;
        double dy = 0.5 * l;
        double dx = cellWidth / 2;
        for (int i = 0; i < currentGrid.getHeight(); i++) {
            for (int j = 0; j < currentGrid.getWidth(); j++) {
                Cell c1 = currentGrid.getCell(i,j);
                if (i % 2 != 0) {
                    shapes[i][j] = addHexToRoot(root,j * cellWidth + dx,i * cellHeight + dy - i * dy,
                            j * cellWidth + dx + dx,i * cellHeight- i * dy,
                            j * cellWidth + 2 * dx + dx, i * cellHeight + dy- i * dy,
                            j * cellWidth + 2 * dx + dx, i * cellHeight + dy + l- i * dy,
                            j * cellWidth + dx + dx, i * cellHeight + cellHeight- i * dy,
                            j * cellWidth + dx, i * cellHeight + dy + l - i * dy, c1.getMyColor());
                }
                else {
                    shapes[i][j] = addHexToRoot(root,j * cellWidth,i * cellHeight + dy - i * dy,
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

    private Shape addHexToRoot (Group root, double x1, double y1, double x2, double y2, double x3, double y3,
                               double x4, double y4, double x5, double y5, double x6, double y6, Paint p) {
        Polygon hex = new Polygon();
        hex.getPoints().addAll(new Double[]{x1, y1, x2, y2, x3, y3, x4, y4, x5, y5, x6, y6 });
        hex.setFill(p);
        addBorderIfNeeded(hex);
        root.getChildren().add(hex);
        return hex;
    }

    private Shape addTriToRoot(Group root, double x1, double y1, double x2, double y2, double x3, double y3, Paint p) {
        Polygon tri = new Polygon();
        tri.getPoints().addAll(new Double[]{x1, y1, x2, y2, x3, y3 });
        tri.setFill(p);
        addBorderIfNeeded(tri);
        root.getChildren().add(tri);
        return tri;
    }

    private Shape addRectToRoot(Group root, double currentX, double currentY, Paint p) {
        Rectangle rec = new Rectangle();
        rec.setX(currentX);
        rec.setY(currentY);
        rec.setWidth(cellWidth);
        rec.setHeight(cellHeight);
        rec.setFill(p);
        addBorderIfNeeded(rec);
        root.getChildren().add(rec);
        return rec;
    }

    private void addBorderIfNeeded(Rectangle rec) {
        if (borderOn) {
            rec.setStroke(COLOR_BLACK);
            rec.setStrokeType(StrokeType.INSIDE);
        }
    }
    private void addBorderIfNeeded(Polygon tri) {
        if (borderOn) {
            tri.setStroke(COLOR_BLACK);
            tri.setStrokeType(StrokeType.INSIDE);
        }
    }

    public void turnBorderOn() {
        borderOn = true;
    }

    public void turnBorderOff() {
        borderOn = false;
    }
}
