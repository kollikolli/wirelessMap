package com.elinz.dataHandling;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

import java.io.FileOutputStream;

public class PersistService extends IntentService {

    public PersistService() {
        super("PersistService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String json = intent.getStringExtra("json");
            persistObjects(json);
        }
    }

    private void persistObjects(String json) {
        try {
            FileOutputStream outputStream;
            outputStream = openFileOutput(ImportController.FILENAME, getApplicationContext().MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
            Toast.makeText(getApplicationContext(), "Speichern abgeschlossen!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Speichern fehlgeschlagen!", Toast.LENGTH_SHORT).show();
        }
    }
}
