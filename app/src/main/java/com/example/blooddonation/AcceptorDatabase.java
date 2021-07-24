package com.example.blooddonation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AcceptorDatabase extends SQLiteOpenHelper {
    SQLiteDatabase sqLiteDatabase;
    ArrayList arrayList;
    public AcceptorDatabase( Context context) {
        super(context, "acceptor.db", null, 1);
        sqLiteDatabase = getWritableDatabase();
        arrayList = new ArrayList();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table acceptors (BloodGroup text, Name text, MobileNo text, Address text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void saveData(String name, String address, String bloodGroup, String mobileNo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("address",address);
        contentValues.put("bloodGroup",bloodGroup);
        contentValues.put("mobileNo",mobileNo);
        sqLiteDatabase.insert("acceptors",null,contentValues);

    }
    public ArrayList getNameandAddress (String bloodGroup){
        Cursor c;
        c = sqLiteDatabase.query("acceptors",null,"BloodGroup=?",new String[]{bloodGroup},
                null,null,null);
        c.moveToFirst();
        if(c.getCount()<1)
            return null;
        else{
            for(int i=0; i<c.getCount();i++) {
                arrayList.add("Name = "+c.getString(c.getColumnIndex("Name"))+"\n"+"Address = "+
                        c.getString(c.getColumnIndex("Address")));
                c.moveToNext();
            }
        }
        return arrayList;

    }
    public ArrayList getAllDetails(String bloodGroup, int row){
        Cursor c;
        c = sqLiteDatabase.query("acceptors",null,"BloodGroup=?",new String[]{bloodGroup},
                null,null,null);
        c.moveToFirst();
        for(int count =1; count<=row; count++)
            c.moveToNext();
        for(int i=0; i<=3; i++)
        arrayList.add(c.getString(i));

        return arrayList;
    }

}
