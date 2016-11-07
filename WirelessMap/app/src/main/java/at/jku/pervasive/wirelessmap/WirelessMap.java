package at.jku.pervasive.wirelessmap;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import at.jku.pervasive.wirelessmap.data.DbHandler;
import at.jku.pervasive.wirelessmap.service.GpsService;
import at.jku.pervasive.wirelessmap.service.WifiService;

public class WirelessMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private WifiService wifi;
    private GpsService gps;

    // Add a marker in Sydney and move the camera
    private LatLng sydney = new LatLng(-34, 151);


    private ServiceConnection serviceCon = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            wifi = ((WifiService.WifiServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            wifi = null;
        }
    };

    public void bindServices() {
        Intent wifiService = null;
        wifiService = new Intent(this, WifiService.class);
        bindService(wifiService, serviceCon, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wireless_map);

        DbHandler.createInstance(this, null, null, 1);
        GpsService.createInstance(this);
        bindServices();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
