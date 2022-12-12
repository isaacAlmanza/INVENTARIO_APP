package com.example.inventarioapp.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminDataBase extends SQLiteOpenHelper {
    final  String materiales_api = "CREATE TABLE MATERIALES_API(CODIGO VARCHAR(100), GRUPO VARCHAR(200), NOMBRE VARCHAR(200)," +
            " NOM_GRUPO VARCHAR(200), FECHA VARCHAR(200) ,HORA VARCHAR(200) , FUNCIONARIO VARCHAR(200), CONTADOR VARCHAR(200), SERIADO VARCHAR(10))";

    final  String materiales_s = "CREATE TABLE MATERIALES_S(CODIGO VARCHAR(100), GRUPO VARCHAR(200), NOMBRE VARCHAR(200)," +
            "PREFIJO VARCHAR(200), VALOR_INI VARCHAR(10), SUFIJO VARCHAR(10), CANTIDAD VARCHAR(10),ESTADO  VARCHAR(10))";

    final  String materiales = "CREATE TABLE MATERIALES(CODIGO VARCHAR(100), NOMBRE VARCHAR(200), CANTIDAD VARCHAR(10)," +
            "ESTADO  VARCHAR(10), FECHA VARCHAR(200) ,HORA VARCHAR(200) , FUNCIONARIO VARCHAR(200), CONTADOR VARCHAR(200))";

    final  String inventarios = "CREATE TABLE INVENTARIOS(CODIGO VARCHAR(100), USER VARCHAR(200), NOMBRE VARCHAR(10))";

    public AdminDataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(materiales_api);
        db.execSQL(materiales_s);
        db.execSQL(materiales);
        db.execSQL(inventarios);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
