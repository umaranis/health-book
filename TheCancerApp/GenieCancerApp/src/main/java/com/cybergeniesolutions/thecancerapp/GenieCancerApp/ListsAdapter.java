package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sadafk on 3/02/2017.
 */
public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.MyViewHolder> {

    private List<ListItem> list;
    private Context context;

    private static final int ACTIVITY_CREATE = 0;
    private DataBaseHelper db;
    private static final String TAG = "ListsAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);

            textView = (TextView)itemView.findViewById(R.id.textView);

        }
    }


    public ListsAdapter(Context context, List<ListItem> list) {
        this.list = list;
        this.context = context;
        db = new DataBaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, null);
        MyViewHolder rcv = new MyViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ListItem listItem = list.get(holder.getAdapterPosition());
        holder.textView.setText(listItem.getListItem());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.textView);
                //inflating menu from xml resource
                popup.inflate(R.menu.appointment_context_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.app_menu_edit: {
                                LayoutInflater li = LayoutInflater.from(context);
                                View promptsView = li.inflate(R.layout.list_item_edit_prompt, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        context);

                                // set list_item_edit_prompt to alertdialog builder
                                alertDialogBuilder.setView(promptsView);

                                final EditText userInput = (EditText) promptsView
                                        .findViewById(R.id.editTextDialogUserInput);
                                userInput.setText(listItem.getListItem());

                                // set dialog message
                                alertDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        // get user input and set it to result
                                                        // edit text
                                                        listItem.setListItem(userInput.getText().toString());
                                                        db.open();
                                                        db.updateListItem(listItem.getId(),userInput.getText().toString());
                                                        db.close();
                                                        notifyDataSetChanged();
                                                    }
                                                })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                // create alert dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // show it
                                alertDialog.show();

                                break;
                            }
                            case R.id.app_menu_delete: {
                                db.open();
                                db.deleteListItem(listItem.getId());
                                Log.v(TAG, "onContextItemSelected()..deleting item id=" + listItem.getId());
                                db.close();
                                list.remove(holder.getAdapterPosition()); // remember to remove it from your adapter data source
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
        return this.list.size();
    }

}
