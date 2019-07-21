package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * Created by sadafk on 21/12/2017.
 */
public class FluidIntakeEditActivity extends AppCompatActivity {

    private EditText fluidText;
    private Button dateButton;
    private Button dayButton;
    private Button addButton;
    private Button addCupButton;
    private Button addBottleButton;
    private Button addLargeBottleButton;
    private RadioGroup radioFluidGroup;
    private RadioButton radioButtonMl;
    private RadioButton radioButtonCup;
    private EditText editTextTotalFluidIntake;
    private TextView textViewFluidIntakeUnit;

    private String fluid_intake;
    private Long rowId;
    private DataBaseHelper dbHelper;
    private static final String TAG = "FluidIntakeEditActivity";

   // private static final int DATE_PICKER_DIALOG = 0;
   // private static final int TIME_DIALOG_ID = 1;
    private Calendar cal;
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private Context context;
    private int checkedRadiobuttonId;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fluid_edit);
        dbHelper = new DataBaseHelper(this);
        context = getApplicationContext();

        fluidText = (EditText) findViewById(R.id.fluid_edit);
        dateButton = (Button) findViewById(R.id.fluid_date_edit);
        dayButton = (Button) findViewById(R.id.fluid_day_edit);
        addButton = (Button) findViewById(R.id.fluid_intake_add);
        addCupButton = (Button) findViewById(R.id.fluid_intake_cup);
        addBottleButton = (Button) findViewById(R.id.fluid_intake_bottle);
        addLargeBottleButton = (Button) findViewById(R.id.fluid_intake_lrg_bottle);
        radioFluidGroup = (RadioGroup) findViewById(R.id.radioFluid);
        radioButtonMl = (RadioButton) findViewById(R.id.radioMl);
        radioButtonCup = (RadioButton) findViewById(R.id.radioCup);
        editTextTotalFluidIntake = (EditText) findViewById(R.id.fluid_intake);
        textViewFluidIntakeUnit = (TextView) findViewById(R.id.fluid_intake_unit);

        Bundle extras = getIntent().getExtras();
        rowId = extras.getLong(DataBaseHelper.KEY_ROWID);

        //Action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cal =Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(cal.getTime());

        int day = cal.get(Calendar.DAY_OF_WEEK);
        dayButton.setText(getDayString(day));

        dateButton.setText(dateForButton);

        fluid_intake = "";

        rowId = savedInstanceState != null ? savedInstanceState.getLong(DataBaseHelper.KEY_ROWID)
                : null;

        //registerButtonListenersAndSetDefaultText();

        Log.v(TAG, "In FluidIntakeEditActivity onCreate()");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(saveState())
                    finish();
            }
        });

        checkedRadiobuttonId = radioButtonMl.getId();

        radioFluidGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String intakeUnitString = "";
                String fluidTextString = editTextTotalFluidIntake.getText().toString();

                if(checkedId == radioButtonMl.getId()) {
                    //radioFluidGroup.check(radioButtonMl.getId());
                    checkedRadiobuttonId = radioButtonMl.getId();
                    intakeUnitString = context.getResources().getString(R.string.radio_ml);
                }
                else if (checkedId == radioButtonCup.getId()) {
                    //radioFluidGroup.check(radioButtonCup.getId());
                    checkedRadiobuttonId = radioButtonCup.getId();
                    intakeUnitString = context.getResources().getString(R.string.radio_cup);
                }
                editTextTotalFluidIntake.setText(fluidTextString);
                textViewFluidIntakeUnit.setText(intakeUnitString);
            }

        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //fluidText = (EditText) findViewById(R.id.fluid_edit);
                //textViewFluidIntake = (TextView) findViewById(R.id.fluid_intake);
                String s1 = fluidText.getText().toString();
                s1 = s1.replaceAll("\\s", "");
                String s2 = editTextTotalFluidIntake.getText().toString();
                s2 = s2.replaceAll("\\s", "");
                int f = Integer.parseInt(s2) + Integer.parseInt(s1);
                editTextTotalFluidIntake.setText("" + f);
            }

        });

        addCupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s2 = editTextTotalFluidIntake.getText().toString();
                s2 = s2.replaceAll("\\s", "");
                int selectedId = radioFluidGroup.getCheckedRadioButtonId();
                int f = 0;
                if(selectedId == radioButtonMl.getId())
                    f = Integer.parseInt(s2) + 250;
                else if(selectedId == radioButtonCup.getId())
                    f = Integer.parseInt(s2) + 1;

                editTextTotalFluidIntake.setText("" + f);
            }

        });

        addBottleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s2 = editTextTotalFluidIntake.getText().toString();
                s2 = s2.replaceAll("\\s", "");
                int selectedId = radioFluidGroup.getCheckedRadioButtonId();
                int f = 0;
                if(selectedId == radioButtonMl.getId())
                    f = Integer.parseInt(s2) + 500;
                else if(selectedId == radioButtonCup.getId())
                    f = Integer.parseInt(s2) + 2;

                editTextTotalFluidIntake.setText("" + f);
            }

        });

        addLargeBottleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s2 = editTextTotalFluidIntake.getText().toString();
                s2 = s2.replaceAll("\\s", "");
                int selectedId = radioFluidGroup.getCheckedRadioButtonId();
                int f = 0;
                if(selectedId == radioButtonMl.getId())
                    f = Integer.parseInt(s2) + 750;
                else if(selectedId == radioButtonCup.getId())
                    f = Integer.parseInt(s2) + 3;

                editTextTotalFluidIntake.setText("" + f);
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
        String intakeUnitString = "";

        if (rowId != null) {

            Cursor c = dbHelper.fetchFluidIntake(rowId);
            startManagingCursor(c);
            if (c != null) {

                if (c.moveToFirst()){
                    String fluidTextString = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.F_INTAKE));
                    int selectedItem = c.getInt(c.getColumnIndexOrThrow(DataBaseHelper.T_UNIT));

                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date date = null;
                    try {
                        String dateString = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.T_DATE));
                        date = dateTimeFormat.parse(dateString);
                        cal.setTime(date);
                        int day = cal.get(Calendar.DAY_OF_WEEK);
                        dayButton.setText(getDayString(day));
                    } catch (ParseException e) { Log.e(TAG, e.getMessage(), e); }
                    updateDateButtonText();

                    if(selectedItem == 0) {
                        radioFluidGroup.check(radioButtonMl.getId());
                        checkedRadiobuttonId = radioButtonMl.getId();
                        intakeUnitString = context.getResources().getString(R.string.radio_ml);
                    }
                    else if(selectedItem == 1) {
                        radioFluidGroup.check(radioButtonCup.getId());
                        checkedRadiobuttonId = radioButtonCup.getId();
                        intakeUnitString = context.getResources().getString(R.string.radio_cup);
                    }
                    editTextTotalFluidIntake.setText(fluidTextString);
                    textViewFluidIntakeUnit.setText(intakeUnitString);
                }
            }
        }
    }

