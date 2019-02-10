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
import java.util.*;

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
        return createSimulation(root.getAttribute(TYPE_ATTRIBUTE),  root);
    }

    private Simulation createSimulation(String simType, Element root) {
        Element settings = (Element) root.getElementsByTagName("settings").item(0);
        Map<String, String> dataValues = extractParameters(settings);
        switch(settings.getElementsByTagName("generatorType").item(0).getTextContent()){
            case "probability" : case "quota" :
                return Simulation.createNewSimulation(simType, dataValues);
            default:
                Element grid = (Element) root.getElementsByTagName("grid").item(0);
                List<Cell> cells = getCells(grid, Integer.parseInt(dataValues.get("rows")), Integer.parseInt(dataValues.get("columns")));
                return Simulation.createNewSimulation(simType, dataValues, cells);
        }

    }

    private Map<String, String> extractParameters(Node settings){
        HashMap<String, String> dataValues = new LinkedHashMap<>();
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
        Map<String, String> dataValues = new HashMap<>();
        dataValues = extractParameters(root);
        dataValues.put("row", Integer.toString(row));
        dataValues.put("column", Integer.toString(col));
        return Cell.createNewCell(cellType, dataValues);
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
