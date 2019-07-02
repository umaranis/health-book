package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

/**
 * Created by sadafk on 16/01/2017.
 */
public class Medicine {

    private String medicine, instructions, startDate, noOfTimes,unit,schedule;
    private long id;

    public Medicine() {
    }

    public Medicine(String medicine, String instructions, String startDate, String noOfTimes,String schedule,String unit, long id) {
        this.medicine = medicine;
        this.instructions = instructions;
        this.id = id;
        this.startDate = startDate;
        this.unit = unit;
        this.schedule = schedule;
        this.noOfTimes = noOfTimes;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getNoOfTimes() {
        return noOfTimes;
    }

    public void setNoOfTimes(String noOfTimes) {
        this.noOfTimes = noOfTimes;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}

