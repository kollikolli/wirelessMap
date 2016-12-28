package at.jku.pervasive.wirelessmap.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import at.jku.pervasive.wirelessmap.data.DbHandler;
import at.jku.pervasive.wirelessmap.model.Cell;

/**
 * Created by kollegger on 07.11.16.
 */

public class CellService extends Service {

    private TelephonyManager mTelephonyManager;

    private PhoneStateListener mPhoneStatelistener = new PhoneStateListener(){
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            CellLocation cellLocation = mTelephonyManager.getCellLocation();
            int cellId = -1;
            if(cellLocation instanceof GsmCellLocation){
                cellId = ((GsmCellLocation) cellLocation).getCid();
            } else if(cellLocation instanceof CdmaCellLocation){
                cellId = ((CdmaCellLocation) cellLocation).getBaseStationId();
            }
            int db = (2 * signalStrength.getGsmSignalStrength()) - 113; // -> dBm

            DbHandler.getInstance().addCell(new Cell(db, cellId, System.currentTimeMillis(), 0,0));
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static final int SCAN_INTERVALL = 5000; // 5sec


    private Timer timer = new Timer();
    private boolean scanning = false;


    @Override
    public int onStartCommand(Intent arg0, int arg1, int arg2) {
        // Service should be explicitely started and stopped
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();


       /* TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();   //This will give info of all sims present inside your mobile
                if(cellInfos!=null){
                    for (int i = 0 ; i<cellInfos.size(); i++){
                        if (cellInfos.get(i).isRegistered()){
                            if(cellInfos.get(i) instanceof CellInfoWcdma){
                                CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) telephonyManager.getAllCellInfo().get(0);
                                CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                                DbHandler.getInstance().addCell(new Cell(cellSignalStrengthWcdma.getDbm(), cellInfoWcdma.getCellIdentity().getCid(), System.currentTimeMillis(), 0, 0));
                            }else if(cellInfos.get(i) instanceof CellInfoGsm){
                                CellInfoGsm cellInfogsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
                                CellSignalStrengthGsm cellSignalStrengthGsm = cellInfogsm.getCellSignalStrength();
                                DbHandler.getInstance().addCell(new Cell(cellSignalStrengthGsm.getDbm(), cellInfogsm.getCellIdentity().getCid(), System.currentTimeMillis(), 0, 0));

                            }else if(cellInfos.get(i) instanceof CellInfoLte){
                                CellInfoLte cellInfoLte = (CellInfoLte) telephonyManager.getAllCellInfo().get(0);
                                CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                                DbHandler.getInstance().addCell(new Cell(cellSignalStrengthLte.getDbm(), cellInfoLte.getCellIdentity().getCi(), System.currentTimeMillis(), 0, 0));
                            }
                        }
                    }
                }
            }
        };

        timer.schedule(tt, 0, SCAN_INTERVALL);*/
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        
        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        // stop the continous task
        if (timer != null)
            timer.cancel();
    }


}
