package at.jku.pervasive.wirelessmap.model;

/**
 * Created by hari on 27.10.16.
 */

public class Wifi {

    private int _wifiId;
    private int _db;
    private String _ssid;
    private String _mac;
    private long _scandate;
    private float _bandwith;
    private int _latency;
    private int _frequency;
    private String _capabilities;
    private int _channelWidth;

    public Wifi() {

    }

    public Wifi(int _db, String _ssid, String _mac, long _scandate, int _frequency, String _capabilities, int _channelWidth) {
        this._db = _db;
        this._ssid = _ssid;
        this._mac = _mac;
        this._scandate = _scandate;
        this._frequency = _frequency;
        this._capabilities = _capabilities;
        this._channelWidth = _channelWidth;
    }

    public Wifi(String ssid) {
        this._ssid = ssid;
    }

    public Wifi(int wifiId, int db, String ssid, String mac, long scandt, float bandwith, int latency) {
        this.set_wifiId(wifiId);
        this.set_db(db);
        this.set_ssid(ssid);
        this.set_mac(mac);
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

    public long get_scandate() {
        return _scandate;
    }

    public void set_scandate(long _scandate) {
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


    public int get_frequency() {
        return _frequency;
    }

    public void set_frequency(int _frequency) {
        this._frequency = _frequency;
    }

    public String get_capabilities() {
        return _capabilities;
    }

    public void set_capabilities(String _capabilities) {
        this._capabilities = _capabilities;
    }

    public int get_channelWidth() {
        return _channelWidth;
    }

    public void set_channelWidth(int _channelWidth) {
        this._channelWidth = _channelWidth;
    }
}
