package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadafk on 13/01/2017.
 */
public class ContactActivity extends AppCompatActivity {
    private List<Contact> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactAdapter mAdapter;
    private Context context;
    private static final String TAG = "ContactsActivity";

    private DataBaseHelper db;
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    private String TYPE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_empty);
        context = getApplicationContext();


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //Action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        TYPE = extras.getString("Type");
        Log.v(TAG, "in onCreate() Type =" + TYPE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Add new", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
                createContact();
            }
        });

        mAdapter = new ContactAdapter(contactList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        db = new DataBaseHelper(this.getApplicationContext());
        db.open();
        fillData();
        //registerForContextMenu(getListView());

    }

    private void fillData() {

        Log.v(TAG, "in filldata()");
        Cursor c = db.fetchContacts(TYPE);
        contactList.clear();
        while (c.moveToNext()) {
            String addressString = c.getString(c.getColumnIndex(db.C_STREET));
            addressString += " " + c.getString(c.getColumnIndex(db.C_SUBURB));
            addressString += " " + c.getString(c.getColumnIndex(db.C_STATE));
            addressString += " " + c.getString(c.getColumnIndex(db.C_POST_CODE));

            Contact contact = new Contact(c.getString(c.getColumnIndex(db.C_NAME)), c.getString(c.getColumnIndex(db.C_PHONE)),
                    c.getString(c.getColumnIndex(db.C_EMAIL)), addressString, c.getLong(c.getColumnIndex(db.KEY_ROWID)) );
            contactList.add(contact);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void createContact() {
        Intent i = new Intent(this, ContactEditActivity.class);
        i.putExtra("Type", TYPE);
        i.putExtra("Page", "Add");
        startActivityForResult(i, ACTIVITY_CREATE);
    }

   /* @Override
   public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.send_menu, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
           /* case R.id.menu_send: {
                String path = Environment.getExternalStorageDirectory().toString() + "/ContactsReport";
                File dir = new File(path);
                if(!dir.exists()) {
                    dir.mkdirs();
                    Log.v(TAG, "dir created");
                }
                String fileName = "contacts.pdf";
                File file = new File(path, fileName);
                createPDF(file);
                Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                intent.setType("application/octet-stream");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Information");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "Send mail..."));

                showPDF(path, fileName);
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

   /* public void showPDF(String path, String filename){
        Log.d(TAG, "in showPdf()");
        File file = new File(path, filename);
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        startActivity(intent);
    }*/

/*    public void createPDF(File file){

        try {
            PDFWriter pdfWriter = new PDFWriter(file, this);
            pdfWriter.createPDF();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }


}