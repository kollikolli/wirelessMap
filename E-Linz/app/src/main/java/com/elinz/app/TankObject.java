package com.elinz.app;

/**
 * Created by sebastian on 16.06.14.
 */
public class TankObject {

    private String id;
    private String name;
    private StationType type;
    private int kwh;
    private int km;


    public TankObject(String id, String name, StationType type, int kwh, int km) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.kwh = kwh;
        this.km = km;
    }

    public TankObject() {
        this.id = "-1";
        this.name = "dummy";
        this.kwh = 0;
        this.km = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StationType getType() { return type; }

    public void setType(StationType type) { this.type = type; }

    public int getKwh() {
        return kwh;
    }

    public void setKwh(int kwh) {
        this.kwh = kwh;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Type: " + type + ", KwH: " + kwh + ", KM: " + km + ";";
    }
}
