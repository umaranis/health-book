package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sadafk on 6/01/2017.
 */
public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView textView;
    public ImageView imageView;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        textView = (TextView)itemView.findViewById(R.id.country_name);
        imageView = (ImageView)itemView.findViewById(R.id.country_photo);
    }

    @Override
    public void onClick(View view) {

        switch((int)getPosition()) {
            case 0: {
                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                view.getContext().startActivity(intent);
                break;
            }
            case 1: {
                Intent intent = new Intent(view.getContext(), ContactActivity.class);
                intent.putExtra("Type", "None");
                view.getContext().startActivity(intent);
                break;
            }
            case 2: {
                Intent intent = new Intent(view.getContext(), MedicineActivity.class);
                view.getContext().startActivity(intent);
                break;
            }
            case 3: {
                Intent intent = new Intent(view.getContext(),AppointmentActivity.class);
                view.getContext().startActivity(intent);
                break;
            }
            case 4: {
                Intent intent = new Intent(view.getContext(),NotesActivity.class);
                view.getContext().startActivity(intent);
                break;
            }
            case 5: {
                Intent intent = new Intent(view.getContext(),MoodActivity.class);
                view.getContext().startActivity(intent);
                break;
            }
            /*case 6: {
                Intent intent = new Intent(view.getContext(),CalmActivity.class);
                view.getContext().startActivity(intent);
                break;
            }*/
            case 6: {
                Intent intent = new Intent(view.getContext(),TempActivity.class);
                view.getContext().startActivity(intent);
                break;
            }
            case 7: {
                Intent intent = new Intent(view.getContext(),OxygenLevelActivity.class);
                view.getContext().startActivity(intent);
                break;
            }
            case 8: {
                Intent intent = new Intent(view.getContext(),FluidIntakeActivity.class);
                view.getContext().startActivity(intent);
                break;
            }
            case 9: {
                Intent intent = new Intent(view.getContext(),LinksActivity.class);
                view.getContext().startActivity(intent);
                break;
            }
            case 10: {
                Intent intent = new Intent(view.getContext(), ListMenuActivity.class);
                view.getContext().startActivity(intent);
                break;
            }
            case 16: {
                Intent intent = new Intent(view.getContext(), SleepActivity.class);
                view.getContext().startActivity(intent);
                break;
            }
        }
    }
}
