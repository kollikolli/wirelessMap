package at.jku.pervasive.wirelessmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import at.jku.pervasive.wirelessmap.data.DbHandler;
import at.jku.pervasive.wirelessmap.model.Bluetooth;
import at.jku.pervasive.wirelessmap.model.Cell;
import at.jku.pervasive.wirelessmap.model.WMOptions;
import at.jku.pervasive.wirelessmap.model.Wifi;
import at.jku.pervasive.wirelessmap.service.BluetoothService;
import at.jku.pervasive.wirelessmap.service.CellService;
import at.jku.pervasive.wirelessmap.service.ConnectService;
import at.jku.pervasive.wirelessmap.service.GpsService;
import at.jku.pervasive.wirelessmap.service.WifiService;

public class WirelessMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static boolean firstRun = true;
    Context mCtx = this;
    private boolean btfilter  = true, wififilter = true, cellfilter = true;
    private LatLng prev = new LatLng(1,1);
    public static final LatLng jku = new LatLng(48.335262, 14.324431);

    private WifiService wifi;
    private GpsService gps;

    Intent bluetoothServiceIntent;
    Intent cellServiceIntent;
    Intent wifiServiceIntent;
    Intent connectionServiceIntent;

    Button cell;
    Button bt;
    Button wifiButton;
    Button export;

    //List<MarkerOptions> lkml = new ArrayList<MarkerOptions>();
    List<CircleOptions> lkml = new ArrayList<CircleOptions>();
    HashMap<String, String> meMap=new HashMap<String, String>();
    final int MARKER_UPDATE_INTERVAL = 60000; /* milliseconds */
    Handler handler = new Handler();



    public static final LatLng jku2 = new LatLng(48.3280601501465, 14.3236999511719);


    Runnable updateMarker = new Runnable() {
        @Override
        public void run() {
            //marker.remove();
            //marker = mMap.addMarker(new MarkerOptions().position(jku2).title("Current Location2"));
            //float cameraZoom = 19;
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jku2, cameraZoom));

            drawMarkers();
            handler.postDelayed(this, MARKER_UPDATE_INTERVAL);
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wireless_map);

        bt = (Button) findViewById(R.id.bt);
        cell = (Button) findViewById(R.id.cell);
        wifiButton = (Button) findViewById(R.id.wifi);
        export = (Button) findViewById(R.id.export);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click) {
                btfilter = !btfilter;
                bt.setText(btfilter ? "BT On" : "BT Off");
            }
        });
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click) {
                cellfilter = !cellfilter;
                cell.setText(cellfilter ? "Cell On" : "Cell Off");
            }
        });
        wifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click) {
                wififilter = !wififilter;
                wifiButton.setText(wififilter ? "Wifi On" : "Wifi Off");
            }
        });


        DbHandler.createInstance(this, null, null, 1);
        GpsService.createInstance(this);


        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click) {
                Toast.makeText(getApplicationContext(), "Downloaded Database", Toast.LENGTH_SHORT).show();
                DbHandler.getInstance().download();
            }
        });

        bluetoothServiceIntent = new Intent(this, BluetoothService.class);
        cellServiceIntent = new Intent(this, CellService.class);
        wifiServiceIntent = new Intent(this, WifiService.class);
        connectionServiceIntent = new Intent(this, ConnectService.class);
        startScannerServices();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        handler.postDelayed(updateMarker, MARKER_UPDATE_INTERVAL);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(updateMarker);
        super.onDestroy();
        stopScannerServices();
    }

    private void startScannerServices() {
        startService(bluetoothServiceIntent);
        startService(cellServiceIntent);
        startService(wifiServiceIntent);
        startService(connectionServiceIntent);
    }

    private void stopScannerServices() {
        stopService(bluetoothServiceIntent);
        stopService(cellServiceIntent);
        stopService(wifiServiceIntent);
        stopService(connectionServiceIntent);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(firstRun) {
            drawMarkers();
            mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
                @Override
                public void onCircleClick(Circle circle) {
                    if (meMap.keySet().contains(circle.getCenter().toString())) {
                        String value = (String) meMap.get(circle.getCenter().toString());
                        //Toast.makeText(mCtx, value, Toast.LENGTH_LONG).show();
                        final Toast toast = Toast.makeText(mCtx, value, Toast.LENGTH_SHORT);
                        toast.show();

                        new CountDownTimer(15000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                toast.show();
                            }

                            public void onFinish() {
                                toast.cancel();
                            }
                        }.start();
                    }
                }
            });
        }
        firstRun = false;




    }

    private void drawMarkers() {
        lkml.clear();
        List<WMOptions> wmos = new ArrayList<>();
        if (wififilter) {
            List<Wifi> wifis = DbHandler.getInstance().getWifis();
            for (int i = 0; i < wifis.size(); i++) {
                Wifi wifi = wifis.get(i);
                LatLng curr = DbHandler.getInstance().getLocationAtTime(wifi.get_scandate());
                if (curr.latitude != prev.latitude && curr.longitude != prev.longitude) {
                    wmos.add(DbHandler.getInstance().getWMORange(wifi.get_scandate(), wififilter, cellfilter, btfilter));
                }
                prev = curr;
            /*CircleOptions c = new CircleOptions()
                    .center(DbHandler.getInstance().getLocationAtTime(wifi.get_scandate()))
                    .radius(10)
                    .clickable(true)
                    .fillColor(Color.GREEN);
            //MarkerOptions m = new MarkerOptions().position(DbHandler.getInstance().getLocationAtTime(wifi.get_scandate())).title("DB: " + wifi.get_db() + " SSID: " + wifi.get_ssid() + " \nMAC: " + wifi.get_mac()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            //lkml.add(m);
            lkml.add(c);
            meMap.put(c.getCenter().toString(), "DB: " + wifi.get_db() + "\nSSID: " + wifi.get_ssid() + " \nMAC: " + wifi.get_mac());*/
            }
        }
        if (cellfilter) {
            List<Cell> cells = DbHandler.getInstance().getCells();
            prev = new LatLng(1, 1);
            for (int i = 0; i < cells.size(); i++) {
                Cell cell = cells.get(i);
                LatLng curr = DbHandler.getInstance().getLocationAtTime(cell.get_scandate());
                if (curr.latitude != prev.latitude && curr.longitude != prev.longitude) {
                    wmos.add(DbHandler.getInstance().getWMORange(cell.get_scandate(), wififilter, cellfilter, btfilter));
                }
                prev = curr;
            /*CircleOptions c = new CircleOptions()
                    .center(DbHandler.getInstance().getLocationAtTime(cell.get_scandate()))
                    .radius(100)
                    .clickable(true)
                    .fillColor(Color.YELLOW);
            //MarkerOptions m = new MarkerOptions().position(DbHandler.getInstance().getLocationAtTime(cell.get_scandate())).title("DB: " + cell.get_db() + " SSID: " + cell.get_gsmcellid()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            //lkml.add(m);
            lkml.add(c);
            meMap.put(c.getCenter().toString(), "DB: " + cell.get_db() + "\nSSID: " + cell.get_gsmcellid());*/
            }
        }
        if (btfilter) {
            List<Bluetooth> bluetooths = DbHandler.getInstance().getBluetooths();
            prev = new LatLng(1, 1);
            for (int i = 0; i < bluetooths.size(); i++) {
                Bluetooth bluetooth = bluetooths.get(i);
                LatLng curr = DbHandler.getInstance().getLocationAtTime(bluetooth.get_scandate());
                if (curr.latitude != prev.latitude && curr.longitude != prev.longitude) {
                    wmos.add(DbHandler.getInstance().getWMORange(bluetooth.get_scandate(), wififilter, cellfilter, btfilter));
                }
                prev = curr;
            /*CircleOptions c = new CircleOptions()
                    .center(DbHandler.getInstance().getLocationAtTime(bluetooth.get_scandate()))
                    .radius(10000)
                    .clickable(true)
                    .strokeColor(Color.GREEN)
                    .strokeWidth((float) 50);
            //MarkerOptions m = new MarkerOptions().position(DbHandler.getInstance().getLocationAtTime(wifi.get_scandate())).title("DB: " + wifi.get_db() + " SSID: " + wifi.get_ssid() + " \nMAC: " + wifi.get_mac()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            //lkml.add(m);
            lkml.add(c);
            meMap.put(c.getCenter().toString(), "DB: " + bluetooth.get_db() + "\nName: " + bluetooth.get_name() + " \nMAC: " + bluetooth.get_mac());*/
            }
        }
        if (btfilter || wififilter || cellfilter) {
            for (int i = 0; i < wmos.size(); i++) {
                WMOptions wmo = wmos.get(i);
                CircleOptions c = new CircleOptions()
                        .center(wmo.getLoc())
                        .radius(wmo.getCountsignals())
                        .clickable(true)
                        .strokeColor(wmo.getColor())
                        .strokeWidth(1)
                        .fillColor(wmo.getColor());
                lkml.add(c);
                meMap.put(c.getCenter().toString(), wmo.getText());
            }
            new LoadMarkerBitmapDescriptor(this, mMap).execute(lkml);
        }
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("WirelessMap Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
class LoadMarkerBitmapDescriptor extends
        AsyncTask<List<CircleOptions>, Void, List<CircleOptions>> {

    Context c;
    GoogleMap mMap;
    public static final LatLng jku = new LatLng(48.335262, 14.324431);
    private Marker marker;


    public LoadMarkerBitmapDescriptor(Context context, GoogleMap mmap)
    {
        c = context;
        mMap = mmap;
    }

    @Override
    protected List<CircleOptions> doInBackground(List<CircleOptions>... params) {
        List<CircleOptions> kmlmarkeroptions = params[0];
        return kmlmarkeroptions;
    }


    protected void onPostExecute(List<CircleOptions> result) {
        mMap.clear();
        marker = mMap.addMarker(new MarkerOptions().position(jku).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        float cameraZoom = 13;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jku, cameraZoom));
        for (int i  = 0; i< result.size(); i++) {
            CircleOptions kmlmarkeroption = result.get(i);
            mMap.addCircle(kmlmarkeroption);
        }

    }
}
