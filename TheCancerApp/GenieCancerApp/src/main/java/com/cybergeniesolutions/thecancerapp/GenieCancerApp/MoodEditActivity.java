package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
 * Created by sadafk on 1/02/2017.
 */
public class MoodEditActivity extends AppCompatActivity {

    private EditText notesText;
    private Button dateButton;
    private Button timeButton;
    private Button dayButton;

    private Button happyButton;
    private Button sadButton;
    private Button mehButton;

    private String mood;
    private Long rowId;
    private DataBaseHelper dbHelper;
    private static final String TAG = "AppointmentEditActivity";

    private static final int DATE_PICKER_DIALOG = 0;
    private static final int TIME_DIALOG_ID = 1;
    private Calendar cal;
    private static final String DATE_FORMAT = "dd-MM-yyyy";

    private static Tracker mTracker;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_edit);
        dbHelper = new DataBaseHelper(this);

        notesText = (EditText) findViewById(R.id.mood_notes_edit);
        dateButton = (Button) findViewById(R.id.mood_date_edit);
        timeButton = (Button) findViewById(R.id.mood_time_edit);
        dayButton = (Button) findViewById(R.id.mood_day_edit);

        happyButton = (Button) findViewById(R.id.mood_happy_edit);
        sadButton = (Button) findViewById(R.id.mood_sad_edit);
        mehButton = (Button) findViewById(R.id.mood_meh_edit);

        Bundle extras = getIntent().getExtras();
        rowId = extras.getLong(DataBaseHelper.KEY_ROWID);


        cal =Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(cal.getTime());

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String  currentTime = df.format(cal.getTime());

        timeButton.setText(currentTime);


        int day = cal.get(Calendar.DAY_OF_WEEK);
        dayButton.setText(getDayString(day));

        dateButton.setText(dateForButton);

        mood = "";

        rowId = savedInstanceState != null ? savedInstanceState.getLong(DataBaseHelper.KEY_ROWID)
                : null;

        registerButtonListenersAndSetDefaultText();

        Log.v(TAG, "In MoodEditActvity onCreate()");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(saveState())
                    finish();
            }
        });

    }

    private void setRowIdFromIntent() {
        if (rowId == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null)
                Log.v(TAG,"in setRowIdFromIntent() page ="+extras.getString("Page"));
            Log.v(TAG,"in setRowIdFromIntent() rowId =" + extras.getLong(DataBaseHelper.KEY_ROWID));
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
        Log.v(TAG,"in onResume() rowid ="+rowId);
        setRowIdFromIntent();
        Log.v(TAG, "in onResume() rowid =" + rowId);
        try {
            populateFields();
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void populateFields() throws java.text.ParseException{
        Log.v(TAG, "in populateFields() rowid =" + rowId);
        if (rowId != null) {

            Cursor c = dbHelper.fetchMood(rowId);
            startManagingCursor(c);
            if (c != null) {

                if (c.moveToFirst()){
                    notesText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.MO_NOTES)));

                    String time = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.MO_TIME));
                    timeButton.setText(time);

                    mood = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.MO_MOOD));
                    if(mood.equals("happy")){
                        happyButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    else if(mood.equals("sad")){
                        sadButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    else if(mood.equals("meh")){
                        mehButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }


                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date date = null;
                    try {
                        String dateString = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.MO_DATE));
                        date = dateTimeFormat.parse(dateString);
                        cal.setTime(date);
                        int day = cal.get(Calendar.DAY_OF_WEEK);
                        dayButton.setText(getDayString(day));
                    } catch (ParseException e) { Log.e(TAG, e.getMessage(), e); }
                    updateDateButtonText();
                    int hour = Integer.parseInt(time.substring(0,2));
                    int min = Integer.parseInt(time.substring(time.length() - 2));
                    Log.v(TAG,"populating time ="+hour +":"+min);
                    cal.set(Calendar.HOUR_OF_DAY, hour);
                    cal.set(Calendar.MINUTE, min);
                }
            }
        }
    }

    private void registerButtonListenersAndSetDefaultText() {

        dateButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showDialog(DATE_PICKER_DIALOG);
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        happyButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                mood = "smile";
                happyButton.setBackground(getResources().getDrawable(R.drawable.smile_teal));
                mehButton.setBackground(getResources().getDrawable(R.drawable.neutral));
                sadButton.setBackground(getResources().getDrawable(R.drawable.frown));
            }
        });

        sadButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                mood = "frown";
                sadButton.setBackground(getResources().getDrawable(R.drawable.frown_teal));
                mehButton.setBackground(getResources().getDrawable(R.drawable.neutral));
                happyButton.setBackground(getResources().getDrawable(R.drawable.smile));
            }
        });

        mehButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                mood = "neutral";
                mehButton.setBackground(getResources().getDrawable(R.drawable.neutral_teal));
                sadButton.setBackground(getResources().getDrawable(R.drawable.frown));
                happyButton.setBackground(getResources().getDrawable(R.drawable.smile));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(DataBaseHelper.KEY_ROWID, rowId);
    }

    private boolean saveState() {
        String notes = notesText.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String date = dateFormat.format(cal.getTime());

        int day = cal.get(Calendar.DAY_OF_WEEK);
        String dayString = getDayString(day);

        String time = (String) timeButton.getText();

        Log.v(TAG, "in saveState() rowid =" + rowId);
        Log.v(TAG, "In saveState() date=" + date);

        if(mood.isEmpty()){
            Toast.makeText(MoodEditActivity.this, getString(R.string.mood_empty_field_message),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else
        {
            if (rowId == null) {
                long id = dbHelper.addMood(date,time,dayString,mood,notes);

                if (id > 0) { rowId = id; }
                return true;
            } else {
                dbHelper.updateMood(rowId,time,date,dayString,mood,notes);
                return true;
            }

        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DATE_PICKER_DIALOG:
                return showDatePicker();
            case TIME_DIALOG_ID:
                return showTimePicker();
        }
        return super.onCreateDialog(id);
    }

    private DatePickerDialog showDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(MoodEditActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateButtonText();
                        int day = cal.get(Calendar.DAY_OF_WEEK);
                        dayButton.setText(getDayString(day));
                    }
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),	cal.get(Calendar.DAY_OF_MONTH));
        return datePicker;
    }

    private TimePickerDialog showTimePicker() {
        TimePickerDialog timePicker = new TimePickerDialog(MoodEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                cal.set(Calendar.HOUR_OF_DAY, selectedHour );
                cal.set(Calendar.MINUTE, selectedMinute);
                String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                timeButton.setText(time);
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);//Yes 24 hour time
        return timePicker;
    }



    private void updateDateButtonText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(cal.getTime());
        dateButton.setText(dateForButton);
    }

    private String getDayString(int day){

        String dayString = "";
        switch (day) {
            case Calendar.SUNDAY: {
                dayString = "Sunday";
                break;
            }
            case Calendar.MONDAY:{
                dayString = "Monday";
                break;
        }
            case Calendar.TUESDAY:{
                dayString = "Tuesday";
                break;
            }
            case Calendar.WEDNESDAY:{
                dayString = "Wednesday";
                break;
            }
            case Calendar.THURSDAY:{
                dayString = "Thursday";
                break;
            }
            case Calendar.FRIDAY:{
                dayString = "Friday";
                break;
            }
            case Calendar.SATURDAY:{
                dayString = "Saturday";
                break;
            }
        }
        return dayString;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

    }
}
