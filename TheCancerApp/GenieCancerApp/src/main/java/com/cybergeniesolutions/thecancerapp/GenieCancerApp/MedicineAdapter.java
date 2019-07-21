package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
 * Created by sadafk on 13/01/2017.
 */
public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {

    private List<Medicine> medicineList;
    private Context mCtx;
    private static final int ACTIVITY_CREATE = 0;
    private DataBaseHelper db;
    private static final String TAG = "MedicineAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView medicine, dose, instructions,dates;

        public MyViewHolder(View view) {
            super(view);
            medicine = (TextView) view.findViewById(R.id.medicine);
            //dose = (TextView) view.findViewById(R.id.dose_plus_unit);
            //instructions = (TextView) view.findViewById(R.id.instructions);
            //dates = (TextView) view.findViewById(R.id.start_and_end_date);
        }
    }


    public MedicineAdapter(List<Medicine> medicineList, Context mCtx) {

        this.medicineList = medicineList;
        this.mCtx = mCtx;
        db = new DataBaseHelper(mCtx);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Medicine medicine = medicineList.get(holder.getAdapterPosition());
        holder.medicine.setText(medicine.getMedicine());
        //holder.dose.setText(medicine.getDose() + " " + medicine.getUnit());
        //holder.instructions.setText(medicine.getInstructions());
        //holder.dates.setText(medicine.getStartDate() );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mCtx, holder.medicine);
                //inflating menu from xml resource
                popup.inflate(R.menu.appointment_context_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.app_menu_edit: {
                                Intent i = new Intent(mCtx, MedicineEditActivity.class);
                                i.putExtra(DataBaseHelper.KEY_ROWID, medicine.getId());
                                i.putExtra("Page", "Edit");
                                Log.v(TAG, "setOnMenuItemClickListener rowid =" + medicine.getId());
                                ((Activity) mCtx).startActivityForResult(i, ACTIVITY_CREATE);
                                break;
                            }
                            case R.id.app_menu_delete: {
                                db.open();
                                cancelAllMedicineReminders(medicine.getId());
                                db.deleteMedicine(medicine.getId());
                                db.deleteMedicineDays(medicine.getId());
                                db.deleteMedicineTimes(medicine.getId());
                                Log.v(TAG, "onContextItemSelected()..deleting item id=" + medicine.getId());
                                db.close();
                                medicineList.remove(holder.getAdapterPosition()); // remember to remove it from your adapter data source
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

    private void cancelAllMedicineReminders(long rowId){
        Cursor c = db.fetchMedicineTimes(rowId);
            while (c.moveToNext()) {
                long medId = c.getLong(c.getColumnIndexOrThrow(DataBaseHelper.KEY_ROWID));
                cancelAlarm(medId + 100);
            }
    }

    private void cancelAlarm(long id) {
        AlarmManager alarmManager = (AlarmManager) mCtx.getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(mCtx, (int)id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(broadcast);
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }
}