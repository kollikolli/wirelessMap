package com.elinz.app;

/**
 * Created by hari on 27.10.16.
 */

public class WIFI {

    private int _wifiId;
    private int _db;
    private String _ssid;
    private String _mac;
    private float _longitude;
    private float _latitude;
    private float _altitude;
    private String _scandate;
    private float _bandwith;
    private int _latency;

    public WIFI() {

    }
    public WIFI(String ssid) {
        this._ssid = ssid;
    }

    public WIFI(int wifiId, int db, String ssid, String mac, float lon, float lat, float alt, String scandt, float bandwith, int latency) {
        this.set_wifiId(wifiId);
        this.set_db(db);
        this.set_ssid(ssid);
        this.set_mac(mac);
        this.set_longitude(lon);
        this.set_latitude(lat);
        this.set_altitude(alt);
        this.set_scandate(scandt);
        this.set_bandwith(bandwith);
        this.set_latency(latency);
    }


    public int get_wifiId() {
        return _wifiId;
    }

    public void set_wifiId(int _wifiId) {
        this._wifiId = _wifiId;
    }

    public int get_db() {
        return _db;
    }

    public void set_db(int _db) {
        this._db = _db;
    }

    public String get_ssid() {
        return _ssid;
    }

    public void set_ssid(String _ssid) {
        this._ssid = _ssid;
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

    public float get_bandwith() {
        return _bandwith;
    }

    public void set_bandwith(float _bandwith) {
        this._bandwith = _bandwith;
    }

    public int get_latency() {
        return _latency;
    }

    public void set_latency(int _latency) {
        this._latency = _latency;
    }


}
