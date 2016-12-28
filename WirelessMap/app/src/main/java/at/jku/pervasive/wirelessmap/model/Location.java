package at.jku.pervasive.wirelessmap.model;


public class Location {

    private long scandate;
    private double longitude;
    private double altitude;
    private double latitude;
    private double accuracy;

    public Location() {

    }

    public Location(double altitude, double latitude, double longitude, double accuracy, long scandate) {
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.scandate = scandate;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getScandate() {
        return scandate;
    }

    public void setScandate(long scandate) {
        this.scandate = scandate;
    }
}

