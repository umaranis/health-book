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
 * Created by sadafk on 3/02/2017.
 */
public class ListMenuApapter extends RecyclerView.Adapter<ListMenuApapter.MyViewHolder> {

    private List<ListItem> itemList;
    private Context context;
    private static final String TAG = "ListMenuAdapter";
    private DataBaseHelper db;
    private static final int ACTIVITY_CREATE = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{

        public TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);
            textView = (TextView)itemView.findViewById(R.id.row_text);
        }

        /*@Override
        public void onClick(View view) {

            String listTitle =  itemList.get((int)getPosition());
            Log.v(TAG, "listTitle=" + listTitle);
            Intent intent = new Intent(view.getContext(), ListsActivity.class);
            intent.putExtra("ListTile", listTitle);
            view.getContext().startActivity(intent);
        }*/
    }

    public ListMenuApapter(Context context, List<ListItem> itemList) {
        this.itemList = itemList;
        this.context = context;
        db = new DataBaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_list_row, null);
        MyViewHolder rcv = new MyViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ListItem listItem = itemList.get(holder.getAdapterPosition());
        holder.textView.setText((CharSequence) listItem.getListItem());

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

                                String listTitle = (String) listItem.getListItem();
                                Log.v(TAG, "listTitle=" + listTitle);

                                Intent i = new Intent(context, ListsActivity.class);
                                i.putExtra("ListTitle", listTitle);
                                i.putExtra("Page", "Edit");
                                Log.v(TAG, "setOnMenuItemClickListener rowid =" + listItem.getListId());
                                ((Activity) context).startActivityForResult(i, ACTIVITY_CREATE);
                                break;
                            }
                            case R.id.app_menu_delete: {
                                db.open();
                                db.deleteList(listItem.getListId());
                                Log.v(TAG, "onContextItemSelected()..deleting item id=" + listItem.getListId());
                                Log.v(TAG, "onContextItemSelected()..deleting item position=" + holder.getAdapterPosition());
                                db.close();
                                itemList.remove(holder.getAdapterPosition());
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
        return this.itemList.size();
    }
}
