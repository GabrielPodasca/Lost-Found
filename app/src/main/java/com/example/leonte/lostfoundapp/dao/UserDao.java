package com.example.leonte.lostfoundapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.leonte.lostfoundapp.manager.DatabaseManager;
import com.example.leonte.lostfoundapp.model.User;
import com.example.leonte.lostfoundapp.manager.DatabaseManager;
import com.example.leonte.lostfoundapp.model.User;

public class UserDao{
    private static final String TABLE_NAME = "users";
    private static final String[] ALL_COLUMNS = {"id","username","password","phone_number"};
    private DatabaseManager databaseManager;

    public UserDao(Context context) {
        databaseManager = new DatabaseManager(context);
    }

    public boolean addUser(User user){
        SQLiteDatabase database = null;
        try{
            ContentValues record = new ContentValues();
            record.put("username",user.getUsername());
            record.put("password",user.getPassword());
            record.put("phone_number",user.getPhoneNumber());
            database = databaseManager.open();
            database.insert(TABLE_NAME,null,record);
            return true;
        }catch(Exception e){
            return false;
        }finally{
            if(database != null){
                databaseManager.close(database);
            }
        }
    }

    public User findUserByUsername(String username){
        SQLiteDatabase database = null;
        User user = null;
        try{
            database = databaseManager.open();
            String where = "username = '"+username+"'";
            Cursor c = database.query(TABLE_NAME,ALL_COLUMNS,where,null,null,null,null);
            c.moveToFirst();
            if(!c.isAfterLast()){
                user = new User();
                user.setId(c.getInt(0));
                user.setUsername(c.getString(1));
                user.setPassword(c.getString(2));
                user.setPhoneNumber(c.getString(3));
            }
        }finally{
            if(database != null){
                databaseManager.close(database);
            }
        }
        return user;
    }

    public String getPhoneNumberByUsername(String username){
        SQLiteDatabase database = null;
        String phoneNumber = "";
        try{
            database = databaseManager.open();
            String where = "username = '"+username+"'";
            Cursor c = database.query(TABLE_NAME,ALL_COLUMNS,where,null,null,null,null);
            c.moveToFirst();
            if(!c.isAfterLast()){
                phoneNumber = c.getString(3);
            }
        }finally{
            if(database != null){
                databaseManager.close(database);
            }
        }
        return phoneNumber;
    }
}
