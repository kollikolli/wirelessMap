package at.jku.pervasive.wirelessmap.model;

/**
 * Created by hari on 27.10.16.
 */

public class Bluetooth {

    private int _bluetoothId;
    private int _db;
    private String _name;
    private String _mac;
    private long _scandate;
    private int _deviceclass;
    private int _bondstate;

    public Bluetooth() {

    }
    public Bluetooth(String name) {
        this._name = name;
    }

    public Bluetooth(int db, String ssid, String mac, long scandt, float bandwith, int latency, int _deviceclass, int _bondstate) {
        this.set_db(db);
        this.set_name(ssid);
        this.set_mac(mac);
        this.set_scandate(scandt);
        this.set_deviceclass(_deviceclass);
        this.set_bondstate(_bondstate);
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

    public long get_scandate() {
        return _scandate;
    }

    public void set_scandate(long _scandate) {
        this._scandate = _scandate;
    }


    public int get_deviceclass() {
        return _deviceclass;
    }

    public void set_deviceclass(int _deviceclass) {
        this._deviceclass = _deviceclass;
    }

    public int get_bondstate() {
        return _bondstate;
    }

    public void set_bondstate(int _bondstate) {
        this._bondstate = _bondstate;
    }
}
