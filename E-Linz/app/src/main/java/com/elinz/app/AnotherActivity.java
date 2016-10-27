package com.elinz.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.elinz.dataHandling.StationController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class AnotherActivity extends Activity {

    private ListView myList;
    private final Context mContext = null;
    private TextView header1, header2;
    private ListView listitem1;
    private boolean sortname = true;
    private boolean sortdistance = true;
    private double lon = 14.327405, lat = 48.295065;
    CheckBox cbC, cbB, cbBi;
    HashMap<String, Boolean> stationtypes = new HashMap<String, Boolean>();
    ArrayList<EStation> list;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final StationController sr = StationController.getInstance(this);

        header1 = (TextView) findViewById(R.id.header1);
        header2 = (TextView) findViewById(R.id.header2);
        cbC=(CheckBox)findViewById(R.id.radio_car);
        cbB=(CheckBox)findViewById(R.id.radio_bike);
        cbBi=(CheckBox)findViewById(R.id.radio_bicycle);

        checkTheBoxes();

        cbC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stationtypes.put("ecar", isChecked);
                ArrayList<EStation> listestation = getStationsFromStationTypes(stationtypes, list);
                listestation = sortDistance(listestation, sortdistance);
                myList = (ListView)findViewById(R.id.list);

                //show the ListView on the screen
                // The adapter MyCustomAdapter is responsible for maintaining the data backing this list and for producing
                // a view to represent an item in that data set.
                myList.setAdapter(new MyCustomAdapter(AnotherActivity.this,listestation));
            }
        });

        cbB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stationtypes.put("ebike", isChecked);
                ArrayList<EStation> listestation = getStationsFromStationTypes(stationtypes, list);
                listestation = sortDistance(listestation, sortdistance);
                myList = (ListView)findViewById(R.id.list);

                //show the ListView on the screen
                // The adapter MyCustomAdapter is responsible for maintaining the data backing this list and for producing
                // a view to represent an item in that data set.
                myList.setAdapter(new MyCustomAdapter(AnotherActivity.this,listestation));
            }
        });
        cbBi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stationtypes.put("ebicycle", isChecked);
                ArrayList<EStation> listestation = getStationsFromStationTypes(stationtypes, list);
                listestation = sortDistance(listestation, sortdistance);
                myList = (ListView)findViewById(R.id.list);

                //show the ListView on the screen
                // The adapter MyCustomAdapter is responsible for maintaining the data backing this list and for producing
                // a view to represent an item in that data set.
                myList.setAdapter(new MyCustomAdapter(AnotherActivity.this,listestation));
            }
        });

        listitem1 = (ListView) findViewById(R.id.list);
        listitem1.setClickable(true);
        listitem1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EStation m = (EStation)view.getTag(R.id.list);
                //Toast.makeText(getApplicationContext(), "Pos[" + position
                //        + "] clicked, with id: " + m.getId(), Toast.LENGTH_SHORT).show();

                Intent tankIntent = new Intent(getApplicationContext(), ETankenActivity.class);
                tankIntent.putExtra("id", m.getId());
                tankIntent.putExtra("name", m.getName());

                ArrayList<StationType> typeList = m.getType();
                tankIntent.putExtra("car", typeList.contains(StationType.CAR));
                tankIntent.putExtra("scooter", typeList.contains(StationType.SCOOTER));
                tankIntent.putExtra("bicycle", typeList.contains(StationType.BICYCLE));
                tankIntent.putExtra("truck", typeList.contains(StationType.TRUCK));

                startActivityForResult(tankIntent, 1);
            }
        });

        header1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<EStation> list = sr.getStations();
                list = sortName(list, sortname);
                sortname = (!sortname);
                myList = (ListView)findViewById(R.id.list);

                //show the ListView on the screen
                // The adapter MyCustomAdapter is responsible for maintaining the data backing this list and for producing
                // a view to represent an item in that data set.
                myList.setAdapter(new MyCustomAdapter(AnotherActivity.this,list));
                //Adapter state changed
            }
        });
        header2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lat = 48.295065;
                lon = 14.327405;
                ArrayList<EStation> list = sr.getStations(lon,lat);
                list = sortDistance(list, sortdistance);
                myList = (ListView)findViewById(R.id.list);

                //show the ListView on the screen
                // The adapter MyCustomAdapter is responsible for maintaining the data backing this list and for producing
                // a view to represent an item in that data set.
                myList.setAdapter(new MyCustomAdapter(AnotherActivity.this,list));
            }
        });

        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()){ // gps enabled, return boolean true/false
            lat = gps.getLatitude(); // returns latitude
            lon = gps.getLongitude(); // returns longitude
            /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setting Dialog Title
            alertDialog.setTitle("Werte");

            // Setting Dialog Message
            alertDialog.setMessage("Long: " + lon + " Lat: " + lat);
            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
            */
            gps.stopUsingGPS();
        } else {
            gps.showSettingsAlert();
        }
        lat = 48.295065;
        lon = 14.327405;
        list = sr.getStations(lon, lat);
        list = sortDistance(list, sortdistance);
        list = getStationsFromStationTypes(stationtypes, list);

        //relate the listView from java to the one created in xml
        myList = (ListView)findViewById(R.id.list);

        //show the ListView on the screen
        // The adapter MyCustomAdapter is responsible for maintaining the data backing this list and for producing
        // a view to represent an item in that data set.
        myList.setAdapter(new MyCustomAdapter(AnotherActivity.this,list));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent statIntent = new Intent(getApplicationContext(), StatisticsActivity.class);

        if(resultCode == RESULT_OK && data.hasExtra("stationtype")) {
            statIntent.putExtra("stationtype", data.getStringExtra("stationtype"));
            startActivity(statIntent);
        }
    }

    private ArrayList<EStation> getStationsFromStationTypes(HashMap<String, Boolean> stationtypes, ArrayList<EStation> list) {
        ArrayList<EStation> listestation = new ArrayList<EStation>();
        for (int i = 0; i<10; i++) {
            EStation e = list.get(i);
            HashMap<SocketType, Integer> hs = new HashMap<SocketType, Integer>();
            hs.put(SocketType.CEE_BLUE, 1);
            hs.put(SocketType.CEE_RED_125A, 1);
            hs.put(SocketType.CEE_RED_32A, 1);
            e.setSocketTypes(hs);
            list.set(i, e);
        }
        for (EStation e : list) {
            if (e.getSocketTypes().isEmpty()) {
                listestation.add(e);
            }
            for (SocketType st: e.getSocketTypes().keySet()) {
                if (stationtypes.get("ecar")) {
                    if (st.equals(SocketType.CEE_BLUE) || st.equals(SocketType.CEE_RED_125A) || st.equals(SocketType.CEE_RED_32A))
                    {
                        listestation.add(e);
                    }
                }
            }
        }
        return listestation;
    }


    /*/@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);
    }*/


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
                startActivity(new Intent(AnotherActivity.this, AnotherActivity.class));
                break;
            case(R.id.menu_tanken):
                startActivity(new Intent(AnotherActivity.this, ETankenActivity.class));
                break;
            case(R.id.menu_statistik):
                startActivity(new Intent(AnotherActivity.this, StatisticsActivity.class));
                break;
        }
        return true;
    }

    public ArrayList<EStation> sortDistance(ArrayList<EStation> list, boolean sortdistance) {
        if (sortdistance) {
            boolean unsortiert = true;
            EStation temp;

            while (unsortiert) {
                unsortiert = false;
                for (int i = 0; i < list.size() - 1; i++) {
                    //for (EStation e : list) {
                    if (list.get(i).getDistance() > list.get(i + 1).getDistance()) {
                        temp = list.get(i);
                        list.set(i, list.get(i + 1));
                        list.set((i + 1), temp);
                        unsortiert = true;
                    }
                }
            }
        } else {
            boolean unsortiert = true;
            EStation temp;

            while (unsortiert) {
                unsortiert = false;
                for (int i = 0; i < list.size() - 1; i++) {
                    //for (EStation e : list) {
                    if (list.get(i).getDistance() < list.get(i + 1).getDistance()) {
                        temp = list.get(i);
                        list.set(i, list.get(i + 1));
                        list.set((i + 1), temp);
                        unsortiert = true;
                    }
                }
            }
        }
        return list;
    }

    public ArrayList<EStation> sortName(ArrayList<EStation> list, boolean sortname) {
        EStation temp;
        if (sortname) {
            for (int x = 1; x < list.size(); x++) {
                for (int y = 0; y < list.size() - x; y++) {
                    if (list.get(y).getName().compareTo(list.get(y + 1).getName()) > 0) {
                        temp = list.get(y);
                        list.set(y, list.get(y + 1));
                        list.set((y + 1), temp);
                    }
                }
            }
        } else {
            for (int x = 1; x < list.size(); x++) {
                for (int y = 0; y < list.size() - x; y++) {
                    if (list.get(y).getName().compareTo(list.get(y + 1).getName()) < 0) {
                        temp = list.get(y);
                        list.set(y, list.get(y + 1));
                        list.set((y + 1), temp);
                    }
                }
            }
        }
        return list;
    }

    public void checkTheBoxes() {
        if (cbC.isChecked()) {
            stationtypes.put("ecar", true);
        } else {
            stationtypes.put("ecar", false);
        }
        if (cbB.isChecked()) {
            stationtypes.put("ebike", true);
        } else {
            stationtypes.put("ebike", false);
        }
        if (cbBi.isChecked()) {
            stationtypes.put("ebicycle", true);
        } else {
            stationtypes.put("ebicycle", false);
        }
    }



}

