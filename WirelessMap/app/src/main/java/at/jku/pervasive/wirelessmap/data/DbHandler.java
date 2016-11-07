package at.jku.pervasive.wirelessmap.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WirelessMap.db";
    private static final String TABLE_WIFI = "wifi";
    private static final String TABLE_BLUETOOTH = "bluetooth";
    private static final String TABLE_CELL = "cell";

    public static final String COLUMN_ID = "_wifiId";
    public static final String COLUMN_DB = "db";
    public static final String COLUMN_SSID = "ssid";
    public static final String COLUMN_MAC = "mac";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_ALTITUDE = "altitude";
    public static final String COLUMN_SCANDATE = "scandate";
    public static final String COLUMN_BANDWITH = "bandwith";
    public static final String COLUMN_LATENCY = "latency";

    public static final String COLUMN_ID2 = "_bluetoothId";
    public static final String COLUMN_NAME = "name";

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
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_DB
                + " INTEGER," + COLUMN_SSID + " TEXT," +
                COLUMN_MAC + " TEXT," +
                COLUMN_LONGITUDE + " REAL," +
                COLUMN_LATITUDE + " REAL," +
                COLUMN_ALTITUDE + " REAL," +
                COLUMN_SCANDATE + " TEXT," +
                COLUMN_BANDWITH + " REAL," +
                COLUMN_LATENCY + " INTEGER" +
                ")";
        String CREATE_BLUETOOTH_TABLE = "CREATE TABLE " +
                TABLE_BLUETOOTH + "("
                + COLUMN_ID2 + " INTEGER PRIMARY KEY," + COLUMN_DB
                + " INTEGER," + COLUMN_NAME + " TEXT," +
                COLUMN_LONGITUDE + " REAL," +
                COLUMN_LATITUDE + " REAL," +
                COLUMN_ALTITUDE + " REAL," +
                COLUMN_SCANDATE + " TEXT," +
                ")";
        String CREATE_CELL_TABLE = "CREATE TABLE " +
                TABLE_CELL + "("
                + COLUMN_ID3 + " INTEGER PRIMARY KEY," + COLUMN_DB
                + " INTEGER," +
                COLUMN_LONGITUDE + " REAL," +
                COLUMN_LATITUDE + " REAL," +
                COLUMN_ALTITUDE + " REAL," +
                COLUMN_SCANDATE + " TEXT," +
                COLUMN_GSMCELLID + " INTEGER," +
                COLUMN_BANDWITH + " REAL," +
                COLUMN_LATENCY + " INTEGER" +
                ")";
        db.execSQL(CREATE_WIFI_TABLE);
        db.execSQL(CREATE_BLUETOOTH_TABLE);
        db.execSQL(CREATE_CELL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIFI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLUETOOTH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CELL);
        onCreate(db);
    }

    public void addWIFI(Wifi wifi) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_DB, wifi.get_db());
        values.put(COLUMN_SSID, wifi.get_ssid());
        values.put(COLUMN_MAC, wifi.get_ssid());
        values.put(COLUMN_LONGITUDE, wifi.get_longitude());
        values.put(COLUMN_LATITUDE, wifi.get_latitude());
        values.put(COLUMN_ALTITUDE, wifi.get_altitude());
        values.put(COLUMN_SCANDATE, wifi.get_scandate());
        values.put(COLUMN_BANDWITH, wifi.get_bandwith());
        values.put(COLUMN_LATENCY, wifi.get_latency());

        SQLiteDatabase db = this.getWritableDatabase();

        Logger.getGlobal().log(Level.SEVERE, "JOWJDQOWJDOQWJODQJWODJQWDOQWJDOQWJDOQWJDOQWJDOQJWODQJWODQWJDOQWJDOQWJDOQWJDOQWJDOQWJDQOWJDOQWQWDOJQWODQJWODJQWODJQWODJQOWDJQWODJQOW");
        db.insert(TABLE_WIFI, null, values);
        db.close();
    }

    public Wifi findWIFI(String ssid) {
        String query = "Select * FROM " + TABLE_WIFI + " WHERE " + COLUMN_SSID + " =  \"" + ssid + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Wifi wifi = new Wifi();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            wifi.set_wifiId(Integer.parseInt(cursor.getString(0)));
            wifi.set_db(Integer.parseInt(cursor.getString(1)));
            wifi.set_ssid(cursor.getString(2));
            wifi.set_mac(cursor.getString(3));
            wifi.set_longitude(Float.parseFloat(cursor.getString(4)));
            wifi.set_latitude(Float.parseFloat(cursor.getString(5)));
            wifi.set_altitude(Float.parseFloat(cursor.getString(6)));
            wifi.set_scandate(cursor.getString(7));
            wifi.set_bandwith(Float.parseFloat(cursor.getString(8)));
            wifi.set_latency(Integer.parseInt(cursor.getString(9)));
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


}
