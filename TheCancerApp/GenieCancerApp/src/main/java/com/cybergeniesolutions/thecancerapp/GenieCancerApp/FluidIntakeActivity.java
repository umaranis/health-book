package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sadafk on 21/12/2017.
 */
public class FluidIntakeActivity extends AppCompatActivity {

    private GridLayoutManager lLayout;
    List<FluidIntake> intakeList = new ArrayList<>();
    FluidIntakeAdapter rcAdapter;
    private static final String TAG = "FluidIntakeActivity";

    private DataBaseHelper db;
    private static final int ACTIVITY_CREATE=0;
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_empty);


        lLayout = new GridLayoutManager(FluidIntakeActivity.this, 1);

        //Action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        RecyclerView rView = (RecyclerView)findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        rcAdapter = new FluidIntakeAdapter(FluidIntakeActivity.this, intakeList);
        rView.setAdapter(rcAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFluidIntake();
            }
        });

        db = new DataBaseHelper(this.getApplicationContext());
        db.open();
        fillData();

    }



    private void fillData() {

        Log.v(TAG, "in filldata()");
        Cursor c = db.fetchAllFluidIntakes();
        intakeList.clear();
        Log.v(TAG, "cursor = " + c.getCount());
        while (c.moveToNext()) {
            FluidIntake intake = new FluidIntake(c.getString(c.getColumnIndex(db.T_DATE)),
                    c.getString(c.getColumnIndex(db.T_DAY)),
                    c.getInt(c.getColumnIndex(db.F_INTAKE)), c.getInt(c.getColumnIndex(db.T_UNIT)), c.getLong(c.getColumnIndex(db.KEY_ROWID)));
            intakeList.add(intake);
            Log.v(TAG, "intake = " + intake.getIntake());
        }

        rcAdapter.notifyDataSetChanged();
    }

    private void createFluidIntake() {
        Intent i = new Intent(this, FluidIntakeEditActivity.class);
        Cursor c = db.fetchAllFluidIntakes();
        long rowId = 0;
        startManagingCursor(c);
        if (c != null) {
            Log.v(TAG, "in createFluidIntake() cursor is not null");
            while (c.moveToNext()) {

                SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_FORMAT);
                Date date = null;
                try {
                    String dateString = c.getString(c.getColumnIndexOrThrow(DataBaseHelper.T_DATE));
                    date = dateTimeFormat.parse(dateString);
                    Calendar cal = Calendar.getInstance();
                    Calendar now = Calendar.getInstance();
                    cal.setTime(date);
                    if(cal.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH) &&
                            cal.get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
                            cal.get(Calendar.YEAR) == now.get(Calendar.YEAR)){
                        rowId = c.getLong(c.getColumnIndexOrThrow(DataBaseHelper.KEY_ROWID));
                    }
                } catch (ParseException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        if(rowId != 0){
            i.putExtra(DataBaseHelper.KEY_ROWID, rowId);
            i.putExtra("Page", "Edit");
        }else {
            i.putExtra("Page", "Add");
        }
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
    /*
            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                    super.onCreateOptionsMenu(menu);
                    MenuInflater mi = getMenuInflater();
                    mi.inflate(R.menu.send_menu, menu);
                    return true;
            }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
                       /* case R.id.menu_send: {
                                //createPDF();
                                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ContactsReport";
                                File file = new File(path, "contacts.pdf");
                                Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                                intent.setType("application/octet-stream");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Information");
                                intent.putExtra(Intent.EXTRA_TEXT, "");
                                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(Intent.createChooser(intent, "Send mail..."));
                                break;
                        }*/
            case android.R.id.home:{
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
