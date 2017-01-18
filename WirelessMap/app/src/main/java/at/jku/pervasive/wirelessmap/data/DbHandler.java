package at.jku.pervasive.wirelessmap.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.jku.pervasive.wirelessmap.WirelessMap;
import at.jku.pervasive.wirelessmap.model.Bluetooth;
import at.jku.pervasive.wirelessmap.model.Cell;
import at.jku.pervasive.wirelessmap.model.Location;
import at.jku.pervasive.wirelessmap.model.WMOptions;
import at.jku.pervasive.wirelessmap.model.Wifi;

/**
 * Created by hari on 27.10.16.
 */

public class DbHandler extends SQLiteOpenHelper{

    public static synchronized void createInstance(Context context, String name,
                                                   SQLiteDatabase.CursorFactory factory, int version){
        if(instance == null)
            instance = new DbHandler(context, name, factory, version);
    }

    public static DbHandler getInstance(){
        return instance;
    }

    private static DbHandler instance = null;

    private final Context context;
    private static final int DATABASE_VERSION = 11;

    private static final String DATABASE_NAME = "WirelessMap_11.db";
    private static final String TABLE_WIFI = "wifi";
    private static final String TABLE_BLUETOOTH = "bluetooth";
    private static final String TABLE_CELL = "cell";
    private static final String TABLE_LOCATION = "location";

    public static final String COLUMN_ID = "_wifiId";
    public static final String COLUMN_DB = "db";
    public static final String COLUMN_SSID = "ssid";
    public static final String COLUMN_MAC = "mac";
    public static final String COLUMN_SCANDATE = "scandate";
    public static final String COLUMN_BANDWITH = "bandwith";
    public static final String COLUMN_LATENCY = "latency";
    public static final String COLUMN_FREQUENCY = "frequency";
    public static final String COLUMN_CAPABILITIES = "capabilities";
    public static final String COLUMN_CHANNELWIDTH = "channelWidth";

    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_ALTITUDE = "altitude";
    public static final String COLUMN_ACCURACY = "accuracy";
    public static final String COLUMN_LAC = "lac";
    public static final String COLUMN_PSC = "psc";
    public static final String COLUMN_BSLATITUDE = "bslatitude";
    public static final String COLUMN_BSLONGITUDE = "bslongitude";
    public static final String COLUMN_NETWORKID = "networkid";
    public static final String COLUMN_SYSID = "sysid";

    public static final String COLUMN_ID2 = "_bluetoothId";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DEVICECLASS = "deviceclass";
    public static final String COLUMN_BONDSTATE = "bondstate";

    public static final String COLUMN_ID3 = "_cellId";
    public static final String COLUMN_GSMCELLID = "gsmcellid";


