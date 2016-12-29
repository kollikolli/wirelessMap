package at.jku.pervasive.wirelessmap.model;

/**
 * Created by hari on 27.10.16.
 */

public class Cell {

    private int _cellId;
    private int _db;
    private int _gsmcellid;
    private long _scandate;
    private float _bandwith;
    private int _latency;
    private int _lac;
    private int _psc;
    private int _bslatitude;
    private int _bslongitude;
    private int _networkid;
    private int _sysid;

    public Cell() {

    }
    public Cell(int _gsmcellid) {
        this._gsmcellid = _gsmcellid;
    }

    public Cell(int db, int gsmcellid, long scandt, float bandwith, int latency, int _lac, int _psc, int _bslatitude, int _bslongitude, int _networkid, int _sysid) {
        this.set_db(db);
        this.set_gsmcellid(gsmcellid);
        this.set_scandate(scandt);
        this.set_bandwith(bandwith);
        this.set_latency(latency);
        this.set_lac(_lac);
        this.set_psc(_psc);
        this.set_bslatitude(_bslatitude);
        this.set_bslongitude(_bslongitude);
        this.set_networkid(_networkid);
        this.set_sysid(_sysid);
    }


    public int get_cellId() {
        return _cellId;
    }

    public void set_cellId(int _cellId) {
        this._cellId = _cellId;
    }

    public int get_db() {
        return _db;
    }

    public void set_db(int _db) {
        this._db = _db;
    }

    public int get_gsmcellid() {
        return _gsmcellid;
    }

    public void set_gsmcellid(int gsmcellid) {
        this._gsmcellid = gsmcellid;
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


    public int get_lac() {
        return _lac;
    }

    public void set_lac(int _lac) {
        this._lac = _lac;
    }

    public int get_psc() {
        return _psc;
    }

    public void set_psc(int _psc) {
        this._psc = _psc;
    }

    public int get_bslatitude() {
        return _bslatitude;
    }

    public void set_bslatitude(int _bslatitude) {
        this._bslatitude = _bslatitude;
    }

    public int get_bslongitude() {
        return _bslongitude;
    }

    public void set_bslongitude(int _bslongitude) {
        this._bslongitude = _bslongitude;
    }

    public int get_networkid() {
        return _networkid;
    }

    public void set_networkid(int _networkid) {
        this._networkid = _networkid;
    }

    public int get_sysid() {
        return _sysid;
    }

    public void set_sysid(int _sysid) {
        this._sysid = _sysid;
    }
}
