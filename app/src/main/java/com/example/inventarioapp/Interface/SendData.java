package com.example.inventarioapp.Interface;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.inventarioapp.Models.DatosDownload;
import com.example.inventarioapp.Models.ModelSaveEncuesta;
import com.example.inventarioapp.Models.ProcedureUpload;
import com.example.inventarioapp.Models.SaveDatosUpload;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public interface SendData {

    static void SendFormulario(Context context, String dato1, String dato2, String dato4, String dato5, String dato6, String dato7, String dato8,String dato9 ,int pos, String tabla) {


        final InterfaceApi[] api = new InterfaceApi[1];
        ArrayList<SaveDatosUpload> main = new ArrayList<>();
        SaveDatosUpload upload = new SaveDatosUpload();
        ModelSaveEncuesta encuesta  = new ModelSaveEncuesta();
        ProcedureUpload proce = new ProcedureUpload();
        //##################################
        proce.setDpto("WS_LEGA_CONTEO_INVENTARIO");
        //=================================
        encuesta.setE_1("0");
        encuesta.setE_2(dato1);
        encuesta.setE_3(dato2);
        encuesta.setE_4(dato4);
        encuesta.setE_5(dato5);
        encuesta.setE_6(dato6);
        encuesta.setE_7(dato7);
        encuesta.setE_8(dato8);
        encuesta.setE_9(dato9);
        encuesta.setE_10(GetSharedPreferences.getSharedData(context).get(0));
        //##################################
        upload.setProcedure(proce);
        upload.setToken(GetSharedPreferences.getSharedData(context.getApplicationContext()).get(1));
        upload.setValores(encuesta);
        //##################################
        main.add(upload);
        //##################################
        api[0] = GetApiService.ApiService(GetSharedPreferences.getSharedData(context.getApplicationContext()).get(2));
        api[0].getSave(main).enqueue(new Callback<DatosDownload>() {
            @Override
            public void onResponse(Call<DatosDownload> call, Response<DatosDownload> response) {
                try {
                    DatosDownload res =  response.body();
                    if (response.isSuccessful()){
                        if (res.getResultado1().get(0).getCampo0().equalsIgnoreCase("1")){

                        }else {

                        }
                    }
                }catch (Exception e){
                    Log.e("TAG", "onResponse: " + e.fillInStackTrace() );
                }
            }

            @Override
            public void onFailure(Call<DatosDownload> call, Throwable t) {

            }
        });



    }
}
