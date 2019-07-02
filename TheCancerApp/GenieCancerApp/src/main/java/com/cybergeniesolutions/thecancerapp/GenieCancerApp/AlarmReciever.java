package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by sadafk on 11/04/2017.
 */
public class AlarmReciever extends BroadcastReceiver
{
    private DataBaseHelper dbHelper;
    private static final String TAG = "MedicineEditActivity";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        String type = extras.getString("type");
        long id = extras.getLong("id");
        long medTimeId = extras.getLong("medTimeId");
        Log.v("Alarm Reciever", "onrecieve() rowid =" + id);
        if(type.equals("app")) {
            Intent notificationIntent = new Intent(context, OnRecieveAppointmentActivity.class);
            notificationIntent.putExtra("id",id);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(OnRecieveAppointmentActivity.class);
            stackBuilder.addNextIntent(notificationIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            //Vibration
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

            //LED
            builder.setLights(Color.BLUE, 3000, 3000);

            //Tone
            builder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));


            Notification notification = builder.setContentTitle("Appointment Reminder")
                    .setContentText("New Notification From GenieCanHelp..")
                    .setTicker("New Message Alert!")
                    .setSmallIcon(R.drawable.reminderlogo)
                    .setContentIntent(pendingIntent).build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify((int)id, notification);
        }
        else
        {
            dbHelper = new DataBaseHelper(context);
            dbHelper.open();
            Cursor c = dbHelper.fetchMedicineDays(id);
            c.moveToFirst();
            int sat = c.getInt(c.getColumnIndexOrThrow(DataBaseHelper.MT_SAT));
            int sun = c.getInt(c.getColumnIndexOrThrow(DataBaseHelper.MT_SUN));
            int mon = c.getInt(c.getColumnIndexOrThrow(DataBaseHelper.MT_MON));
            int tue = c.getInt(c.getColumnIndexOrThrow(DataBaseHelper.MT_TUE));
            int wed = c.getInt(c.getColumnIndexOrThrow(DataBaseHelper.MT_WED));
            int thu = c.getInt(c.getColumnIndexOrThrow(DataBaseHelper.MT_THU));
            int fri = c.getInt(c.getColumnIndexOrThrow(DataBaseHelper.MT_FRI));

            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_WEEK);
            boolean sendNotification = false;
            if(day == 1 && sun == 1)
                sendNotification = true;
            if(day==2 && mon==1)
                sendNotification = true;
            if(day==3 && tue==1)
                sendNotification = true;
            if(day==4 && wed==1)
                sendNotification = true;
            if(day==5 && thu==1)
                sendNotification = true;
            if(day==6 && fri==1)
                sendNotification = true;
            if(day==7 && sat==1)
                sendNotification = true;


            if(sendNotification) {
                Intent notificationIntent = new Intent(context, OnRecieverMedicineActivity.class);
                notificationIntent.putExtra("id", id);
                notificationIntent.putExtra("medTimeId",medTimeId);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(OnRecieverMedicineActivity.class);
                stackBuilder.addNextIntent(notificationIntent);

                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

                //Vibration
                builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

                //LED
                builder.setLights(Color.BLUE, 3000, 3000);

                //Tone
                builder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));


                Notification notification = builder.setContentTitle("Medicine Reminder")
                        .setContentText("New Notification From GenieCanHelp..")
                        .setTicker("New Message Alert!")
                        .setSmallIcon(R.drawable.reminderlogo)
                        .setContentIntent(pendingIntent).build();

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify((int)medTimeId, notification);

            }
        }

    }


}