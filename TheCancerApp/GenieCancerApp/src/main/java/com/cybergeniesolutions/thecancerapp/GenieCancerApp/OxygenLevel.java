package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

/**
 * Created by sadafk on 5/12/2017.
 */
public class OxygenLevel {

    public String date, day;
    public String time;
    public int level;
    public long id;

    public OxygenLevel(){
        super();
    }

    public OxygenLevel(String date, String time, String day,int level, long id) {
        super();
        this.date = date;
        this.time = time;
        this.day = day;
        this.level = level;
        this.id = id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLevel(){ return level;}

    public void setlevel(int level) { this.level = level; }
}
