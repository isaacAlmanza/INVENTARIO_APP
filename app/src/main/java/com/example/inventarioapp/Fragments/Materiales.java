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


public class Materiales extends Fragment {

    TextView tv_descrpcion;
    EditText et_codigo, et_cantidad;
    Button btn_agregar, btn_search;
    ConstraintLayout layout;
    String codigo, descripcion, cantidad;

  /*  private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

   */

    public Materiales() {
        // Required empty public constructor
    }

    public static Materiales newInstance(String param1, String param2) {
        Materiales fragment = new Materiales();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
  //      args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  /*      if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

   */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_materiales, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views(view);
        btn_agregar.setOnClickListener(v -> {
           if (ValidarDatos()){
               GuardarDatos();
           }
        });

        btn_search.setOnClickListener( v -> {
            Search();
        });

    }
    void Views(View view){
        btn_search = view.findViewById(R.id.search);
        et_codigo = view.findViewById(R.id.codigoMaterial);
        et_cantidad =view.findViewById(R.id.cantidad);
        btn_agregar = view.findViewById(R.id.agregar);
        //tv_descrpcion = view.findViewById(R.id.descripcion_material);
        layout =view.findViewById(R.id.layout_materiales);

    }

    void getTexts(){
        codigo =et_codigo.getText().toString().trim();
        descripcion  = tv_descrpcion.getText().toString().trim();
        cantidad = et_cantidad.getText().toString().trim();
    }

    void GuardarDatos(){
      try {
          getTexts();
          SQLiteDatabase db = AdminDB.Connection(getActivity());
          ContentValues values = new ContentValues();
          values.put("CODIGO", codigo );
          values.put("NOMBRE", descripcion);
          values.put("CANTIDAD", cantidad);
          values.put("ESTADO", "N");
          Cursor cursor = db.rawQuery("SELECT * FROM MATERIALES WHERE CODIGO LIKE '"+codigo+"'", null);
          if (cursor.getCount()==0){
              db.insert("MATERIALES" ,null, values);
              Log.e("TAG", "GuardarDatos: Guarda" );
              Clean();
          }else {
              db.update("MATERIALES",values, "CODIGO =?", new String[]{codigo});
              Log.e("TAG", "GuardarDatos: Actualiza" );
              Clean();
          }

      }catch (Exception e){
          Log.e("TAG", "GuardarDatos: " + e.fillInStackTrace() );
      }
    }

    boolean ValidarDatos(){
        if (!codigo.isEmpty()|| !descripcion.isEmpty() || !cantidad.isEmpty() ){
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
                Cursor cursor = db.rawQuery("SELECT * FROM MATERIALES_API WHERE CODIGO LIKE '"+codigo+"' and SERIADO LIKE 'N'", null);
                if (cursor.getCount()>0){
                    while (cursor.moveToNext()){
                        tv_descrpcion.setText(cursor.getString(2));
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
    void Clean(){
        et_codigo.setText("");
        et_cantidad.setText("");
        tv_descrpcion.setText("");
    }
}