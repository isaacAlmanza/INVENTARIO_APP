package com.example.inventarioapp.Fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.inventarioapp.Interface.AdminDB;
import com.example.inventarioapp.R;
import com.google.android.material.snackbar.Snackbar;

public class Materiales_Seriados extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   // private static final String ARG_PARAM1 = "param1";
   // private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
   // private String mParam1;
   // private String mParam2;
    TextView tv_descrpcion, tv_grupo;
    ConstraintLayout layout;
    EditText et_codigo, et_cantidad, et_prefijo, et_valor_ini, et_sufijo, et_formato, et_caracter ;
    Button btn_agregar, btn_search;
    String codigo, descripcion, cantidad, grupo, prefijo, valor_ini, sufijo, formato, caracter;

    public Materiales_Seriados() {
        // Required empty public constructor
    }

    public static Materiales_Seriados newInstance(String param1, String param2) {
        Materiales_Seriados fragment = new Materiales_Seriados();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_materiales__seriados, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views(view);
        btn_agregar.setOnClickListener( v -> {
           if (ValidarDatos()){
               GuardarDatos();
           }
        });
        btn_search.setOnClickListener(v -> {
            Search();
        });


    }
    // RELACIONAMOS LAS VISTAS CON LOS FRAGMENTS
    void Views(View view){
        et_codigo = view.findViewById(R.id.codigoMaterial_s);
        et_cantidad =view.findViewById(R.id.cantidad_mat_s);
        et_formato = view.findViewById(R.id.formato);
        et_caracter =view.findViewById(R.id.caracter);
        btn_agregar = view.findViewById(R.id.agregar_material_s);
        btn_search =  view.findViewById(R.id.search_s);
        tv_descrpcion = view.findViewById(R.id.descripcion_material_s);
        et_prefijo =view.findViewById(R.id.prefijo);
        layout =view.findViewById(R.id.layout_materiales);
        et_valor_ini = view.findViewById(R.id.valorInicial);
        et_sufijo =view.findViewById(R.id.sufijo);
    }

    void getTexts(){
        codigo =et_codigo.getText().toString().trim();
        descripcion  = tv_descrpcion.getText().toString().trim();
        cantidad = et_cantidad.getText().toString().trim();
        grupo = tv_grupo.getText().toString().trim();
        prefijo =et_prefijo.getText().toString().trim();
        valor_ini = et_valor_ini.getText().toString().trim();
        sufijo =et_sufijo.getText().toString().trim();
        formato = et_formato.getText().toString().trim();
        caracter =et_caracter.getText().toString().trim();
    }

    void GuardarDatos(){
        try {
        getTexts();
        SQLiteDatabase db = AdminDB.Connection(getActivity());
        ContentValues values = new ContentValues();
        values.put("CODIGO", codigo );
        values.put("NOMBRE", descripcion);
        values.put("GRUPO", grupo);
        values.put("PREFIJO", prefijo);
        values.put("VALOR_INI", valor_ini);
        values.put("SUFIJO", sufijo);
        values.put("CANTIDAD", cantidad);
        values.put("ESTADO", "N");
        Cursor cursor = db.rawQuery("SELECT * FROM MATERIALES_S WHERE CODIGO LIKE '"+codigo+"'", null);
        if (cursor.getCount()==0){
            db.insert("MATERIALES_S" ,null, values);
        }else {
            db.update("MATERIALES_S",values, "CODIGO =?", new String[]{codigo});
        }

        }catch (Exception e){
            Log.e("TAG", "GuardarDatos: " + e.fillInStackTrace() );
        }
    }

    boolean ValidarDatos(){
        if (!codigo.isEmpty()|| !descripcion.isEmpty() || !cantidad.isEmpty() || !prefijo.isEmpty() || !valor_ini.isEmpty() || !sufijo.isEmpty() ){
            return true;
        }else {
            Snackbar.make(layout, "Todos los datos son requeridos", Snackbar.LENGTH_LONG).setBackgroundTint(getActivity().getColor(R.color.danger)).show();
            return false;
        }
    }


    void Search(){
     try {
         SQLiteDatabase db = AdminDB.Connection(getActivity());
         codigo =et_codigo.getText().toString().trim();
      if (!codigo.isEmpty()){
          Cursor cursor = db.rawQuery("SELECT * FROM MATERIALES_API WHERE CODIGO LIKE '"+codigo+"' and SERIADO LIKE 'S'", null);
          if (cursor.getCount()>0){
              while (cursor.moveToNext()){
                  tv_descrpcion.setText(cursor.getString(2));
                  tv_grupo.setText(cursor.getString(1));
              }
          }else {
              Snackbar.make(layout, "Material no encontrado", Snackbar.LENGTH_LONG).setBackgroundTint(getActivity().getColor(R.color.danger)).show();
          }
      }else {
          Snackbar.make(layout, "Ingrese un código primero", Snackbar.LENGTH_LONG).setBackgroundTint(getActivity().getColor(R.color.danger)).show();

      }
     }catch (Exception e){
         Log.e("TAG", "Search: " + e.fillInStackTrace() );
     }
    }



}