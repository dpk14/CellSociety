package mainpackage;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class Visualization {
    private int numRows = 10;
    private int numCols = 10;
    private double cellWidth = 15.0;
    private double cellHeight = 15.0;
    private double simSpeed = 1.0;
    private Simulation currentSimType;


    public static final Paint COLOR_AGENT_RED = Color.RED;
    public static final Paint COLOR_AGENT_BLUE = Color.BLUE;
    public static final Paint COLOR_EMPTY = Color.WHITESMOKE;
    public static final Paint COLOR_BLACK = Color.BLACK;

    public Visualization(int numRows, int numCols, double cellWidth, double cellHeight, double simSpeed) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.simSpeed = simSpeed;
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
                if (c instanceof AgentCell && ((AgentCell) c).getType().equals("BLUE")) {
                    addRectangleToRoot(root, currentX, currentY, COLOR_AGENT_BLUE);

                    System.out.print("1 ");
                } else if (c instanceof AgentCell && ((AgentCell) c).getType().equals("RED")) {
                    addRectangleToRoot(root, currentX, currentY, COLOR_AGENT_RED);

                    System.out.print("2 ");
                } else if (c instanceof EmptyCell) {
                    addRectangleToRoot(root, currentX, currentY, COLOR_EMPTY);

                    System.out.print("0 ");
                } else if (c == null) {
                    System.out.print("6 ");
                }
                else {
                    System.out.print("9 ");
                }
            }
            System.out.println();
        }
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=");
        return root;
    }

    private void addRectangleToRoot(Group root, double currentX, double currentY, Paint colorAgentBlue) {
        Rectangle rec = new Rectangle();
        rec.setX(currentX);
        rec.setY(currentY);
        rec.setWidth(cellWidth);
        rec.setHeight(cellHeight);
        rec.setFill(colorAgentBlue);
        rec.setStroke(COLOR_BLACK);
        rec.setStrokeType(StrokeType.INSIDE);
        root.getChildren().add(rec);
    }

    public Cell[][] getInitialGrid(String simulationType) {
        if (simulationType.equals("SEGREGATION")) { }

        currentSimType = new SegregationSimulation(10,10,0.5,0.5,0.1);
        currentSimType.setupSimulation();
        return  currentSimType.getMyGrid();
    }









}
