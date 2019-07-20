package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

/**
 * Created by sadafk on 6/01/2017.
 */

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;


public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_CREATE=0;

    private DataBaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        Log.v(TAG, "in onCreate()");


        //Action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DataBaseHelper(this.getApplicationContext());
        db.open();
        fillData();

    }

    private void fillData() {

        Log.v(TAG,"in filldata()");
        Cursor c = db.fetchProfile();

        if(c != null){
            Log.v(TAG,"c is  not null");
            if (c.moveToFirst()){
                Log.v(TAG,"c moved to first");
                String nameString = c.getString(c.getColumnIndex(db.P_NAME));
                Log.v(TAG,"nameString="+nameString);
                TextView nameTv = (TextView) findViewById(R.id.patient_name);
                nameTv.setText(nameString);

                String dobString = c.getString(c.getColumnIndex(db.P_DOB));
                Log.v(TAG,"dobString="+dobString);
                TextView dobTv = (TextView) findViewById(R.id.patient_dob);
                dobTv.setText(dobString);

                String heightString = c.getString(c.getColumnIndex(db.P_HEIGHT));
                Log.v(TAG,"heightString="+heightString);
                TextView heightTv = (TextView) findViewById(R.id.height);
                heightTv.setText(heightString);

                String weightString = c.getString(c.getColumnIndex(db.P_WEIGHT));
                Log.v(TAG,"weightString="+weightString);
                TextView weightTv = (TextView) findViewById(R.id.weight);
                weightTv.setText(weightString);

                String bmiString = c.getString(c.getColumnIndex(db.P_BMI));
                Log.v(TAG,"bmiString="+bmiString);
                TextView bmiTv = (TextView) findViewById(R.id.bmi);
                bmiTv.setText(bmiString);

                String surfaceAreaString = c.getString(c.getColumnIndex(db.P_SURFACE_AREA));
                Log.v(TAG,"surfaceAreaString="+surfaceAreaString);
                TextView surfaceAreaTv = (TextView) findViewById(R.id.surface_area);
                surfaceAreaTv.setText(surfaceAreaString);

                String parentString = c.getString(c.getColumnIndex(db.P_PARENT_NAME));
                Log.v(TAG,"parentString="+parentString);
                TextView parentTv = (TextView) findViewById(R.id.patient_parent_name);
                parentTv.setText(parentString);

                c = db.fetchContacts("Profile");

                if(c != null){
                    if (c.moveToFirst()){

                        String phoneString = c.getString(c.getColumnIndex(db.C_PHONE));
                        Log.v(TAG,"phoneString="+phoneString);
                        TextView phoneTv = (TextView) findViewById(R.id.patient_phone);
                        phoneTv.setText(phoneString);

                        String emailString = c.getString(c.getColumnIndex(db.C_EMAIL));
                        Log.v(TAG,"emailString="+emailString);
                        TextView emailTv = (TextView) findViewById(R.id.patient_email);
                        emailTv.setText(emailString);

                        String addressString = c.getString(c.getColumnIndex(db.C_STREET)) + " "+c.getString(c.getColumnIndex(db.C_SUBURB)) + " "
                                + c.getString(c.getColumnIndex(db.C_STATE)) + " "+ c.getString(c.getColumnIndex(db.C_POST_CODE));
                        Log.v(TAG,"addressString="+addressString);
                        TextView addressTv = (TextView) findViewById(R.id.patient_address);
                        addressTv.setText(addressString);

                    }
                }

            }
        }else{
            Log.v(TAG,"c is null");
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.profile_menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_delete:{
                deleteProfile();
                return true;
            }
            case R.id.menu_edit:{
                editProfile();
                return true;
            }
            case android.R.id.home:{
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteProfile() {

        Log.v(TAG,"in deleteProfile");
        Cursor c = db.fetchProfile();
        long id = 1;
        if(c != null){
            Log.v(TAG,"c is  not null");
            if (c.moveToFirst()){
                Log.v(TAG,"c moved to first");
                id = c.getLong(c.getColumnIndex(db.KEY_ROWID));
            }
        }
        db.deleteProfile(id);
        db.deleteProfileContact();
        Intent i = new Intent(this, ProfileActivity.class);
        startActivityForResult(i, ACTIVITY_CREATE);
        //onCreate(null);
    }

    private void editProfile() {
        Log.v(TAG,"in editProfile");
        Intent i = new Intent(this, ProfileEditActivity.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }



}
