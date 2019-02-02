package cells;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class StateChangeCell extends Cell{
    String myState;
    public static final String EMPTY_IMAGE = "GameOfLifeImages/empty.gif";
    public static final String POPULATED_IMAGE = "GameOfLifeImages/populated.gif";

    public static final String FULL_IMAGE = "PercolationImages/full.gif";
    public static final String CLOSED_IMAGE = "PercolationImages/closed.gif";
    public static final String OPEN_IMAGE = "PercolationImages/open.gif";

    public static final String TREE_IMAGE = "SpreadingFireImages/tree.gif";
    public static final String BURNING_IMAGE = "SpredingFireImages/burning.gif";

    public StateChangeCell(int row, int column, String state) {
        super(row, column, new ImageView(new Image(EMPTY_IMAGE)));
        myState=state;
        setImage();
    }

    private void setImage() {
        ImageView p;
        if (myState.equals("EMPTY")) { p = new ImageView(new Image(EMPTY_IMAGE)); }
        else if (myState.equals("POPULATED")) { p = new ImageView(new Image(POPULATED_IMAGE)); }
        else if (myState.equals("FULL")) { p = new ImageView(new Image(FULL_IMAGE)); }
        else if (myState.equals("CLOSED")) { p = new ImageView(new Image(CLOSED_IMAGE)); }
        else if (myState.equals("OPEN")) { p = new ImageView(new Image(OPEN_IMAGE)); }
        else if (myState.equals("TREE")) { p = new ImageView(new Image(TREE_IMAGE)); }
        else if (myState.equals("BURNING")) { p = new ImageView(new Image(BURNING_IMAGE)); }
        else { p = new ImageView(new Image(EMPTY_IMAGE)); }
        myImage = p;
    }

    public void setState(String state){
        myState=state;
        setImage();
    }

    public String getState(){
        return myState;
    }

}
