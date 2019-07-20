package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

/**
 * Created by sadafk on 12/12/2017.
 */
public class FluidIntake {

    public String date, day;
    public int intake;
    public int unit;
    public long id;

    public FluidIntake(){
        super();
    }

    public FluidIntake(String date,String day,int intake,int unit, long id) {
        super();
        this.date = date;
        this.intake = intake;
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

    public int getIntake() {
        return intake;
    }

    public void setIntake(int intake) {
        this.intake = intake;
    }

    public int getUnit(){ return unit;}

    public void setUnit(int unit) { this.unit = unit; }
}
