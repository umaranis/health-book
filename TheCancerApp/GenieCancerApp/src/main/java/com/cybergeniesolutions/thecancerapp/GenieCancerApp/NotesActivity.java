package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

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

import com.itextpdf.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadafk on 17/01/2017.
 */
public class NotesActivity extends AppCompatActivity {
    private List<Note> notesList = new ArrayList<Note>();
    private RecyclerView recyclerView;
    private NotesAdapter mAdapter;
    private DataBaseHelper db;
    private static final String TAG = "NotesActivity";
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_list);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //Action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), NoteEditActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        mAdapter = new NotesAdapter(notesList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        db = new DataBaseHelper(this.getApplicationContext());
        db.open();
        fillData();
    }

    private void fillData() {

        Log.v(TAG, "in filldata()");

        notesList.clear();

        Cursor c = db.fetchAllNotes();
        String[] from = new String[]{DataBaseHelper.N_TITLE};
        int[] to = new int[]{R.id.row_text};
        while (c.moveToNext()) {

            Note note = new Note(c.getString(c.getColumnIndex(db.N_TITLE)), c.getString(c.getColumnIndex(db.N_BODY)), c.getLong(c.getColumnIndex(db.KEY_ROWID)) );
            notesList.add(note);
        }

        mAdapter.notifyDataSetChanged();
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
          /*  case R.id.menu_send: {
                String path = Environment.getExternalStorageDirectory().toString() + "/GCHReports";
                File dir = new File(path);
                if(!dir.exists()) {
                    dir.mkdirs();
                    Log.v(TAG, "dir created");
                }
                String fileName = "notes.pdf";
                File file = new File(path, fileName);
                try {
                    createPDF(file);
                }
                catch(IOException e){

                }catch (DocumentException e){

                }
                Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                intent.setType("application/octet-stream");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Notes");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "Send mail..."));
                break;
            }
            case R.id.menu_pdf:{
                String path = Environment.getExternalStorageDirectory().toString() + "/GCHReports";
                File dir = new File(path);
                if(!dir.exists()) {
                    dir.mkdirs();
                    Log.v(TAG, "dir created");
                }
                String fileName = "notes.pdf";
                File file = new File(path, fileName);
                try {
                    createPDF(file);
                }
                catch(IOException e){

                }catch (DocumentException e){

                }
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

  /*  public void showPDF(String path, String filename){
        Log.d(TAG, "in showPdf()");
        File file = new File(path, filename);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
        preface.add(new Paragraph("Notes", catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(
                "Report generated by: " + "GenieCanHelp" + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));
        document.add(preface);

        Paragraph empty = new Paragraph();
        // We add one empty line
        addEmptyLine(empty, 5);
        document.add(empty);


        PdfPTable table = new PdfPTable(8);

        for(int aw = 0; aw < 16; aw++){
            table.addCell("hi");
        }
        document.add(table);
        document.close();
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillData();
    }
}

