package mainpackage;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Paint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PopulationGraph {

    private Map<Paint, Integer> map;
    private Group root;
    private List<XYChart.Series> plots;
    private int counter;


    private CategoryAxis xAxis = new CategoryAxis();
    private NumberAxis yAxis = new NumberAxis();
    private LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);

    PopulationGraph (Map<Paint, Integer> m) {
        map = m;
        root = new Group();
        plots = new ArrayList<>();
        xAxis.setLabel("Iterations");
        for (Paint p : m.keySet()) {
            XYChart.Series s = new XYChart.Series();
            s.setName(p.toString());

            plots.add(s);
            lineChart.getData().add(s);
        }
        counter = 1;
    }

    public void addPoint(Map<Paint, Integer> m) {


        for (Paint p : m.keySet()) {
            if (!existAsSeries(p)) {
                XYChart.Series s = new XYChart.Series();
                s.setName(p.toString());
                plots.add(s);
                lineChart.getData().add(s);
            }
        }

        for (Paint p : m.keySet()) {
            for (XYChart.Series s : plots) {
                if (s.getName().equals(p.toString())) {
                    s.getData().add(new XYChart.Data(Integer.toString(counter), m.get(p)));
                }
//                System.out.println(s.getName());
//                System.out.println(p.toString());
            }
        }
        counter++;
    }

    private boolean existAsSeries(Paint p) {
        for (int i = 0; i < plots.size(); i++) {
            if (plots.get(i).getName().equals(p.toString())) {
                return true;
            }
        }
        return false;
    }


    public Node getGraphRootNode() {
        lineChart.setLayoutX(0);
        lineChart.setLayoutY(540);
        //lineChart.setMaxWidth(850);
        lineChart.setMinWidth(800);
        lineChart.setMaxHeight(250);
        return lineChart;
    }

}
