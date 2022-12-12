package com.example.inventarioapp.Interface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.inventarioapp.Models.DatosDownload;
import com.example.inventarioapp.Models.DatosSync;
import com.example.inventarioapp.Models.DatosUpload;
import com.example.inventarioapp.Models.ProcedureUpload;
import java.util.ArrayList;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public interface SyncDatos {

   static int SinronzarDatos(int pos, Context context){
       final int[] con = {0};
       InterfaceApi api;
        api  = GetApiService.ApiService(GetSharedPreferences.getSharedData(context).get(2));
        ArrayList<DatosUpload> main = new ArrayList<>();
        DatosUpload upload = new DatosUpload();
        DatosSync datos = new DatosSync();
        ProcedureUpload proce = new ProcedureUpload();
        proce.setDpto("WS_PARAMETROS_INVENTARIOS");
        datos.setOpcion("1");
        datos.setE1("");
        datos.setE2("");
        datos.setE3("");
        upload.setProcedure(proce);
        upload.setValores(datos);
        upload.setToken(GetSharedPreferences.getSharedData(context).get(1));
        main.add(upload);
       // Log.e(TAG, "SinronzarDatos: " + opcion);
        api.getDatosUpload(main).enqueue(new Callback<DatosDownload>() {
            @Override
            public void onResponse(@NonNull Call<DatosDownload> call, @NonNull Response<DatosDownload> response) {
                DatosDownload res = response.body();
                SQLiteDatabase db =AdminDB.Connection(context);

                if (response.isSuccessful()){
                    con[0]++;
                    String s_1, s_2, s_3, s_4, s_5, s_6;
                    ContentValues values = new ContentValues();
                    switch (pos){
                        case 1:
                            db.delete("MATERIALES_API", null, null);
                            for (int i = 0; i < Objects.requireNonNull(res).getResultado1().size(); i++) {
                                s_1 = res.getResultado1().get(i).getCampo0();
                                s_2 = res.getResultado1().get(i).getCampo1();
                                s_3 = res.getResultado1().get(i).getCampo2();
                                s_4 = res.getResultado1().get(i).getCampo3();
                                s_5 = res.getResultado1().get(i).getCampo4();
                                values.put("CODIGO",s_1);
                                values.put("GRUPO",s_2);
                                values.put("NOMBRE",s_3);
                                values.put("NOM_GRUPO",s_4);
                                values.put("SERIADO",s_5);
                                db.insert("MATERIALES_API", null, values);
                            }

                            break;

                    }
                }

                db.close();
            }

            @Override
            public void onFailure(@NonNull Call<DatosDownload> call, @NonNull Throwable t) {

            }

        });

        return con[0];
    }
}
