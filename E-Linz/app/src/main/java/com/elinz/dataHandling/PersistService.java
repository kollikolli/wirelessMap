package com.elinz.dataHandling;
/**
 * @file PersistService.java
 * @brief Contains only the Class "PersistService"
 */
import android.app.IntentService;
import android.content.Intent;

import java.io.FileOutputStream;

/**
 * @class PersistService
 * @author sebastian
 * @brief Persists a given content to a specific file
 */
public class PersistService extends IntentService {

    /**
     * @brief Constructor
     */
    public PersistService() {
        super("PersistService");
    }

    /**
     * @brief Handles the given Intent
     * Extracts the file-name and the JSON-String form the Intent-Bundle
     */
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String file = intent.getStringExtra("file");
            String json = intent.getStringExtra("json");
            persistObjects(file, json);
        }
    }

    /**
     * @brief Persists a JSON-String to the respective File given by the name of the File
     */
    private void persistObjects(String file, String json) {
        try {
            FileOutputStream outputStream;
            outputStream = openFileOutput(file, getApplicationContext().MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
