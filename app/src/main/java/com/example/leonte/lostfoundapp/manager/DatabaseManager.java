package com.example.leonte.lostfoundapp.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "lost_or_found.db";
    private static final String CREATE_USERS_TABLE = ""+
            "CREATE TABLE users(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "username TEXT NOT NULL," +
            "password TEXT NOT NULL," +
            "phone_number TEXT NOT NULL)";

    public DatabaseManager(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }

    public SQLiteDatabase open(){
        return getWritableDatabase();
    }

    public void close(SQLiteDatabase db){
        db.close();
    }
}