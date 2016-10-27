package com.elinz.app;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;


public class OSM2EStations implements ContentHandler{

    private ArrayList<EStation> eStations = new ArrayList<EStation>();
    private String currentValue;
    private EStation e;

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
            }

        }



    }

    // Methode wird aufgerufen wenn der Parser zu einem End-Tag kommt
    public void endElement(String uri, String localName, String qName)
            throws SAXException {


        if (localName.equals("node")) {

            if(e.getId()!=""){
                eStations.add(e);
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
