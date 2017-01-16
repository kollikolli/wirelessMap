package at.jku.pervasive.wirelessmap.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hari on 16.01.17.
 */

public class WMOptions {
    private LatLng loc;
    private String text;
    private int color;
    private int countsignals;

    public WMOptions(int countsignals, int color, String text, LatLng loc) {
        this.countsignals=countsignals;
        this.color = color;
        this.text = text;
        this.loc =loc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getCountsignals() {
        return countsignals;
    }

    public void setCountsignals(int countsignals) {
        this.countsignals = countsignals;
    }

    public LatLng getLoc() {
        return loc;
    }

    public void setLoc(LatLng loc) {
        this.loc = loc;
    }
}
