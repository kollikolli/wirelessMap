package com.elinz.dataHandling;

/**
 * @file JSONParser.java
 * @brief Contains only the Singleton-Class "JSONParser"
 */

import com.elinz.app.EStation;
import com.elinz.app.TankObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @class JSONParser
 * @author sebastian
 * @brief Converts lists of EStations to JSON-Strings and vice versa
 */
public class JSONParser {
    private static JSONParser ourInstance = new JSONParser();

    /**
     * @brief Static method to return an instance of JSONParser
     */
    public static JSONParser getInstance() {
        return ourInstance;
    }

    /**
     * @brief Private constructor
     */
    private JSONParser() {
    }

    /**
     * @brief Transforms an arraylist of EStations to a JSON-String
     */
    public String getStationsToJSON(ArrayList<EStation> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    /**
     * @brief Transforms a JSON-String to an arraylist of EStations
     */
    public ArrayList<EStation> getJSONToStations(String json) {
        ArrayList<EStation> list = new ArrayList<EStation>();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<EStation>>(){}.getType();
        list = gson.fromJson(json, collectionType);
        return list;
    }

    /**
     * @brief Transforms an arraylist of TankObject to a JSON-String
     */
    public String getTankObjsToJSON(ArrayList<TankObject> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    /**
     * @brief Transforms a JSON-String to an arraylist of TankObject
     */
    public ArrayList<TankObject> getJSONToTankObjs(String json) {
        ArrayList<TankObject> list = new ArrayList<TankObject>();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<TankObject>>(){}.getType();
        list = gson.fromJson(json, collectionType);
        return list;
    }
}
