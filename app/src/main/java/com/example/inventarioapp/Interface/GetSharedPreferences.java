package com.example.inventarioapp.Interface;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.inventarioapp.R;

import java.util.ArrayList;

public interface GetSharedPreferences {
    static ArrayList<String> getSharedData(Context context){
        SharedPreferences preferences =context.getSharedPreferences(String.valueOf(R.string.token),context.MODE_PRIVATE);
        String token =  preferences.getString(String.valueOf(R.string.token),"");
        String user =   preferences.getString(String.valueOf("user"),"");
        String url = preferences.getString("url", "");
        String imei = preferences.getString("imei", "");

        ArrayList<String> list =  new ArrayList<>();
        list.add(0, user);
        list.add(1,token);
        list.add(2, url);
        list.add(3, imei);
        return  list;
    }
    static ArrayList<String> getSharedEncuesta(Context context){
        SharedPreferences preferences =context.getSharedPreferences("ENCUESTA",context.MODE_PRIVATE);
        String idOrden = preferences.getString("idOrden", "");
        String fecha = preferences.getString("fecha_eje", "");
        String hora = preferences.getString("hora_eje", "");
        String categoria = preferences.getString("categoria", "");
        String titulo = preferences.getString("titulo", "");
        ArrayList<String> list =  new ArrayList<>();
        list.add(0, idOrden);
        list.add(1, fecha);
        list.add(2, hora);
        list.add(3, categoria);
        list.add(4, titulo);
        return  list;
    }
}
