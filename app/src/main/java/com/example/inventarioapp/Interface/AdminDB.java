package com.example.inventarioapp.Interface;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.inventarioapp.DataBase.AdminDataBase;

public interface AdminDB {

    static SQLiteDatabase Connection(Context context){
        AdminDataBase adminDB = new AdminDataBase(context, GetSharedPreferences.getSharedData(context).get(0), null, 1);
        SQLiteDatabase db = adminDB.getWritableDatabase();
        return db;
    }
}
