package at.jku.pervasive.wirelessmap.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectService extends Service {


    private Timer timer = new Timer();
    private static final int NUMBER_OF_PACKTETS = 5;

    private static final int SCAN_INTERVALL = 20000; // 20sec

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent arg0, int arg1, int arg2) {
        // Service should be explicitely started and stopped
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();


        TimerTask tt = new TimerTask() {

            @Override
            public void run() {
                double latency = getLatency("velebe.de");
                long bandwith = getBandWidth("velebe.de", "wirelessmap", "wirelessmap", "file.txt", 22);
                Log.e("Latency: ", String.valueOf(latency));
                Log.e("Bandwith: ", String.valueOf(bandwith));
            }
        };

        timer.scheduleAtFixedRate(tt, 0, SCAN_INTERVALL);
    }

    private long getBandWidth(String host, String user, String password, String remoteFile, int port) {

        try
        {
            long startTime = System.currentTimeMillis();
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");
            System.out.println("Crating SFTP Channel.");
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            System.out.println("SFTP Channel created.");
            InputStream out= null;
            out= sftpChannel.get(remoteFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(out));
            while (br.readLine() != null);
            br.close();

            long endTime = System.currentTimeMillis();

            sftpChannel.disconnect();
            session.disconnect();

            return (endTime - startTime); //byte per seconds as file is 1MB
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // stop the continous task
        if (timer != null)
            timer.cancel();
    }



    public double getLatency(String ipAddress){
        String pingCommand = "/system/bin/ping -c " + NUMBER_OF_PACKTETS + " " + ipAddress;
        String inputLine = "";
        double avgRtt = 0;

        try {
            // execute the command on the environment interface
            Process process = Runtime.getRuntime().exec(pingCommand);
            // gets the input stream to get the output of the executed command
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            inputLine = bufferedReader.readLine();
            while ((inputLine != null)) {
                if (inputLine.length() > 0 && inputLine.contains("avg")) {  // when we get to the last line of executed ping command
                    break;
                }
                inputLine = bufferedReader.readLine();
            }
        }
        catch (IOException e){
            Log.e("Error", "getLatency: EXCEPTION");
        }

        // Extracting the average round trip time from the inputLine string
        String afterEqual = inputLine.substring(inputLine.indexOf("="), inputLine.length()).trim();
        String afterFirstSlash = afterEqual.substring(afterEqual.indexOf('/') + 1, afterEqual.length()).trim();
        String strAvgRtt = afterFirstSlash.substring(0, afterFirstSlash.indexOf('/'));
        avgRtt = Double.valueOf(strAvgRtt);

        return avgRtt;
    }
}
