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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sadafk on 17/01/2017.
 */
public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MyViewHolder> {

    private List<Mood> moodList;
    private Context context;

    private static final int ACTIVITY_CREATE = 0;
    private DataBaseHelper db;
    private static final String TAG = "MoodAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView dayNdateText;
        public TextView timeText;
        public TextView notesText;
        public ImageView moodImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            dayNdateText = (TextView)itemView.findViewById(R.id.day_and_date);
            timeText = (TextView)itemView.findViewById(R.id.time);
            notesText = (TextView)itemView.findViewById(R.id.notes);
            moodImage = (ImageView)itemView.findViewById(R.id.mood);
        }


    }


    public MoodAdapter(Context context, List<Mood> moodList) {
        this.moodList = moodList;
        this.context = context;
        db = new DataBaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mood_row, null);
        MyViewHolder rcv = new MyViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Mood mood = moodList.get(holder.getAdapterPosition());
        Log.v(TAG, "dayNdateText =" + mood.getDay() + " " + mood.getDate());
        holder.dayNdateText.setText(mood.getDay() + "    " + mood.getDate());

        holder.timeText.setText(mood.getTime());
        Log.v(TAG, "timeText =" + mood.getTime());

        holder.notesText.setText(mood.getNotes());
        Log.v(TAG, "notesText =" + mood.getNotes());

        if(mood.getMood().equals("smile")){
            holder.moodImage.setImageResource(R.drawable.smile);
        }
        else if(mood.getMood().equals("neutral")){
            holder.moodImage.setImageResource(R.drawable.neutral);
        }
        else if(mood.getMood().equals("frown")){
            holder.moodImage.setImageResource(R.drawable.frown);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.notesText);
                //inflating menu from xml resource
                popup.inflate(R.menu.appointment_context_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.app_menu_edit: {
                                Intent i = new Intent(context, MoodEditActivity.class);
                                i.putExtra(DataBaseHelper.KEY_ROWID, mood.getId());
                                i.putExtra("Page", "Edit");
                                Log.v(TAG, "setOnMenuItemClickListener rowid =" + mood.getId());
                                ((Activity) context).startActivityForResult(i, ACTIVITY_CREATE);
                                break;
                            }
                            case R.id.app_menu_delete: {
                                db.open();
                                db.deleteMood(mood.getId());
                                Log.v(TAG, "onContextItemSelected()..deleting item id=" + mood.getId());
                                db.close();
                                moodList.remove(holder.getAdapterPosition()); // remember to remove it from your adapter data source
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
        return this.moodList.size();
    }



}
