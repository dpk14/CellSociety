package cells;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;
import java.util.Map;

public class EmptyCell extends Cell {
    public static final Paint COLOR_EMPTY = Color.WHITESMOKE;
    public static final String DATA_TYPE = "EmptyCell";
//    public static final List<String> DATA_FIELDS = List.of(
//            "row", "column");

    public EmptyCell(int row, int column){
        super(row, column, COLOR_EMPTY);
    }

    public EmptyCell(Map<String, String> dataValues){
        super(Integer.parseInt(dataValues.get("row")), Integer.parseInt(dataValues.get("column")), COLOR_EMPTY);
    }
}
