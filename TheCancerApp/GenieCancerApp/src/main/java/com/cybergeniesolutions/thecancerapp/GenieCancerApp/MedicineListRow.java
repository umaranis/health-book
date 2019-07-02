package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

/**
 * Created by sadafk on 17/04/2017.
 */
public class MedicineListRow {

    private String time, dose;
    private long medId,rowId;

    public MedicineListRow(String time, String dose,long medId, long rowId) {
        this.time = time;
        this.dose = dose;
        this.medId = medId;
        this.rowId = rowId;
    }

    public long getMedId() {
        return medId;
    }

    public void setMedId(long medId) {
        this.medId = medId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }
}
