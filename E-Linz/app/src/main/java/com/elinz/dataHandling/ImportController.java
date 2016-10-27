package com.elinz.dataHandling;

/**
 * @file ImportController
 * @brief Contains only the Singleton-Class "ImportController"
 */

import android.content.Context;

import com.elinz.app.EStation;
import com.elinz.app.TankObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @class ImportController
 * @author sebastian
 * @brief Reads from storage and returns the locally stored EStations
 */
public class ImportController {

    private static ImportController ourInstance = new ImportController();

    /**
     * @brief Static getter-method to return an instance of ImportController
     */
    public static ImportController getInstance() {
        return ourInstance;
    }

    /**
     * @brief Private constructor
     */
    private ImportController() {
    }

    /**
     * @brief Returns the EStations from a given file
     */
    public ArrayList<EStation> readStations(Context con, String file) {
       String json = readFromStorage(con, file);
       ArrayList<EStation> list = JSONParser.getInstance().getJSONToStations(json);
       if(list != null) {
           return list;
       } else {
           return new ArrayList<EStation>();
       }
    }

    public ArrayList<TankObject> readStatistics(Context con, String file) {
        String json = readFromStorage(con, file);
        ArrayList<TankObject> list = JSONParser.getInstance().getJSONToTankObjs(json);
        if(list != null) {
            return list;
        } else {
            return new ArrayList<TankObject>();
        }
    }

    private String readFromStorage(Context con, String file) {
        String json = "";
        try {
            FileInputStream fis;
            fis = con.openFileInput(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            json = sb.toString();

            fis.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    public boolean checkFileExistance(Context con, String fname){
        File file = con.getFileStreamPath(fname);
        return file.exists();
    }

}
