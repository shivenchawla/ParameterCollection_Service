package com.recordservice.chawls.recordservice_v1;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by chawls on 2/23/2016.
 */
public class Record extends Service {

    private static long counter = 0;
    private MainActivity mainActivity;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (counter < 1)
            Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        //Calling logging functions inside the service
        setPowerConsumptionTextView();
        setAreaCodeTextView();
        setLogTextView();

        // Let it continue running until it is stopped
        Toast.makeText(this, Long.toString(++counter), Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ;
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    //Logging functions:
    void setPowerConsumptionTextView() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                MainActivity.powerConsumptionTextView.setText("Battery Level Remaining: " + level + "%");
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }
    void setAreaCodeTextView() {
        final TelephonyManager telephony = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if(telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM)
        {
            final GsmCellLocation cellLocation = (GsmCellLocation)telephony.getCellLocation();
            if(cellLocation != null)
            {
                int cid = cellLocation.getCid() & 0xffff;
                int lac = cellLocation.getLac() & 0xffff;
                MainActivity.areaCodeTextView.setText("LAC: " + lac);
                MainActivity.cellIDTextView.setText(" CID: " + cid);
            }
        }
    }
    void setLogTextView(){
        Toast.makeText(this,"Logger",Toast.LENGTH_LONG).show();
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log=new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }

            //writing to log-file via writeCommand
            writeCommand("logcat -d -f","/myLogs/mylog.log");

            //writing to log-file via writeFile()
            //writeFile(log.toString(),"/myLogs/mylog.log");

            //writing to text box on screen to verify
            MainActivity.logTextView.setText(log.toString());

        } catch (IOException e) {
        }
    }
    private boolean writeCommand(String commandToExec, String fullFilePath) throws IOException {
        File filename = new File(Environment.getExternalStorageDirectory()+fullFilePath); //"/mylog.log");
        filename.createNewFile();
        String cmd = commandToExec+filename.getAbsolutePath();  //"logcat -d -f"
        Runtime.getRuntime().exec(cmd);
        return true;
    }
}