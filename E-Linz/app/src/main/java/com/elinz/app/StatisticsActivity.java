package com.elinz.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.elinz.dataHandling.FileTable;
import com.elinz.dataHandling.ImportController;
import com.elinz.dataHandling.PersistService;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;


public class StatisticsActivity extends ActionBarActivity {

    TextView lastStation;
    TextView mComStation;

    TextView totalKwH;
    TextView avgKwH;
    TextView lastKwH;
    TextView lowestKwH;
    TextView highestKwH;

    TextView totalKm;
    TextView avgKm;
    TextView lastKm;
    TextView lowestKm;
    TextView highestKm;

    Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initaliseViews();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, StationType.getTypes());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(dataAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                StationType selectedType = evalType(parent.getItemAtPosition(pos).toString());
                setViews(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        Intent intent = getIntent();

        if(intent.hasExtra("stationtype")) {
            String type = intent.getStringExtra("stationtype");
            typeSpinner.setSelection(dataAdapter.getPosition(type));
        }
    }

    private void initaliseViews() {
        lastStation = (TextView) findViewById(R.id.stat_label_station_recent_value);
        mComStation = (TextView) findViewById(R.id.stat_label_station_most_value);

        totalKwH = (TextView) findViewById(R.id.stat_label_kwh_total_value);
        avgKwH = (TextView) findViewById(R.id.stat_label_kwh_avg_value);
        lastKwH = (TextView) findViewById(R.id.stat_label_kwh_recent_value);
        lowestKwH = (TextView) findViewById(R.id.stat_label_kwh_lowest_value);
        highestKwH = (TextView) findViewById(R.id.stat_label_kwh_highest_value);

        totalKm = (TextView) findViewById(R.id.stat_label_km_total_value);
        avgKm = (TextView) findViewById(R.id.stat_label_km_avg_value);
        lastKm = (TextView) findViewById(R.id.stat_label_km_recent_value);
        lowestKm = (TextView) findViewById(R.id.stat_label_km_lowest_value);
        highestKm = (TextView) findViewById(R.id.stat_label_km_highest_value);

        typeSpinner = (Spinner) findViewById(R.id.stat_type_spinner);
    }


    private StationType evalType(String s) {
            if(s.equals("Car")) {
                Toast.makeText(getApplicationContext(), "Car", Toast.LENGTH_SHORT).show();
                return StationType.CAR;
            }

            if(s.equals("Scooter")) {
                Toast.makeText(getApplicationContext(), "Scooter", Toast.LENGTH_SHORT).show();
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

    private void setViews(StationType type) {
        // getting all the tank-objs
        ArrayList<TankObject> tankList = ImportController.getInstance().readStatistics(getApplicationContext(), FileTable.STATISTICS);

        // list for the filtered tank-objs
        ArrayList<TankObject> list = new ArrayList<TankObject>();

        for(TankObject t : tankList) {
            if(t.getType() == type)
                list.add(t);
        }

        int listSize = list.size();

        if(listSize > 0) {  // on not empty list do real initialisation

            lastStation.setText(list.get(listSize - 1).getName());
            mComStation.setText(getMComStation(list));

            int[] kwhs = getKwhs(list);
            totalKwH.setText("" + kwhs[0]);
            avgKwH.setText("" + kwhs[1]);
            lastKwH.setText("" + kwhs[2]);
            lowestKwH.setText("" + kwhs[3]);
            highestKwH.setText("" + kwhs[4]);

            int[] kms = getKms(list);
            totalKm.setText("" + kms[0]);
            avgKm.setText("" + kms[1]);
            lastKm.setText("" + kms[2]);
            lowestKm.setText("" + kms[3]);
            highestKm.setText("" + kms[4]);

        } else {    // on empty list du dummy-initialisation

            lastStation.setText("?");
            mComStation.setText("?");

            totalKwH.setText("0");
            avgKwH.setText("0");
            lastKwH.setText("0");
            lowestKwH.setText("0");
            highestKwH.setText("0");

            totalKm.setText("0");
            avgKm.setText("0");
            lastKm.setText("0");
            lowestKm.setText("0");
            highestKm.setText("0");

        }

    }

    private String getMComStation(ArrayList<TankObject> tankList) {
        TankObject mComObj = new TankObject();
        mComObj.setName(" ");   // if no station in list, an empty name should be returned

        int mComCount = 0;
        int tmpCount;

        for(TankObject tempObj : tankList) {
            tmpCount = 0;

            for(TankObject o : tankList) {
                if(o.getId().equals(tempObj.getId())) {
                    tmpCount++;
                }
            }

            if(tmpCount > mComCount) {
                mComObj = tempObj;
                mComCount = tmpCount;
            }
        }

        return mComObj.getName();
    }

    private int[] getKwhs(ArrayList<TankObject> tankList) {
        int total = 0;
        int avg = 0;
        int low = 0;
        int high = 0;

        int tmp = 0;

        int[] result = {0, 0, 0, 0, 0};

        if(!tankList.isEmpty()) {
            low = tankList.get(0).getKwh();
            high = low;

            for(TankObject o : tankList) {
                tmp = o.getKwh();

                if(tmp < low) {
                    low = tmp;
                }

                if(tmp > high) {
                    high = tmp;
                }

                total += tmp;
            }

            avg = total / tankList.size();

            result[0] = total;
            result[1] = avg;
            result[2] = tankList.get(tankList.size()-1).getKwh();
            result[3] = low;
            result[4] = high;

            return result;
        }

        return result;
    }

    private int[] getKms(ArrayList<TankObject> tankList) {
        int total = 0;
        int avg = 0;
        int low = 0;
        int high = 0;

        int tmp = 0;

        int[] result = {0, 0, 0, 0, 0};

        if(!tankList.isEmpty()) {
            low = tankList.get(0).getKm();
            high = low;

            for(TankObject o : tankList) {
                tmp = o.getKm();

                if(tmp < low) {
                    low = tmp;
                }

                if(tmp > high) {
                    high = tmp;
                }

                total += tmp;
            }

            avg = total / tankList.size();

            result[0] = total;
            result[1] = avg;
            result[2] = tankList.get(tankList.size()-1).getKm();
            result[3] = low;
            result[4] = high;

            return result;
        }

        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myMenuInflater = getMenuInflater();
        myMenuInflater.inflate(R.menu.statistic_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_statistik_clear) {
            try {
                FileOutputStream outputStream;
                outputStream = openFileOutput(FileTable.STATISTICS, getApplicationContext().MODE_PRIVATE);
                outputStream.write("".getBytes());
                outputStream.close();

                Log.i(STORAGE_SERVICE, "Statistic storage cleared.");

                // set views according to cleared storage
                setViews(evalType(String.valueOf(typeSpinner.getSelectedItem())));

            } catch (Exception e) {
                Log.e(STORAGE_SERVICE, "Failed to clear statistic storage!");
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
