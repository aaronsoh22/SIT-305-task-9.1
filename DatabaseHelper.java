package com.example.lostandfoundapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "LostanndFound.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Lost(Name TEXT ,Phone TEXT, Description TEXT, Date TEXT,Location TEXT)");
        DB.execSQL("create Table Found(Name TEXT ,Phone TEXT, Description TEXT, Date TEXT,Location TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("drop Table if exists Lost ");
        DB.execSQL("drop Table if exists Found ");
    }


    public Boolean savedatalost(String Name,String Phone, String Description, String Date,String Location)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", Name);
        contentValues.put("Phone", Phone);
        contentValues.put("Description", Description);
        contentValues.put("Date", Date);
        contentValues.put("Location", Location);
        long result=DB.insert("Lost", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public Boolean savedatafound(String Name,String Phone, String Description, String Date,String Location)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", Name);
        contentValues.put("Phone", Phone);
        contentValues.put("Description", Description);
        contentValues.put("Date", Date);
        contentValues.put("Location", Location);
        long result=DB.insert("Found", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }


    public boolean deletelost(String Name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        long result = DB.delete("Lost", "Name=?", new String[]{Name});
        return result != -1;
    }

    public boolean deletefound(String Name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        long result = DB.delete("Found", "Name=?", new String[]{Name});
        return result != -1;
    }
    public Cursor getMatchedData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Found.Name AS FoundName, Found.Location AS FoundLocation, Found.Date AS FoundDate " +
                "FROM Found INNER JOIN Lost ON Lost.Name = Found.Name AND Lost.Location = Found.Location " +
                "AND Lost.Date = Found.Date";
        return db.rawQuery(query, null);
    }
    public void deleteMatchingEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("DELETE FROM Lost WHERE EXISTS " +
                    "(SELECT 1 FROM Found WHERE Lost.Name = Found.Name " +
                    "AND Lost.Location = Found.Location " +
                    "AND Lost.Date = Found.Date)");

            db.execSQL("DELETE FROM Found WHERE EXISTS " +
                    "(SELECT 1 FROM Lost WHERE Lost.Name = Found.Name " +
                    "AND Lost.Location = Found.Location " +
                    "AND Lost.Date = Found.Date)");

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }







    public Cursor getdatalost ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("Select * from Lost", null);
    }

    public Cursor getdatafound ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("Select * from Found", null);
    }
}
