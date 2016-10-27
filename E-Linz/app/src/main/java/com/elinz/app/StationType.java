package com.elinz.app;

/**
 * @file StationType.java
 * @brief Contains only the Class "StationType"
 */

import java.util.ArrayList;

/**
 * @class StationType
 * @author sebastian
 * @brief Enum to distinguish between Cars, Bikes and Bicycles;
 */
public enum StationType {
    CAR, SCOOTER, BICYCLE, TRUCK;

    public static ArrayList<String> getTypes() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Car");
        list.add("Scooter");
        list.add("Bicycle");
        list.add("Truck");

        return list;
    }
}


