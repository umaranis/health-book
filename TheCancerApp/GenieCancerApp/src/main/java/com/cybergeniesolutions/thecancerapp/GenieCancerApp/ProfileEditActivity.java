package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

/**
 * Created by sadafk on 19/01/2017.
 */
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class ProfileEditActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText heightText;
    private EditText weightText;
    private EditText bmiText;
    private EditText surfaceAreaText;
    private EditText phoneText;
    private EditText emailText;
    private EditText streetText;
    private EditText suburbText;
    private EditText stateText;
    private EditText codeText;
    private EditText parentText;
    private Button dateButton;
    private Long rowId;
    private DataBaseHelper dbHelper;
    private static final String TAG = "ProfileEditActivity";

    private static final int DATE_PICKER_DIALOG = 0;
    private Calendar cal;
    private static final String DATE_FORMAT = "dd-MM-yyyy";

    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);
        dbHelper = new DataBaseHelper(this);

        nameText = (EditText) findViewById(R.id.p_name_edit);
        heightText = (EditText) findViewById(R.id.p_height_edit);
        weightText = (EditText) findViewById(R.id.p_weight_edit);
        bmiText = (EditText) findViewById(R.id.p_bmi_edit);
        surfaceAreaText = (EditText) findViewById(R.id.p_surface_area_edit);
        phoneText = (EditText) findViewById(R.id.p_phone_edit);
        emailText = (EditText) findViewById(R.id.p_email_edit);
        parentText = (EditText) findViewById(R.id.p_parent_name_edit);
        dateButton = (Button) findViewById(R.id.p_dob);
        streetText = (EditText) findViewById(R.id.p_street_edit);
        suburbText = (EditText) findViewById(R.id.p_suburb_edit);
        stateText = (EditText) findViewById(R.id.p_state_edit);
        codeText = (EditText) findViewById(R.id.p_post_code_edit);

        cal =Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(cal.getTime());

        dateButton.setText(dateForButton);

        registerButtonListenersAndSetDefaultText();

        Log.v(TAG, "In ProfileEditActvity onCreate()");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (saveState())
                    finish();
            }
        });

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

        try {
            populateFields();
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void populateFields() throws java.text.ParseException{

        Log.v(TAG,"In populateFields()");
        Cursor c = dbHelper.fetchProfile();
        if (c != null) {

            if (c.moveToFirst()){
                nameText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.P_NAME)));
                parentText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.P_PARENT_NAME)));

                SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_FORMAT);
                Date date = null;
                try {
                    String dateString = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.P_DOB));
                    date = dateTimeFormat.parse(dateString);
                    cal.setTime(date);
                } catch (ParseException e) { Log.e(TAG, e.getMessage(), e); }

                heightText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.P_HEIGHT)));
                weightText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.P_WEIGHT)));
                bmiText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.P_BMI)));
                surfaceAreaText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.P_SURFACE_AREA)));
            }
            updateDateButtonText();

            c = dbHelper.fetchContacts("Profile");

            if(c != null){
                if (c.moveToFirst()){
                    phoneText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.C_PHONE)));
                    emailText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.C_EMAIL)));
                    streetText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.C_STREET)));
                    suburbText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.C_SUBURB)));
                    stateText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.C_STATE)));
                    codeText.setText(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.C_POST_CODE)));
                    rowId = c.getLong(c.getColumnIndexOrThrow(DataBaseHelper.KEY_ROWID));
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


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DATE_PICKER_DIALOG:
                return showDatePicker();
        }
        return super.onCreateDialog(id);
    }

    private DatePickerDialog showDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(ProfileEditActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateButtonText();
                    }
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),	cal.get(Calendar.DAY_OF_MONTH));
        return datePicker;
    }

    private void updateDateButtonText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(cal.getTime());
        dateButton.setText(dateForButton);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

    }


    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private long getId(){

        Cursor c = dbHelper.fetchProfile();
        long id = 1;
        if(c != null){
            Log.v(TAG,"c is  not null");
            if (c.moveToFirst()){
                Log.v(TAG,"c moved to first");
                id = c.getLong(c.getColumnIndex(dbHelper.KEY_ROWID));
            }
        }

        return id;
    }
