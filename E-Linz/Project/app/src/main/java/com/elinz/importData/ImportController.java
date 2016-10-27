package com.elinz.dataHandling;

import android.content.Context;
import android.net.ConnectivityManager;

import com.elinz.app.EStation;
import com.elinz.app.StationType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.LinkedList;


import java.io.InputStream;
import java.net.URL;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xml.Parser;
import org.opengis.feature.simple.SimpleFeature;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by sebastian on 01.04.14.
 */
public class ImportController {

    public static final String FILENAME = "stations.json";
    private final String GMLURL = "http://data.linz.gv.at/katalog/linz_ag/linz_ag_strom/stromtankstellen/Stromtankstellen.gml";

    private static ImportController ourInstance = new ImportController();

    public static ImportController getInstance() {
        return ourInstance;
    }

    private ImportController() {
    }

    public LinkedList<EStation> importStations(Context con) throws ParserConfigurationException, SAXException, IOException {
        if (isOnline(con))
            return readFromInternet();
        else
            return readFromStorage(con);
    }

    public LinkedList<EStation> readFromInternet() throws IOException, ParserConfigurationException, SAXException {

        final InputStream in = new URL(GMLURL).openStream();

        GMLConfiguration gml = new GMLConfiguration();
        Parser parser = new Parser(gml);
        parser.setStrict(false);

        FeatureCollection<?, ?> features = (FeatureCollection<?, ?>) parser.parse(in);
        FeatureIterator<?> i = features.features();

        int nfeatures = 0;
        while( i.hasNext() ) {
            SimpleFeature f = (SimpleFeature) i.next();

            System.out.println(f.getAttribute("NAME"));
            System.out.println(f.getBounds().getMaxX());
            System.out.println(f.getBounds().getMaxY());

            nfeatures++;
        }

        System.out.println("Number of features: " + nfeatures);

        return null;

    }

    public LinkedList<EStation> readFromStorage(Context con) {
        String json = "";
        try {
            FileInputStream fis;
            fis = con.openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            json = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonToStations(json);
    }

    private LinkedList<EStation> jsonToStations(String json) {
        LinkedList<EStation> list = new LinkedList<EStation>();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<LinkedList<EStation>>(){}.getType();
        list = gson.fromJson(json, collectionType);

        return list;
    }

    public boolean isOnline(Context con) {
        ConnectivityManager cm =
                (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void persistStations(LinkedList<EStation> stations, Context con) throws JSONException {
        try {
            JSONObject persistObj = new JSONObject();
            JSONArray list = new JSONArray();

            for(EStation stat : stations) {
                JSONObject obj = new JSONObject();
                obj.put("name", stat.getName());
                obj.put("lon", stat.getLongitude());
                obj.put("lat", stat.getLatitude());

                JSONArray types = new JSONArray();
                for(StationType type : stat.getType()) {
                    types.put(type.toString());
                }

                obj.put("type", types);
                list.put(obj);
            }

            persistObj.put("stations", list);

            FileOutputStream fos;
            fos = con.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(persistObj.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
