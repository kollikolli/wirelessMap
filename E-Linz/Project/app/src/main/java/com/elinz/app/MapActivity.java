package com.elinz.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class MapActivity extends ActionBarActivity {


    private GoogleMap googleMap;
    private ArrayList<EStation> eStations = new ArrayList<EStation>();
    private boolean internalData1;
    private boolean internalData2;


    private BroadcastReceiver receiverDS = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int resultCode = bundle.getInt(DownloadService.RESULT);
                if (resultCode == RESULT_OK) {
                    Toast.makeText(MapActivity.this,
                            "Download Linz AG complete",
                            Toast.LENGTH_LONG).show();

                    try {
                        readJsonStream(openFileInput("eStations.json"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Length: " + eStations.size());

                    for(EStation e:eStations){
                        Location l = new Location(e.getName());
                        l.setLatitude(e.getLatitude());
                        l.setLongitude(e.getLongitude());
                        drawMarker(l,e.getName(),"Lat: "+e.getLatitude()+" ,Lon: "+e.getLongitude());
                    }

                } else {
                    Toast.makeText(MapActivity.this, "Download failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    private BroadcastReceiver receiverDSOSM = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();


            if (bundle != null) {
                int resultCode = bundle.getInt(DownloadServiceOSM.RESULT);
                if (resultCode == RESULT_OK) {
                    Toast.makeText(MapActivity.this,
                            "Download from OSM complete. ",
                            Toast.LENGTH_LONG).show();

                    try {
                        readJsonStream(openFileInput("eStationsOSM.json"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Length: " + eStations.size());

                    for(EStation e:eStations){
                        Location l = new Location(e.getName());
                        l.setLatitude(e.getLatitude());
                        l.setLongitude(e.getLongitude());
                        drawMarker(l,e.getName(),"Lat: "+e.getLatitude()+" ,Lon: "+e.getLongitude());
                    }

                } else {
                    Toast.makeText(MapActivity.this, "Download OSM failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deleteFile("eStations.json");
        deleteFile("eStationsOSM.json");

        if(fileExistance("eStations.json")){
            /*CHECK VERSION AND IF OLD LOAD LINZ AG DATA ELSE LOAD FROM FILE*/
            try {



                System.out.println("*************************************");
                System.out.println("***read LinzAG Data from Internal Storage***");
                System.out.println("*************************************");
                readJsonStream(openFileInput("eStations.json"));
                internalData1=true;
                System.out.println("Done");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            Intent intent = new Intent(this, DownloadService.class);
            startService(intent);
            System.out.println("*************************************");
            System.out.println("***Started downloading LinzAG Data***");
            System.out.println("*************************************");
        }

        if(fileExistance("eStationsOSM.json")){
            /*CHECK VERSION AND IF OLD LOAD LINZ AG DATA ELSE LOAD FROM FILE*/
            try {
                System.out.println("*************************************");
                System.out.println("***read OSM Data from Internal Storage***");
                System.out.println("*************************************");
                readJsonStream(openFileInput("eStationsOSM.json"));
                System.out.println("Done");
                internalData2=true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            Intent intent = new Intent(this, DownloadServiceOSM.class);
            startService(intent);
            System.out.println("*************************************");
            System.out.println("***Started downloading OSM***");
            System.out.println("*************************************");
        }

        try {
            // Loading map
            initializeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }


        if(internalData1&&internalData2) {
            System.out.println("Length: " + eStations.size());

            for (EStation e : eStations) {
                Location l = new Location(e.getName());
                l.setLatitude(e.getLatitude());
                l.setLongitude(e.getLongitude());
                drawMarker(l, e.getName(), "Lat: " + e.getLatitude() + " ,Lon: " + e.getLongitude());
            }
        }

    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();


            LatLng cameraLatLng = new LatLng(48.306297, 14.286209);
            float cameraZoom = 10;
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraLatLng, cameraZoom));


            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeMap();
        registerReceiver(receiverDS, new IntentFilter(DownloadService.NOTIFICATION));
        registerReceiver(receiverDSOSM, new IntentFilter(DownloadServiceOSM.NOTIFICATION));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiverDS);
        unregisterReceiver(receiverDSOSM);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void drawMarker(Location location, String title, String distance){
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(title);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        marker.snippet(distance);
        googleMap.addMarker(marker);

    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_map, container, false);
            return rootView;
        }
    }

        public void readJsonStream(InputStream in) throws IOException {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            try {
                readEStationsArray(reader);
            } finally {
                    reader.close();
                }
            }

        public void readEStationsArray(JsonReader reader) throws IOException {
            reader.beginArray();
            while (reader.hasNext()) {
                eStations.add(readEStations(reader));
            }
            reader.endArray();

        }

        public EStation readEStations(JsonReader reader) throws IOException {

            String id="";
            double longitude=0.00;
            double latitude=0.00;
            String ename ="";
            String type="";

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("id")) {
                    id = reader.nextString();
                } else if (name.equals("name")) {
                    ename = reader.nextString();
                } else if (name.equals("longitude")) {
                    longitude = reader.nextDouble();
                } else if (name.equals("latitude")) {
                    latitude = reader.nextDouble();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();

            return new EStation(id, ename, longitude, latitude);
        }

    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }


    public void fileDelete(String name){
        getBaseContext().deleteFile(name);
    }






}
