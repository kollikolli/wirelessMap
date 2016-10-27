package com.elinz.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.elinz.dataHandling.FileTable;

import java.io.FileOutputStream;


/**
 * Created by hari on 27.10.16.
 */

public class DatabaseActivity extends ActionBarActivity {

    TextView idView;
    EditText ssidBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        idView = (TextView) findViewById(R.id.wifiId);
        ssidBox = (EditText) findViewById(R.id.ssid);
    }

    public void newWifi (View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        String ssid = ssidBox.getText().toString();

        WIFI wifi = new WIFI(ssid);

        dbHandler.addWIFI(wifi);
        ssidBox.setText("");
    }

    public void lookupWIFI (View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        WIFI wifi =
                dbHandler.findWIFI(ssidBox.getText().toString());

        if (wifi != null) {
            idView.setText(String.valueOf(wifi.get_ssid()));

            ssidBox.setText(String.valueOf(wifi.get_wifiId()));
        } else {
            idView.setText("No Match Found");
        }
    }

    public void removeWifi (View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        boolean result = dbHandler.deleteWifi(ssidBox.getText().toString());

        if (result)
        {
            idView.setText("Record Deleted");
            ssidBox.setText("");
        }
        else
            idView.setText("No Match Found");
    }
}
