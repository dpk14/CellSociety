package mainpackage;

import cells.*;
import simulations.*;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
    public static final String ERROR_MESSAGE = "XML file does not represent %s";
    private final String TYPE_ATTRIBUTE;
    private final DocumentBuilder DOCUMENT_BUILDER;
    public enum SimulationType{
        GAMEOFLIFE{
            public Simulation create(List<String> dataValues, List<Cell> cells){
                return new GameOfLifeSimulation(dataValues, cells);
            }
        },
        SPREADINGFIRE{
            public Simulation create(List<String> dataValues, List<Cell> cells){
                return new SpreadingFireSimulation(dataValues, cells);
            }
        },
        PERCOLATION{
            public Simulation create(List<String> dataValues, List<Cell> cells){
                return new PercolationSimulation(dataValues, cells);
            }
        },
        WATORWORLD{
            public Simulation create(List<String> dataValues, List<Cell> cells){
                return new WatorWorldSimulation(dataValues, cells);
            }
        },
        SEGREGATION{
            public Simulation create(List<String> dataValues, List<Cell> cells){
                return new SegregationSimulation(dataValues, cells);
            }
        };
        abstract Simulation create(List<String> dataValues, List<Cell> cells);
    }

    public enum CellType{
        AGENT{
            public Cell create(List<String> parameters){
                return new AgentCell(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), parameters.get(2));
            }
        },
        EMPTY{
            public Cell create(List<String> parameters){
                return new EmptyCell(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)));
            }
        }/*,
        FISH{
            public Cell create(List<String> parameters){
                return new FishCell(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)));
            }
        },
        SHARK{
            public Cell create(List<String> parameters){
                return new FishCell(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)));
            }
        }*/;
        abstract Cell create(List<String> parameters);
    }
    /**
     * Create a parser for XML files of given type.
     */
    public XMLParser (String type) {
        DOCUMENT_BUILDER = getDocumentBuilder();
        TYPE_ATTRIBUTE = type;
    }

    public Simulation getSimulation(File dataFile){
        Element root = getRootElement(dataFile);
        Element settings = (Element) root.getElementsByTagName("settings").item(0);
        ArrayList <String> dataValues = getDataValues(settings, SegregationSimulation.DATA_FIELDS);
        Element grid = (Element) root.getElementsByTagName("grid").item(0);
        List<Cell> cells = getCells(grid, Integer.parseInt(dataValues.get(2)), Integer.parseInt(dataValues.get(3))); //
        return selectSimType(root.getAttribute(TYPE_ATTRIBUTE), dataValues, cells);
    }

    private ArrayList<Cell> getCells(Element grid, int rows, int columns){
        ArrayList<Cell> cells = new ArrayList<>();
        for(int i = 0; i < rows; i++){
            Element row = (Element) grid.getElementsByTagName("row").item(i);
            for(int j = 0; j < columns; j++){
                String cellType = row.getElementsByTagName("Cell").item(j).getTextContent();
                if(cellType.equals("EMPTY")){
                    cells.add(new EmptyCell(i, j));
                }
                else {
                    cells.add(new AgentCell(i, j, cellType));
                }
            }
        }
        return cells;
    }

    private ArrayList<String> getDataValues(Element settings, List<String> dataFields){
        ArrayList<String> dataValues = new ArrayList<>();
        for(String field : dataFields){
            dataValues.add(getTextValue(settings, field));
        }
        return dataValues;
    }

    // Get root element of an XML file
    private Element getRootElement (File xmlFile) {
        try {
            DOCUMENT_BUILDER.reset();
            var xmlDocument = DOCUMENT_BUILDER.parse(xmlFile);
            return xmlDocument.getDocumentElement();
        }
        catch (SAXException | IOException e) {
            throw new XMLException(e);
        }
    }

    // Returns if this is a valid XML file for the specified object type
    private boolean isValidFile (Element root, String type) {
        return getAttribute(root, TYPE_ATTRIBUTE).equals(type);
    }

    // Get value of Element's attribute
    private String getAttribute (Element e, String attributeName) {
        return e.getAttribute(attributeName);
    }

    // Get value of Element's text
    private String getTextValue (Element e, String tagName) {
        var nodeList = e.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        else {
            // FIXME: empty string or null, is it an error to not find the text value?
            return "";
        }
    }

    // Boilerplate code needed to make a documentBuilder
    private DocumentBuilder getDocumentBuilder () {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            throw new XMLException(e);
        }
    }

    private Simulation selectSimType(String simType, List<String> dataValues, List<Cell> cells){
        switch (simType) {
            case SegregationSimulation.DATA_TYPE :
                return SimulationType.SEGREGATION.create(dataValues, cells);
            case WatorWorldSimulation.DATA_TYPE :
                return SimulationType.WATORWORLD.create(dataValues, cells);
            case PercolationSimulation.DATA_TYPE :
                return SimulationType.PERCOLATION.create(dataValues, cells);
            case SpreadingFireSimulation.DATA_TYPE :
                return SimulationType.SPREADINGFIRE.create(dataValues, cells);
            case GameOfLifeSimulation.DATA_TYPE :
                return SimulationType.GAMEOFLIFE.create(dataValues, cells);
        }
        throw new XMLException(ERROR_MESSAGE, "any kind of Simulation");
    }

    private Cell selectCellType(String cell, List<String> parameters){
        switch (simType) {
            case AgentCell.DATA_TYPE :
                return CellType.AGENT.create(parameters);
        }
        throw new XMLException(ERROR_MESSAGE, "any kind of Simulation");
    }
}
