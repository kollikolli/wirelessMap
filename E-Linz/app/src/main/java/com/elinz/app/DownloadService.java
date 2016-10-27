package com.elinz.app;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.elinz.dataHandling.FileTable;
import com.elinz.dataHandling.JSONParser;
import com.elinz.dataHandling.PersistService;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @class DownloadService LinzAG
 * @brief Laden der LinzAG Open Data
 * @author Thomas
 */
public class DownloadService extends IntentService {

    private ArrayList<EStation> eStations;
    protected static final String EVENT = "DownloadService_Event";

    public DownloadService() {
        super("DownloadService");
        this.eStations = new ArrayList<EStation>();
    }
    @Override
    /**
     * @brief Starten des Services mit beliebigen Intent, wenn Device online werden Daten geladen
     * @param Intent intent
     */
    protected void onHandleIntent(Intent intent) {
        if(isOnline(this)){
            loadEstations();
            Log.i(STORAGE_SERVICE,"We are online!");
        }else{
            publishResults(MapActivity.RESULT_OK -1);
        }
    }

    /**
     * @brief Laden der LinzAG Daten aus .gml Datei
     */
    private void loadEstations() {
        int result = MapActivity.RESULT_OK -1;
        try {
            System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
            // XMLReader erzeugen
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();

            // Pfad zur XML Datei
            InputStream in = new java.net.URL("http://data.linz.gv.at/katalog/linz_ag/linz_ag_strom/stromtankstellen/Stromtankstellen.gml").openStream();
            //FileReader reader = new FileReader("C:\\estation\\Stromtankstellen.gml");
            InputSource inputSource = new InputSource(in);

            // DTD kann optional übergeben werden
            // inputSource.setSystemId("X:\\personen.dtd");
            OpenDataLinz2EStations data = new OpenDataLinz2EStations();

            // PersonenContentHandler wird übergeben
            xmlReader.setContentHandler(data);

            // Parsen wird gestartet
            xmlReader.parse(inputSource);

            eStations.addAll(data.getEstations());

            in.close();

            saveFile();

            result++;

            publishResults(result);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }


    }

    /**
     * @brief Kontaktiert angemeldete BroadcastReceiver nach beenden des Services
     * @param result
     */
    private void publishResults(int result) {
        Intent intent = new Intent(EVENT);
        intent.putExtra("source", MapActivity.DS);
        intent.putExtra("result", result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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

    /**
     * @brief Persistiert die geladen Daten in JSON Files
     */
    private void saveFile(){
        String json = JSONParser.getInstance().getStationsToJSON(eStations);
        Intent intent = new Intent(getApplicationContext(), PersistService.class);
        intent.putExtra("file", FileTable.STATIONS);
        intent.putExtra("json", json);
        Log.i(STORAGE_SERVICE, json);
        startService(intent);
    }

    /**
     * @brief Prüft über ConnectivityManager ob Device online ist
     * @param con
     * @return true or false
     */

    public boolean isOnline(Context con) {
        ConnectivityManager cm =
                (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}
