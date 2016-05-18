package com.recordservice.chawls.recordservice_v1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final long REPEAT_TIME = 10*1000;  //binary value for 30000
    private Intent intent;
    public static TextView areaCodeTextView, cellIDTextView, powerConsumptionTextView, logTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        intent = new Intent(getBaseContext(), Record.class);
        logTextView = (TextView)findViewById(R.id.textLog);
        areaCodeTextView = (TextView) findViewById(R.id.textLocationAreaCode);
        cellIDTextView = (TextView) findViewById(R.id.textCellID);
        powerConsumptionTextView = (TextView) findViewById(R.id.textPowerConsumption);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Method to start the service
    public void startService(View view) {
        //Periodically starting the service with alarm manager
        Calendar calendar=Calendar.getInstance();

        PendingIntent pendingIntent=PendingIntent.getService(this,0,intent, 0);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), REPEAT_TIME, pendingIntent);
    }

    // Method to stop the service
    public void stopService(View view) {
        PendingIntent pendingIntent=PendingIntent.getService(this,0,intent, 0);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        stopService(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
