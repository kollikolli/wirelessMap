package com.elinz.app;

/**
 * @file EStation.java
 * @brief Contains only the Class "EStation"
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by sebastian on 01.04.14.
 */

import java.util.Map;

/**
 * @class EStation
 * @brief class represents logical entity for the real E-Stations
 * @author sebastian
 */

public class EStation {

    private String id;
    private String name;
    private double longitude;
    private double latitude;
    private String position;
    private double distance;
    private ArrayList<StationType> type;
    private HashMap<SocketType, Integer> socketTypes;

    /**
     * @brief default Constructor
     */
    public EStation() {
        id = null;
        name = "E-Ladestation";
        longitude = 0.0;
        latitude = 0.0;
        type = new ArrayList<StationType>();
        socketTypes = new HashMap<SocketType, Integer>();
    }

    /**
     * @brief Constructor with not-empty parameter-list
     */
    public EStation(String id, String name, double longitude, double latitude) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * @brief Constructors with long paramter-list (Station Types)
     */
    public EStation(String id, String name, double longitude, double latitude, ArrayList<StationType> type) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
    }

    /**
     * @brief Constructors with long paramter-list (StationTypes and SocketTypes)
     */
    public EStation(String id, String name, double longitude, double latitude, ArrayList<StationType> type, HashMap<SocketType, Integer> socketTypes) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
        this.socketTypes = socketTypes;
    }


    /**
     * @brief Getter-method for "ID"
     */
    public String getId() {
        return id;
    }

    /**
     * @brief Getter-method for "name"
     */
    public String getName() {
        return name;
    }

    /**
     * @brief Getter-method for "longitude"
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @brief Getter-method for "latitude"
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @brief Getter-method for "StationTypes"
     */
    public ArrayList<StationType> getType() {
        return type;
    }

    public double getDistance() { return distance; }


    /**
     * @brief Getter-method for "SocketTypes"
     */
    public HashMap<SocketType, Integer> getSocketTypes() {
        return socketTypes;
    }

    // Setters

    /**
     * @brief Setter-method for "ID"
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @brief Setter-method for "Name"
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @brief Setter-method for "Longitude"
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @brief Setter-method for "Latitude"
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @brief Setter-method for "StationTypes"
     */
    public void setType(ArrayList<StationType> type) {
        this.type = type;
    }

    /**
     * @brief Getter-method for "Position"
     */
    public String getPosition() {
        return position;
    }

    /**
     * @brief Setter-method for "Position"
     */
    public void setPosition(String position) {
        this.position = position;
    }

    public void setDistance(double dis) { this.distance = dis; }

    /**
     * @brief Setter-method for "Position" by String
     */
    public void setLatLonFromPosition(String position) {

        setLatitude(Double.parseDouble(position.substring(0, position.indexOf(" ", 0))));
        setLongitude(Double.parseDouble(position.substring(position.indexOf(" ", 0) + 1, position.lastIndexOf(" "))));

    }
    /**
     * @brief Setter-method for "Latitude" by String
     */
    public void setLatFromString(String position) {
        this.latitude = Double.parseDouble(position);
    }

    /**
     * @brief Setter-method for "Longitude" by String
     */
    public void setLonFromString(String position) {
        this.longitude = Double.parseDouble(position);
    }

    public void setSocketTypes(HashMap<SocketType, Integer> socketTypes) {
        this.socketTypes = socketTypes;
    }

    /**
     * @brief Setter-method for "SocketTypes"
     */
    public String toString() {
        String s=  "ID: " + id + "; Name: " + name + "; Lon: " + longitude + "; Lat: " + latitude;
        for(StationType t : type){
            s = s + "Typ: "+t+" ";
        }

        for (Map.Entry<SocketType, Integer> entry : socketTypes.entrySet()) {
            s = s + " SocketType: "+ entry.getKey();
            s = s + " Anzahl: "+ entry.getValue();
        }
        return s;
    }
    /**
     * @brief Checks if the Position and another EStation's Position are equal
     */
    public boolean isEqual(EStation f) {
        if (round(this.latitude) == round(f.getLatitude()) && round(this.longitude) == round(f.getLongitude())) {
            return true;
        }

        return false;
    }

    /**
     * @brief Rounds a double value
     */
    private static double round(double s) {

        double x = (Math.round(s * 100000));
        return (x / 100000);
    }
}

