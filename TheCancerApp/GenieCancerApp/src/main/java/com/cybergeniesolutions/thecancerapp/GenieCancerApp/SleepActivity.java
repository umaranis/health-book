package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SleepActivity extends AppCompatActivity {

    List<Sleep> sleepList = new ArrayList<>();
    SleepAdapter rcAdapter;
    private RecyclerView rView;
    private static final String TAG = "SleepActivity";

    private DataBaseHelper db;

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL);
    private PDFHelper pdfHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_empty);


        //Action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rView.setLayoutManager(mLayoutManager);

        rView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rView.setHasFixedSize(true);
        rView.setLayoutManager(mLayoutManager);
        rcAdapter = new SleepAdapter(SleepActivity.this, sleepList);
        rView.setAdapter(rcAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Do something
                fillData();
                if(sleepList.size() <= 0) {
                    fetchFitbitData();
                    fillData();
                }
            }
        });

        db = new DataBaseHelper(this.getApplicationContext());
        db.open();
        //fillData();

        String fileName = "sleep.pdf";
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path, fileName);

        pdfHelper = new PDFHelper(file, this.getApplicationContext());
    }
    private void fillData() {

        Log.v(TAG, "in filldata()");
        Cursor c = db.fetchAllSleep();
        sleepList.clear();
        Log.v(TAG, "cursor = " + c.getCount());
        while (c.moveToNext()) {
            Sleep sleep = new Sleep(c.getString(c.getColumnIndex("Day")),
                    c.getString(c.getColumnIndex("Sleep_Count")),
                    c.getString(c.getColumnIndex("Total_Minutes")),
                    c.getString(c.getColumnIndex("Time_In_Bed")),
                    c.getLong(c.getColumnIndex(db.KEY_ROWID)));


            sleepList.add(sleep);
            //Log.v(TAG, "heart rate = " + heartRate.getHeartRate());
        }

        rcAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.send_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_send: {
               /* String fileName = "sleep.pdf";
                String path = Environment.getExternalStorageDirectory().toString();

                File file = new File(path + "/" + fileName);
                if (!file.exists()) {
                    pdfHelper.createFile(this);
                }

                try {
                    createPDF(file);
                }
                catch(IOException e){
                    e.printStackTrace();

                }catch (DocumentException e){
                    e.printStackTrace();
                }
                pdfHelper.createEmail();*/
                break;
            }
            case R.id.menu_pdf:{
                /*String fileName = "sleep.pdf";
                String path = Environment.getExternalStorageDirectory().toString();

                File file = new File(path + "/" + fileName);

                if (!file.exists()) {
                    pdfHelper.createFile(this);
                }

                try {
                    createPDF(file);
                }
                catch(IOException e){
                    e.printStackTrace();

                }catch (DocumentException e){
                    e.printStackTrace();
                }
                pdfHelper.showPDF();*/
                break;
            }
            case android.R.id.home:{
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchFitbitData() {
        final DataBaseHelper dbHelper = new DataBaseHelper(this);
        dbHelper.open();
        dbHelper.deleteSleepAll();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.fitbit.com/1.2/user/-/sleep/date/2019-07-01/2019-07-21.json";

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: "+ response.substring(0,500));
                        try {
                            JSONArray array = response.getJSONArray("sleep");
                            for(int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                String date = obj.getString("dateOfSleep");
                                String minutesAsleep = obj.getString("minutesAsleep");
                                String timeInBed = obj.getString("timeInBed");

                                dbHelper.addSleep(date, Integer.parseInt(minutesAsleep), 1, Integer.parseInt(timeInBed));
                            }
                            fillData();
                        }
                        catch(Exception e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //textView.setText("That didn't work!");
                        int a = 5;
                        a++;
                    }
                })
        {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyMkRQNUIiLCJzdWIiOiI0VzNKS0giLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJyaHIgcnBybyByc2xlIiwiZXhwIjoxNTY0MjE4OTcwLCJpYXQiOjE1NjM2MTQxNzB9.zRzuc-6hU1JcIFSgPVT2x7bMIEKjXaX2Ixe7rN87ZQY");
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


/*
    public void createPDF(File file) throws IOException, DocumentException {
        Log.d(TAG, "in createPdf()");
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        Log.d(TAG, "in createPdf() file creatred");
        document.open();
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Heart Rate Records", catFont));

        Paragraph empty = new Paragraph();
        // We add one empty line
        addEmptyLine(empty, 2);
        document.add(empty);

        preface.add(new Paragraph(getContactString(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                small));

        document.add(preface);

        addEmptyLine(preface, 1);
        document.add(empty);

        document.add(getRecordTable());
        document.add(empty);
        document.add(new Paragraph(
                "Report generated by: " + "Health Book" + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));
        document.close();
    }

    private PdfPTable getRecordTable() {

        Log.v(TAG, "in getRecordString()");
        Cursor c = db.fetchAllHeartRate();

        PdfPTable table = new PdfPTable(3);
        // the cell object
        PdfPCell cell;

        // create header cell

        cell = new PdfPCell(new Phrase("Date"));
        cell.setColspan(1);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Heart Rate"));
        cell.setColspan(1);
        table.addCell(cell);


        if(c != null){
            Log.v(TAG,"c is  not null");
            while (c.moveToNext()){

                String dateString = c.getString(c.getColumnIndex(db.DAY);
                Log.v(TAG,"dateString="+dateString);
                table.addCell(dateString);

                String heartRateString = c.getString(c.getColumnIndex(db.HeartRate));
                table.addCell(heartRateString);

            }
        }

        return table;


    }

    private String getContactString() {

        Log.v(TAG, "in getContactString");
        Cursor c = db.fetchProfile();
        String contactString = "";

        if(c != null){
            Log.v(TAG,"c is  not null");
            if (c.moveToFirst()){
                Log.v(TAG,"c moved to first");
                String nameString = c.getString(c.getColumnIndex(db.P_NAME));
                contactString += "Pateint's Name:";
                contactString = contactString + " " + nameString + "\n";
                //Log.v(TAG,"conactString="+contactString);

                String dobString = c.getString(c.getColumnIndex(db.P_DOB));
                contactString += "Date of birth:";
                contactString = contactString + " " + dobString + "\n";
                //Log.v(TAG,"conactString="+contactString);

                String parentString = c.getString(c.getColumnIndex(db.P_PARENT_NAME));
                contactString += "Parent Name:";
                contactString = contactString + " " + parentString+ "\n";
                //Log.v(TAG,"conactString="+contactString);

                String heightString = c.getString(c.getColumnIndex(db.P_HEIGHT));
                contactString += "Height:";
                contactString = contactString + " " + heightString+ "\n";
                //Log.v(TAG,"conactString="+contactString);

                String weightString = c.getString(c.getColumnIndex(db.P_WEIGHT));
                contactString += "Weight:";
                contactString = contactString + " " + weightString+ "\n";
                //Log.v(TAG,"conactString="+contactString);

                String bmiString = c.getString(c.getColumnIndex(db.P_BMI));
                contactString += "BMI:";
                contactString = contactString + " " + bmiString+ "\n";
                //Log.v(TAG,"conactString="+contactString);

                String surfaceAreaString = c.getString(c.getColumnIndex(db.P_SURFACE_AREA));
                contactString += "Surface Area:";
                contactString = contactString + " " + surfaceAreaString+ "\n";
                //Log.v(TAG,"conactString="+contactString);

                c = db.fetchContacts("Profile");

                if(c != null){
                    if (c.moveToFirst()){

                        String phoneString = c.getString(c.getColumnIndex(db.C_PHONE));
                        contactString += "Phone:";
                        contactString = contactString + " " + phoneString+ "\n";
                        //Log.v(TAG,"conactString="+contactString);

                        String emailString = c.getString(c.getColumnIndex(db.C_EMAIL));
                        contactString += "Email:";
                        contactString = contactString + " " + emailString+ "\n";
                        //Log.v(TAG,"conactString="+contactString);

                        String addressString = c.getString(c.getColumnIndex(db.C_STREET)) + " "+c.getString(c.getColumnIndex(db.C_SUBURB)) + " "
                                + c.getString(c.getColumnIndex(db.C_STATE)) + " "+ c.getString(c.getColumnIndex(db.C_POST_CODE));
                        contactString += "Address:";
                        contactString = contactString + " " + addressString+ "\n";
                        //Log.v(TAG,"conactString="+contactString);

                    }
                }

            }
        }else{
            Log.v(TAG,"c is null");
        }

        return contactString;

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
    */
}
