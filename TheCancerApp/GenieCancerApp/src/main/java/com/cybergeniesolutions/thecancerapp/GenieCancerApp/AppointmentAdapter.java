package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

/**
 * Created by sadafk on 13/01/2017.
 */
public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {

    private List<Appointment> appointmentList;
    private Context mCtx;
    private static final int ACTIVITY_CREATE = 0;
    private DataBaseHelper db;
    private static final String TAG = "AppointmentAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView with, where, when;
        public Button play;

        public MyViewHolder(View view) {
            super(view);
            with = (TextView) view.findViewById(R.id.with);
            where = (TextView) view.findViewById(R.id.where);
            when = (TextView) view.findViewById(R.id.when);
            play = (Button) view.findViewById(R.id.play);
        }
    }


    public AppointmentAdapter(List<Appointment> appointmentList,  Context mCtx) {
        this.appointmentList = appointmentList;
        this.mCtx = mCtx;
        db = new DataBaseHelper(mCtx);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Appointment appointment = appointmentList.get(holder.getAdapterPosition());
        holder.with.setText(appointment.getWith());
        holder.when.setText(appointment.getWhen()+ " at "+appointment.getWhenTime());
        holder.where.setText(appointment.getWhere());
        holder.play.setText("play");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mCtx, holder.where);
                //inflating menu from xml resource
                popup.inflate(R.menu.appointment_context_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.app_menu_edit: {
                                Intent i = new Intent(mCtx, AppointmentEditActivity.class);
                                i.putExtra(DataBaseHelper.KEY_ROWID, appointment.getId());
                                i.putExtra("Page", "Edit");
                                Log.v(TAG, "setOnMenuItemClickListener rowid =" + appointment.getId());
                                ((Activity) mCtx).startActivityForResult(i, ACTIVITY_CREATE);
                                break;
                            }
                            case R.id.app_menu_delete: {
                                db.open();
                                db.deleteAppointment(appointment.getId());
                                Log.v(TAG, "onContextItemSelected()..deleting item id=" + appointment.getId());
                                db.close();
                                appointmentList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                cancelAlarm(appointment.getId());

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

        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException, SecurityException, IllegalStateException {

                /*MediaPlayer mediaPlayer ;
                String AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "App" + "AudioRecording.3gp";

                mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();*/

                //Toast.makeText(MainActivity.this, "Recording Playing", Toast.LENGTH_LONG).show();

            }
        });
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
        return appointmentList.size();
    }
}
