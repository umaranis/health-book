package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadafk on 17/01/2017.
 */
public class ListsActivity extends AppCompatActivity {

    private LinearLayoutManager lLayout;
    List<ListItem> list = new ArrayList<>();
    ListsAdapter rcAdapter;
    private static final String TAG = "ListsActivity";
    String listTitle = "";
    private EditText addEditText;
    private Button saveButton;
    private EditText titleEditText;

    private DataBaseHelper db;
    private static final int ACTIVITY_CREATE=0;
    private static Tracker mTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        Bundle extras = getIntent().getExtras();
        listTitle = extras.getString("ListTitle");

        //Action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(listTitle);

        lLayout = new LinearLayoutManager(ListsActivity.this);

        RecyclerView rView = (RecyclerView)findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        rcAdapter = new ListsAdapter(ListsActivity.this, list);
        rView.setAdapter(rcAdapter);

        db = new DataBaseHelper(this.getApplicationContext());
        db.open();
        fillData();
        db.close();
        saveButton = (Button)findViewById(R.id.list_confirm);
        titleEditText = (EditText)findViewById(R.id.l_title);
        titleEditText.setText(listTitle);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                addEditText = (EditText)findViewById(R.id.list_item_edit);
                String s = addEditText.getText().toString();
                s = s.replaceAll("\\s","");
                if(!s.equals("")) {
                    db.open();
                    String newListItem = addEditText.getText().toString();
                    db.addListItem(newListItem, db.fetchListID(listTitle));
                    fillData();
                    db.close();
                    rcAdapter.notifyDataSetChanged();
                    addEditText.setText("");
                }
            }
        });

        titleEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleEditText.setCursorVisible(true);
                titleEditText.setFocusableInTouchMode(true);
                titleEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                titleEditText.requestFocus(); //to trigger the soft input
            }
        });

        final Handler mHandler = new Handler();
        titleEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(userStoppedTyping, 2000); // 2 second
            }

            Runnable userStoppedTyping = new Runnable() {

                @Override
                public void run() {
                    Log.v(TAG, "stopped typing");
                    db.open();
                    int listID = db.fetchListID(listTitle);
                    db.updateList(listID, titleEditText.getText().toString());
                    db.close();
                }
            };
        });
    }
    private void fillData() {

        Log.v(TAG, "in filldata()");
        int listId = db.fetchListID(listTitle);
        Cursor c = db.fetchAllListItems(listId);
        list.clear();
        while (c.moveToNext()) {
            ListItem listItem = new ListItem(c.getString(c.getColumnIndex(db.L_LIST_ITEM)), c.getInt(c.getColumnIndex(db.L_LIST_ID)),
                    c.getLong(c.getColumnIndex(db.KEY_ROWID)));
            list.add(listItem);
        }

        rcAdapter.notifyDataSetChanged();
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
                Intent homeIntent = new Intent(this, ListMenuActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
