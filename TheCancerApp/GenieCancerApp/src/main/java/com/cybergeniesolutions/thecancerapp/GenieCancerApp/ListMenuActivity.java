package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by sadafk on 3/02/2017.
 */
public class ListMenuActivity extends AppCompatActivity{

    private static final String TAG = "ListsActivity";
    List<ListItem> listMenuItems = new ArrayList<>();
    ListMenuApapter rcAdapter;

    private DataBaseHelper db;
    private static final int ACTIVITY_CREATE=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_list);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createList();

            }
        });

        RecyclerView rView = (RecyclerView)findViewById(R.id.recycler_view);

        //Action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rView.setLayoutManager(mLayoutManager);
        rView.setItemAnimator(new DefaultItemAnimator());

        rView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        rcAdapter = new ListMenuApapter(ListMenuActivity.this, listMenuItems);
        rView.setAdapter(rcAdapter);

        db = new DataBaseHelper(this.getApplicationContext());
        db.open();
        fillData();
        db.close();
    }

    private void fillData() {

        Log.v(TAG, "in filldata()");
        Cursor c = db.fetchAllLists();
        listMenuItems.clear();
        while (c.moveToNext()) {
            ListItem menuItem = new ListItem(c.getString(c.getColumnIndex(db.L_LIST_TITLE)),c.getInt(c.getColumnIndex(db.KEY_ROWID)));
            listMenuItems.add(menuItem);
        }

        rcAdapter.notifyDataSetChanged();
    }

    private void createList() {

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.list_item_edit_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set list_item_edit_prompt to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        userInput.setText("");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                db.open();
                                long listId = db.addList(userInput.getText().toString());
                                db.close();
                                ListItem listItem = new ListItem(userInput.getText().toString(), (int)listId);
                                listMenuItems.add(listItem);
                                rcAdapter.notifyDataSetChanged();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        db.open();
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

