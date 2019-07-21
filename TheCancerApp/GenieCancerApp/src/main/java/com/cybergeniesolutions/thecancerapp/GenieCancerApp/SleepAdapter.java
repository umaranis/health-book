package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SleepAdapter extends RecyclerView.Adapter<SleepAdapter.MyViewHolder> {

    private List<Sleep> sleepList;
    private Context context;

    private static final int ACTIVITY_CREATE = 0;
    private DataBaseHelper db;
    private static final String TAG = "SleepAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView dayText;
        public TextView totalMinsText;
        public TextView sleepCountText;
        public TextView timeInBedText;

        public MyViewHolder(View itemView) {
            super(itemView);

            dayText = (TextView)itemView.findViewById(R.id.day);
            totalMinsText = (TextView)itemView.findViewById(R.id.total_mins);
            sleepCountText = (TextView)itemView.findViewById(R.id.sleep_count);
            timeInBedText = (TextView)itemView.findViewById(R.id.time_in_bed);
        }
    }


    public SleepAdapter(Context context, List<Sleep> sleepList) {
        this.sleepList = sleepList;
        this.context = context;
        db = new DataBaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sleep_row, null);
        MyViewHolder rcv = new MyViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Sleep sleep = sleepList.get(holder.getAdapterPosition());
        Log.v(TAG, "dayText =" + sleep.getDay());
        holder.dayText.setText(sleep.getDay());

        holder.totalMinsText.setText(sleep.getTotalMins());
        Log.v(TAG, "totalMinsText =" + sleep.getTotalMins());

        holder.sleepCountText.setText(sleep.getSleepCount());
        Log.v(TAG, "sleepCountText =" + sleep.getSleepCount());

        holder.timeInBedText.setText(sleep.getTimeInBed());
        Log.v(TAG, "timeInBedText =" + sleep.getTimeInBed());

    }

    @Override
    public int getItemCount() {
        return this.sleepList.size();
    }

}

