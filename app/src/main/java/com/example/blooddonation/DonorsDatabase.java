package com.example.blooddonation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DonorsDatabase extends SQLiteOpenHelper {
    SQLiteDatabase sqLiteDatabase;
    public DonorsDatabase( Context context) {
        super(context, "donors.db", null, 1);
        sqLiteDatabase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table donors (Name text, Address text, DOB text, BloodGroup text, DonationDate text, MobileNo text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void saveData(String name, String address, String dob, String bloodGroup, String donationDate, String mobileNo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",name);
        contentValues.put("Address",address);
        contentValues.put("DOB",dob);
        contentValues.put("BloodGroup",bloodGroup);
        contentValues.put("DonationDate",donationDate);
        contentValues.put("MobileNo",mobileNo);
        sqLiteDatabase.insert("donors",null,contentValues);
    }
    public boolean isPresent(String phoneNo){
        //checking if phone no is already in Database
        Cursor c;
        c = sqLiteDatabase.query("donors",null,"MobileNo=?",new String[] {phoneNo},null,
                null,null);
        if(c.getCount()<1)
            return false;
        else
            return true;
    }

    public String getBloodGroup(String toString) {
        Cursor c;
        c = sqLiteDatabase.query("donors",null,"MobileNo=?",new String[] {toString},null,
                null,null);
        c.moveToFirst();
        return c.getString(3);
    }
}
