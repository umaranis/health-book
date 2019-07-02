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
 * Created by sadafk on 21/12/2017.
 */

public class FluidIntakeAdapter extends RecyclerView.Adapter<FluidIntakeAdapter.MyViewHolder> {

    private List<FluidIntake> intakeList;
    private Context context;

    private static final int ACTIVITY_CREATE = 0;
    private DataBaseHelper db;
    private static final String TAG = "FluidIntakeAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView dayNdateText;
        public TextView intakeText;

        public MyViewHolder(View itemView) {
            super(itemView);

            dayNdateText = (TextView)itemView.findViewById(R.id.fluid_day_and_date);
            intakeText = (TextView)itemView.findViewById(R.id.fluid_intake);
        }


    }


    public FluidIntakeAdapter(Context context, List<FluidIntake> intakeList) {
        this.intakeList = intakeList;
        this.context = context;
        db = new DataBaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fluid_intake_row, null);
        MyViewHolder rcv = new MyViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final FluidIntake intake = intakeList.get(holder.getAdapterPosition());
        Log.v(TAG, "dayNdateText =" + intake.getDay() + " " + intake.getDate());
        holder.dayNdateText.setText(intake.getDay() + "    " + intake.getDate());

        Log.v(TAG, "intakeUnit =" + intake.getUnit());
        int intakeUnit = intake.getUnit();
        String intakeUnitString = "";
        if(intakeUnit == 0)
            intakeUnitString = context.getResources().getString(R.string.radio_ml);
        else {
            if(intake.getIntake() > 1)
                intakeUnitString = context.getResources().getString(R.string.radio_cup)+"s";
            else
                intakeUnitString = context.getResources().getString(R.string.radio_cup);
        }

        holder.intakeText.setText(intake.getIntake() + " " + intakeUnitString);
        Log.v(TAG, "intakeText =" + intake.getIntake());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.intakeText);
                //inflating menu from xml resource
                popup.inflate(R.menu.appointment_context_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.app_menu_edit: {
                                Intent i = new Intent(context, FluidIntakeEditActivity.class);
                                i.putExtra(DataBaseHelper.KEY_ROWID, intake.getId());
                                i.putExtra("Page", "Edit");
                                Log.v(TAG, "setOnMenuItemClickListener rowid =" + intake.getId());
                                ((Activity) context).startActivityForResult(i, ACTIVITY_CREATE);
                                break;
                            }
                            case R.id.app_menu_delete: {
                                db.open();
                                db.deleteFluidIntake(intake.getId());
                                Log.v(TAG, "onContextItemSelected()..deleting item id=" + intake.getId());
                                db.close();
                                intakeList.remove(holder.getAdapterPosition()); // remember to remove it from your adapter data source
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
        return this.intakeList.size();
    }



}
