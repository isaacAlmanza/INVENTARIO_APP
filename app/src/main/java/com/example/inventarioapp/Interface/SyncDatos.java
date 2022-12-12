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

   static void SinronzarDatos(int pos, Context context){
       InterfaceApi api;
        api  = GetApiService.ApiService(GetSharedPreferences.getSharedData(context).get(2));
        ArrayList<DatosUpload> main = new ArrayList<>();
        DatosUpload upload = new DatosUpload();
        DatosSync datos = new DatosSync();
        ProcedureUpload proce = new ProcedureUpload();
        proce.setDpto("WS_PARAMETROS_INVENTARIOS");
        datos.setOpcion(String.valueOf(pos));
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
                    String s_1, s_2, s_3, s_4, s_5, s_6;
                    ContentValues values = new ContentValues();
                    Cursor cursor;
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

                        case 2:
                            db.delete("INVENTARIOS", null, null);
                            for (int i = 0; i < Objects.requireNonNull(res).getResultado1().size(); i++) {
                                s_1 = res.getResultado1().get(i).getCampo0();
                                s_2 = res.getResultado1().get(i).getCampo1();
                                s_3 = res.getResultado1().get(i).getCampo2();
                                values.put("CODIGO",s_1);
                                values.put("USER",s_2);
                                values.put("NOMBRE",s_3);

                                db.insert("INVENTARIOS", null, values);
                            }
                            break;
/*
                        case 11:
                            db.delete("ENCUESTA_DESCRIPCION_LISTA", null, null);
                            for (int i = 0; i < Objects.requireNonNull(res).getResultado1().size() ; i++) {
                                s_1 = res.getResultado1().get(i).getCampo0();
                                s_2 = res.getResultado1().get(i).getCampo1();
                                s_3 = res.getResultado1().get(i).getCampo2();
                                values.put("S_1",s_1);
                                values.put("S_2",s_2);
                                values.put("S_3",s_3);
                                db.insert("ENCUESTA_DESCRIPCION_LISTA", null, values);
                            }
                            break;

                        case 13:
                            db.delete("CUADRILAS", null, null);
                            for (int i = 0; i < Objects.requireNonNull(res).getResultado1().size(); i++) {
                                s_1 = res.getResultado1().get(i).getCampo0();
                                s_2 = res.getResultado1().get(i).getCampo1();
                                s_3 = res.getResultado1().get(i).getCampo2();
                                s_4 = res.getResultado1().get(i).getCampo3();
                                s_5 = res.getResultado1().get(i).getCampo4();
                                values.put("S_1",s_1);
                                values.put("S_2",s_2);
                                values.put("S_3",s_3);
                                values.put("S_4",s_4);
                                db.insert("CUADRILAS", null, values);

                            }
                            break;

                        case 14:
                            db.delete("TIPOS_ENCUESTAS", null, null);
                            for (int i = 0; i < Objects.requireNonNull(res).getResultado1().size(); i++) {
                                s_1 = res.getResultado1().get(i).getCampo0();
                                s_2 = res.getResultado1().get(i).getCampo1();
                                values.put("S_1",s_1);
                                values.put("S_2",s_2);
                                db.insert("TIPOS_ENCUESTAS", null, values);

                            }

                            break;
                        case 15:
                            db.delete("PROYECTOS", null, null);
                            for (int i = 0; i < Objects.requireNonNull(res).getResultado1().size(); i++) {
                                s_1 = res.getResultado1().get(i).getCampo0();
                                s_2 = res.getResultado1().get(i).getCampo1();
                                s_3 = res.getResultado1().get(i).getCampo2();
                                s_4 = res.getResultado1().get(i).getCampo3();
                                values.put("S_1",s_1);
                                values.put("S_2",s_2);
                                values.put("S_3",s_3);
                                values.put("S_4",s_4);
                                db.insert("PROYECTOS", null, values);
                            }

                            break;
                        case 16:
                            db.delete("RECORRIDOS", null, null);

                            for (int i = 0; i < Objects.requireNonNull(res).getResultado1().size(); i++) {
                                s_1 = res.getResultado1().get(i).getCampo0();
                                s_2 = res.getResultado1().get(i).getCampo1();
                                s_3 = res.getResultado1().get(i).getCampo2();
                                s_4 = res.getResultado1().get(i).getCampo3();
                                values.put("S_1",s_1);
                                values.put("S_2",s_2);
                                values.put("S_3",s_3);
                                values.put("S_4",s_4);
                                db.insert("RECORRIDOS", null, values);
                            }
                            break;

                        case 17:
                            db.delete("TIPO_RESPUESTAS", null, null);

                            for (int i = 0; i < Objects.requireNonNull(res).getResultado1().size(); i++) {
                                s_1 = res.getResultado1().get(i).getCampo0();
                                s_2 = res.getResultado1().get(i).getCampo1();
                                s_3 = res.getResultado1().get(i).getCampo2();
                                values.put("S_1",s_1);
                                values.put("S_2",s_2);
                                values.put("S_3",s_3);
                                db.insert("TIPO_RESPUESTAS", null, values);
                            }

                            break;
                        case 18:
                            db.delete("ENCABEZADO", null, null);

                            for (int i = 0; i < Objects.requireNonNull(res).getResultado1().size(); i++) {
                                s_1 = res.getResultado1().get(i).getCampo0();
                                s_2 = res.getResultado1().get(i).getCampo1();
                                s_3 = res.getResultado1().get(i).getCampo2();
                                s_4 = res.getResultado1().get(i).getCampo3();
                                s_5 = res.getResultado1().get(i).getCampo4();
                                s_6 = res.getResultado1().get(i).getCampo5();
                                values.put("S_1",s_1);
                                values.put("S_2",s_2);
                                values.put("S_3",s_3);
                                values.put("S_4",s_4);
                                values.put("S_5",s_5);
                                values.put("S_6",s_6);
                                values.put("CREADO", "N");
                                db.insert("ENCABEZADO", null, values);
                            }

                            break;
                        case 19:
                            db.delete("ENCABEZADO_DETALLE", null, null);
                            for (int i = 0; i < Objects.requireNonNull(res).getResultado1().size(); i++) {
                                s_1 = res.getResultado1().get(i).getCampo0();
                                s_2 = res.getResultado1().get(i).getCampo1();
                                s_3 = res.getResultado1().get(i).getCampo2();
                                s_4 = res.getResultado1().get(i).getCampo3();
                                s_5 = res.getResultado1().get(i).getCampo4();
                                values.put("S_1",s_1);
                                values.put("S_2",s_2);
                                values.put("S_3",s_3);
                                values.put("S_4",s_4);
                                values.put("S_5",s_5);
                                db.insert("ENCABEZADO_DETALLE", null, values);
                            }

                            break;
                        case 20:
                            db.delete("INFO_FIRMAS", null, null);
                            for (int i = 0; i < Objects.requireNonNull(res).getResultado1().size(); i++) {
                                s_1 = res.getResultado1().get(i).getCampo0();
                                s_2 = res.getResultado1().get(i).getCampo1();
                                s_3 = res.getResultado1().get(i).getCampo2();
                                values.put("S_1",s_1);
                                values.put("S_2",s_2);
                                values.put("S_3",s_3);
                                db.insert("INFO_FIRMAS", null, values);
                            }

                            break;
                            */
                    }
                }

                db.close();
            }

            @Override
            public void onFailure(@NonNull Call<DatosDownload> call, @NonNull Throwable t) {

            }

        });


    }
}
