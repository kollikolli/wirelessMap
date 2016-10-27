package com.elinz.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * @class OSM2EStations
 * @brief Mapping Klasse für OpenStreetMap
 * @author Thomas
 */
public class OSM2EStations implements ContentHandler{

    private ArrayList<EStation> eStations = new ArrayList<EStation>();
    private String currentValue;
    private EStation e;
    private HashMap<SocketType,Integer> socketTypes = new HashMap<SocketType,Integer>();
    private ArrayList<StationType> stationTypes = new ArrayList<StationType>();

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        currentValue = new String(ch, start, length);
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes atts) throws SAXException {
        if (localName.equals("node")) {
            // Neue Person erzeugen
            e = new EStation();
            e.setId(atts.getValue("id"));
            e.setLatFromString(atts.getValue("lat"));
            e.setLonFromString(atts.getValue("lon"));
        }

        if (localName.equals("tag")) {

            if(atts.getValue("k").equals("name")){
                e.setName(atts.getValue("v"));
            }else if(atts.getValue("k").equals("socket:schuko")){
                socketTypes.put(SocketType.SCHUKO,Integer.parseInt(atts.getValue("v")));
            }else if(atts.getValue("k").equals("socket:cee_blue")){
                socketTypes.put(SocketType.CEE_BLUE,Integer.parseInt(atts.getValue("v")));
            }else if(atts.getValue("k").equals("socket:cee_red_16a")){
                socketTypes.put(SocketType.CEE_RED_16A,Integer.parseInt(atts.getValue("v")));
            }else if(atts.getValue("k").equals("socket:cee_red_32a")){
                socketTypes.put(SocketType.CEE_RED_32A,Integer.parseInt(atts.getValue("v")));
            }else if(atts.getValue("k").equals("socket:cee_red_64a")){
                socketTypes.put(SocketType.CEE_RED_64A,Integer.parseInt(atts.getValue("v")));
            }else if(atts.getValue("k").equals("socket:cee_red_125a")){
                socketTypes.put(SocketType.CEE_RED_125A,Integer.parseInt(atts.getValue("v")));
            }else if(atts.getValue("k").equals("socket:type1")){
                socketTypes.put(SocketType.TYPE1,Integer.parseInt(atts.getValue("v")));
            }else if(atts.getValue("k").equals("socket:type2")){
                socketTypes.put(SocketType.TYPE2,Integer.parseInt(atts.getValue("v")));
            }else if(atts.getValue("k").equals("socket:type3")){
                socketTypes.put(SocketType.TYPE3,Integer.parseInt(atts.getValue("v")));
            }else if(atts.getValue("k").equals("socket:chademo")){
                socketTypes.put(SocketType.CHADEMO,Integer.parseInt(atts.getValue("v")));
            }else if(atts.getValue("k").equals("socket:scame")){
                socketTypes.put(SocketType.SCAME,Integer.parseInt(atts.getValue("v")));
            }else if(atts.getValue("k").equals("car")&&atts.getValue("v").equals("yes")){
                stationTypes.add(StationType.CAR);
            }else if(atts.getValue("k").equals("bicycle")&&atts.getValue("v").equals("yes")){
                stationTypes.add(StationType.BICYCLE);
            }else if(atts.getValue("k").equals("scooter")&&atts.getValue("v").equals("yes")){
                stationTypes.add(StationType.SCOOTER);
            }else if(atts.getValue("k").equals("truck")&&atts.getValue("v").equals("yes")){
                stationTypes.add(StationType.TRUCK);
            }
        }
    }


    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (localName.equals("node")) {
            if(e.getId()!=""){
                e.setSocketTypes((HashMap<SocketType,Integer>)socketTypes.clone());
                e.setType((ArrayList<StationType>) stationTypes.clone());
                eStations.add(e);
                socketTypes.clear();
                stationTypes.clear();
            }
        }
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void endPrefixMapping(String arg0) throws SAXException {
    }

    @Override
    public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
            throws SAXException {
    }

    @Override
    public void processingInstruction(String arg0, String arg1)
            throws SAXException {
    }

    @Override
    public void setDocumentLocator(Locator arg0) {
    }

    @Override
    public void skippedEntity(String arg0) throws SAXException {
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void startPrefixMapping(String arg0, String arg1)
            throws SAXException {
    }

    public ArrayList<EStation> getEstations() {
        return eStations;
    }
}
