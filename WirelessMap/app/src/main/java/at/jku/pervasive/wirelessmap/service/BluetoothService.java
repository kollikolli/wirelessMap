package at.jku.pervasive.wirelessmap.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.Timer;
import java.util.TimerTask;

import at.jku.pervasive.wirelessmap.data.DbHandler;
import at.jku.pervasive.wirelessmap.model.Bluetooth;


/**
 * Created by kollegger on 07.11.16.
 */

public class BluetoothService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static final int SCAN_INTERVALL = 20000; // 20sec

    private BluetoothAdapter bluetoothAdapter;

    private Timer timer = new Timer();
    private boolean scanning = false;


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                DbHandler.getInstance().addBluetooth(new Bluetooth(device.getType(), device.getName(), device.getAddress(), System.currentTimeMillis(), 0, 0));
            }
        }
    };

    @Override
    public int onStartCommand(Intent arg0, int arg1, int arg2) {
        // Service should be explicitely started and stopped
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            return;
        }

        TimerTask tt = new TimerTask() {

            @Override
            public void run() {
                boolean a = bluetoothAdapter.startDiscovery();
            }
        };

        timer.scheduleAtFixedRate(tt, 0, SCAN_INTERVALL);

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        // stop the continous task
        if (timer != null)
            timer.cancel();
        // unregister the wifi listener
        unregisterReceiver(mReceiver);
    }
}
