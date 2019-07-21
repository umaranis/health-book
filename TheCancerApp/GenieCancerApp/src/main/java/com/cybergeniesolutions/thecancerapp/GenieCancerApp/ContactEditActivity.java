package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by sadafk on 20/01/2017.
 */
public class ContactEditActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText phoneText;
    private EditText emailText;
    private EditText streetText;
    private EditText suburbText;
    private EditText stateText;
    private EditText postCodeText;
    private String type;
    private Long rowId;
    private DataBaseHelper dbHelper;
    private static final String TAG = "ContactEditActivity";


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
        setContentView(R.layout.contact_edit);
        dbHelper = new DataBaseHelper(this);
        Log.v(TAG, "in onCreate() rowid =" + rowId);

        /*ActionBar actionBar = getActionBar();
        actionBar.show();
        actionBar.setIcon(R.drawable.home_icon);
        actionBar.setHomeButtonEnabled(true);*/

        Bundle extras = getIntent().getExtras();
        type = extras.getString("Type");
        rowId = extras.getLong(DataBaseHelper.KEY_ROWID);
        Log.v(TAG,"in onCreate() Type ="+type);

        nameText = (EditText) findViewById(R.id.c_name_edit);
        phoneText = (EditText) findViewById(R.id.c_phone_edit);
        emailText = (EditText) findViewById(R.id.c_email_edit);
        streetText = (EditText) findViewById(R.id.c_street_edit);
        suburbText = (EditText) findViewById(R.id.c_suburb_edit);
        stateText = (EditText) findViewById(R.id.c_state_edit);
        postCodeText = (EditText) findViewById(R.id.c_postcode_edit);


        rowId = savedInstanceState != null ? savedInstanceState.getLong(DataBaseHelper.KEY_ROWID)
                : null;

        Log.v(TAG,"in onCreate() rowid ="+rowId);

        registerButtonListenersAndSetDefaultText();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saveState())
                    finish();
            }
        });

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

 /*   private void setRowIdFromIntent() { if (rowId == null) {
		Bundle extras = getIntent().getExtras();
		rowId = extras != null
		? extras.getLong(DataBaseHelper.KEY_ROWID)
		: null;
		}
	}*/

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
        Log.v(TAG,"in onResume() rowid ="+rowId);
        try {
            populateFields();
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void populateFields() throws java.text.ParseException{
        Log.v(TAG,"in populateFields() rowid ="+rowId);
        if (rowId != null) {

            Cursor contact = dbHelper.fetchContact(rowId);
            startManagingCursor(contact);
            if (contact != null) {

                if (contact.moveToFirst()){
                    nameText.setText(contact.getString(contact.getColumnIndexOrThrow(DataBaseHelper.C_NAME)));
                    phoneText.setText(contact.getString(contact.getColumnIndexOrThrow(DataBaseHelper.C_PHONE)));
                    emailText.setText(contact.getString(contact.getColumnIndexOrThrow(DataBaseHelper.C_EMAIL)));
                    streetText.setText(contact.getString(contact.getColumnIndexOrThrow(DataBaseHelper.C_STREET)));
                    suburbText.setText(contact.getString(contact.getColumnIndexOrThrow(DataBaseHelper.C_SUBURB)));
                    stateText.setText(contact.getString(contact.getColumnIndexOrThrow(DataBaseHelper.C_STATE)));
                    postCodeText.setText(contact.getString(contact.getColumnIndexOrThrow(DataBaseHelper.C_POST_CODE)));
                }
            }
        }
    }

    private void registerButtonListenersAndSetDefaultText() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(DataBaseHelper.KEY_ROWID, rowId);
    }

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private boolean saveState() {
        String name = nameText.getText().toString();
        String phone = phoneText.getText().toString();
        String email = emailText.getText().toString();
        String street = streetText.getText().toString();
        String suburb = suburbText.getText().toString();
        String state = stateText.getText().toString();
        String postCode = postCodeText.getText().toString();

        Log.v(TAG,"in saveState() Type ="+type);
        Log.v(TAG,"in saveState() rowid ="+rowId);

        if(name.isEmpty()){
            Toast.makeText(ContactEditActivity.this, getString(R.string.name_empty_field_message),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else
        if(email.isEmpty() && phone.isEmpty()){
            if (rowId == null) {
                long id = dbHelper.addContact(name,phone, email, street,suburb,state,postCode,type);

                if (id > 0) { rowId = id; }
                return true;
            } else {
                dbHelper.updateContact(rowId, name,phone, email, street,suburb,state,postCode,type);
                return true;
            }

        }else{
            if (!email.isEmpty() && phone.isEmpty() && checkEmail(email)){
                if (rowId == null) {
                    long id = dbHelper.addContact(name,phone, email, street,suburb,state,postCode,type);

                    if (id > 0) { rowId = id; }
                    return true;
                } else {
                    dbHelper.updateContact(rowId, name,phone, email, street,suburb,state,postCode,type);
                    return true;
                }

            }else if(email.isEmpty() && !phone.isEmpty() && PhoneNumberUtils.isGlobalPhoneNumber(phone)){
                if (rowId == null) {
                    long id = dbHelper.addContact(name,phone, email, street,suburb,state,postCode,type);

                    if (id > 0) { rowId = id; }
                    return true;
                } else {
                    dbHelper.updateContact(rowId, name,phone, email, street,suburb,state,postCode,type);
                    return true;
                }

            }else
            if(!email.isEmpty() && !phone.isEmpty() && checkEmail(email) && PhoneNumberUtils.isGlobalPhoneNumber(phone)){

                if (rowId == null) {
                    long id = dbHelper.addContact(name,phone, email, street,suburb,state,postCode,type);

                    if (id > 0) { rowId = id; }
                    return true;
                } else {
                    dbHelper.updateContact(rowId, name,phone, email, street,suburb,state,postCode,type);
                    return true;
                }

            }

            if (!checkEmail(email) && !email.isEmpty()){
                Toast.makeText(ContactEditActivity.this, getString(R.string.email_invalid_message),
                        Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(ContactEditActivity.this, getString(R.string.phone_invalid_message),
                        Toast.LENGTH_SHORT).show();
            }
            return false;

        }

    }
}
