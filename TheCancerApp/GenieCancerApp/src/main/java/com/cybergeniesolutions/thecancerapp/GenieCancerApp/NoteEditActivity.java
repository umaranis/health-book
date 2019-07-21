package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

/**
 * Created by sadafk on 17/01/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class NoteEditActivity extends AppCompatActivity {
    public static int numTitle = 1;
    public static String curDate = "";
    public static String curText = "";
    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;

    private Cursor note;

    private DataBaseHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new DataBaseHelper(this);
        mDbHelper.open();

        setContentView(R.layout.notes_edit);

        mTitleText = (EditText) findViewById(R.id.n_title);
        mBodyText = (EditText) findViewById(R.id.n_body);

        mRowId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(DataBaseHelper.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(DataBaseHelper.KEY_ROWID)
                    : null;
        }

        populateFields();

    }

    public static class LineEditText extends EditText{
        // we need this constructor for LayoutInflater
        public LineEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setColor(Color.BLUE);
        }

        private Rect mRect;
        private Paint mPaint;

        @Override
        protected void onDraw(Canvas canvas) {

            int height = getHeight();
            int line_height = getLineHeight();

            int count = height / line_height;

            if (getLineCount() > count)
                count = getLineCount();

            Rect r = mRect;
            Paint paint = mPaint;
            int baseline = getLineBounds(0, r);

            for (int i = 0; i < count; i++) {

                canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
                baseline += getLineHeight();

                super.onDraw(canvas);
            }

        }
    }

    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();
        if(!body.isEmpty()){
            if (title.isEmpty()){
                String arr[] = body.split(" ", 2);
                title = arr[0];
            }
            if(mRowId == null){
                long id = mDbHelper.addNote(title, body);
                if(id > 0){ mRowId = id; }
            }else{
                mDbHelper.updateNote(mRowId, title, body);
            }
        }
    }


    private void populateFields() {
        if (mRowId != null) {
            note = mDbHelper.fetchNote(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
                    note.getColumnIndexOrThrow(DataBaseHelper.N_TITLE)));
            mBodyText.setText(note.getString(
                    note.getColumnIndexOrThrow(DataBaseHelper.N_BODY)));
            curText = note.getString(
                    note.getColumnIndexOrThrow(DataBaseHelper.N_BODY));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(DataBaseHelper.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.note_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_edit_delete:{
                if(note != null){
                    note.close();
                    note = null;
                }
                if(mRowId != null){
                    mDbHelper.deleteNote(mRowId);
                }
                finish();

                return true;
            }
            case R.id.menu_edit_save:{
                saveState();
                finish();
                return true;
            }
            case android.R.id.home:{
                /*Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                return true;*/
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


