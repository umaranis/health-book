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
 * Created by sadafk on 27/07/2017.
 */
public class TempAdapter extends RecyclerView.Adapter<TempAdapter.MyViewHolder> {

    private List<Temperature> tempList;
    private Context context;

    private static final int ACTIVITY_CREATE = 0;
    private DataBaseHelper db;
    private static final String TAG = "TempAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView dayNdateText;
        public TextView timeText;
        public TextView tempText;

        public MyViewHolder(View itemView) {
            super(itemView);

            dayNdateText = (TextView)itemView.findViewById(R.id.day_and_date);
            timeText = (TextView)itemView.findViewById(R.id.time);
            tempText = (TextView)itemView.findViewById(R.id.temp);
        }


    }


    public TempAdapter(Context context, List<Temperature> tempList) {
        this.tempList = tempList;
        this.context = context;
        db = new DataBaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.temp_row, null);
        MyViewHolder rcv = new MyViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Temperature temp = tempList.get(holder.getAdapterPosition());
        Log.v(TAG, "dayNdateText =" + temp.getDay() + " " + temp.getDate());
        holder.dayNdateText.setText(temp.getDay() + "    " + temp.getDate());

        holder.timeText.setText(temp.getTime());
        Log.v(TAG, "timeText =" + temp.getTime());
        Log.v(TAG, "timeUnit =" + temp.getUnit());
        int tempUnit = temp.getUnit();
        String tempUnitString = "";
        if(tempUnit == 0)
            tempUnitString = context.getResources().getString(R.string.radio_temp_c);
        else
            tempUnitString = context.getResources().getString(R.string.radio_temp_f);

        holder.tempText.setText(temp.getTemp() + " " + tempUnitString);
        Log.v(TAG, "tempText =" + temp.getTemp());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.tempText);
                //inflating menu from xml resource
                popup.inflate(R.menu.appointment_context_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.app_menu_edit: {
                                Intent i = new Intent(context, TempEditActivity.class);
                                i.putExtra(DataBaseHelper.KEY_ROWID, temp.getId());
                                i.putExtra("Page", "Edit");
                                Log.v(TAG, "setOnMenuItemClickListener rowid =" + temp.getId());
                                ((Activity) context).startActivityForResult(i, ACTIVITY_CREATE);
                                break;
                            }
                            case R.id.app_menu_delete: {
                                db.open();
                                db.deleteTemp(temp.getId());
                                Log.v(TAG, "onContextItemSelected()..deleting item id=" + temp.getId());
                                db.close();
                                tempList.remove(holder.getAdapterPosition()); // remember to remove it from your adapter data source
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
        return this.tempList.size();
    }



}
