package mainpackage;

import cells.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import simulations.*;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLParser {
    public static final String ERROR_MESSAGE = "XML file does not represent %s";
    public static final String CELL_ERROR_MESSAGE = "There is no such thing as a '%s/' cell type";
    private final String TYPE_ATTRIBUTE;
    private final DocumentBuilder DOCUMENT_BUILDER;


    /**
     * Create a parser for XML files of given type. Will be called by RunSimulation in order to read in selected XML
     * file. The type passed in will be "simType".
     * @param type - data type of object you want to read in from XML file
     */
    public XMLParser (String type) {
        DOCUMENT_BUILDER = getDocumentBuilder();
        TYPE_ATTRIBUTE = type;
    }

    /**
     * Given a file, returns a simulation that corresponds to the XML file. This will be called from RunSimulation in
     * order to begin a simulation
     * @param dataFile - data file to be read
     * @return
     */
    public Simulation getSimulation(File dataFile){
        Element root = getRootElement(dataFile);
        Element settings = (Element) root.getElementsByTagName("settings").item(0);
        Element grid = (Element) root.getElementsByTagName("grid").item(0);
        return createSimulation(root.getAttribute(TYPE_ATTRIBUTE), settings, grid);
    }

    private Simulation createSimulation(String simType, Element settings, Element grid) {
        Map<String, String> dataValues = extractSimParameters(settings);
        // TEMPORARY, FOR TESTING RANDOM-CONFIG XML FILES WITH SEGREGATION
        if(simType.equals(SegregationSimulation.DATA_TYPE)) return new SegregationSimulation(dataValues);;
        List<Cell> cells;
        cells = getCells(grid, Integer.parseInt(dataValues.get("rows")), Integer.parseInt(dataValues.get("columns")));
        switch (simType) {
            case SegregationSimulation.DATA_TYPE:
                return new SegregationSimulation(dataValues, cells);
            case WatorWorldSimulation.DATA_TYPE:
                return new WatorWorldSimulation(dataValues, cells);
            case PercolationSimulation.DATA_TYPE:
                return new PercolationSimulation(dataValues, cells);
            case SpreadingFireSimulation.DATA_TYPE:
                return new SpreadingFireSimulation(dataValues, cells);
            case GameOfLifeSimulation.DATA_TYPE:
                return new GameOfLifeSimulation(dataValues, cells);
        }
        throw new XMLException(ERROR_MESSAGE, "any kind of Simulation");
    }

    public Map<String, String> extractSimParameters(Node settings){
        HashMap<String, String> dataValues = new HashMap<>();
        NodeList nodeList = settings.getChildNodes();
        for(int k = 0; k < nodeList.getLength(); k++){
            if (nodeList.item(k).getNodeType() == Node.ELEMENT_NODE) {
                dataValues.put(nodeList.item(k).getNodeName(), nodeList.item(k).getTextContent());
            }
        }
        return dataValues;
    }

    private ArrayList<Cell> getCells(Element grid, int rows, int columns){
        ArrayList<Cell> cells = new ArrayList<>();
        for(int i = 0; i < rows; i++){
            Element row = (Element) grid.getElementsByTagName("row").item(i);
            for(int j = 0; j < columns; j++){
                Element currentCell = (Element)row.getElementsByTagName("Cell").item(j);
                String cellType = currentCell.getAttribute("cellType");
                cells.add(createCell(i, j, cellType, currentCell));
            }
        }
        return cells;
    }

    private Cell createCell(int row, int col, String cellType,  Element root){
        ArrayList<String> dataValues = new ArrayList<>();
        dataValues.add(Integer.toString(row));
        dataValues.add(Integer.toString(col));
        dataValues.addAll(extractCellParameters(root));
        switch (cellType) {
            case AgentCell.DATA_TYPE :
                return new AgentCell(dataValues);
            case EmptyCell.DATA_TYPE :
                return new EmptyCell(dataValues);
            case FishCell.DATA_TYPE :
                return new FishCell(dataValues);
            case SharkCell.DATA_TYPE :
                return new SharkCell(dataValues);
            case StateChangeCell.DATA_TYPE :
                return new StateChangeCell(dataValues);
        }
        throw new XMLException(CELL_ERROR_MESSAGE, cellType);
    }

    private List<String> extractCellParameters(Element grid){
        List<String> cellValues = new ArrayList<>();
        NodeList nodeList = grid.getChildNodes();
        for(int k = 0; k < nodeList.getLength(); k++){
            if (nodeList.item(k).getNodeType() == Node.ELEMENT_NODE) {
                cellValues.add(nodeList.item(k).getTextContent());
            }
        }
        return cellValues;
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
}