    private DbHandler(Context context, String name,
                      SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WIFI_TABLE = "CREATE TABLE " +
                TABLE_WIFI + "("
                + COLUMN_ID + " INTEGER," + COLUMN_DB
                + " INTEGER," + COLUMN_SSID + " TEXT," +
                COLUMN_MAC + " TEXT," +
                COLUMN_FREQUENCY + " INTEGER," +
                COLUMN_CAPABILITIES + " TEXT," +
                COLUMN_CHANNELWIDTH + " INTEGER," +
                COLUMN_SCANDATE + " INTEGER PRIMARY KEY," +
                COLUMN_BANDWITH + " REAL," +
                COLUMN_LATENCY + " INTEGER" +
                ")";
        String CREATE_BLUETOOTH_TABLE = "CREATE TABLE " +
                TABLE_BLUETOOTH + "("
                + COLUMN_ID2 + " INTEGER," + COLUMN_DB
                + " INTEGER," + COLUMN_NAME + " TEXT," +
                COLUMN_BONDSTATE + " INTEGER," +
                COLUMN_DEVICECLASS + " INTEGER," +
                COLUMN_MAC + " TEXT," +
                COLUMN_SCANDATE + " INTEGER PRIMARY KEY" +
                ")";
        String CREATE_CELL_TABLE = "CREATE TABLE " +
                TABLE_CELL + "("
                + COLUMN_ID3 + " INTEGER," + COLUMN_DB
                + " INTEGER," +
                COLUMN_SCANDATE + " INTEGER PRIMARY KEY," +
                COLUMN_GSMCELLID + " INTEGER," +
                COLUMN_LAC + " INTEGER," +
                COLUMN_PSC + " INTEGER," +
                COLUMN_BSLATITUDE + " INTEGER," +
                COLUMN_BSLONGITUDE + " INTEGER," +
                COLUMN_NETWORKID + " INTEGER," +
                COLUMN_SYSID + " INTEGER," +
                COLUMN_BANDWITH + " REAL," +
                COLUMN_LATENCY + " INTEGER" +
                ")";
        String CREATE_LOC_TABLE = "CREATE TABLE " +
                TABLE_LOCATION + "("
                + COLUMN_SCANDATE + " INTEGER PRIMARY KEY,"+
                COLUMN_LONGITUDE + " REAL," +
                COLUMN_LATITUDE + " REAL," +
                COLUMN_ALTITUDE + " REAL" +
                COLUMN_ACCURACY + " REAL" +
                ")";
        db.execSQL(CREATE_WIFI_TABLE);
        db.execSQL(CREATE_BLUETOOTH_TABLE);
        db.execSQL(CREATE_CELL_TABLE);
        db.execSQL(CREATE_LOC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIFI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLUETOOTH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CELL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        onCreate(db);
    }

    public void addWIFI(Wifi wifi) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_DB, wifi.get_db());
        values.put(COLUMN_SSID, wifi.get_ssid());
        values.put(COLUMN_MAC, wifi.get_mac());
        values.put(COLUMN_SCANDATE, wifi.get_scandate());
        values.put(COLUMN_BANDWITH, wifi.get_bandwith());
        values.put(COLUMN_LATENCY, wifi.get_latency());
        values.put(COLUMN_FREQUENCY, wifi.get_frequency());
        values.put(COLUMN_CAPABILITIES, wifi.get_capabilities());
        values.put(COLUMN_CHANNELWIDTH, wifi.get_capabilities());
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("Write on Wifi Database", "SSID: "+ wifi.get_ssid() + " MAC: " + wifi.get_mac());
        db.insert(TABLE_WIFI, null, values);
        db.close();
    }
    public void addLocation(Location location) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_SCANDATE, location.getScandate());
        values.put(COLUMN_LONGITUDE, location.getLongitude());
        values.put(COLUMN_LATITUDE, location.getLatitude());
        values.put(COLUMN_ALTITUDE, location.getAltitude());

        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("Write on Location DB", "Timestamp: "+ location.getScandate());
        db.insert(TABLE_LOCATION, null, values);
        db.close();
    }

    public void addBluetooth(Bluetooth bluetooth) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCANDATE, bluetooth.get_scandate());
        values.put(COLUMN_DB, bluetooth.get_db());
        values.put(COLUMN_MAC, bluetooth.get_mac());
        values.put(COLUMN_ID2, bluetooth.get_bluetoothId());
        values.put(COLUMN_NAME, bluetooth.get_name());
        values.put(COLUMN_DEVICECLASS, bluetooth.get_deviceclass());
        values.put(COLUMN_BONDSTATE, bluetooth.get_bondstate());

        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("Write on Bluetooth DB", "Timestamp: "+ bluetooth.get_scandate());
        db.insert(TABLE_BLUETOOTH, null, values);
        db.close();
    }

    public void addCell(Cell cell) {


        ContentValues values = new ContentValues();
        values.put(COLUMN_SCANDATE, cell.get_scandate());
        values.put(COLUMN_DB, cell.get_db());
        values.put(COLUMN_GSMCELLID, cell.get_cellId());
        values.put(COLUMN_LATENCY, cell.get_latency());
        values.put(COLUMN_BANDWITH, cell.get_bandwith());
        values.put(COLUMN_LAC, cell.get_lac());
        values.put(COLUMN_PSC, cell.get_psc());
        values.put(COLUMN_BSLATITUDE, cell.get_bslatitude());
        values.put(COLUMN_BSLONGITUDE, cell.get_bslongitude());
        values.put(COLUMN_NETWORKID, cell.get_networkid());
        values.put(COLUMN_SYSID, cell.get_sysid());

        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("Write on Cell DB", "Timestamp: "+ cell.get_scandate());
        db.insert(TABLE_CELL, null, values);
        db.close();
    }

    public Wifi findWIFI(String ssid) {
        String query = "Select * FROM " + TABLE_WIFI + " WHERE " + COLUMN_SSID + " =  \"" + ssid + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Wifi wifi = new Wifi();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            wifi.set_wifiId(Integer.parseInt(cursor.getString(0)));
            wifi.set_db(Integer.parseInt(cursor.getString(1)));
            wifi.set_ssid(cursor.getString(2));
            wifi.set_mac(cursor.getString(3));
            wifi.set_scandate(cursor.getLong(4));
            wifi.set_bandwith(cursor.getFloat(5));
            wifi.set_latency(cursor.getInt(6));
            cursor.close();
        } else {
            wifi = null;
        }
        db.close();
        return wifi;
    }


    public boolean deleteWifi(String ssid) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_WIFI + " WHERE " + COLUMN_SSID + " =  \"" + ssid + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Wifi wifi = new Wifi();

        if (cursor.moveToFirst()) {
            wifi.set_wifiId(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_WIFI, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(wifi.get_wifiId()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public List<Wifi> getWifis() {
        List<Wifi> wifis = new ArrayList<>();

        String query = "Select * FROM " + TABLE_WIFI + " ORDER BY " + COLUMN_SCANDATE + " DESC  LIMIT 100";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){
            Wifi wifi = new Wifi();
            wifi.set_wifiId(cursor.getInt(0));
            wifi.set_db(cursor.getInt(1));
            wifi.set_ssid(cursor.getString(2));
            wifi.set_mac(cursor.getString(3));
            wifi.set_frequency(cursor.getInt(4));
            wifi.set_capabilities(cursor.getString(5));
            wifi.set_channelWidth(cursor.getInt(6));
            wifi.set_scandate(cursor.getLong(7));
            wifi.set_bandwith(cursor.getFloat(8));
            wifi.set_latency(cursor.getInt(9));
            wifis.add(wifi);
        }

        cursor.close();
        db.close();

        return wifis;
    }

    public List<Cell> getCells() {
        List<Cell> cells = new ArrayList<>();

        String query = "Select * FROM " + TABLE_CELL  + " ORDER BY " + COLUMN_SCANDATE + " DESC  LIMIT 100";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        while(cursor.moveToNext()){
            Cell cell = new Cell();
            cell.set_cellId(cursor.getInt(0));
            cell.set_db(cursor.getInt(1));
            cell.set_scandate(cursor.getLong(2));
            cell.set_gsmcellid(cursor.getInt(3));
            cell.set_lac(cursor.getInt(4));
            cell.set_psc(cursor.getInt(5));
            cell.set_bslatitude(cursor.getInt(6));
            cell.set_bslongitude(cursor.getInt(7));
            cell.set_networkid(cursor.getInt(8));
            cell.set_networkid(cursor.getInt(9));
            cell.set_bandwith(cursor.getFloat(10));
            cell.set_latency(cursor.getInt(11));
            cells.add(cell);
        }

        cursor.close();
        db.close();

        return cells;
    }

    public List<Bluetooth> getBluetooths() {
        List<Bluetooth> bluetooths = new ArrayList<>();

        String query = "Select * FROM " + TABLE_BLUETOOTH  + " ORDER BY " + COLUMN_SCANDATE + " DESC LIMIT 100";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        while(cursor.moveToNext()){
            Bluetooth bluetooth = new Bluetooth();
            bluetooth.set_bluetoothId(cursor.getInt(0));
            bluetooth.set_db(cursor.getInt(1));
            bluetooth.set_name(cursor.getString(2));
            bluetooth.set_bondstate(cursor.getInt(3));
            bluetooth.set_deviceclass(cursor.getInt(4));
            bluetooth.set_mac(cursor.getString(5));
            bluetooth.set_scandate(cursor.getLong(6));
            bluetooths.add(bluetooth);
        }

        cursor.close();
        db.close();

        return bluetooths;
    }

    public WMOptions getWMORange(long scandate, boolean wififilter, boolean cellfilter, boolean btfilter){


        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(scandate);
        long past = timeSeconds - 8;
        long future = timeSeconds + 8;
        past = past*1000;
        future = future*1000;
        LatLng curr = getLocationAtTime(scandate);
        String query;
        SQLiteDatabase db;
        Cursor cursor;
        List<Bluetooth> bluetooths = new ArrayList<>();
        List<Wifi> wifis = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();
        if (wififilter) {
            query = "Select DISTINCT * FROM " + TABLE_WIFI + " WHERE " + COLUMN_SCANDATE + " BETWEEN " + past + " AND " + future + ";"; // AND strftime('%s', " + scandate + " / 1000)";

            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);


            while (cursor.moveToNext()) {
                Wifi wifi = new Wifi();
                wifi.set_wifiId(cursor.getInt(0));
                wifi.set_db(cursor.getInt(1));
                wifi.set_ssid(cursor.getString(2));
                wifi.set_mac(cursor.getString(3));
                wifi.set_frequency(cursor.getInt(4));
                wifi.set_capabilities(cursor.getString(5));
                wifi.set_channelWidth(cursor.getInt(6));
                wifi.set_scandate(cursor.getLong(7));
                wifi.set_bandwith(cursor.getFloat(8));
                wifi.set_latency(cursor.getInt(9));
                LatLng llat = getLocationAtTime(wifi.get_scandate());
                if (llat.longitude == curr.longitude && llat.latitude == curr.latitude) {
                    wifis.add(wifi);
                }
            }
            cursor.close();
            db.close();
        }
        if (btfilter) {

            query = "Select DISTINCT * FROM " + TABLE_BLUETOOTH + " WHERE " + COLUMN_SCANDATE + " BETWEEN " + past + " AND " + future + ";";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);

            String mac = "";
            while (cursor.moveToNext()) {
                Bluetooth bluetooth = new Bluetooth();
                bluetooth.set_bluetoothId(cursor.getInt(0));
                bluetooth.set_db(cursor.getInt(1));
                bluetooth.set_name(cursor.getString(2));
                bluetooth.set_bondstate(cursor.getInt(3));
                bluetooth.set_deviceclass(cursor.getInt(4));
                bluetooth.set_mac(cursor.getString(5));
                bluetooth.set_scandate(cursor.getLong(6));
                LatLng llat = getLocationAtTime(bluetooth.get_scandate());
                if (llat.longitude == curr.longitude && llat.latitude == curr.latitude) {
                    bluetooths.add(bluetooth);
                }
            }

            cursor.close();
            db.close();
        }
        if (cellfilter) {

            query = "Select DISTINCT * FROM " + TABLE_CELL + " WHERE " + COLUMN_SCANDATE + " BETWEEN " + past + " AND " + future + ";";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);


            while (cursor.moveToNext()) {
                Cell cell = new Cell();
                cell.set_cellId(cursor.getInt(0));
                cell.set_db(cursor.getInt(1));
                cell.set_scandate(cursor.getLong(2));
                cell.set_gsmcellid(cursor.getInt(3));
                cell.set_lac(cursor.getInt(4));
                cell.set_psc(cursor.getInt(5));
                cell.set_bslatitude(cursor.getInt(6));
                cell.set_bslongitude(cursor.getInt(7));
                cell.set_networkid(cursor.getInt(8));
                cell.set_sysid(cursor.getInt(9));
                cell.set_bandwith(cursor.getFloat(10));
                cell.set_latency(cursor.getInt(11));
                LatLng llat = getLocationAtTime(cell.get_scandate());
                if (llat.longitude == curr.longitude && llat.latitude == curr.latitude) {
                    cells.add(cell);
                }
            }

            cursor.close();

            db.close();
        }


        int sumdb = 0;
        int avgwifidb = 0;
        int avgcelldb = 0;
        String wifidata = "";
        if (wififilter) {
            for (int i = 0; i < wifis.size(); i++) {
                Wifi w = wifis.get(i);
                avgwifidb += w.get_db();
                wifidata += "SSID: " + w.get_ssid() + " DB: " + w.get_db() + " BW: " + w.get_bandwith() + "\n";
            }
            sumdb += avgwifidb;
            if (wifis.size()>0) {
                avgwifidb = avgwifidb / wifis.size();
            } else {
                avgwifidb = avgwifidb / 1;
            }
        }
        String bluetoothdata = "";
        if (btfilter) {
            for (int i = 0; i < bluetooths.size(); i++) {
                Bluetooth bt = bluetooths.get(i);
                bluetoothdata += "Name: " + bt.get_name() + " MAC: " + bt.get_mac() + " BS: " + bt.get_bondstate() + " DC: " + bt.get_deviceclass() + "\n";
            }
        }
        String celldata = "";
        if (cellfilter) {
            for (int i = 0; i < cells.size(); i++) {
                Cell c = cells.get(i);
                avgcelldb += c.get_db();
                celldata += "CID: " + c.get_cellId() + " GSMID: " + c.get_gsmcellid() + " DB: " + c.get_db() + "\n";
            }
            if (wififilter) {
                sumdb += (avgcelldb * -1);
            } else {
                sumdb += avgcelldb;
            }
            if (cells.size()>0) {
                avgcelldb = avgcelldb / cells.size();
            } else
            {
                avgcelldb = avgcelldb / 1;
            }
        }
        int res = 0;
        if (wififilter && cellfilter) {
            if ((wifis.size() + cells.size()) > 0) {
                res = sumdb / (wifis.size() + cells.size());
            } else {
                res = sumdb / 1;
            }
            res = res * -1;
        } else {
            if (wififilter) {
                if (wifis.size() > 0) {
                    res = sumdb / wifis.size();
                } else {
                    res = sumdb / 1;
                }
                res = res * -1;
            } else {
                if (cellfilter) {
                    if (cells.size() > 0) {
                        res = sumdb / cells.size();
                    } else {
                        res = sumdb / 1;
                    }
                }
            }
        }
        int color;
        if (res >= 40) {
            if (res >= 80) {
                color = Color.GREEN;
            } else
            {
                color = Color.YELLOW;
            }
        } else {
            color = Color.RED;
        }
        int countsignals = 0;
        if (wififilter && cellfilter && btfilter) {
            countsignals = (bluetooths.size() + wifis.size() + cells.size()) * 10;
        } else {
            if (wififilter && cellfilter) {
                countsignals = (wifis.size() + cells.size()) * 10;
            } else {
                if (wififilter && btfilter) {
                    countsignals = (wifis.size() + bluetooths.size()) * 10;
                } else {
                    if (cellfilter && btfilter) {
                        countsignals = (cells.size() + bluetooths.size()) * 10;
                    } else {
                        if (wififilter) {
                            countsignals = wifis.size() * 10;
                        } else {
                            if (cellfilter) {
                                countsignals = cells.size() * 10;
                            } else {
                                if (btfilter) {
                                    countsignals = bluetooths.size() * 10;
                                    color = Color.BLUE;
                                }
                            }
                        }
                    }
                }
            }
        }
        String text = "";
        if (wififilter) {
            text += "WIFI:\tAVGDB:" + avgwifidb + "\tCOUNT:" + wifis.size() + "\n";
            text += wifidata;
        }
        if (btfilter) {
            text += "BLUETOOTH:\tCOUNT:" + bluetooths.size() + "\n";
            text += bluetoothdata;
        }
        if (cellfilter) {
            text += "CELL:\tDB:" + avgcelldb + "\n";
            text += celldata;
        }
        WMOptions wmo = new WMOptions(countsignals, color, text, curr);

        return wmo;

    }


    public LatLng getLocationAtTime(long scandate){

        String query = "Select * FROM " + TABLE_LOCATION + " ORDER BY ABS("+COLUMN_SCANDATE+"-"+scandate+") LIMIT 1" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToNext()){
            LatLng pos = new LatLng(cursor.getFloat(2), cursor.getFloat(1));
            cursor.close();
            db.close();
            return pos;
        }

        cursor.close();
        db.close();
        return WirelessMap.jku;

    }

    public void download() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String  currentDBPath= "//data//" + "at.jku.pervasive.wirelessmap"
                        + "//databases//" + DATABASE_NAME;
                String backupDBPath  = "/Download/dbDump"+System.currentTimeMillis()+".db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

            }
        } catch (Exception e) {
            Log.e("Export Database", "Error");

        }
    }
}
