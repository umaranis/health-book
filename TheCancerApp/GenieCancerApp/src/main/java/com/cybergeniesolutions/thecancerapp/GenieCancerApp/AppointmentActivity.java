
package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sadafk on 13/01/2017.
 */
public class AppointmentActivity extends AppCompatActivity {
    private List<Appointment> appointmentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AppointmentAdapter mAdapter;
    private PDFHelper pdfHelper;


    private static final String TAG = "AppointmentActivity";

    private DataBaseHelper db;
    private static final int ACTIVITY_CREATE=0;

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_empty);

        //Action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAppointment();
            }
        });


        mAdapter = new AppointmentAdapter(appointmentList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        db = new DataBaseHelper(this.getApplicationContext());
        db.open();
        fillData();

        String fileName = "Appointments.pdf";
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path, fileName);

        pdfHelper = new PDFHelper(file, this.getApplicationContext());

    }

    private void fillData() {

        Log.v(TAG, "in filldata()");
        Cursor c = db.fetchAllAppointments();
        appointmentList.clear();
        while (c.moveToNext()) {
            Appointment appointment = new Appointment(c.getString(c.getColumnIndex(db.A_WITH)),
                    c.getString(c.getColumnIndex(db.A_WHERE)), c.getString(c.getColumnIndex(db.A_WHEN)), c.getString(c.getColumnIndex(DataBaseHelper.A_WHEN_TIME)),
                    c.getLong(c.getColumnIndex(db.KEY_ROWID)));
            appointmentList.add(appointment);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void createAppointment() {
        Intent i = new Intent(this, AppointmentEditActivity.class);
        i.putExtra("Page", "Add");
        startActivityForResult(i, ACTIVITY_CREATE);
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
                String path = Environment.getExternalStorageDirectory().toString();
                String fileName = "Appointments.pdf";
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
                pdfHelper.createEmail();
                break;
            }
            case R.id.menu_pdf:{
                String fileName = "Appointments.pdf";
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
                pdfHelper.showPDF();
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
        preface.add(new Paragraph("Appointments List", catFont));

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
                "Report generated by: " + "GenieCanHelp" + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));

        document.close();
    }

    private PdfPTable getRecordTable() {

        Log.v(TAG, "in getRecordString()");
        Cursor c = db.fetchAllAppointments();

        PdfPTable table = new PdfPTable(3);
        // the cell object
        PdfPCell cell;

        // create header cell

        cell = new PdfPCell(new Phrase("With"));
        cell.setColspan(1);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Where"));
        cell.setColspan(1);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("When"));
        cell.setColspan(1);
        table.addCell(cell);


        if(c != null){
            Log.v(TAG,"c is  not null");
            while (c.moveToNext()){

                String withString = c.getString(c.getColumnIndex(db.A_WITH));
                Log.v(TAG,"withString="+withString);
                table.addCell(withString);

                String whereString = c.getString(c.getColumnIndex(db.A_WHERE));
                table.addCell(whereString);

                String whenString = c.getString(c.getColumnIndex(db.A_WHEN)) + " at "+ c.getString(c.getColumnIndex(db.A_WHEN_TIME)) ;
                table.addCell(whenString);
            }
        }

        return table;


    }

    private String getContactString() {

        Log.v(TAG,"in getContactString");
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

}