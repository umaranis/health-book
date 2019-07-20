package com.cybergeniesolutions.thecancerapp.GenieCancerApp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadafk on 13/01/2017.
 */
public class CalmActivity extends AppCompatActivity {

    private GridLayoutManager lLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calm_menu_layout);


        List<MainMenuItem> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(CalmActivity.this, 2);

        RecyclerView rView = (RecyclerView)findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(CalmActivity.this, rowListItem);
        rView.setAdapter(rcAdapter);

    }

    private List<MainMenuItem> getAllItemList(){

        List<MainMenuItem> allItems = new ArrayList<MainMenuItem>();
        /*allItems.add(new MainMenuItem(getResources().getString(R.string.action_breath), R.drawable.profile_icon));
        allItems.add(new MainMenuItem(getResources().getString(R.string.action_meditate), R.drawable.contacts_icon));
        allItems.add(new MainMenuItem(getResources().getString(R.string.action_prayers), R.drawable.record_icon));
        allItems.add(new MainMenuItem(getResources().getString(R.string.action_affirmations), R.drawable.notes_icon));
        allItems.add(new MainMenuItem(getResources().getString(R.string.action_sleep), R.drawable.notes_icon));*/

        return allItems;
    }
}
