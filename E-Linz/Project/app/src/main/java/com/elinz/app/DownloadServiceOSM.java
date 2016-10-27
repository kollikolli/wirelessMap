package com.elinz.app;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Tom on 19.05.2014.
 */
public class DownloadServiceOSM extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String FILENAME = "eStationsOSM.json";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "OSM";
    private ArrayList<EStation> eStations;

    public DownloadServiceOSM() {
        super("DownloadServiceOSM");
        this.eStations = new ArrayList<EStation>();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        loadEstations();
    }

    void loadEstations() {
        try {
            System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
            // XMLReader erzeugen
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();

            // URL zur XML Datei
            InputStream in = new java.net.URL("http://www.overpass-api.de/api/xapi?node[amenity=charging_station][bbox=12.7002,47.38719,15.04028,48.85387]").openStream();
            InputSource inputSource = new InputSource(in);

            OSM2EStations data = new OSM2EStations();
            xmlReader.setContentHandler(data);
            xmlReader.parse(inputSource);

            eStations.addAll(data.getEstations());
            in.close();


            System.out.println("*******************");
            System.out.println("*******************");
            System.out.println("OSM Data, Size: " + eStations.size());
            System.out.println("*******************");
            System.out.println("*******************");

            saveFile();
            publishResults(FILENAME, result);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }

    private void publishResults(String filename, int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILENAME, filename);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }

    public static void printConvEStations(ArrayList<EStation> eStations){
        System.out.println("Anzahl "+eStations.size());
        for(EStation e: eStations){
            System.out.println("ID: "+e.getId());
            System.out.println("Name: "+e.getName());
            System.out.println("Lat: "+e.getLatitude()+", Lon: "+e.getLongitude());
            System.out.println();
        }
    }


    private void saveFile(){
        FileOutputStream fos = null;
        try {
            fos = this.getApplicationContext().openFileOutput(FILENAME, Context.MODE_PRIVATE);
                try {
                    String start = "[";
                    String comma = ",";
                    fos.write(start.getBytes("utf-8"));

                    for(EStation e : eStations){
                       // String start = "{"+'\"'+"EStation"+'\"'+": ";
                        fos.write(e.getJSON().toString().getBytes("utf-8"));

                        if(!(eStations.indexOf(e)==(eStations.size())-1)){
                         fos.write(comma.getBytes());
                        }


                    }
                    String end = "]";
                    fos.write(end.getBytes("utf-8"));
                    fos.close();
                    result = Activity.RESULT_OK;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
