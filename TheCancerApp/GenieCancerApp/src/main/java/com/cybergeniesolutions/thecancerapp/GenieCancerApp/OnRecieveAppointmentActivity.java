package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by sadafk on 10/05/2017.
 */
public class OnRecieveAppointmentActivity extends AppCompatActivity {

    private DataBaseHelper dbHelper;
    private static final String TAG = "OnRecieveAppointmentActivity";
    private TextView bodyTextView;
    private Button buttonOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDialogTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_reminder_layout);

        dbHelper = new DataBaseHelper(this);
        bodyTextView = (TextView) findViewById(R.id.body);
        buttonOK = (Button) findViewById(R.id.button_ok);
        String bodyText = "You have an appointment with ";
        Bundle extras = getIntent().getExtras();
        long rowId = extras.getLong("id");
        Log.v(TAG, "id=" + rowId);
        dbHelper.open();
        Cursor c = dbHelper.fetchAppointment(rowId);

                bodyText = bodyText + c.getString(c.getColumnIndexOrThrow(DataBaseHelper.A_WITH));
                String whereText = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.A_WHERE));
                if(whereText!= null && !whereText.equals(""))
                    bodyText =  bodyText + " at " + whereText;
                String time = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.A_WHEN_TIME));
                //String dateString = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.A_WHEN));
                bodyText = bodyText +" tomorrow at "+ time;
        dbHelper.close();
        bodyTextView.setText(bodyText);
        buttonOK.setText("OK");
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel((int)rowId);

        registerButtonListenersAndSetDefaultText();

    }

    private void registerButtonListenersAndSetDefaultText() {


        buttonOK.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }

}
