package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sadafk on 17/01/2017.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private List<Note> notesList;
    private Context mCtx;
    private static final int ACTIVITY_CREATE = 0;
    private DataBaseHelper db;
    private static final String TAG = "NotesAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView;

        public MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.row_text);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), NoteEditActivity.class);
            view.getContext().startActivity(intent);
            switch((int)getPosition()) {
                case 0: {

                }
            }
        }
    }


    public NotesAdapter(List<Note> noteList, Context mCtx) {

        this.notesList = noteList;
        this.mCtx = mCtx;
        db = new DataBaseHelper(mCtx);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.generic_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Note note = notesList.get(holder.getAdapterPosition());
        holder.textView.setText(note.getTitle());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mCtx, holder.textView);
                //inflating menu from xml resource
                popup.inflate(R.menu.appointment_context_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.app_menu_edit: {
                                Intent i = new Intent(mCtx, NoteEditActivity.class);
                                i.putExtra(DataBaseHelper.KEY_ROWID, note.getId());
                                i.putExtra("Type", "None");
                                i.putExtra("Page", "Edit");
                                Log.v(TAG, "setOnMenuItemClickListener rowid =" + note.getId());
                                ((Activity) mCtx).startActivityForResult(i, ACTIVITY_CREATE);
                                break;
                            }
                            case R.id.app_menu_delete: {
                                db.open();
                                db.deleteNote(note.getId());
                                Log.v(TAG, "onContextItemSelected()..deleting item id=" + note.getId());
                                Log.v(TAG, "onContextItemSelected()..deleting item position=" + holder.getAdapterPosition());
                                db.close();
                                notesList.remove(holder.getAdapterPosition()); // remember to remove it from your adapter data source
                                notifyItemRemoved(holder.getAdapterPosition());

                                break;
                            }

                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}

