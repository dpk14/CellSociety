package mainpackage;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
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

    /**
     * Create a parser for XML files of given type.
     */
    public XMLParser (String type) {
        DOCUMENT_BUILDER = getDocumentBuilder();
        TYPE_ATTRIBUTE = type;
    }

    public Simulation getSimulation(File dataFile){
        Element root = getRootElement(dataFile);
        if (! isValidFile(root, SegregationSimulation.DATA_TYPE)) {
            throw new XMLException(ERROR_MESSAGE, SegregationSimulation.DATA_TYPE);
        }
        Element settings = (Element) root.getElementsByTagName("settings").item(0);
        ArrayList <String> dataValues = getDataValues(settings, SegregationSimulation.DATA_FIELDS);
        Element grid = (Element) root.getElementsByTagName("grid").item(0);
        List<Cell> cells = getCells(grid, Integer.parseInt(dataValues.get(2)), Integer.parseInt(dataValues.get(3))); //
        return new SegregationSimulation(dataValues, cells);
    }

    private ArrayList<Cell> getCells(Element grid, int rows, int columns){
        ArrayList<Cell> cells = new ArrayList<>();
        for(int i = 0; i < rows; i++){
            Element row = (Element) grid.getElementsByTagName("row").item(i);
            for(int j = 0; j < columns; j++){
                String cellType = grid.getElementsByTagName("column").item(j).getTextContent();
                cells.add(new AgentCell(i, j, cellType));
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

}
