package mainpackage;

import java.util.List;

public class AgentCell extends Cell {
    private String myType;

    public AgentCell(int row, int column, String myType){
        super(row, column);
        this.myType = myType;
    }

    public String getType(){
        return myType;
    }

    public double calculatePercentage(List<Cell> neighbors){
        int sameType = 0;
        int differentType = 0;
        for(Cell cell : neighbors){
            if (cell instanceof AgentCell && ((AgentCell) cell).getType().equals(this.getType())) {
                sameType++;
            } else if (cell instanceof AgentCell && !((AgentCell) cell).getType().equals(this.getType())) {
                differentType++;
            }

        }
        //System.out.print("| " + (double) sameType/(sameType+differentType));
        return (double) sameType/(sameType+differentType);
    }
}
