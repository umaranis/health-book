package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sadafk on 13/01/2017.
 */
public class CalmAdapter extends RecyclerView.Adapter<CalmAdapter.MyViewHolder> {

    private List<String> calmList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView text;
        public ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            text = (TextView)itemView.findViewById(R.id.country_name);
            image = (ImageView)itemView.findViewById(R.id.country_photo);
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(view.getContext(), "Clicked Menu Position = " + getPosition(), Toast.LENGTH_SHORT).show();
            /*switch((int)getPosition()) {
                case 0: {
                    Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                    view.getContext().startActivity(intent);
                    break;
                }
                case 1: {
                    Intent intent = new Intent(view.getContext(), ContactActivity.class);
                    view.getContext().startActivity(intent);
                    break;
                }

            }*/
        }
    }


    public CalmAdapter(List<String> calmList) {
        this.calmList = calmList;
    }

    private List<MainMenuItem> itemList;
    private Context context;

    public CalmAdapter(Context context, List<MainMenuItem> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.text.setText(itemList.get(position).getName());
        holder.image.setImageResource(itemList.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
