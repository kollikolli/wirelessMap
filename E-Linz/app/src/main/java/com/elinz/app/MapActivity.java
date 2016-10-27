package com.elinz.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.location.Location;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.elinz.dataHandling.FileTable;
import com.elinz.dataHandling.ImportController;
import com.elinz.dataHandling.StationController;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.Map;

/**
 * @class MapActivity
 * @brief Initialisiert Google Map mit E-Stations als Map-Marker
 * @author Thomas
 * @author Sebastian
 * @author Harald
 *
 * @todo DownloadServices nur einmal ausführen, MapMarker Snippet bearbeiten (weiterleiten auf andere Activity)
 */


public class MapActivity extends ActionBarActivity {

    private GoogleMap googleMap;
    private ArrayList<EStation> eStations;

    protected static final int RESULT_OK = 1;
    protected static final int NO_RESULT = -1;
    protected static final String DS = "DownloadService";
    protected static final String DS_OSM = "DownloadServiceOSM";
    private StationController stationController;


    /**
     *
     * @class BroadCastReceiver LinzAG
     * @brief Dieser BroadcastReceiver wird aufgerufen wenn der DownloadService der LinzAG Daten fertig ist
     *
     */

    private BroadcastReceiver receiverDS = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String source = bundle.getString("source");
                int resultCode = bundle.getInt("result");

                if (source.equals(DS) && resultCode == RESULT_OK) {

                    Toast.makeText(MapActivity.this,
                            "Download complete. ",
                            Toast.LENGTH_LONG).show();

                    eStations = stationController.getInstance(getBaseContext(),FileTable.STATIONS).getStations();
                    Log.i(STORAGE_SERVICE, "Num Of EStations"+eStations.size());

                    for (EStation e : eStations) {
                        Location l = new Location(e.getName());
                        l.setLatitude(e.getLatitude());
                        l.setLongitude(e.getLongitude());
                        drawMarker(l, e.getName(), e.getId());
                    }
                }


            } else {
                if(eStations.size()==0){
                    Toast.makeText(MapActivity.this, "Download failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    /**
     *
     * @class BroadCastReceiver OpenStreetMap
     * @brief Dieser BroadcastReceiver wird aufgerufen wenn der DownloadService der OpenStreetMap Daten fertig ist
     *
     */

    private BroadcastReceiver receiverDSOSM = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {

                String source = bundle.getString("source");
                int resultCode = bundle.getInt("result");

                if (source.equals(DS_OSM) && resultCode == RESULT_OK) {
                    Toast.makeText(MapActivity.this,
                            "Download from OSM complete. ",
                            Toast.LENGTH_LONG).show();

                    eStations = stationController.getInstance(getBaseContext(),FileTable.STATIONS_OSM).getStations();

                    Log.i(STORAGE_SERVICE, "Num Of EStations"+eStations.size());

                    for(EStation e:eStations){
                        Location l = new Location(e.getName());
                        l.setLatitude(e.getLatitude());
                        l.setLongitude(e.getLongitude());
                        drawMarker(l,e.getName(),e.getId());
                    }



                } else {
                    if(eStations.size()==0){
                        Toast.makeText(MapActivity.this, "Download OSM failed",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       try {
            // Loading map
            initializeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

        startDownloadServices();
        eStations = new ArrayList<EStation>();

        if(ImportController.getInstance().checkFileExistance(this,FileTable.STATIONS_OSM)){
            eStations = stationController.getInstance(this,FileTable.STATIONS_OSM).getStations();
        }


        if((ImportController.getInstance().checkFileExistance(this,FileTable.STATIONS))){
            eStations = stationController.getInstance(this,FileTable.STATIONS).getStations();
            System.out.println("Baby Here");
        }


        if(eStations.size()>0) {
            Log.i(STORAGE_SERVICE, "Draw From File"+eStations.size());
            for (EStation e : eStations) {
                Log.i(STORAGE_SERVICE, e.toString());
                Location l = new Location(e.getName());
                l.setLatitude(e.getLatitude());
                l.setLongitude(e.getLongitude());
                drawMarker(l, e.getName(), e.getId());
            }
        }

    }


    /**
     * @brief intialiseren der Google Map
     *
     * */
    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            LatLng cameraLatLng = new LatLng(48.306297, 14.286209);
            float cameraZoom = 1000;
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraLatLng, cameraZoom));
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                // Defines the contents of the InfoWindow
                @Override
                public View getInfoContents(Marker arg0) {

                    final EStation e = getStationById(arg0.getSnippet());

                    // Getting view from the layout file info_window_layout
                    View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);

                    TextView title = (TextView) v.findViewById(R.id.title);
                    title.setText(e.getName());

                    TextView socketTypesView = (TextView) v.findViewById(R.id.socketTypes);
                    String socketTypes="";
                    if(e.getSocketTypes()==null||e.getSocketTypes().size()==0){
                        socketTypes=("- keine Angaben -");
                    }else {
                        String s ="";
                        for (Map.Entry st : e.getSocketTypes().entrySet()) {
                            //last element without comma
                                socketTypes = socketTypes +st.getValue()+"x "+st.getKey()+"\n";
                        }
                    }
                    socketTypes =socketTypes.substring(0,socketTypes.length()-1);

                    socketTypesView.setText(socketTypes);

                    TextView typesView = (TextView) v.findViewById(R.id.types);
                    String types ="";
                    if(e.getType()==null||e.getType().size()==0){
                        types=("- keine Angaben -");
                    }else {
                        String s ="";
                        for (int i=0;i<e.getType().size();i++) {
                            //last element without comma
                            if(i==(e.getType().size()-1)){
                                types = types + e.getType().get(i);
                            }else{
                                types = types + e.getType().get(i)+", ";
                            }
                        }
                    }
                    typesView.setText(types);

                    Button button = (Button) v.findViewById(R.id.tankButton);

                    // Returning the view containing InfoWindow contents
                    return v;

                }
            });

            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                public void onInfoWindowClick(Marker marker) {
                            Toast.makeText(MapActivity.this,
                            "Tanken an der E-Station mit der ID: "+marker.getSnippet(),
                            Toast.LENGTH_LONG).show();



                }
            });

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private EStation getStationById(String id){
        for(EStation e: eStations){
            if(e.getId().equals(id)){return e;}
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        initializeMap();

        LocalBroadcastManager.getInstance(this).registerReceiver(receiverDS, new IntentFilter(DownloadService.EVENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverDSOSM, new IntentFilter(DownloadServiceOSM.EVENT));

    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregister Broadcast-Receivers
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverDS);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverDSOSM);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myMenuInflater = getMenuInflater();
        myMenuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case(R.id.menu_list):
                startActivity(new Intent(MapActivity.this, AnotherActivity.class));
                break;
            case(R.id.menu_tanken):
                startActivity(new Intent(MapActivity.this, ETankenActivity.class));
                break;
            case(R.id.menu_statistik):
                startActivity(new Intent(MapActivity.this, StatisticsActivity.class));
                break;
            case(R.id.menu_database):
                startActivity(new Intent(MapActivity.this, DatabaseActivity.class));
                break;
        }
        return true;
    }


    /**
     *
     * @brief Zeichnen der Marker an Location auf der Map
     *
     * @param location
     * @param title
     * @param id
     */
    private void drawMarker(Location location, String title, String id){
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(title);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        marker.snippet(id);
        googleMap.addMarker(marker);

    }



    /**
     * @brief Methode zum Starten der Download Services
     */
    private void startDownloadServices() {

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        intent = new Intent(this, DownloadServiceOSM.class);
        startService(intent);
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
}
