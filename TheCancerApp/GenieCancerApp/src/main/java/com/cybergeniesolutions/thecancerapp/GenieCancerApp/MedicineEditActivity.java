package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by sadafk on 30/01/2017.
 */
public class MedicineEditActivity extends AppCompatActivity{

    private EditText nameText;
    private EditText unitText;
    private Button startDateButton;
    private EditText instructionText;
    private Button sunButton;
    private Button monButton;
    private Button tueButton;
    private Button wedButton;
    private Button thuButton;
    private Button friButton;
    private Button satButton;
    private Button noOfTimesButton;
    private TextView repeatTextView;

    private static Tracker mTracker;


    private Long rowId;
    private DataBaseHelper dbHelper;
    private static final String TAG = "MedicineEditActivity";

    private static final int DATE_PICKER_DIALOG_START_DATE = 0;
    //private static final int DATE_PICKER_DIALOG_END_DATE = 1;
    private Calendar startDatecal;
    //private Calendar endDatecal;
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private LinearLayoutManager lLayout;
    MedicineListAdapter rcAdapter;
    List<MedicineListRow> list = new ArrayList<>();
    boolean daysSelected[] = new boolean[7];
    String radioButtonSelected;
    private String reminderSetting;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_edit);
        dbHelper = new DataBaseHelper(this);
        context = getApplicationContext();

        nameText = (EditText) findViewById(R.id.med_name_edit);
        unitText = (EditText) findViewById(R.id.med_unit_edit);
        instructionText = (EditText) findViewById(R.id.med_instructions_edit);
        startDateButton = (Button) findViewById(R.id.med_start_date_edit);
        noOfTimesButton = (Button) findViewById(R.id.med_no_of_days_edit);
        sunButton = (Button) findViewById(R.id.med_sun_edit);
        monButton = (Button) findViewById(R.id.med_mon_edit);
        tueButton = (Button) findViewById(R.id.med_tue_edit);
        wedButton = (Button) findViewById(R.id.med_wed_edit);
        thuButton = (Button) findViewById(R.id.med_thu_edit);
        friButton = (Button) findViewById(R.id.med_fri_edit);
        satButton = (Button) findViewById(R.id.med_sat_edit);
        repeatTextView = (TextView) findViewById(R.id.med_repeat_edit);

        radioButtonSelected = "Daily";

        Bundle extras = getIntent().getExtras();
        rowId = extras.getLong(DataBaseHelper.KEY_ROWID);


        startDatecal =Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForStartDateButton = dateFormat.format(startDatecal.getTime());

        startDateButton.setText(dateForStartDateButton);

        lLayout = new LinearLayoutManager(MedicineEditActivity.this){
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };


        RecyclerView rView = (RecyclerView) findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        rcAdapter = new MedicineListAdapter(MedicineEditActivity.this, list);
        rView.setAdapter(rcAdapter);

        rowId = savedInstanceState != null ? savedInstanceState.getLong(DataBaseHelper.KEY_ROWID)
                : null;

        registerButtonListenersAndSetDefaultText();

        Log.v(TAG, "In MedicineEditActvity onCreate()");

        for(int i= 0; i < 7; i++)
        {
            daysSelected[i] = true;
        }
        repeatTextView.setText("sun,mon,tue,wed,thu,fri,sat");

        noOfTimesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "noOfTimesButton clicked");
                showDialogForNoOfTimes();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(saveState()) {
                    finish();
                }
            }
        });

    }

    private void showDialogForNoOfTimes() {

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.med_times_dialogbox, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set list_item_edit_prompt to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        userInput.setText("");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String input = userInput.getText().toString();
                                if (!input.equals("") || !input.isEmpty()) {
                                    noOfTimesButton.setText(userInput.getText().toString());
                                    int rowsNeeded = Integer.parseInt(noOfTimesButton.getText().toString());
                                    Log.v(TAG, "rowsNeeded= " + rowsNeeded);
                                    if (rowsNeeded != list.size()) {
                                        list.clear();
                                        for (int i = 0; i < rowsNeeded; i++) {
                                            MedicineListRow medicineListRow = new MedicineListRow("08:00", "1", 0, 0);
                                            list.add(medicineListRow);
                                            Log.v(TAG, "row added");
                                        }
                                    }
                                    rcAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(MedicineEditActivity.this, "Please enter number of times medicine needs to be taken per day.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void setRowIdFromIntent() {
        if (rowId == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null)
                Log.v(TAG,"in setRowIdFromIntent() page ="+extras.getString("Page"));
            Log.v(TAG,"in setRowIdFromIntent() rowId =" + extras.getLong(DataBaseHelper.KEY_ROWID));
            if ((extras.getString("Page")).equals("Edit"))
                rowId = extras.getLong(DataBaseHelper.KEY_ROWID);
            else {
                rowId = null;
            }
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
        long med_id = 0;
        reminderSetting = dbHelper.fetchSetting("Medicines_Reminders");
        Log.v(TAG, "in populateFields() reminderSetting =" + reminderSetting);
        if (rowId != null) {

            Cursor c = dbHelper.fetchMedicine(rowId);
            Log.v(TAG, "in populateFields() rowid =" + rowId);
            startManagingCursor(c);
            if (c != null) {

                if (c.moveToFirst()){
                    med_id = c.getLong(c.getColumnIndexOrThrow(DataBaseHelper.KEY_ROWID));
                    nameText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.M_NAME)));
                    noOfTimesButton.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.M_NO_OF_TIMES)));
                    unitText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.M_UNIT)));
                    instructionText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.M_INSTRUCTIONS)));
                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date startDate = null;
                    try {
                        String startdateString = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.M_START_DATE));
                        startDate = dateTimeFormat.parse(startdateString);
                        startDatecal.setTime(startDate);
                    } catch (ParseException e) { Log.e(TAG, e.getMessage(), e); }
                    updateDatestartButtonText();
                }
            }
            else{
            }
            c = dbHelper.fetchMedicineTimes(med_id);
            Log.v(TAG, "In MedicineEditActvity Populate()  fetching Medicine times med_id = " + med_id);
            list.clear();
            if (c != null) {
                Log.v(TAG, "In MedicineEditActvity Populate()  no of MedicineListRow = " + c.getCount());
                while (c.moveToNext()) {
                    String time = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.MT_TIME));
                    String dose = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.MT_DOSE));
                    long id = c.getLong(c.getColumnIndexOrThrow(DataBaseHelper.KEY_ROWID));
                    MedicineListRow medicineListRow = new MedicineListRow(time, dose,rowId , id);
                    list.add(medicineListRow);
                    Log.v(TAG, "In MedicineEditActvity Populate()  MedicineListRow = " + time + " " + dose);
                }
                rcAdapter.notifyDataSetChanged();
            }

            c = dbHelper.fetchMedicineDays(med_id);
            Log.v(TAG, "In MedicineEditActvity Populate()  fetching Medicine days med_id = " + med_id);
            if (c != null) {

                while (c.moveToNext()) {
                    int sat = c.getInt(c.getColumnIndexOrThrow(DataBaseHelper.MT_SAT));
                    int sun = c.getInt(c.getColumnIndexOrThrow(DataBaseHelper.MT_SUN));
                    int mon = c.getInt(c.getColumnIndexOrThrow(DataBaseHelper.MT_MON));
                    int tue = c.getInt(c.getColumnIndexOrThrow(DataBaseHelper.MT_TUE));
                    int wed = c.getInt(c.getColumnIndexOrThrow(DataBaseHelper.MT_WED));
                    int thu = c.getInt(c.getColumnIndexOrThrow(DataBaseHelper.MT_THU));
                    int fri = c.getInt(c.getColumnIndexOrThrow(DataBaseHelper.MT_FRI));
                    if(sun == 0) {
                        daysSelected[0] = false;
                        sunButton.setTextColor(getResources().getColor(R.color.colorLightGrey));
                    }
                    if(mon == 0) {
                        daysSelected[1] = false;
                        monButton.setTextColor(getResources().getColor(R.color.colorLightGrey));
                    }
                    if(tue == 0) {
                        daysSelected[2] = false;
                        tueButton.setTextColor(getResources().getColor(R.color.colorLightGrey));
                    }
                    if(wed == 0) {
                        daysSelected[3] = false;
                        wedButton.setTextColor(getResources().getColor(R.color.colorLightGrey));
                    }
                    if(thu == 0) {
                        daysSelected[4] = false;
                        thuButton.setTextColor(getResources().getColor(R.color.colorLightGrey));
                    }
                    if(fri == 0) {
                        daysSelected[5] = false;
                        friButton.setTextColor(getResources().getColor(R.color.colorLightGrey));
                    }
                    if(sat == 0) {
                        daysSelected[6] = false;
                        satButton.setTextColor(getResources().getColor(R.color.colorLightGrey));
                    }
                    setRepeatText();
                }
            }

        }else{
            list.clear();
            MedicineListRow medicineListRow = new MedicineListRow("08:00","1",0,0);
            list.add(medicineListRow);
            noOfTimesButton.setText("1");
            Log.v(TAG, "In MedicineEditActvity Populate()  adding dummy data to list");
        }

    }

    private void registerButtonListenersAndSetDefaultText() {

        startDateButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showDialog(DATE_PICKER_DIALOG_START_DATE);
            }
        });


        sunButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "sunButton clicked");
                if(daysSelected[0]) {
                    sunButton.setTextColor(getResources().getColor(R.color.colorLightGrey));
                    daysSelected[0] = false;
                }
                else
                {
                    sunButton.setTextColor(getResources().getColor(R.color.colorGenie));
                    daysSelected[0] = true;
                }
                setRepeatText();
            }
        });
        monButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "monButton clicked");
                if(daysSelected[1]) {
                    monButton.setTextColor(getResources().getColor(R.color.colorLightGrey));
                    daysSelected[1] = false;
                }
                else
                {
                    monButton.setTextColor(getResources().getColor(R.color.colorGenie));
                    daysSelected[1] = true;
                }
                setRepeatText();
            }
        });
        tueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "tueButton clicked");
                if(daysSelected[2]) {
                    tueButton.setTextColor(getResources().getColor(R.color.colorLightGrey));
                    daysSelected[2] = false;
                }
                else
                {
                    tueButton.setTextColor(getResources().getColor(R.color.colorGenie));
                    daysSelected[2] = true;
                }
                setRepeatText();
            }
        });
        wedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "wedButton clicked");
                if(daysSelected[3]) {
                    wedButton.setTextColor(getResources().getColor(R.color.colorLightGrey));
                    daysSelected[3] = false;
                }
                else
                {
                    wedButton.setTextColor(getResources().getColor(R.color.colorGenie));
                    daysSelected[3] = true;
                }
                setRepeatText();
            }
        });
        thuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "thuButton clicked");
                if (daysSelected[4]) {
                    thuButton.setTextColor(getResources().getColor(R.color.colorLightGrey));
                    daysSelected[4] = false;
                } else {
                    thuButton.setTextColor(getResources().getColor(R.color.colorGenie));
                    daysSelected[4] = true;
                }
                setRepeatText();
            }
        });
        friButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "friButton clicked");
                if (daysSelected[5]) {
                    friButton.setTextColor(getResources().getColor(R.color.colorLightGrey));
                    daysSelected[5] = false;
                } else {
                    friButton.setTextColor(getResources().getColor(R.color.colorGenie));
                    daysSelected[5] = true;
                }
                setRepeatText();
            }
        });

        satButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "satButton clicked");
                if (daysSelected[6]) {
                    satButton.setTextColor(getResources().getColor(R.color.colorLightGrey));
                    daysSelected[6] = false;
                } else {
                    satButton.setTextColor(getResources().getColor(R.color.colorGenie));
                    daysSelected[6] = true;
                }
                setRepeatText();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(DataBaseHelper.KEY_ROWID, rowId);
    }

    private boolean saveState() {
        String name = nameText.getText().toString();
        String unit = unitText.getText().toString();
        String instruction = instructionText.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String startDate = dateFormat.format(startDatecal.getTime());
        String noOfTimes = noOfTimesButton.getText().toString();

        Log.v(TAG, "in saveState() rowid =" + rowId);

        if (name.isEmpty()) {
            Toast.makeText(MedicineEditActivity.this, "Please enter medicine name",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (rowId == null) {
                long id = dbHelper.addMedicine(name, unit, startDate, noOfTimes, instruction, radioButtonSelected);
                Log.v(TAG, "in saveState() adding new med rowid  =" + id);

                for (int i = 0; i < list.size(); i++) {
                    MedicineListRow listItem = list.get(i);
                    Log.v(TAG, "In saveState() listItem.getDose " + listItem.getDose());
                    Log.v(TAG, "In saveState() listItem.getTime " + listItem.getTime());
                    Log.v(TAG, "In saveState() rowid " + id);
                    long medTimesId = dbHelper.addMedicineTime(listItem.getDose(), listItem.getTime(), id);
                    Log.v(TAG, "In saveState() medicine time added ");
                    String time = listItem.getTime();
                    String hour = time.substring(0, 2);
                    String min = time.substring(time.length() - 2);
                    startDatecal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                    startDatecal.set(Calendar.MINUTE, Integer.parseInt(min));
                    Log.v(TAG, "In saveState() hour = " + hour);
                    Log.v(TAG, "In saveState() min = " + min);

                    if (reminderSetting.equals("ON")) {
                        setAlarm(medTimesId+100,id);
                        Log.v(TAG, "In saveState() set alarm");
                    }

                }

                dbHelper.addMedicineDays(daysSelected, id);

                if (id > 0) {
                    rowId = id;
                }
                return true;
            } else {
                dbHelper.updateMedicine(rowId, name, unit, startDate, noOfTimes, instruction, radioButtonSelected);
                Cursor c = dbHelper.fetchMedicineTimes(rowId);
                while(c.moveToNext()){
                    long medId = c.getLong(c.getColumnIndexOrThrow(DataBaseHelper.KEY_ROWID));
                    cancelAlarm(medId+100);
                }

                dbHelper.updateMedicineDays(daysSelected, rowId);
                dbHelper.deleteMedicineTimes(rowId);

                for (int i = 0; i < list.size(); i++) {
                    MedicineListRow listItem = list.get(i);
                    long medTimeId = dbHelper.addMedicineTime(listItem.getDose(), listItem.getTime(), rowId);
                    String time = listItem.getTime();
                    String hour = time.substring(0, 2);
                    String min = time.substring(time.length() - 2);
                    startDatecal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                    startDatecal.set(Calendar.MINUTE, Integer.parseInt(min));
                    Log.v(TAG, "In saveState() hour = " + hour);
                    Log.v(TAG, "In saveState() min = " + min);

                    if (reminderSetting.equals("ON")) {
                        setAlarm(medTimeId+100,rowId);
                        Log.v(TAG, "In saveState() set alarm");
                    }

                }
                return true;
            }

        }
    }



    private void setRepeatText(){
        String repeat = "";
        if( daysSelected[0])
            repeat = repeat + "sun,";
        if( daysSelected[1])
            repeat = repeat + "mon,";
        if( daysSelected[2])
            repeat = repeat + "tue,";
        if( daysSelected[3])
            repeat = repeat + "wed,";
        if( daysSelected[4])
            repeat = repeat + "thu,";
        if( daysSelected[5])
            repeat = repeat + "fri,";
        if( daysSelected[6])
            repeat = repeat + "sat,";
        if (repeat != null && repeat.length() > 0) {
            repeat = repeat.substring(0, repeat.length()-1);
        }
        repeatTextView.setText(repeat);
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
        long alarmMillis = startDatecal.getTimeInMillis();
        Calendar now = Calendar.getInstance();
        if (startDatecal.before(now))
            alarmMillis+= 86400000L;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmMillis, AlarmManager.INTERVAL_DAY, broadcast);
    }

    private void cancelAlarm(long id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, (int)id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(broadcast);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DATE_PICKER_DIALOG_START_DATE:
                return showStartDatePicker();
            //case DATE_PICKER_DIALOG_END_DATE:
            //    return showEndDatePicker();
        }
        return super.onCreateDialog(id);
    }

    private DatePickerDialog showStartDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(MedicineEditActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                        startDatecal.set(Calendar.YEAR, year);
                        startDatecal.set(Calendar.MONTH, monthOfYear);
                        startDatecal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDatestartButtonText();
                    }
                },
                startDatecal.get(Calendar.YEAR), startDatecal.get(Calendar.MONTH),	startDatecal.get(Calendar.DAY_OF_MONTH));
        return datePicker;
    }


    private void updateDatestartButtonText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(startDatecal.getTime());
        startDateButton.setText(dateForButton);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

    }

}
