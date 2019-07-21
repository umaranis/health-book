package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

public class Sleep{

    public String day;
    public String total_mins;
    public String sleep_count;
    public String time_in_bed;
    public long id;

    public Sleep(){
        super();
    }

    public Sleep(String day, String sleep_count,String total_mins, String time_in_bed, long id) {
        super();
        this.total_mins = total_mins;
        this.sleep_count = sleep_count;
        this.time_in_bed = time_in_bed;
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

    public String getTotalMins() {
        return total_mins;
    }

    public void setTotalMins(String total_mins) {
        this.total_mins = total_mins;
    }

    public String getSleepCount() {
        return sleep_count;
    }

    public void setSleepCount(String sleep_count) {
        this.sleep_count = sleep_count;
    }

    public String getTimeInBed() {
        return time_in_bed;
    }

    public void setTimeInBed(String time_in_bed) {
        this.time_in_bed = time_in_bed;
    }


}
