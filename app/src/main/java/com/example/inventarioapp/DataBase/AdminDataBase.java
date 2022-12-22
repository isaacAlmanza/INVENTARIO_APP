package com.example.inventarioapp.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminDataBase extends SQLiteOpenHelper {
    final  String materiales_api = "CREATE TABLE MATERIALES_API(CODIGO VARCHAR(100), GRUPO VARCHAR(200), NOMBRE VARCHAR(200)," +
            " NOM_GRUPO VARCHAR(200), SERIADO VARCHAR(10))";

    final  String cuadrillas = "CREATE TABLE CUADRILLAS(CODIGO VARCHAR(100), NOMBRE VARCHAR(200), CUADRILLA  VARCHAR(100))";

   

    public AdminDataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(materiales_api);
        db.execSQL(cuadrillas);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