/*    private void registerButtonListenersAndSetDefaultText() {

        dateButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showDialog(DATE_PICKER_DIALOG);
            }
        });

    }
*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(DataBaseHelper.KEY_ROWID, rowId);
    }

    private boolean saveState() {
        String intake = editTextTotalFluidIntake.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String date = dateFormat.format(cal.getTime());

        // get selected radio button from radioGroup
        int selectedId = radioFluidGroup.getCheckedRadioButtonId();
        if(selectedId == radioButtonMl.getId())
            selectedId = 0;
        else if(selectedId == radioButtonCup.getId())
            selectedId = 1;

        int day = cal.get(Calendar.DAY_OF_WEEK);
        String dayString = getDayString(day);

        Log.v(TAG, "in saveState() rowid =" + rowId);
        Log.v(TAG, "In saveState() date=" + date);

        if(intake.isEmpty() || intake.equals("0")){
            Toast.makeText(FluidIntakeEditActivity.this, "Please enter fluid intake to save data",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else
        {
            if (rowId == null) {
                long id = dbHelper.addFluidIntake(date, selectedId, dayString, Integer.parseInt(intake));

                if (id > 0) { rowId = id; }
                return true;
            } else {
                dbHelper.updateFluidIntake(rowId, Integer.parseInt(intake), selectedId, date, dayString);
                return true;
            }

        }

    }

 /*   @Override
   protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DATE_PICKER_DIALOG:
                return showDatePicker();
        }
        return super.onCreateDialog(id);
    }
*/
  /*  private DatePickerDialog showDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(FluidIntakeEditActivity.this,
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
    }*/

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
