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
 * Created by sadafk on 8/12/2017.
 */
public class OxygenLevelAdapter extends RecyclerView.Adapter<OxygenLevelAdapter.MyViewHolder> {

    private List<OxygenLevel> oxygenLevelList;
    private Context context;

    private static final int ACTIVITY_CREATE = 0;
    private DataBaseHelper db;
    private static final String TAG = "OxygenLevelAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView dayNdateText;
        public TextView timeText;
        public TextView oxyLevelText;

        public MyViewHolder(View itemView) {
            super(itemView);

            dayNdateText = (TextView)itemView.findViewById(R.id.oxy_day_and_date);
            timeText = (TextView)itemView.findViewById(R.id.oxy_time);
            oxyLevelText = (TextView)itemView.findViewById(R.id.oxy_level);
        }


    }


    public OxygenLevelAdapter(Context context, List<OxygenLevel> oxygenLevelList) {
        this.oxygenLevelList = oxygenLevelList;
        this.context = context;
        db = new DataBaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.oxygen_level_row, null);
        MyViewHolder rcv = new MyViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final OxygenLevel oxygenLevel = oxygenLevelList.get(holder.getAdapterPosition());
        Log.v(TAG, "dayNdateText =" + oxygenLevel.getDay() + " " + oxygenLevel.getDate());
        holder.dayNdateText.setText(oxygenLevel.getDay() + "    " + oxygenLevel.getDate());

        holder.timeText.setText(oxygenLevel.getTime());
        Log.v(TAG, "timeText =" + oxygenLevel.getTime());

        holder.oxyLevelText.setText(oxygenLevel.getLevel() + "%");
        Log.v(TAG, "oxyLevelText =" + oxygenLevel.getLevel());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.oxyLevelText);
                //inflating menu from xml resource
                popup.inflate(R.menu.appointment_context_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.app_menu_edit: {
                                Intent i = new Intent(context, OxygenLevelEditActivity.class);
                                i.putExtra(DataBaseHelper.KEY_ROWID, oxygenLevel.getId());
                                i.putExtra("Page", "Edit");
                                Log.v(TAG, "setOnMenuItemClickListener rowid =" + oxygenLevel.getId());
                                ((Activity) context).startActivityForResult(i, ACTIVITY_CREATE);
                                break;
                            }
                            case R.id.app_menu_delete: {
                                db.open();
                                db.deleteOxyLevels(oxygenLevel.getId());
                                Log.v(TAG, "onContextItemSelected()..deleting item id=" + oxygenLevel.getId());
                                db.close();
                                oxygenLevelList.remove(holder.getAdapterPosition()); // remember to remove it from your adapter data source
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
        return this.oxygenLevelList.size();
    }



}
