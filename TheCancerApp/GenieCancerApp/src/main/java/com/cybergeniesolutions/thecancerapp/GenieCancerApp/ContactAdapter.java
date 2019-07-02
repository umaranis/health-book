package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
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
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private List<Contact> contactsList;
    private Context mCtx;
    private static final int ACTIVITY_CREATE = 0;
    private DataBaseHelper db;
    private static final String TAG = "ContactAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone, email, address;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            phone = (TextView) view.findViewById(R.id.phone);
            email = (TextView) view.findViewById(R.id.email);
            address = (TextView) view.findViewById(R.id.address);

        }


    }


    public ContactAdapter(List<Contact> contactsList, Context mCtx) {
        this.contactsList = contactsList;
        this.mCtx = mCtx;
        db = new DataBaseHelper(mCtx);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ContactAdapter.MyViewHolder holder, final int position) {

        Log.v(TAG, "onContextItemSelected()..item position=" + holder.getAdapterPosition());

        final Contact contact = contactsList.get(holder.getAdapterPosition());
        holder.name.setText(contact.getName());
        holder.phone.setText(contact.getPhone());
        holder.email.setText(contact.getEmail());
        holder.address.setText(contact.getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mCtx, holder.phone);
                //inflating menu from xml resource
                popup.inflate(R.menu.contact_context_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.menu_edit: {
                                Intent i = new Intent(mCtx, ContactEditActivity.class);
                                i.putExtra(DataBaseHelper.KEY_ROWID, contact.getId());
                                i.putExtra("Type", "None");
                                i.putExtra("Page", "Edit");
                                Log.v(TAG, "setOnMenuItemClickListener rowid =" + contact.getId());
                                ((Activity) mCtx).startActivityForResult(i, ACTIVITY_CREATE);
                                break;
                            }
                            case R.id.menu_delete: {
                                db.open();
                                db.deleteContact(contact.getId());
                                Log.v(TAG, "onContextItemSelected()..deleting item id=" + contact.getId());
                                Log.v(TAG, "onContextItemSelected()..deleting item position=" + holder.getAdapterPosition());
                                db.close();
                                contactsList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());

                                break;
                            }
                            case R.id.menu_call: {
                                Log.v(TAG, "R.id.menu_call contact.getId()=" + contact.getId());
                                db.open();
                                Cursor c = db.fetchContact(contact.getId());

                                String phoneString = c.getString(c.getColumnIndex(db.C_PHONE));
                                db.close();
                                Intent intent = new Intent(Intent.ACTION_CALL,
                                        Uri.parse("tel:" + phoneString));
                                if (ActivityCompat.checkSelfPermission(mCtx, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions((Activity) mCtx, new String[]{android.Manifest.permission.CALL_PHONE}, 105);
                                } else {
                                    ((Activity) mCtx).startActivity(intent);

                                }


                                break;
                            }
                            case R.id.menu_email: {
                                Log.v(TAG, "R.id.menu_call contact.getId()=" + contact.getId());
                                db.open();
                                Cursor c = db.fetchContact(contact.getId());

                                Intent in = new Intent(Intent.ACTION_SEND);
                                in.setType("application/octet-stream");
                                //in.setType("text/plain");
                                Log.v(TAG, "setDetail() email address:" + c.getString(c.getColumnIndex(db.C_EMAIL)));
                                in.putExtra(Intent.EXTRA_EMAIL, new String[]{c.getString(c.getColumnIndex(db.C_EMAIL))});
                                in.putExtra(Intent.EXTRA_SUBJECT, "");
                                in.putExtra(Intent.EXTRA_TEXT, "");
                                db.close();
                                try {
                                    ((Activity) mCtx).startActivity(Intent.createChooser(in, "Send mail..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    break;
                                }
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
        return contactsList.size();
    }
}