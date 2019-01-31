package cells;

import java.util.List;

public class EmptyCell extends Cell {

    public static final String DATA_TYPE = "EmptyCell";
    public static final List<String> DATA_FIELDS = List.of(
            "row", "column");

    public EmptyCell(int row, int column){
        super(row, column);
    }
}
