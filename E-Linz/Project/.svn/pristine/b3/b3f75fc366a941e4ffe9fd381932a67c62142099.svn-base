package com.elinz.app;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by sebastian on 01.04.14.
 */

import java.util.LinkedList;

/**
 * Created by sebastian on 01.04.14.
 */
public class EStation {

    private String id;
    private String name;
    private double longitude;
    private double latitude;
    private String position;
    private LinkedList<StationType> type;

    // Constructors
    public EStation() {
        id = null;
        name = "default name";
        longitude = 0.0;
        latitude = 0.0;
        type = new LinkedList<StationType>();
    }

    public EStation(String id, String name, double longitude, double latitude, LinkedList<StationType> type) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
    }

    public EStation(String id, String name, double longitude, double latitude) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;

    }


    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public LinkedList<StationType> getType() {
        return type;
    }


    // Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setType(LinkedList<StationType> type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setLatLonFromPosition(String position) {

        setLatitude(Double.parseDouble(position.substring(0, position.indexOf(" ", 0))));
        setLongitude(Double.parseDouble(position.substring(position.indexOf(" ", 0)+1,position.lastIndexOf(" "))));

    }

    public void setLatFromString(String position) {

        this.latitude = Double.parseDouble(position);

    }

    public void setLonFromString(String position) {

        this.longitude = Double.parseDouble(position);

    }

    public boolean isEqual(EStation f) {



        if(round(this.latitude)==round(f.getLatitude())&&round(this.longitude)==round(f.getLongitude())){
            return true;
        }

        return false;
    }

    private static double round(double s){

        double x = (Math.round(s*100000));
        return (x/100000);
    }

    public JSONObject getJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", this.getId());
            object.put("name", this.getName());
            object.put("latitude", this.getLatitude());
            object.put("longitude", this.getLongitude());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }


}

