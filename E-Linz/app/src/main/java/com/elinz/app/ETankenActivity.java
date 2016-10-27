package com.elinz.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.elinz.dataHandling.FileTable;
import com.elinz.dataHandling.ImportController;
import com.elinz.dataHandling.JSONParser;
import com.elinz.dataHandling.PersistService;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class ETankenActivity extends ActionBarActivity {

    TextView labelStation;
    TextView labelKwH;
    TextView labelKm;
    EditText valueStation;
    EditText valueKwH;
    EditText valueKm;
    Spinner typeSpinner;
    Button tanken;

    String id;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etanken);

        labelStation = (TextView) findViewById(R.id.tanken_station_label);
        labelKwH = (TextView) findViewById(R.id.tanken_kwh_label);
        labelKm = (TextView) findViewById(R.id.tanken_km_label);
        valueStation = (EditText) findViewById(R.id.tanken_station_edit);
        valueKwH = (EditText) findViewById(R.id.tanken_kwh_edit);
        valueKm = (EditText) findViewById(R.id.tanken_km_edit);
        tanken = (Button) findViewById(R.id.tanken_button);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");

        valueStation.setText(name);
        valueStation.setKeyListener(null);
        valueStation.setEnabled(false);

        ArrayList<String> types = new ArrayList<String>();

        // evaluate the possible types for this very station
        if(intent.getBooleanExtra("car", false))
            types.add("Car");
        if(intent.getBooleanExtra("scooter", false))
            types.add("Scooter");
        if(intent.getBooleanExtra("bicycle", false))
            types.add("Bicycle");
        if(intent.getBooleanExtra("truck", false))
            types.add("Truck");

        // if for some reason no types available, add all
        if(types.size() == 0)
            types = StationType.getTypes();

        typeSpinner = (Spinner) findViewById(R.id.tanken_type_spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, types);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(dataAdapter);
    }

    public void doTanken(View view) {
        String kwh = valueKwH.getText().toString();
        String km = valueKm.getText().toString();

        if(!kwh.equals("KwH") && !km.equals("Kilometer")) {
            Log.i(STORAGE_SERVICE, "Tanken-Button geklickt");

            // get the current list
            ArrayList<TankObject> list = ImportController.getInstance().readStatistics(getApplicationContext(), FileTable.STATISTICS);
            TankObject obj = new TankObject(id, name, evalType(), Integer.parseInt(kwh), Integer.parseInt(km));
            list.add(obj);

            Log.i(STORAGE_SERVICE, "Neues Tank-Obj " + obj.toString());

            persistTankObj(list);

            finish();
        } else {
            Log.i(STORAGE_SERVICE, "Tanken-Button geklickt - Eingabe mangelhaft");
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("stationtype", String.valueOf(typeSpinner.getSelectedItem()));
        setResult(RESULT_OK, intent);
        super.finish();
    }

    private StationType evalType() {
        String s = String.valueOf(typeSpinner.getSelectedItem());

        Log.i("Tank type", s);

        if(s.equals("Car")) {
            return StationType.CAR;
        }

        if(s.equals("Scooter")) {
            return StationType.SCOOTER;
        }

        if(s.equals("Bicycle")) {
            Toast.makeText(getApplicationContext(), "Bicycle", Toast.LENGTH_SHORT).show();
            return StationType.BICYCLE;
        }

        if(s.equals("Truck")) {
            Toast.makeText(getApplicationContext(), "Truck", Toast.LENGTH_SHORT).show();
            return StationType.TRUCK;
        }
        // if anything goes wrong
        Log.e("Error within Spinner", "No suitable type found for!!");
        return StationType.BICYCLE;
    }

    private void persistTankObj(ArrayList<TankObject> list) {
        String json = JSONParser.getInstance().getTankObjsToJSON(list);
        Intent intent = new Intent(getApplicationContext(), PersistService.class);
        intent.putExtra("file", FileTable.STATISTICS);
        intent.putExtra("json", json);
        Log.i(STORAGE_SERVICE, json);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
}
