package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.List;

/**
 * Created by sadafk on 14/04/2017.
 */
public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.MyViewHolder> {

    private List<MedicineListRow> list;
    private Context context;

    private static final int ACTIVITY_CREATE = 0;
    private DataBaseHelper db;
    private static final String TAG = "MedicineListAdapter";


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView timeTextView;
        public TextView doseTextView;

        public MyViewHolder(View itemView) {
            super(itemView);

            timeTextView = (TextView)itemView.findViewById(R.id.medicine_time_textView);
            doseTextView = (TextView)itemView.findViewById(R.id.medicine_dose_textView);

        }
    }


    public MedicineListAdapter(Context context, List<MedicineListRow> list) {
        this.list = list;
        this.context = context;
        db = new DataBaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_list_row, null);
        MyViewHolder rcv = new MyViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final String time= list.get(holder.getAdapterPosition()).getTime();
        final String dose= list.get(holder.getAdapterPosition()).getDose();
        Log.v(TAG, "onBindViewHolder..dose row no.=" + holder.getAdapterPosition());
        holder.timeTextView.setText(time);
        if (Integer.parseInt(dose) > 1)
            holder.doseTextView.setText("take " + dose + " doses");
        else
            holder.doseTextView.setText("take " + dose + " dose");

        final int adapterPosition = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.dose_edit_dialogbox, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.dose_input);
                userInput.setText(list.get(adapterPosition).getDose());

                final TimePicker timePicker = (TimePicker) promptsView.findViewById(R.id.doseTimePicker);
                timePicker.setIs24HourView(true);
                String time = list.get(adapterPosition).getTime();
                timePicker.setHour(Integer.parseInt(time.substring(0, 2)));
                timePicker.setMinute(Integer.parseInt(time.substring(time.length() - 2)));
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String input = userInput.getText().toString();
                                        if(!input.equals("") || !input.isEmpty()) {
                                            list.get(adapterPosition).setDose(userInput.getText().toString());
                                            Log.v(TAG, "onBindViewHolder..dose listrow number=" + adapterPosition);
                                            String strTime = "" + timePicker.getMinute();
                                            if (strTime.length() == 1) {
                                                strTime = '0' + strTime;
                                            }
                                            String strTimeHour = "" + timePicker.getHour();
                                            if (strTimeHour.length() == 1) {
                                                strTimeHour = '0' + strTimeHour;
                                            }
                                            String doseTime = strTimeHour + ":" + strTime;
                                            list.get(adapterPosition).setTime(doseTime);
                                            Log.v(TAG, "onBindViewHolder..dose time listrow number=" + adapterPosition);
                                            //holder.timeTextView.setText(doseTime);
                                            notifyDataSetChanged();
                                        }else
                                        {
                                            Toast.makeText(context, "Please enter number of doses",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return this.list.size();
    }

}


