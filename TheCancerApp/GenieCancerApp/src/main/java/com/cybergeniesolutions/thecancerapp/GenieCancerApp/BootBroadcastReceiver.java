package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sadafk on 11/04/2017.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    private DataBaseHelper dbHelper;
    private Calendar cal;
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String TAG = "BootBroadcastReceiver";

    @Override
    public void onReceive(Context pContext, Intent intent) {

        setAllAppointmentAlarm(pContext);
        setAllMedicineAlarms(pContext);

    }

    private void setAllMedicineAlarms(Context pContext){
        DataBaseHelper db = new DataBaseHelper(pContext);
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
                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date startDate = null;
                    try {
                        String startdateString = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.M_START_DATE));
                        startDate = dateTimeFormat.parse(startdateString);
                        cal.setTime(startDate);
                    } catch (ParseException e) { Log.e(TAG, e.getMessage(), e); }
                    String hour = time.substring(0, 2);
                    String min = time.substring(time.length() - 2);
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                    cal.set(Calendar.MINUTE, Integer.parseInt(min));

                    Log.v(TAG, "In setAllMedicineReminders() hour = " + hour);
                    Log.v(TAG, "In setAllMedicineReminders() min = " + min);

                    setAlarm(medTimesId+100,rowId, pContext);
                    Log.v(TAG, "In setAllMedicinesAlarm() set alarm");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    private void setAlarm(long medTimeId, long medId, Context pContext) {

        AlarmManager alarmManager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        notificationIntent.putExtra("type", "med");
        notificationIntent.putExtra("id", medId);
        notificationIntent.putExtra("medTimeId", medTimeId);
        PendingIntent broadcast = PendingIntent.getBroadcast(pContext, (int) medTimeId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.v(TAG, "setting alarm id=" + medTimeId);
        // alarmManager.setExact(AlarmManager.RTC_WAKEUP, startDatecal.getTimeInMillis() + (alarmDayGap * 86400000), broadcast);
        long alarmMillis = cal.getTimeInMillis();
        Calendar now = Calendar.getInstance();
        if (cal.before(now))
            alarmMillis+= 86400000L;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmMillis, AlarmManager.INTERVAL_DAY, broadcast);
    }

    private void setAllAppointmentAlarm(Context pContext){

        dbHelper = new DataBaseHelper(pContext);
        dbHelper.open();
        cal = Calendar.getInstance();
        try {
            Cursor c = dbHelper.fetchAppointmentReminders();
            if (c != null) {

                if (c.moveToFirst()) {
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

                    AlarmManager alarmManager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);

                    Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                    notificationIntent.addCategory("android.intent.category.DEFAULT");
                    notificationIntent.putExtra("type", "app");
                    notificationIntent.putExtra("id", id);

                    PendingIntent broadcast = PendingIntent.getBroadcast(pContext, (int)id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
                    //Toast.makeText(this, "Alarm is Scheduled", Toast.LENGTH_LONG).show();
                }
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        dbHelper.close();

    }
}
