package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sadafk on 30/01/2017.
 */
public class AppointmentEditActivity extends AppCompatActivity {

    private EditText withText;
    private EditText whereText;
    private Button whenButton;
    private Button whenTimeButton;
    private Long rowId;
    private DataBaseHelper dbHelper;
    private static final String TAG = "AppointmentEditActivity";

    private static final int DATE_PICKER_DIALOG = 0;
    private static final int TIME_DIALOG_ID = 1;
    private Calendar cal;
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private String reminderSetting;
    private Context context;
    private static Tracker mTracker;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_edit);
        dbHelper = new DataBaseHelper(this);
        context = getApplicationContext();

        withText = (EditText) findViewById(R.id.app_with_edit);
        whereText = (EditText) findViewById(R.id.app_where_edit);
        whenButton = (Button) findViewById(R.id.app_when_edit);
        whenTimeButton = (Button) findViewById(R.id.app_when_time_edit);

        Bundle extras = getIntent().getExtras();
        rowId = extras.getLong(DataBaseHelper.KEY_ROWID);


        cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(cal.getTime());

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String currentTime = df.format(cal.getTime());

        whenTimeButton.setText(currentTime);

        whenButton.setText(dateForButton);

        rowId = savedInstanceState != null ? savedInstanceState.getLong(DataBaseHelper.KEY_ROWID)
                : null;

        registerButtonListenersAndSetDefaultText();

        Log.v(TAG, "In AppointmentEditActvity onCreate()");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(saveState()) {
                    showDialogSaveState();
                }
            }
        });

    }

    private void setRowIdFromIntent() {
        if (rowId == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null)
                Log.v(TAG, "in setRowIdFromIntent() page =" + extras.getString("Page"));
            Log.v(TAG, "in setRowIdFromIntent() rowId =" + extras.getLong(DataBaseHelper.KEY_ROWID));
            if ((extras.getString("Page")).equals("Edit"))
                rowId = extras.getLong(DataBaseHelper.KEY_ROWID);
            else
                rowId = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbHelper.open();
        Log.v(TAG, "in onResume() rowid =" + rowId);
        setRowIdFromIntent();
        Log.v(TAG, "in onResume() rowid =" + rowId);
        try {
            populateFields();
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void populateFields() throws java.text.ParseException {
        Log.v(TAG, "in populateFields() rowid =" + rowId);
        reminderSetting = dbHelper.fetchSetting("Appointments_Reminders");
        Log.v(TAG, "in populateFields() reminderSetting =" + reminderSetting);
        if (rowId != null) {

            Cursor c = dbHelper.fetchAppointment(rowId);
            startManagingCursor(c);
            if (c != null) {

                if (c.moveToFirst()) {
                    withText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.A_WITH)));
                    whereText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.A_WHERE)));
                    String time = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.A_WHEN_TIME));
                    whenTimeButton.setText(time);
                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date date = null;
                    try {
                        String dateString = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.A_WHEN));
                        date = dateTimeFormat.parse(dateString);
                        cal.setTime(date);
                    } catch (ParseException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    updateDateButtonText();
                    int hour = Integer.parseInt(time.substring(0, 2));
                    int min = Integer.parseInt(time.substring(time.length() - 2));
                    Log.v(TAG, "populating time =" + hour + ":" + min);
                    cal.set(Calendar.HOUR_OF_DAY, hour);
                    cal.set(Calendar.MINUTE, min);

                }
            }
        }
    }

    private void registerButtonListenersAndSetDefaultText() {

        whenButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showDialog(DATE_PICKER_DIALOG);
            }
        });

        whenTimeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(DataBaseHelper.KEY_ROWID, rowId);
    }

    private boolean saveState() {
        String with = withText.getText().toString();
        String where = whereText.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String when = dateFormat.format(cal.getTime());
        String time = whenTimeButton.getText().toString();
        int rem = 0;
        Log.v(TAG, "in saveState() rowid =" + rowId);
        Log.v(TAG, "In saveState() when=" + when);

        if (with.isEmpty()) {
            Toast.makeText(AppointmentEditActivity.this, getString(R.string.name_empty_field_message),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (rowId == null) {
                long id = dbHelper.addAppointment(with, when, where, time, rem);

                if (id > 0) {
                    rowId = id;
                }
                if (reminderSetting.equals("ON")){
                    setAlarm(rowId);
                    Log.v(TAG, "In saveState() set alarm" + when);
                }
                return true;
            } else {
                dbHelper.updateAppointment(rowId, with, when, where, time, rem);
                if (reminderSetting.equals("ON"))
                    setAlarm(rowId);
                return true;
            }

        }

    }

    private void showDialogSaveState(){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.appointment_reminder_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set list_item_edit_prompt to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void cancelAlarm(long id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, (int)id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(broadcast);
    }

    private void setAlarm(long id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        notificationIntent.putExtra("type", "app");
        notificationIntent.putExtra("id", id);

        PendingIntent broadcast = PendingIntent.getBroadcast(this, (int)id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.v(TAG, "setting alarm id=" + id);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - (86400000), broadcast);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_DIALOG:
                return showDatePicker();
            case TIME_DIALOG_ID:
                return showTimePicker();
        }
        return super.onCreateDialog(id);
    }

    private TimePickerDialog showTimePicker() {
        TimePickerDialog timePicker = new TimePickerDialog(AppointmentEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                cal.set(Calendar.HOUR_OF_DAY, selectedHour);
                cal.set(Calendar.MINUTE, selectedMinute);
                String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                whenTimeButton.setText(time);
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);//Yes 24 hour time
        return timePicker;
    }

    private DatePickerDialog showDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(AppointmentEditActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateButtonText();
                    }
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        return datePicker;
    }

    private void updateDateButtonText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(cal.getTime());
        whenButton.setText(dateForButton);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

    }

}
