package at.jku.pervasive.wirelessmap.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import at.jku.pervasive.wirelessmap.data.DbHandler;
import at.jku.pervasive.wirelessmap.model.Wifi;

public class WifiService extends Service {

    private static final int SCAN_INTERVALL = 20; // 5sec

    private WifiManager wifi;

    private Timer timer = new Timer();
    private boolean scanning = false;
    private WifiScanListener wifiListener;


    @Override
    public int onStartCommand(Intent arg0, int arg1, int arg2) {
        // Service should be explicitely started and stopped
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        // request wifi service
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        // register wifi listener
        wifiListener = new WifiScanListener();
        registerReceiver(wifiListener, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        startFingerPrinting();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // stop the continous task
        if (timer != null)
            timer.cancel();
        // unregister the wifi listener
        unregisterReceiver(wifiListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void startFingerPrinting() {

        // start fingerprinting at a fixed rated (every 10 seconds)
        TimerTask tt = new TimerTask() {

            @Override
            public void run() {

                // ensure that wifi is enabled
                if (!wifi.isWifiEnabled()) {
                    wifi.setWifiEnabled(true);
                }

                // only perform this action if the last scan has already
                // finished.
                if (!scanning) {
                    wifi.startScan();
                    scanning = true;
                }
            }
        };
        // run the task continously
        timer.scheduleAtFixedRate(tt, 0, SCAN_INTERVALL);
    }

    protected void collectSignals() {
        // get all available wifi signals
        wifi.startScan();
        List<ScanResult> scanRes = wifi.getScanResults();

        // store in database
        updateDatabase(scanRes);
    }



    private void updateDatabase(List<ScanResult> scanRes) {
        if (scanRes == null)
            return;

        long currTime = System.currentTimeMillis();

        TreeSet<ScanResult> sortedSignals = new TreeSet<ScanResult>(
                new Comparator<ScanResult>() {

                    @Override
                    public int compare(ScanResult first, ScanResult second) {
                        // the bigger the signal strength, the better
                        return first.level - second.level;
                    }
                });
        sortedSignals.addAll(scanRes);

        // append the best 5 found signals
        int c = 0;
        for (ScanResult currRes : sortedSignals) {
            if (c >= 5) // skip adding, after 5 signals
                break;
            // add signals to scan


            DbHandler.getInstance().addWIFI(
                new Wifi(currRes.level, currRes.SSID, currRes.BSSID, System.currentTimeMillis())
            );
            c++;

        }
    }

    private class WifiScanListener extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            collectSignals();
            scanning = false;
        }
    }

}
