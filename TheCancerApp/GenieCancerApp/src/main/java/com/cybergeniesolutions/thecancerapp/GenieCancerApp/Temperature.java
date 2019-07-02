package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

/**
 * Created by sadafk on 27/07/2017.
 */
public class Temperature {

    public String date, day;
    public String time;
    public String temp;
    public int unit;
    public long id;

    public Temperature(){
        super();
    }

    public Temperature(String date, String time, String day,String temp,int unit, long id) {
        super();
        this.date = date;
        this.time = time;
        this.temp = temp;
        this.day = day;
        this.unit = unit;
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

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public int getUnit(){ return unit;}

    public void setUnit(int unit) { this.unit = unit; }
}
