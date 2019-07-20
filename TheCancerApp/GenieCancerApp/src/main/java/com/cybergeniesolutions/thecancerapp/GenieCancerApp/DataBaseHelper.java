package com.cybergeniesolutions.thecancerapp.GenieCancerApp;

/**
 * Created by sadafk on 20/01/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DataBaseHelper";
    private Context context;
    private String DB_PATH = "data/data/com.cybergeniesolutions.thecancerapp.thecancerapp/databases/";
    private static String DB_NAME = "thecancerapp.db";
    public static final int DB_VERSION = 26;
    public static final String KEY_ROWID = "_id";
    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String R_DATE = "R_Date";
    public static final String R_DOSE = "R_Dose";
    public static final String R_PROTIEN = "R_Protein";
    public static final String R_WEIGHT = "R_Weight";
    public static final String R_TEMP = "R_Temp";
    public static final String R_RELAPSE = "R_Relapse";
    public static final String C_NAME = "C_Name";
    public static final String C_STREET = "C_Street";
    public static final String C_SUBURB = "C_Suburb";
    public static final String C_STATE = "C_State";
    public static final String C_POST_CODE = "C_Post_Code";
    public static final String C_EMAIL = "C_Email";
    public static final String C_PHONE = "C_Phone";
    public static final String C_TYPE = "C_Type";
    public static final String M_NAME = "M_Name";
    public static final String M_NO_OF_TIMES = "M_No_Of_Times";
    public static final String M_UNIT = "M_Unit";
    public static final String M_START_DATE = "M_Start_Date";
    public static final String M_SCHEDULE = "M_Schedule";
    public static final String M_INSTRUCTIONS = "M_Instructions";
    public static final String P_NAME = "P_Name";
    public static final String P_DOB = "P_Dob";
    public static final String P_HEIGHT = "P_Height";
    public static final String P_WEIGHT = "P_Weight";
    public static final String P_BMI = "P_Bmi";
    public static final String P_SURFACE_AREA = "P_Surface_Area";
    public static final String P_UR = "P_Ur";
    public static final String P_PARENT_NAME = "P_Parent_Name";
    public static final String N_TITLE = "N_Title";
    public static final String N_BODY = "N_Body";
    public static final String I_ILLNESS = "I_Illness";
    public static final String I_START_DATE = "I_Start_Date";
    public static final String I_END_DATE = "I_End_Date";
    public static final String A_WITH = "A_With";
    public static final String A_WHEN = "A_When";
    public static final String A_WHERE = "A_Where";
    public static final String A_WHEN_TIME = "A_When_Time";
    public static final String A_REM = "A_Reminder";
    public static final String MO_TIME = "MO_Time";
    public static final String MO_DAY = "MO_Day";
    public static final String MO_DATE = "MO_Date";
    public static final String MO_MOOD = "MO_Mood";
    public static final String MO_NOTES = "MO_Notes";
    public static final String MT_TIME = "MT_Times";
    public static final String MT_DOSE = "MT_Dose";
    public static final String MT_SAT = "MT_Sat";
    public static final String MT_SUN = "MT_Sun";
    public static final String MT_MON = "MT_Mon";
    public static final String MT_TUE = "MT_Tue";
    public static final String MT_WED = "MT_Wed";
    public static final String MT_THU = "MT_Thu";
    public static final String MT_FRI = "MT_Fri";
    public static final String MT_MED_ID = "MT_Med_Id";
    public static final String T_TEMP = "T_Temp";
    public static final String T_UNIT = "T_Unit";
    public static final String T_TIME = "T_Time";
    public static final String T_DAY = "T_Day";
    public static final String T_DATE = "T_Date";
    public static final String L_LIST_TITLE = "L_List_Title";
    public static final String L_LIST_ITEM = "L_Item";
    public static final String L_LIST_ID = "L_List_Id";
    public static final String S_TITLE = "S_Title";
    public static final String S_VALUE = "S_Value";
    public static final String O_lEVEL = "O_Level";
    public static final String F_INTAKE = "F_Intake";
    public static final String INSTRUCTIONS = "Instructions";
    public static final String TABLE_RECORDS = "Records";
    public static final String TABLE_CONTACTS = "Contacts";
    public static final String TABLE_MEDICINES = "Medicines";
    public static final String TABLE_MEDICINES_DOSE_TIMES = "MedicinesDoseTimes";
    public static final String TABLE_MEDICINES_DOSE_DAYS = "MedicinesDoseDays";
    public static final String TABLE_NOTES = "Notes";
    public static final String TABLE_PROFILE = "Profile";
    public static final String TABLE_ILLNESSES = "Illnesses";
    public static final String TABLE_APPOINTMENTS = "Appointments";
    public static final String TABLE_MOODS = "Moods";
    public static final String TABLE_LISTS = "Lists";
    public static final String TABLE_LIST_ITEMS = "ListItems";
    public static final String TABLE_SETTINGS = "Settings";
    public static final String TABLE_TEMP = "Temperatures";
    public static final String TABLE_OXYGEN_LEVEL = "OxygenLevels";
    public static final String TABLE_FLUID_INTAKE = "FluidIntake";

    private static final String CREATE_FLUID_INTAKE_TABLE = "create table " + TABLE_FLUID_INTAKE + " ("
            + KEY_ROWID + " integer primary key autoincrement, "
            + T_DATE + " text not null, "
            + T_DAY + " text not null, "
            + T_UNIT + " numeric, "
            + F_INTAKE + " numeric);";

    private static final String CREATE_OXYGEN_LEVEL_TABLE = "create table " + TABLE_OXYGEN_LEVEL + " ("
            + KEY_ROWID + " integer primary key autoincrement, "
            + T_DATE + " text not null, "
            + T_DAY + " text not null, "
            + T_TIME + " text not null, "
            + O_lEVEL + " numeric);";

    private static final String CREATE_TEMP_TABLE = "create table " + TABLE_TEMP + " ("
            + KEY_ROWID + " integer primary key autoincrement, "
            + T_DATE + " text not null, "
            + T_DAY + " text not null, "
            + T_TIME + " text not null, "
            + T_TEMP + " text not null, "
            + T_UNIT + " numeric);";

    private static final String CREATE_RECORDS_TABLE = "create table " + TABLE_RECORDS + " ("
            + R_PROTIEN + " text not null, "
            + KEY_ROWID + " integer primary key autoincrement, "
            + R_DATE + " text not null, "
            + R_WEIGHT + " text not null, "
            + R_TEMP + " text not null, "
            + R_RELAPSE + " numeric, "
            + R_DOSE + " text not null);";


    private static final String CREATE_PROFILE_TABLE = "create table " + TABLE_PROFILE + " ("
            + P_NAME + " text not null, "
            + KEY_ROWID + " integer primary key autoincrement, "
            + P_DOB + " text not null, "
            + P_PARENT_NAME + " text not null, "
            + P_BMI + " text not null, "
            + P_HEIGHT + " text not null, "
            + P_WEIGHT + " text not null, "
            + P_SURFACE_AREA + " text not null, "
            + P_UR + " text not null);";

    private static final String PROFILE_TABLE_ALTER_1 = "ALTER TABLE "
            + TABLE_PROFILE + " ADD COLUMN " + P_BMI + " text not null;";

    private static final String PROFILE_TABLE_ALTER_2 = "ALTER TABLE "
            + TABLE_PROFILE + " ADD COLUMN " + P_HEIGHT + " text not null;";

    private static final String PROFILE_TABLE_ALTER_3 = "ALTER TABLE "
            + TABLE_PROFILE + " ADD COLUMN " + P_WEIGHT + " text not null;";

    private static final String PROFILE_TABLE_ALTER_4 = "ALTER TABLE "
            + TABLE_PROFILE + " ADD COLUMN " + P_SURFACE_AREA + " text not null;";

    private static final String PROFILE_TABLE_ALTER_5 = "UPDATE "
            + TABLE_PROFILE + " SET" + " P_BMI= IfNull(" + P_BMI + ",''), " + P_HEIGHT + "= IfNull(" + P_HEIGHT + ",0);";

    private static final String PROFILE_TABLE_ALTER_6 = "UPDATE "
            + TABLE_PROFILE + " SET" + " P_WEIGHT= IfNull(" + P_WEIGHT + ",''), " + P_SURFACE_AREA + "= IfNull(" + P_SURFACE_AREA + ",0);";

    private static final String CREATE_APPOINTMENTS_TABLE = "create table " + TABLE_APPOINTMENTS + " ("
            + A_WITH + " text not null, "
            + A_WHEN + " text not null, "
            + A_WHEN_TIME + " text not null, "
            + A_REM + " integer, "
            + KEY_ROWID + " integer primary key autoincrement, "
            + A_WHERE + " text not null);";

    private static final String CREATE_CONTACTS_TABLE = "create table " + TABLE_CONTACTS + " ("
            + C_NAME + " text not null, "
            + C_TYPE + " text not null, "
            + KEY_ROWID + " integer primary key autoincrement, "
            + C_STREET + " text not null, "
            + C_SUBURB + " text not null, "
            + C_STATE + " text not null, "
            + C_POST_CODE + " text not null, "
            + C_EMAIL + " text not null, "
            + C_PHONE + " text not null);";

    private static final String CREATE_MEDICINES_TABLE = "create table " + TABLE_MEDICINES + " ("
            + M_NAME + " text not null, "
            + M_START_DATE + " text not null, "
            + M_INSTRUCTIONS + " text not null, "
            + M_SCHEDULE + " text not null, "
            + M_UNIT + " text not null, "
            + M_NO_OF_TIMES + " numeric, "
            + KEY_ROWID + " integer primary key autoincrement);";

    private static final String CREATE_MEDICINES_TIME_TABLE = "create table " + TABLE_MEDICINES_DOSE_TIMES + " ("
            + MT_TIME + " text not null, "
            + MT_MED_ID + " integer, "
            + KEY_ROWID + " integer primary key autoincrement, "
            + MT_DOSE + " text not null);";

    private static final String CREATE_MEDICINES_DAYS_TABLE = "create table " + TABLE_MEDICINES_DOSE_DAYS + " ("
            + MT_SAT + " integer, "
            + MT_SUN + " integer, "
            + MT_MON + " integer, "
            + MT_TUE + " integer, "
            + MT_WED + " integer, "
            + MT_THU + " integer, "
            + MT_FRI + " integer, "
            + MT_MED_ID + " integer, "
            + KEY_ROWID + " integer primary key autoincrement);";

    private static final String CREATE_NOTES_TABLE = "create table " + TABLE_NOTES + " ("
            + N_TITLE + " text not null, "
            + KEY_ROWID + " integer primary key autoincrement, "
            + N_BODY + " text not null);";

    private static final String CREATE_INSTRUCTIONS_TABLE = "create table " + INSTRUCTIONS + " ("
            + KEY_ROWID + " integer primary key autoincrement, "
            + INSTRUCTIONS + " text not null);";

    private static final String CREATE_ILLNESSES_TABLE = "create table " + TABLE_ILLNESSES + " ("
            + I_ILLNESS + " text not null, "
            + I_START_DATE + " text not null, "
            + I_END_DATE + " text not null, "
            + KEY_ROWID + " integer primary key autoincrement);";

    private static final String CREATE_MOODS_TABLE = "create table " + TABLE_MOODS + " ("
            + MO_DATE + " text not null, "
            + MO_DAY + " text not null, "
            + MO_TIME + " text not null, "
            + MO_MOOD + " text not null, "
            + MO_NOTES + " text not null, "
            + KEY_ROWID + " integer primary key autoincrement);";

    private static final String CREATE_LISTS_TABLE = "create table " + TABLE_LISTS + " ("
            + L_LIST_TITLE + " text not null, "
            + KEY_ROWID + " integer primary key autoincrement);";

    private static final String CREATE_LIST_ITEM_TABLE = "create table " + TABLE_LIST_ITEMS + " ("
            + L_LIST_ITEM + " text not null, "
            + L_LIST_ID + " integer, "
            + KEY_ROWID + " integer primary key autoincrement);";

    private static final String CREATE_SETTINGS_TABLE = "create table " + TABLE_SETTINGS + " ("
            + S_TITLE + " text not null, "
            + S_VALUE + " text not null, "
            + KEY_ROWID + " integer primary key autoincrement);";



    public SQLiteDatabase mDataBase;


    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        Log.v(TAG,"DataBaseHelper() DB_VERSION = " + DB_VERSION);
        Log.v(TAG,"DataBaseHelper() path" + DB_PATH + DB_NAME);
        boolean dbexist = checkdatabase();
        if (dbexist) {
        } else {
            Log.v(TAG,"DataBaseHelper() database doesn't exist");
            this.getReadableDatabase();
        }
    }


    private boolean checkdatabase() {
        boolean checkdb = false;
        try {
            File dbfile = new File(DB_PATH + DB_NAME);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            throw new Error("Database doesn't exist");
        }

        return checkdb;
    }


    public void open() {
        // Open the database
        //mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null,
        //        SQLiteDatabase.OPEN_READWRITE);
        mDataBase = this.getWritableDatabase();
    }

    public long addSetting(String title, String value) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(S_TITLE, title);
        initialValues.put(S_VALUE,value);

        return  mDataBase.insert(TABLE_SETTINGS, null, initialValues);
    }

    public long addProfile(String name, String dob, String ur, String parent_name, String bmi, String height, String weight, String surfaceArea) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(P_NAME, name);
        initialValues.put(P_UR,ur);
        initialValues.put(P_DOB, dob);
        initialValues.put(P_BMI, bmi);
        initialValues.put(P_HEIGHT, height);
        initialValues.put(P_WEIGHT, weight);
        initialValues.put(P_SURFACE_AREA, surfaceArea);
        initialValues.put(P_PARENT_NAME, parent_name);

        return  mDataBase.insert(TABLE_PROFILE, null, initialValues);
    }

    public long addRecord(String date, String protien, String dose, String weight, String temp,int relapse) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(R_DATE, date);
        initialValues.put(R_PROTIEN, protien);
        initialValues.put(R_DOSE, dose);
        initialValues.put(R_WEIGHT, weight);
        initialValues.put(R_TEMP, temp);
        initialValues.put(R_RELAPSE, relapse);
        return  mDataBase.insert(TABLE_RECORDS, null, initialValues);
    }

    public long addNote(String title, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(N_TITLE, title);
        initialValues.put(N_BODY, body);
        return  mDataBase.insert(TABLE_NOTES, null, initialValues);
    }

    public long addMedicine(String name,String unit, String startDate, String noOfTimes, String instruction, String schedule) {
        Log.v(TAG,"addMedicine()");
        ContentValues initialValues = new ContentValues();
        initialValues.put(M_NAME, name);
        initialValues.put(M_UNIT, unit);
        initialValues.put(M_NO_OF_TIMES, noOfTimes);
        initialValues.put(M_INSTRUCTIONS, instruction);
        initialValues.put(M_START_DATE, startDate);
        initialValues.put(M_SCHEDULE, schedule);
        return  mDataBase.insert(TABLE_MEDICINES, null, initialValues);
    }
    public long addMedicineTime(String dose,String time, long medId) {
        Log.v(TAG, "addMedicineTime()");
        ContentValues initialValues = new ContentValues();
        initialValues.put(MT_DOSE, dose);
        initialValues.put(MT_TIME, time);
        initialValues.put(MT_MED_ID, medId);
        return  mDataBase.insert(TABLE_MEDICINES_DOSE_TIMES, null, initialValues);
    }

    public long addMedicineDays(boolean days[], long medId) {
        Log.v(TAG, "addMedicineDays()");
        ContentValues initialValues = new ContentValues();
        if(days[0])
            initialValues.put(MT_SUN, 1);
        else
            initialValues.put(MT_SUN, 0);

        if(days[1])
            initialValues.put(MT_MON, 1);
        else
            initialValues.put(MT_MON, 0);

        if(days[2])
            initialValues.put(MT_TUE, 1);
        else
            initialValues.put(MT_TUE, 0);

        if(days[3])
            initialValues.put(MT_WED, 1);
        else
            initialValues.put(MT_WED, 0);

        if(days[4])
            initialValues.put(MT_THU, 1);
        else
            initialValues.put(MT_THU, 0);

        if(days[5])
            initialValues.put(MT_FRI, 1);
        else
            initialValues.put(MT_FRI, 0);

        if(days[6])
            initialValues.put(MT_SAT, 1);
        else
            initialValues.put(MT_SAT, 0);

        initialValues.put(MT_MED_ID, medId);
        return  mDataBase.insert(TABLE_MEDICINES_DOSE_DAYS, null, initialValues);
    }

    public long addIllness(String illness, String startDate,String endDate) {
        Log.v(TAG, "addIllness()");
        ContentValues initialValues = new ContentValues();
        initialValues.put(I_ILLNESS, illness);
        initialValues.put(I_START_DATE, startDate);
        initialValues.put(I_END_DATE, endDate);
        return  mDataBase.insert(TABLE_ILLNESSES, null, initialValues);
    }

    public long addAppointment(String with, String when,String where, String time, int rem) {
        Log.v(TAG, "addAppointments()");
        ContentValues initialValues = new ContentValues();
        initialValues.put(A_WITH, with);
        initialValues.put(A_WHEN, when);
        initialValues.put(A_WHERE, where);
        initialValues.put(A_WHEN_TIME, time);
        initialValues.put(A_REM, rem);
        return  mDataBase.insert(TABLE_APPOINTMENTS, null, initialValues);
    }

    public long addContact(String name, String phone,String email,String street,String suburb,String state, String postCode,String type) {
        Log.v(TAG,"addContact() type =" + type);
        ContentValues initialValues = new ContentValues();
        initialValues.put(C_NAME, name);
        initialValues.put(C_PHONE, phone);
        initialValues.put(C_EMAIL, email);
        initialValues.put(C_STREET, street);
        initialValues.put(C_SUBURB, suburb);
        initialValues.put(C_STATE, state);
        initialValues.put(C_POST_CODE, postCode);
        initialValues.put(C_TYPE, type);
        return  mDataBase.insert(TABLE_CONTACTS, null, initialValues);
    }

    public long addMood(String date, String time,String day,String mood, String notes) {
        Log.v(TAG, "addMood()");
        ContentValues initialValues = new ContentValues();
        initialValues.put(MO_DATE, date);
        initialValues.put(MO_DAY, day);
        initialValues.put(MO_MOOD, mood);
        initialValues.put(MO_NOTES, notes);
        initialValues.put(MO_TIME, time);
        return  mDataBase.insert(TABLE_MOODS, null, initialValues);
    }

    public long addTemp(String date, String time,String day,String temp, int unit) {
        Log.v(TAG, "addTemp()");
        ContentValues initialValues = new ContentValues();
        initialValues.put(T_DATE, date);
        initialValues.put(T_DAY, day);
        initialValues.put(T_UNIT, unit);
        initialValues.put(T_TEMP, temp);
        initialValues.put(T_TIME, time);
        return  mDataBase.insert(TABLE_TEMP, null, initialValues);
    }

    public long addOxyLevel(String date, String time,String day,int level) {
        Log.v(TAG, "addOxyLevel()");
        ContentValues initialValues = new ContentValues();
        initialValues.put(T_DATE, date);
        initialValues.put(T_DAY, day);
        initialValues.put(O_lEVEL, level);
        initialValues.put(T_TIME, time);
        return  mDataBase.insert(TABLE_OXYGEN_LEVEL, null, initialValues);
    }

    public long addFluidIntake(String date, int unit,String day,int intake) {
        Log.v(TAG, "addFluidIntake()");
        ContentValues initialValues = new ContentValues();
        initialValues.put(T_DATE, date);
        initialValues.put(T_DAY, day);
        initialValues.put(F_INTAKE, intake);
        initialValues.put(T_UNIT, unit);
        return  mDataBase.insert(TABLE_FLUID_INTAKE, null, initialValues);
    }

    public long addListItem(String listItem, int listID) {
        Log.v(TAG, "addListItem()");
        ContentValues initialValues = new ContentValues();
        initialValues.put(L_LIST_ITEM, listItem);
        initialValues.put(L_LIST_ID, listID);
        return  mDataBase.insert(TABLE_LIST_ITEMS, null, initialValues);
    }

    public long addList(String listTitle) {
        Log.v(TAG, "addList() list = " + listTitle);
        ContentValues initialValues = new ContentValues();
        initialValues.put(L_LIST_TITLE, listTitle);
        return  mDataBase.insert(TABLE_LISTS, null, initialValues);
    }

    public long addInstruction(String ins) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(INSTRUCTIONS, ins);
        return  mDataBase.insert(INSTRUCTIONS, null, initialValues);
    }


    public Cursor fetchAllRecords() {

        Log.v(TAG, "inside fetchAllRecords()");
        return mDataBase.query(TABLE_RECORDS, new String[]{KEY_ROWID, R_DATE, R_DOSE, R_PROTIEN, R_WEIGHT, R_TEMP, R_RELAPSE}, null, null, null, null, null);

    }

    public Cursor fetchAllSETTINGS() {

        Log.v(TAG, "inside fetchAllSettins()");
        return mDataBase.query(TABLE_SETTINGS, new String[]{KEY_ROWID, S_TITLE, S_VALUE}, null, null, null, null, null);

    }

    public Cursor fetchAllLists() {

        Log.v(TAG, "inside fetchAllLists()");
        return mDataBase.query(TABLE_LISTS, new String[]{KEY_ROWID, L_LIST_TITLE}, null, null, null, null, null);

    }

    public Cursor fetchAllListItems(int listID) {

        Log.v(TAG, "inside fetchAllListItems()");
        Cursor mCursor =
                mDataBase.query(true, TABLE_LIST_ITEMS, new String[] {KEY_ROWID,
                                L_LIST_ITEM, L_LIST_ID}, L_LIST_ID + "='" + listID + "'", null,
                        null, null, null, null);
        if (mCursor != null) {
            //mCursor.moveToFirst();
            Log.v(TAG,"inside fetchAllListItems cursor is not null");
        }
        return mCursor;

    }

    public Cursor fetchAllMedicines() {

        Log.v(TAG,"inside fetchAllMedicines()");
        return mDataBase.query(TABLE_MEDICINES, new String[] {KEY_ROWID, M_NAME,M_NO_OF_TIMES,M_START_DATE,M_SCHEDULE,M_UNIT,M_INSTRUCTIONS}, null, null, null, null, null);

    }


    public Cursor fetchAllNotes() {

        Log.v(TAG,"inside fetchAllNotes()");
        return mDataBase.query(TABLE_NOTES, new String[] {KEY_ROWID, N_TITLE,N_BODY}, null, null, null, null, null);

    }

    public Cursor fetchAllIllnesses() {

        Log.v(TAG,"inside fetchAllIllnesses()");
        return mDataBase.query(TABLE_ILLNESSES, new String[] {KEY_ROWID, I_ILLNESS,I_START_DATE,I_END_DATE}, null, null, null, null, null);

    }

    public Cursor fetchAllAppointments() {

        Log.v(TAG,"inside fetchAllAppointments()");
        return mDataBase.query(TABLE_APPOINTMENTS, new String[] {KEY_ROWID, A_WITH,A_WHEN,A_WHERE,A_WHEN_TIME,A_REM}, null, null, null, null, null);

    }



    public Cursor fetchAllMoods() {

        Log.v(TAG,"inside fetchAllMoods()");
        return mDataBase.query(TABLE_MOODS, new String[] {KEY_ROWID, MO_TIME,MO_DAY,MO_DATE,MO_MOOD, MO_NOTES}, null, null, null, null, null);

    }

     public Cursor fetchAllTemps() {

        Log.v(TAG,"inside fetchAllTemps()");
        return mDataBase.query(TABLE_TEMP, new String[] {KEY_ROWID, T_TEMP,T_UNIT,T_DATE,T_DAY,T_TIME}, null, null, null, null, null);

    }

    public Cursor fetchAllOxyLevels() {

        Log.v(TAG,"inside fetchAllOxyLevels()");
        return mDataBase.query(TABLE_OXYGEN_LEVEL, new String[] {KEY_ROWID, O_lEVEL,T_DATE,T_DAY,T_TIME}, null, null, null, null, null);

    }

    public Cursor fetchAllFluidIntakes() {

        Log.v(TAG,"inside fetchAllFluidIntakes()");
        return mDataBase.query(TABLE_FLUID_INTAKE, new String[] {KEY_ROWID, F_INTAKE,T_DATE,T_DAY,T_UNIT}, null, null, null, null, null);

    }

    public Cursor fetchAllInstructions() {

        Log.v(TAG,"inside fetchAllInstructions()");
        return mDataBase.query(INSTRUCTIONS, new String[] {KEY_ROWID, INSTRUCTIONS}, null, null, null, null, null);

    }

    public Cursor fetchProfile() {

        Log.v(TAG,"inside fetchProfile");
        return mDataBase.query(TABLE_PROFILE, new String[] {KEY_ROWID,
                P_NAME,P_DOB,P_PARENT_NAME,P_UR, P_BMI, P_HEIGHT, P_WEIGHT, P_SURFACE_AREA}, null, null, null, null, null);

    }



    public Cursor fetchContacts(String type) {

        Log.v(TAG,"inside fetchContacts type =" + type);
        Cursor mCursor =
                mDataBase.query(true, TABLE_CONTACTS, new String[] {KEY_ROWID,
                                C_NAME,C_PHONE,C_EMAIL,C_STREET,C_SUBURB,C_STATE,C_POST_CODE}, C_TYPE + "='" + type + "'", null,
                        null, null, null, null);
        if (mCursor != null) {
            //mCursor.moveToFirst();
            Log.v(TAG,"inside fetchContacts cursor is not null");
        }
        return mCursor;

    }

    public String fetchSetting(String title) {

        Log.v(TAG,"inside fetchSetting title =" + title);
        String value = "OFF";
        Cursor mCursor =
                mDataBase.query(true, TABLE_SETTINGS, new String[] {KEY_ROWID,
                                S_VALUE, S_TITLE}, S_TITLE + "='" + title + "'", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            Log.v(TAG,"inside fetchSetting cursor is not null");
            value = mCursor.getString(mCursor.getColumnIndexOrThrow(S_VALUE));
        }
        return value;

    }

    public int fetchListID(String listTitle) {
        int id = 0;
        Log.v(TAG,"inside fetchListID listTitle =" + listTitle);
        Cursor mCursor =
                mDataBase.query(true, TABLE_LISTS, new String[] {KEY_ROWID}, L_LIST_TITLE + "='" + listTitle + "'", null,
                        null, null, null, null);
        if (mCursor != null && (mCursor.getCount() > 0)) {
            mCursor.moveToFirst();
            Log.v(TAG, "inside fetchListID cursor is not null");
            id = mCursor.getInt(mCursor.getColumnIndex(KEY_ROWID));
        }
        return id;

    }


    public Cursor fetchRecord(long rowId) {
        Cursor mCursor =
                mDataBase.query(true, TABLE_RECORDS, new String[] {KEY_ROWID,
                                R_DATE, R_DOSE,R_PROTIEN,R_WEIGHT,R_TEMP,R_RELAPSE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchRecordWithDate(String date) {
        Cursor mCursor =
                mDataBase.query(true, TABLE_RECORDS, new String[] {KEY_ROWID,
                                R_DATE, R_DOSE,R_PROTIEN,R_WEIGHT,R_TEMP,R_RELAPSE}, R_DATE + "='" + date + "'", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchContact(long rowId) {
        Cursor mCursor =
                mDataBase.query(true, TABLE_CONTACTS, new String[] {KEY_ROWID,
                                C_NAME,C_PHONE,C_EMAIL,C_STREET,C_SUBURB,C_STATE,C_POST_CODE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchRelapses() {
        Cursor mCursor =
                mDataBase.query(true, TABLE_RECORDS, new String[] {KEY_ROWID,R_DATE}, R_RELAPSE + "=1", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchMedicine(long rowId) {
        Cursor mCursor =
                mDataBase.query(true, TABLE_MEDICINES, new String[] {KEY_ROWID,
                                M_NAME, M_NO_OF_TIMES,M_START_DATE,M_SCHEDULE,M_UNIT,M_INSTRUCTIONS}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);

        return mCursor;
    }

    public Cursor fetchMedicineTimes(long medId) {
        Cursor mCursor =
                mDataBase.query(true, TABLE_MEDICINES_DOSE_TIMES, new String[] {KEY_ROWID,
                                MT_DOSE, MT_TIME,MT_MED_ID}, MT_MED_ID + "=" + medId, null,
                        null, null, null, null);

        return mCursor;
    }

    public Cursor fetchMedicineTime(long rowId) {
        Cursor mCursor =
                mDataBase.query(true, TABLE_MEDICINES_DOSE_TIMES, new String[] {KEY_ROWID,
                                MT_DOSE, MT_TIME,MT_MED_ID}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);

        return mCursor;
    }

    public Cursor fetchMedicineDays(long medId) {
        Cursor mCursor =
                mDataBase.query(true, TABLE_MEDICINES_DOSE_DAYS, new String[] {KEY_ROWID,
                                MT_SAT, MT_SUN,MT_MON,MT_TUE,MT_WED,MT_THU,MT_FRI,MT_MED_ID}, MT_MED_ID + "=" + medId, null,
                        null, null, null, null);

        return mCursor;
    }

    public Cursor fetchNote(long rowId) {
        Cursor mCursor =
                mDataBase.query(true, TABLE_NOTES, new String[] {KEY_ROWID,
                                N_TITLE, N_BODY}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchIllness(long rowId) {
        Cursor mCursor =
                mDataBase.query(true, TABLE_ILLNESSES, new String[] {KEY_ROWID,
                                I_ILLNESS, I_START_DATE,I_END_DATE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAppointment(long rowId) {
        Log.v(TAG, "inside fetchAppointment");
        Cursor mCursor =
                mDataBase.query(true, TABLE_APPOINTMENTS, new String[] {KEY_ROWID,
                                A_WITH, A_WHERE,A_WHEN,A_WHEN_TIME,A_REM}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            Log.v(TAG, "cursor is not null");
        }
        return mCursor;
    }

    public Cursor fetchAppointmentReminders() {
        Cursor mCursor =
                mDataBase.query(true, TABLE_APPOINTMENTS, new String[] {KEY_ROWID,
                                A_WITH, A_WHERE,A_WHEN,A_WHEN_TIME,A_REM}, A_REM + "=" + 1, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchMood(long rowId) {
        Cursor mCursor =
                mDataBase.query(true, TABLE_MOODS, new String[] {KEY_ROWID,
                                MO_MOOD, MO_DATE,MO_DAY,MO_TIME,MO_NOTES}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchTemp(long rowId) {
        Cursor mCursor =
                mDataBase.query(true, TABLE_TEMP, new String[] {KEY_ROWID,
                                T_TEMP, T_UNIT,T_DAY,T_DATE,T_TIME}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchOxyLevel(long rowId) {
        Cursor mCursor =
                mDataBase.query(true, TABLE_OXYGEN_LEVEL, new String[] {KEY_ROWID,
                                O_lEVEL,T_DAY,T_DATE,T_TIME}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchFluidIntake(long rowId) {
        Cursor mCursor =
                mDataBase.query(true, TABLE_FLUID_INTAKE, new String[] {KEY_ROWID,
                                F_INTAKE,T_DAY,T_DATE,T_UNIT}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchInstruction(long rowId) {
        Cursor mCursor =
                mDataBase.query(true, INSTRUCTIONS, new String[] {KEY_ROWID,
                                INSTRUCTIONS}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public boolean updateRecord(long rowId, String date, String dose, String protien,String weight,String temp,int relapse) {
        ContentValues args = new ContentValues();
        args.put(R_DATE, date);
        args.put(R_DOSE, dose);
        args.put(R_PROTIEN, protien);
        args.put(R_WEIGHT, weight);
        args.put(R_TEMP, temp);
        args.put(R_RELAPSE, relapse);
        return 	mDataBase.update(TABLE_RECORDS, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateSetting(String title, String value) {
        ContentValues args = new ContentValues();
        args.put(S_VALUE, value);
        return 	mDataBase.update(TABLE_SETTINGS, args, S_TITLE + "='" + title +"'", null) > 0;
    }

    public boolean updateList(long rowId, String listTitle) {
        ContentValues args = new ContentValues();
        args.put(L_LIST_TITLE, listTitle);
        return 	mDataBase.update(TABLE_LISTS, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateListItem(long rowId, String listItem) {
        ContentValues args = new ContentValues();
        args.put(L_LIST_ITEM, listItem);
        return 	mDataBase.update(TABLE_LIST_ITEMS, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateRecordWithDate(String date, String dose, String protien,String weight,String temp,int relapse) {
        ContentValues args = new ContentValues();
        //args.put(R_DATE, date);
        args.put(R_DOSE, dose);
        args.put(R_PROTIEN, protien);
        args.put(R_WEIGHT, weight);
        args.put(R_TEMP, temp);
        args.put(R_RELAPSE, relapse);
        return 	mDataBase.update(TABLE_RECORDS, args, R_DATE + "='" + date + "'", null) > 0;
    }

    public boolean updateProfile(long rowId, String name, String parent_name, String ur,String dob, String bmi, String height, String weight, String surfaceArea) {
        Log.v(TAG, "updateProfile");
        ContentValues args = new ContentValues();
        args.put(P_NAME, name);
        args.put(P_PARENT_NAME, parent_name);
        args.put(P_UR, ur);
        args.put(P_DOB, dob);
        args.put(P_BMI, bmi);
        args.put(P_WEIGHT, weight);
        args.put(P_HEIGHT, height);
        args.put(P_SURFACE_AREA, surfaceArea);
        return 	mDataBase.update(TABLE_PROFILE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateMedicine(long rowId, String name, String unit,String startDate, String noOfTimes, String instruction, String schedule) {
        ContentValues args = new ContentValues();
        args.put(M_NAME, name);
        args.put(M_NO_OF_TIMES, noOfTimes);
        args.put(M_UNIT, unit);
        args.put(M_INSTRUCTIONS, instruction);
        args.put(M_START_DATE, startDate);
        args.put(M_SCHEDULE, schedule);
        return 	mDataBase.update(TABLE_MEDICINES, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateMedicineDoseList(long rowId, String dose, String time,long medId) {
        ContentValues args = new ContentValues();
        args.put(MT_MED_ID, medId);
        args.put(MT_TIME, time);
        args.put(MT_DOSE, dose);
        return 	mDataBase.update(TABLE_MEDICINES_DOSE_TIMES, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateMedicineDays(boolean days[],long medId) {
        ContentValues args = new ContentValues();
        args.put(MT_MED_ID, medId);
        if(days[0])
            args.put(MT_SUN, 1);
        else
            args.put(MT_SUN, 0);

        if(days[1])
            args.put(MT_MON, 1);
        else
            args.put(MT_MON, 0);

        if(days[2])
            args.put(MT_TUE, 1);
        else
            args.put(MT_TUE, 0);

        if(days[3])
            args.put(MT_WED, 1);
        else
            args.put(MT_WED, 0);

        if(days[4])
            args.put(MT_THU, 1);
        else
            args.put(MT_THU, 0);

        if(days[5])
            args.put(MT_FRI, 1);
        else
            args.put(MT_FRI, 0);

        if(days[6])
            args.put(MT_SAT, 1);
        else
            args.put(MT_SAT, 0);
        return 	mDataBase.update(TABLE_MEDICINES_DOSE_DAYS, args, MT_MED_ID + "=" + medId, null) > 0;
    }

    public boolean updateNote(long rowId, String title, String body) {
        ContentValues args = new ContentValues();
        args.put(N_TITLE, title);
        args.put(N_BODY, body);
        return 	mDataBase.update(TABLE_NOTES, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateIllness(long rowId, String illness, String startDate, String endDate) {
        ContentValues args = new ContentValues();
        args.put(I_ILLNESS, illness);
        args.put(I_START_DATE, startDate);
        args.put(I_END_DATE, endDate);
        return 	mDataBase.update(TABLE_ILLNESSES, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateAppointment(long rowId, String with, String when, String where, String time, int rem) {
        ContentValues args = new ContentValues();
        args.put(A_WITH, with);
        args.put(A_WHEN, when);
        args.put(A_WHERE, where);
        args.put(A_WHEN_TIME, time);
        args.put(A_REM, rem);
        return 	mDataBase.update(TABLE_APPOINTMENTS, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateContact(long rowId, String name, String phone, String email,String street,String suburb,String state,String postCode,String type) {
        Log.v(TAG,"updateContact() type =" + type);
        ContentValues args = new ContentValues();
        args.put(C_NAME, name);
        args.put(C_PHONE, phone);
        args.put(C_EMAIL, email);
        args.put(C_STREET, street);
        args.put(C_SUBURB, suburb);
        args.put(C_STATE, state);
        args.put(C_POST_CODE, postCode);
        args.put(C_TYPE, type);
        return 	mDataBase.update(TABLE_CONTACTS, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateMood(long rowId, String time, String date, String day,String mood, String notes) {
        Log.v(TAG, "updateMood()");
        ContentValues args = new ContentValues();
        args.put(MO_TIME, time);
        args.put(MO_MOOD, mood);
        args.put(MO_DAY, day);
        args.put(MO_DATE, date);
        args.put(MO_NOTES, notes);
        return 	mDataBase.update(TABLE_MOODS, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateTemp(long rowId, String temp, int unit,String date, String time, String day) {
        Log.v(TAG, "updateTemp()");
        ContentValues args = new ContentValues();
        args.put(T_TEMP, temp);
        args.put(T_UNIT, unit);
        args.put(T_DATE, date);
        args.put(T_DAY, day);
        args.put(T_TIME, time);
        return 	mDataBase.update(TABLE_TEMP, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateOxyLevel(long rowId, int level, String date, String time, String day) {
        Log.v(TAG, "updateOxyLevel()");
        ContentValues args = new ContentValues();
        args.put(O_lEVEL, level);
        args.put(T_DATE, date);
        args.put(T_DAY, day);
        args.put(T_TIME, time);
        return 	mDataBase.update(TABLE_OXYGEN_LEVEL, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateFluidIntake(long rowId, int intake, int unit,String date,  String day) {
        Log.v(TAG, "updateFluidIntake()");
        ContentValues args = new ContentValues();
        args.put(F_INTAKE, intake);
        args.put(T_DATE, date);
        args.put(T_DAY, day);
        args.put(T_UNIT, unit);
        return 	mDataBase.update(TABLE_FLUID_INTAKE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateProfileContact(String name, String phone, String email,String street,String suburb,String state,String postCode,String type) {
        Log.v(TAG,"updateContact() type =" + type);
        ContentValues args = new ContentValues();
        args.put(C_NAME, name);
        args.put(C_PHONE, phone);
        args.put(C_EMAIL, email);
        args.put(C_STREET, street);
        args.put(C_SUBURB, suburb);
        args.put(C_STATE, state);
        args.put(C_POST_CODE, postCode);
        return 	mDataBase.update(TABLE_CONTACTS, args, C_TYPE + "='Profile'" , null) > 0;
    }

    public boolean updateInstruction(long rowId, String ins) {

        ContentValues args = new ContentValues();
        args.put(INSTRUCTIONS, ins);
        return 	mDataBase.update(INSTRUCTIONS, args, KEY_ROWID + "=" + rowId, null) > 0;
    }


    public boolean deleteSetting(String title) {
        return 	mDataBase.delete(TABLE_SETTINGS, S_TITLE + "=" + title, null) > 0;
    }

    public boolean deleteRecord(long rowId) {
        return 	mDataBase.delete(TABLE_RECORDS, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteList(long rowId) {
        boolean returnValue = false;

        if((mDataBase.delete(TABLE_LISTS, KEY_ROWID + "=" + rowId, null) > 0) && (mDataBase.delete(TABLE_LIST_ITEMS, L_LIST_ID + "=" + rowId, null) > 0))
            returnValue = true;

        return returnValue;
    }

    public boolean deleteListItem(long id) {
        return 	mDataBase.delete(TABLE_LIST_ITEMS, KEY_ROWID + "=" + id, null) > 0;
    }

    public boolean deleteMedicine(long rowId) {
        return 	mDataBase.delete(TABLE_MEDICINES, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteMedicineDays(long medId) {
        return 	mDataBase.delete(TABLE_MEDICINES_DOSE_DAYS, MT_MED_ID + "=" + medId, null) > 0;
    }

    public boolean deleteMedicineTimes(long medId) {
        return 	mDataBase.delete(TABLE_MEDICINES_DOSE_TIMES, MT_MED_ID + "=" + medId, null) > 0;
    }

    public boolean deleteNote(long rowId) {
        return 	mDataBase.delete(TABLE_NOTES, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteIllness(long rowId) {
        return 	mDataBase.delete(TABLE_ILLNESSES, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteContact(long rowId) {
        return 	mDataBase.delete(TABLE_CONTACTS, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteAppointment(long rowId) {
        return 	mDataBase.delete(TABLE_APPOINTMENTS, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteMood(long rowId) {
        return 	mDataBase.delete(TABLE_MOODS, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteInstruction(long rowId) {
        return 	mDataBase.delete(INSTRUCTIONS, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteProfile(long rowId) {
        return 	mDataBase.delete(TABLE_PROFILE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteProfileContact() {
        return 	mDataBase.delete(TABLE_CONTACTS, C_TYPE + "= 'Profile'", null) > 0;
    }

    public boolean deleteTemp(long rowId) {
        return 	mDataBase.delete(TABLE_TEMP, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteOxyLevels(long rowId) {
        return 	mDataBase.delete(TABLE_OXYGEN_LEVEL, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteFluidIntake(long rowId) {
        return 	mDataBase.delete(TABLE_FLUID_INTAKE, KEY_ROWID + "=" + rowId, null) > 0;
    }


    @Override
    public synchronized void close() {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.v(TAG, "onCreate()");
        db.execSQL(CREATE_RECORDS_TABLE);
        Log.v(TAG, "onCreate() Records table created");
        db.execSQL(CREATE_MEDICINES_TABLE);
        Log.v(TAG, "onCreate() Medicines table created");
        db.execSQL(CREATE_CONTACTS_TABLE);
        Log.v(TAG, "onCreate() contacts table created");
        db.execSQL(CREATE_NOTES_TABLE);
        Log.v(TAG, "onCreate() notes table created");
        db.execSQL(CREATE_PROFILE_TABLE);
        Log.v(TAG, "onCreate() profile table created");
        db.execSQL(CREATE_ILLNESSES_TABLE);
        Log.v(TAG, "onCreate() Illnesses table created");
        db.execSQL(CREATE_INSTRUCTIONS_TABLE);
        Log.v(TAG, "onCreate() Instructions table created");
        db.execSQL(CREATE_APPOINTMENTS_TABLE);
        Log.v(TAG, "onCreate() Appointments table created");
        db.execSQL(CREATE_MOODS_TABLE);
        Log.v(TAG, "onCreate() Moods table created");
        db.execSQL(CREATE_LISTS_TABLE);
        Log.v(TAG, "onCreate() Lists table created");
        db.execSQL(CREATE_LIST_ITEM_TABLE);
        Log.v(TAG, "onCreate() List Items table created");
        db.execSQL(CREATE_MEDICINES_TIME_TABLE);
        Log.v(TAG, "onCreate() medicine time table created");
        db.execSQL(CREATE_MEDICINES_DAYS_TABLE);
        Log.v(TAG, "onCreate() medicine days table created");
        db.execSQL(CREATE_SETTINGS_TABLE);
        Log.v(TAG, "onCreate() settings table created");

        //Create new tables
        db.execSQL(CREATE_TEMP_TABLE);
        Log.v(TAG, "onCreate() Temprature table created");

        db.execSQL(CREATE_OXYGEN_LEVEL_TABLE);
        Log.v(TAG, "onCreate() OxygenLevels table created");

        db.execSQL(CREATE_FLUID_INTAKE_TABLE);
        Log.v(TAG, "onCreate() FluidIntakes table created");

        ContentValues initialValues1 = new ContentValues();
        initialValues1.put(S_TITLE, "Appointments_Reminders");
        initialValues1.put(S_VALUE, "ON");
        db.insert(TABLE_SETTINGS, null, initialValues1);

        initialValues1 = new ContentValues();
        initialValues1.put(S_TITLE, "Medicines_Reminders");
        initialValues1.put(S_VALUE, "ON");
        db.insert(TABLE_SETTINGS, null, initialValues1);

        initialValues1 = new ContentValues();
        initialValues1.put(L_LIST_TITLE, this.context.getResources().getString(R.string.action_list1));
        db.insert(TABLE_LISTS, null, initialValues1);

        ContentValues initialValues2 = new ContentValues();
        initialValues2.put(L_LIST_TITLE, this.context.getResources().getString(R.string.action_list2));
        db.insert(TABLE_LISTS, null, initialValues2);

        ContentValues initialValues3 = new ContentValues();
        initialValues3.put(L_LIST_TITLE, this.context.getResources().getString(R.string.action_list3));
        db.insert(TABLE_LISTS, null, initialValues3);

        int id=0;
        Cursor mCursor =
                db.query(true, TABLE_LISTS, new String[] {KEY_ROWID}, L_LIST_TITLE + "='" + this.context.getResources().getString(R.string.action_list1) + "'", null,
                        null, null, null, null);
        if (mCursor != null && (mCursor.getCount() > 0)) {
            mCursor.moveToFirst();
            id = mCursor.getInt(mCursor.getColumnIndex(KEY_ROWID));
        }

        ContentValues initialValues5 = new ContentValues();
        initialValues5.put(L_LIST_ITEM, "Reports");
        initialValues5.put(L_LIST_ID, id);
        db.insert(TABLE_LIST_ITEMS, null, initialValues5);

        initialValues5 = new ContentValues();
        initialValues5.put(L_LIST_ITEM, "Test results");
        initialValues5.put(L_LIST_ID, id);
        db.insert(TABLE_LIST_ITEMS, null, initialValues5);

        initialValues5 = new ContentValues();
        initialValues5.put(L_LIST_ITEM, "Medicine List");
        initialValues5.put(L_LIST_ID, id);
        db.insert(TABLE_LIST_ITEMS, null, initialValues5);

        id=0;
        mCursor =
                db.query(true, TABLE_LISTS, new String[] {KEY_ROWID}, L_LIST_TITLE + "='" + this.context.getResources().getString(R.string.action_list2) + "'", null,
                        null, null, null, null);
        if (mCursor != null && (mCursor.getCount() > 0)) {
            mCursor.moveToFirst();
            id = mCursor.getInt(mCursor.getColumnIndex(KEY_ROWID));
        }

        initialValues5 = new ContentValues();
        initialValues5.put(L_LIST_ITEM, "Book or magazines");
        initialValues5.put(L_LIST_ID, id);
        db.insert(TABLE_LIST_ITEMS, null, initialValues5);

        initialValues5 = new ContentValues();
        initialValues5.put(L_LIST_ITEM, "Games, crosswords, puzzles");
        initialValues5.put(L_LIST_ID, id);
        db.insert(TABLE_LIST_ITEMS, null, initialValues5);

        initialValues5 = new ContentValues();
        initialValues5.put(L_LIST_ITEM, "Comfy clothes");
        initialValues5.put(L_LIST_ID, id);
        db.insert(TABLE_LIST_ITEMS, null, initialValues5);

        initialValues5 = new ContentValues();
        initialValues5.put(L_LIST_ITEM, "Laptop or tablets");
        initialValues5.put(L_LIST_ID, id);
        db.insert(TABLE_LIST_ITEMS, null, initialValues5);

        initialValues5 = new ContentValues();
        initialValues5.put(L_LIST_ITEM, "Friend");
        initialValues5.put(L_LIST_ID, id);
        db.insert(TABLE_LIST_ITEMS, null, initialValues5);

        initialValues5 = new ContentValues();
        initialValues5.put(L_LIST_ITEM, "Movies");
        initialValues5.put(L_LIST_ID, id);
        db.insert(TABLE_LIST_ITEMS, null, initialValues5);
    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG," inside onUpgrade () old version = "+ oldVersion);

        if (oldVersion < 27) {
            db.execSQL(PROFILE_TABLE_ALTER_1);
            db.execSQL(PROFILE_TABLE_ALTER_2);
            db.execSQL(PROFILE_TABLE_ALTER_3);
            db.execSQL(PROFILE_TABLE_ALTER_4);
            Log.v(TAG, "onUpgrade Profile table altered");

            //replace all NULL values with empty string
            db.execSQL(PROFILE_TABLE_ALTER_5);
            db.execSQL(PROFILE_TABLE_ALTER_6);
            Log.v(TAG, "onUpgrade profile table all null values replaced");

            //Create new tables
            db.execSQL(CREATE_TEMP_TABLE);
            Log.v(TAG, "onCreate() Temprature table created");

            db.execSQL(CREATE_OXYGEN_LEVEL_TABLE);
            Log.v(TAG, "onCreate() OxygenLevels table created");

            db.execSQL(CREATE_FLUID_INTAKE_TABLE);
            Log.v(TAG, "onCreate() FluidIntakes table created");
        }

    }
}

