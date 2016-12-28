package at.jku.pervasive.wirelessmap;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import at.jku.pervasive.wirelessmap.data.DbHandler;
import at.jku.pervasive.wirelessmap.model.Bluetooth;
import at.jku.pervasive.wirelessmap.model.Cell;
import at.jku.pervasive.wirelessmap.model.Wifi;
import at.jku.pervasive.wirelessmap.service.BluetoothService;
import at.jku.pervasive.wirelessmap.service.CellService;
import at.jku.pervasive.wirelessmap.service.GpsService;
import at.jku.pervasive.wirelessmap.service.WifiService;

public class WirelessMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private WifiService wifi;
    private GpsService gps;

    Intent bluetoothServiceIntent;
    Intent cellServiceIntent;
    Intent wifiServiceIntent;



    public static final LatLng jku = new LatLng(48.335262, 14.324431);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wireless_map);

        DbHandler.createInstance(this, null, null, 1);
        GpsService.createInstance(this);


        bluetoothServiceIntent = new Intent(this, BluetoothService.class);
        cellServiceIntent = new Intent(this, CellService.class);
        wifiServiceIntent = new Intent(this, WifiService.class);
        startScannerServices();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopScannerServices();
    }

    private void startScannerServices() {
        startService(bluetoothServiceIntent);
        startService(cellServiceIntent);
        startService(wifiServiceIntent);
    }

    private void stopScannerServices() {
        stopService(bluetoothServiceIntent);
        stopService(cellServiceIntent);
        stopService(wifiServiceIntent);
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

        mMap.addMarker(new MarkerOptions().position(jku).title("Current Location"));
        float cameraZoom = 19;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jku, cameraZoom));

        drawMarkers();
    }

    private void drawMarkers() {
        List<Wifi> wifis = DbHandler.getInstance().getWifis();
        List<Cell> cells = DbHandler.getInstance().getCells();
        //List<Bluetooth> bluetooths = DbHandler.getInstance().getBluetooths();


        if(wifis.size() > 0) {
            Wifi wifi = wifis.get(0);
            MarkerOptions m = new MarkerOptions().position(DbHandler.getInstance().getLocationAtTime(wifi.get_scandate())).title("DB: " + wifi.get_db() + " SSID: " + wifi.get_ssid() + " \nMAC: " + wifi.get_mac());
            mMap.addMarker(m);

            Cell cell = cells.get(0);
            MarkerOptions x = new MarkerOptions().position(DbHandler.getInstance().getLocationAtTime(cell.get_scandate())).title("DB: " + cell.get_db() + " SSID: " + cell.get_gsmcellid());
            mMap.addMarker(m);
        }
    }
}
