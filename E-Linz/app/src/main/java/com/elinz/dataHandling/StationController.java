package com.elinz.dataHandling;
/**
 * @file StationController.java
 * @brief Contains only the Class "StationController"
 */

import android.content.Context;
import android.location.Location;

import com.elinz.app.EStation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @class StationController
 * @author sebastian
 * @brief Contains the available EStations and returns it in wished order
 * Contains also the 2 helper-classes "ByNameComparator" and "ByLocationComparator"
 */
public class StationController {
    private static StationController ourInstance;

    Context con;
    ArrayList<EStation> list;
    /**
     * @brief Static method to return an instance of StationController
     */
    public static StationController getInstance(Context con) {
        if (ourInstance == null) {
            ourInstance = new StationController(con);
        }
        return ourInstance;
    }

    /**
     * @brief Another static method to return an instance of StationController
     */
    public static StationController getInstance(Context con, String filename) {
        if (ourInstance == null) {
            ourInstance = new StationController(con,filename);
        }else{
           ourInstance.addFromFile(con,filename);
        }
        return ourInstance;
    }

    /**
     * @brief Private constructor
     */
    private StationController(Context con) {
        this.con = con;

        if(list==null) {
            list = ImportController.getInstance().readStations(con,FileTable.STATIONS);
            list.addAll(ImportController.getInstance().readStations(con,FileTable.STATIONS_OSM));
        }
        removeDuplicates();

    }
    /**
     * @brief Another private constructor
     */
    private StationController(Context con, String filename) {
        this.con = con;

        if(list==null) {

            list = ImportController.getInstance().readStations(con,filename);
            System.out.println("After List Length:"+list.size());

        }else{

            System.out.println("Before List Length:"+list.size());
            list.addAll(ImportController.getInstance().readStations(con,filename));
            System.out.println("After After List Length:"+list.size());
        }
        removeDuplicates();
    }
    /**
     * @brief Adds the EStation from a specific file to the list of EStations
     */
    public void addFromFile(Context con, String filename){
        this.con = con;

        if(list==null) {
            list = ImportController.getInstance().readStations(con,filename);
        }else{
            list.addAll(ImportController.getInstance().readStations(con,filename));
        }
        removeDuplicates();

    }


    /**
     * @brief Getter-method to return the EStations sorted by Name
     */
    public ArrayList<EStation> getStations() {
        Collections.sort(list, new ByNameComparator());
        removeDuplicates();
        return list;
    }
    /**
     * @brief Getter-method to return the EStations sorted by distance
     */
    public ArrayList<EStation> getStations(double lon, double lat) {
        Collections.sort(list, new ByLocationComparator(lon, lat));
        removeDuplicates();
        return list;
    }
    /**
     * @brief Private Comparator "ByNameComparator" to compare the Name of 2 EStations
     */
    private class ByNameComparator implements Comparator<EStation> {
        @Override
        public int compare(EStation e1, EStation e2) {
            return e1.getName().compareTo(e2.getName());
        }
    }
    /**
     * @brief Private Comparator "ByLocationComparator" to compare the Position of 2 EStations
     */
    private class ByLocationComparator implements Comparator<EStation> {
        private double lon;
        private double lat;

        public ByLocationComparator(double lon, double lat) {
            super();
            this.lon = lon;
            this.lat = lat;
        }

        @Override
        public int compare(EStation e1, EStation e2) {
            e1.setDistance(calcdistance(e1.getLatitude(), e1.getLongitude(), lat, lon));
            e2.setDistance(calcdistance(e2.getLatitude(), e1.getLongitude(), lat, lon));
            if (e1.getDistance()<e2.getDistance()) {
                return -1;
            }
            if (e1.getDistance()>e2.getDistance()) {
                return 1;
            }

            return 0;
        }

        public int distFrom(double lat1, double lng1, double lat2, double lng2) {
            double earthRadius = 3958.75;
            double dLat = Math.toRadians(lat2-lat1);
            double dLng = Math.toRadians(lng2-lng1);
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(dLng/2) * Math.sin(dLng/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double dist = earthRadius * c;

            int meterConversion = 1609;
            //  double zahl_gerundet=Math.rint(zahl*100)/100;

            return (int) (dist * meterConversion/1000);
        }

        public double calcdistance(double lat1, double lon1, double lat2, double lon2) {
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;
            return Math.rint(dist*100)/100;
        }

        /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
        private double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
        private double rad2deg(double rad) {
            return (rad * 180.0 / Math.PI);
        }
    }

    /**
     * @brief Method to remove duplicates from the list of EStations
     * @todo implement method to find and remove Duplicates
     */
    private void removeDuplicates(){
        HashMap<String, EStation> hashSet = new HashMap<String, EStation>();


        //remove with same ID
        for (EStation e : list) {
            if (!hashSet.containsKey(e.getId())) {
                hashSet.put(e.getId(), e);
            }
        }
        list.clear();
        list.addAll(hashSet.values());


        //remove Stations closer than 5 Meter

        for (int i = 0; i<list.size();i++) {
            for(int x = i+1; x<list.size();x++){
                float [] dist = new float[1];
                Location.distanceBetween(list.get(i).getLatitude(),list.get(i).getLongitude(),list.get(x).getLatitude(),list.get(x).getLongitude(),dist);
                if(dist[0]<7){
                    System.out.println("Distance between Points closer than 7 m: "+dist[0]+", duplicated Stations");
                    System.out.println(list.get(i).toString());
                    System.out.println(list.get(x).toString());
                    removeDuplicate(list.get(i),list.get(x));
                }
            }
        }
    }

    private boolean removeDuplicate(EStation a, EStation b){
        if(a.getName().equals("E-Ladestation")&&b.getName().equals("E-Ladestation")){
            if(a.getSocketTypes().size()>b.getSocketTypes().size()){
                list.remove(getIndexInList(b));return true;
            }else{
                list.remove(getIndexInList(a));return true;
            }
        }else{
            if(a.getName().equals("E-Ladestation")){list.remove(getIndexInList(a));return true;}
            if(b.getName().equals("E-Ladestation")){list.remove(getIndexInList(b));return true;}

            if(a.getSocketTypes().size()>b.getSocketTypes().size()){
                list.remove(getIndexInList(b));return true;
            }else{
                list.remove(getIndexInList(a));return true;
            }
        }
    }

    private int getIndexInList(EStation e){
        for(int i =0; i<list.size();i++){
            if(list.get(i).getId().equals(e.getId())){return i;}
        }
        return 0;
    }

}
