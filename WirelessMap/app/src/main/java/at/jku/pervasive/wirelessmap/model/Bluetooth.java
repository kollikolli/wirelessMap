package at.jku.pervasive.wirelessmap.model;

/**
 * Created by hari on 27.10.16.
 */

public class Bluetooth {

    private int _bluetoothId;
    private int _db;
    private String _name;
    private String _mac;
    private float _longitude;
    private float _latitude;
    private float _altitude;
    private String _scandate;

    public Bluetooth() {

    }
    public Bluetooth(String name) {
        this._name = name;
    }

    public Bluetooth(int wifiId, int db, String ssid, String mac, float lon, float lat, float alt, String scandt, float bandwith, int latency) {
        this.set_bluetoothId(wifiId);
        this.set_db(db);
        this.set_name(ssid);
        this.set_mac(mac);
        this.set_longitude(lon);
        this.set_latitude(lat);
        this.set_altitude(alt);
        this.set_scandate(scandt);
    }


    public int get_bluetoothId() {
        return _bluetoothId;
    }

    public void set_bluetoothId(int _bluetoothId) {
        this._bluetoothId = _bluetoothId;
    }

    public int get_db() {
        return _db;
    }

    public void set_db(int _db) {
        this._db = _db;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_mac() {
        return _mac;
    }

    public void set_mac(String _mac) {
        this._mac = _mac;
    }

    public float get_longitude() {
        return _longitude;
    }

    public void set_longitude(float _longitude) {
        this._longitude = _longitude;
    }

    public float get_latitude() {
        return _latitude;
    }

    public void set_latitude(float _latitude) {
        this._latitude = _latitude;
    }

    public float get_altitude() {
        return _altitude;
    }

    public void set_altitude(float _altitude) {
        this._altitude = _altitude;
    }

    public String get_scandate() {
        return _scandate;
    }

    public void set_scandate(String _scandate) {
        this._scandate = _scandate;
    }


}
