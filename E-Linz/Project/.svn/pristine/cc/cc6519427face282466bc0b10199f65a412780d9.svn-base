package com.elinz.app;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class OpenDataVersionChecker extends AsyncTask<URL, Integer, Long> {

    Context c;

    public OpenDataVersionChecker(Context context){
        c = context;
    }


    @Override
    protected Long doInBackground(URL... urls) {
        checkData();
        return null;
    }

    public void checkData(){

        JSONParser parser = new JSONParser();
        try {
            final InputStream in = new URL("http://ckan.data.linz.gv.at/api/rest/package/stromtankstellenderlinzag").openStream();

            saveFile("version.json", in);

            JSONObject jsonObject = new JSONObject(getJSONFromFile());

            String name = (String) jsonObject.get("version");
            System.out.println(name);
        }catch(JSONException e){


        }catch(MalformedURLException e){

        }catch(IOException e){

        }



    }

    private String getJSONFromFile(){

        String json = null;
        try {

            InputStream is = c.openFileInput("version.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;


    }


    private void saveFile(String filename, InputStream inputStr) throws IOException {

       FileOutputStream fos = c.getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);

        int temp = -1;
        while ((temp = inputStr.read()) != -1) {
            fos.write(temp);
        }
        fos.close();
    }

}
