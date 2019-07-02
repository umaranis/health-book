package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

/**
 * Created by sadafk on 6/01/2017.
 */
public class MainMenuItem {

    public int icon;
    public String title;
    public MainMenuItem(){
        super();
    }

    public MainMenuItem(String title, int icon) {
        super();
        this.icon = icon;
        this.title = title;
    }

    public String getName() {
        return this.title;
    }

    public int getPhoto() {
        return this.icon;
    }
}
