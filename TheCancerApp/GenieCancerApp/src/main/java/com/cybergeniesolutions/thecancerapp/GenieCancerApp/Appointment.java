package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

/**
 * Created by sadafk on 13/01/2017.
 */
public class Appointment {
    private String with, where, when, whenTime;
    private long id;
    public Appointment() {
    }

    public Appointment(String with, String where, String when, String whenTime, long id) {
        this.with = with;
        this.where = where;
        this.when = when;
        this.id = id;
        this.whenTime = whenTime;
    }

    public String getWhenTime() {
        return whenTime;
    }

    public void setWhenTime(String whenTime) {
        this.whenTime = whenTime;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getWith() {
        return with;
    }

    public void setWith(String with) {
        this.with = with;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }
}

