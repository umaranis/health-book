package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sadafk on 4/05/2017.
 */
public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private static final int ACTIVITY_CREATE=0;

    private DataBaseHelper db;
    private Switch appointmentRemSwitch;
    private Switch medicineRemSwitch;

    private Calendar cal;
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    boolean daysSelected[] = new boolean[7];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        Log.v(TAG, "in onCreate()");

        //Action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appointmentRemSwitch = (Switch) findViewById(R.id.appointmentReminderSwitch);
        medicineRemSwitch = (Switch) findViewById(R.id.medicineReminderSwitch);

        db = new DataBaseHelper(this.getApplicationContext());
        db.open();
        fillData();
        db.close();
        appointmentRemSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (appointmentRemSwitch.isChecked()) {
                    db.open();
                    db.updateSetting("Appointments_Reminders", "ON");
                    db.close();
                    setAllAppointmentsAlarms();
                }
                else {
                    db.open();
                    db.updateSetting("Appointments_Reminders", "OFF");
                    db.close();
                    Log.v(TAG, "cancelling all alarms option off");
                    cancelAllAppointmentsAlarms();
                }
            }
        });

        medicineRemSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (medicineRemSwitch.isChecked()) {
                    db.open();
                    db.updateSetting("Medicines_Reminders", "ON");
                    db.close();
                    setAllMedicineReminders();
                }
                else {
                    db.open();
                    db.updateSetting("Medicines_Reminders", "OFF");
                    db.close();
                    cancelAllMedicineReminders();
                }
            }
        });



    }

    private void fillData() {

        Log.v(TAG, "in filldata()");
        Cursor c = db.fetchAllSETTINGS();

        if(c != null){
            Log.v(TAG,"c is  not null number of rows = " + c.getCount());
            while (c.moveToNext()){
                Log.v(TAG,"c moved to first");
                String titleString = c.getString(c.getColumnIndex(db.S_TITLE));
                Log.v(TAG, "titleString=" + titleString);

                String valueString = c.getString(c.getColumnIndex(db.S_VALUE));
                Log.v(TAG, "valueString=" + valueString);

                if(titleString.equals("Appointments_Reminders") && valueString.equals("ON"))
                    appointmentRemSwitch.setChecked(true);

                if(titleString.equals("Medicines_Reminders") && valueString.equals("ON"))
                    medicineRemSwitch.setChecked(true);


            }
        }else{
            Log.v(TAG,"c is null");
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case android.R.id.home:{
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAllMedicineReminders(){

        db.open();
        cal = Calendar.getInstance();
        try {
            Cursor cMed = db.fetchAllMedicines();
            while(cMed.moveToNext()) {
                long rowId = cMed.getLong(cMed.getColumnIndexOrThrow(DataBaseHelper.KEY_ROWID));
                Log.v(TAG, "In setAllMedicineReminders() rowid " + rowId);
                Cursor c = db.fetchMedicineTimes(rowId);
                while(c.moveToNext()) {
                    String time = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.MT_TIME));
                    long medTimesId = c.getLong(c.getColumnIndexOrThrow(DataBaseHelper.KEY_ROWID));
                    String hour = time.substring(0, 2);
                    String min = time.substring(time.length() - 2);
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                    cal.set(Calendar.MINUTE, Integer.parseInt(min));
                    Log.v(TAG, "In setAllMedicineReminders() hour = " + hour);
                    Log.v(TAG, "In setAllMedicineReminders() min = " + min);

                    setAlarm(medTimesId+100,rowId);
                    Log.v(TAG, "In setAllMedicinesAlarm() set alarm");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();

    }

    private void setAlarm(long medTimeId, long medId) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        notificationIntent.putExtra("type", "med");
        notificationIntent.putExtra("id", medId);
        notificationIntent.putExtra("medTimeId", medTimeId);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, (int) medTimeId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.v(TAG, "setting alarm id=" + medTimeId);
        // alarmManager.setExact(AlarmManager.RTC_WAKEUP, startDatecal.getTimeInMillis() + (alarmDayGap * 86400000), broadcast);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
    }


    private void cancelAllMedicineReminders(){
        db.open();
        Cursor cMed = db.fetchAllMedicines();
        while(cMed.moveToNext()) {
            long rowId = cMed.getLong(cMed.getColumnIndexOrThrow(DataBaseHelper.KEY_ROWID));
            Cursor c = db.fetchMedicineTimes(rowId);
            while (c.moveToNext()) {
                long medId = c.getLong(c.getColumnIndexOrThrow(DataBaseHelper.KEY_ROWID));
                cancelAlarm(medId + 100);
            }
        }
        db.close();

    }

    private void cancelAlarm(long id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, (int)id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(broadcast);
    }

    private void setAllAppointmentsAlarms(){
        db.open();
        cal = Calendar.getInstance();
        try {
            Cursor c = db.fetchAllAppointments();
            if (c != null) {

                while(c.moveToNext()) {
                    long id = c.getLong(c.getColumnIndexOrThrow(DataBaseHelper.KEY_ROWID));
                    String time = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.A_WHEN_TIME));
                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date date = null;
                    try {
                        String dateString = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.A_WHEN));
                        date = dateTimeFormat.parse(dateString);
                        cal.setTime(date);
                    } catch (ParseException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    int hour = Integer.parseInt(time.substring(0, 2));
                    int min = Integer.parseInt(time.substring(time.length() - 2));
                    Log.v(TAG, "populating time =" + hour + ":" + min);
                    cal.set(Calendar.HOUR_OF_DAY, hour);
                    cal.set(Calendar.MINUTE, min);

                    Calendar calendar1 = Calendar.getInstance();
                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/M/yyyy h:mm");
                    String currentDate = formatter1.format(calendar1.getTimeInMillis()+86400000);
                    String alarmDate = formatter1.format(cal.getTime());
                    Log.v(TAG, "current date" + currentDate);
                    Log.v(TAG, "alarm date" + alarmDate);
                    if(currentDate.compareTo(alarmDate)<=0) {
                        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                        Log.v(TAG, "setting alarm id" + id);
                        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                        notificationIntent.addCategory("android.intent.category.DEFAULT");
                        notificationIntent.putExtra("type", "app");
                        notificationIntent.putExtra("id", id);
                        PendingIntent broadcast = PendingIntent.getBroadcast(this, (int) id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - (86400000), broadcast);
                    }
                }
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        db.close();
    }

    private void cancelAllAppointmentsAlarms(){
        db.open();
        cal = Calendar.getInstance();
        try {
            Cursor c = db.fetchAllAppointments();
            if (c != null) {

                while(c.moveToNext()) {
                    long id = c.getLong(c.getColumnIndexOrThrow(DataBaseHelper.KEY_ROWID));
                    Log.v(TAG, "cancelling alarm id" + id);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                    notificationIntent.addCategory("android.intent.category.DEFAULT");

                    PendingIntent broadcast = PendingIntent.getBroadcast(this, (int)id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(broadcast);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        db.open();
        fillData();
        db.close();
    }
}