/*
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {

            case android.R.id.home:{
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                return true;
            }
        }
        return super.onMenuItemSelected(featureId, item);
    }
*/
    private boolean saveState() {
        String name = nameText.getText().toString();
        String height = heightText.getText().toString();
        String weight = weightText.getText().toString();
        String bmi = bmiText.getText().toString();
        String surfaceArea = surfaceAreaText.getText().toString();
        String phone = phoneText.getText().toString();
        String email = emailText.getText().toString();
        String street = streetText.getText().toString();
        String suburb = suburbText.getText().toString();
        String state = stateText.getText().toString();
        String postCode = codeText.getText().toString();
        String parentName = parentText.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dob = 	dateFormat.format(cal.getTime());

        Log.v(TAG,"In saveState() Parent name="+parentName);
        Log.v(TAG,"In saveState() dob="+dob);

        if(name.isEmpty()){
            Toast.makeText(ProfileEditActivity.this, getString(R.string.name_empty_field_message),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else
        if(email.isEmpty() && phone.isEmpty()){
            if (rowId == null) {
                long id = dbHelper.addProfile(name, dob, "", parentName,bmi,height,weight,surfaceArea);
                dbHelper.addContact(name,phone, email, street,suburb,state,postCode,"Profile");
                Log.v(TAG,"In saveState() profile id="+id);
                if (id > 0) { rowId = id; }
                return true;
            } else {
                dbHelper.updateProfile(getId(), name, parentName,"",dob, bmi,height,weight,surfaceArea);
                dbHelper.updateProfileContact(name, phone, email,street,suburb,state,postCode,"Profile");
                return true;
            }

        }else{
            if (!email.isEmpty() && phone.isEmpty() && checkEmail(email)){
                if (rowId == null) {
                    dbHelper.addProfile(name, dob, "", parentName,bmi,height,weight,surfaceArea);
                    long id = dbHelper.addContact(name,phone, email, street,suburb,state,postCode,"Profile");
                    Log.v(TAG,"In saveState() id="+id);
                    if (id > 0) { rowId = id; }
                    return true;
                } else {
                    dbHelper.updateProfile(getId(), name, parentName,"",dob,bmi,height,weight,surfaceArea);
                    dbHelper.updateProfileContact(name, phone, email,street,suburb,state,postCode,"Profile");
                    return true;
                }

            }else if(email.isEmpty() && !phone.isEmpty() && PhoneNumberUtils.isGlobalPhoneNumber(phone)){
                if (rowId == null) {
                    dbHelper.addProfile(name, dob, "", parentName,bmi,height,weight,surfaceArea);
                    long id = dbHelper.addContact(name,phone, email, street,suburb,state,postCode,"Profile");
                    Log.v(TAG,"In saveState() id="+id);
                    if (id > 0) { rowId = id; }
                    return true;
                } else {
                    dbHelper.updateProfile(getId(), name, parentName,"",dob,bmi,height,weight,surfaceArea);
                    dbHelper.updateProfileContact(name, phone, email,street,suburb,state,postCode,"Profile");
                    return true;
                }

            }else
            if(!email.isEmpty() && !phone.isEmpty() && checkEmail(email) && PhoneNumberUtils.isGlobalPhoneNumber(phone)){

                if (rowId == null) {
                    dbHelper.addProfile(name, dob, "", parentName,bmi,height,weight,surfaceArea);
                    long id = dbHelper.addContact(name,phone, email, street,suburb,state,postCode,"Profile");
                    Log.v(TAG,"In saveState() id="+id);
                    if (id > 0) { rowId = id; }
                    return true;
                } else {
                    dbHelper.updateProfile(getId(), name, parentName,"",dob,bmi,height,weight,surfaceArea);
                    dbHelper.updateProfileContact(name, phone, email,street,suburb,state,postCode,"Profile");
                    return true;
                }

            }

            if (!checkEmail(email) && !email.isEmpty()){
                Toast.makeText(ProfileEditActivity.this, getString(R.string.email_invalid_message),
                        Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(ProfileEditActivity.this, getString(R.string.phone_invalid_message),
                        Toast.LENGTH_SHORT).show();
            }

            Log.v(TAG,"In ProfileEditActvity saveProfile is false");
            return false;

        }

    }

}

