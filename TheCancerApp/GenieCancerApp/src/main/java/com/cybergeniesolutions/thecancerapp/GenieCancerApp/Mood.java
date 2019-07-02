package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

/**
 * Created by sadafk on 17/01/2017.
 */
public class Mood {

    public String date, day;
    public String time;
    public String mood;
    public String notes;
    public long id;
    public Mood(){
        super();
    }

    public Mood(String date, String time, String day,String mood, String notes, long id) {
        super();
        this.date = date;
        this.time = time;
        this.mood = mood;
        this.notes = notes;
        this.day = day;
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

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
