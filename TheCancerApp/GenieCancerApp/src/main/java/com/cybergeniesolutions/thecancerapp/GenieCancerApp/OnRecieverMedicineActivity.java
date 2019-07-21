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
 * Created by sadafk on 11/05/2017.
 */
public class OnRecieverMedicineActivity extends AppCompatActivity {

    private DataBaseHelper dbHelper;
    private static final String TAG = "OnRecieveMedicineActivity";
    private TextView bodyTextView;
    private Button buttonOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDialogTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_reminder_layout);

        dbHelper = new DataBaseHelper(this);
        bodyTextView = (TextView) findViewById(R.id.body);
        buttonOK = (Button) findViewById(R.id.button_ok);
        String bodyText = "Take ";
        Bundle extras = getIntent().getExtras();
        long rowId = extras.getLong("id");
        Log.v(TAG, "id=" + rowId);
        long medTimeId = extras.getLong("medTimeId");
        Log.v(TAG, "medTimeId=" + medTimeId);
        dbHelper.open();
        Cursor c = dbHelper.fetchMedicine(rowId);
        c.moveToFirst();
        String medName = c.getString(c.getColumnIndex(DataBaseHelper.M_NAME));

        c = dbHelper.fetchMedicineTime(medTimeId-100);
        c.moveToFirst();
        String dose = c.getString(c.getColumnIndex(DataBaseHelper.MT_DOSE));

        bodyText = bodyText + dose + " dose of " + medName;

        dbHelper.close();
        bodyTextView.setText(bodyText);
        buttonOK.setText("OK");

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel((int)medTimeId);

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
