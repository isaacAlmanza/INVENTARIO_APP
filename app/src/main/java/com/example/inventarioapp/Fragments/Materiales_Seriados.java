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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventarioapp.Interface.AdminDB;
import com.example.inventarioapp.Interface.GetApiService;
import com.example.inventarioapp.Interface.GetSharedPreferences;
import com.example.inventarioapp.Interface.InterfaceApi;
import com.example.inventarioapp.Interface.SendData;
import com.example.inventarioapp.Interface.ViewSnackBar;
import com.example.inventarioapp.Models.DatosDownload;
import com.example.inventarioapp.Models.ModelSaveEncuesta;
import com.example.inventarioapp.Models.ProcedureUpload;
import com.example.inventarioapp.Models.SaveDatosUpload;
import com.example.inventarioapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import okio.Utf8;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Materiales_Seriados extends Fragment {

    FrameLayout layout;
    EditText et_funcionario, et_cantidad, et_prefijo, et_valor_ini, et_sufijo, et_formato, et_caracter, et_observacion ;
    Button btn_agregar;
    TextView tv_grupo;
    String codigo,  cantidad, prefijo, valor_ini, sufijo, formato, caracter, funcionario, observacion;

    public Materiales_Seriados() {
        // Required empty public constructor
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

    }


    // RELACIONAMOS LAS VISTAS CON LOS FRAGMENTS
    void Views(View view){
        et_observacion = view.findViewById(R.id.observacion_s);
        tv_grupo= view.findViewById(R.id.grupo_material);
        et_cantidad =view.findViewById(R.id.cantidad_mat_s);
        et_formato = view.findViewById(R.id.formato);
        et_funcionario = view.findViewById(R.id.funcionario_s);
        et_caracter =view.findViewById(R.id.caracter);
        btn_agregar = view.findViewById(R.id.agregar_material_s);
        et_prefijo =view.findViewById(R.id.prefijo);
        layout =getActivity().findViewById(R.id.layout_main);
        et_valor_ini = view.findViewById(R.id.valorInicial);
        et_sufijo =view.findViewById(R.id.sufijo);
    }

    void getTexts(){
        observacion = et_observacion.getText().toString().trim().replace("'", "''");
        cantidad = et_cantidad.getText().toString().trim();
        funcionario = et_funcionario.getText().toString().trim();
        prefijo =et_prefijo.getText().toString().trim();
        valor_ini = et_valor_ini.getText().toString().trim();
        sufijo =et_sufijo.getText().toString().trim();
        formato = et_formato.getText().toString().trim();
        caracter =et_caracter.getText().toString().trim();

    }

    void GuardarDatos(){
        try {
             getTexts();
            TextView textView = getActivity().findViewById(R.id.search_tv);
            codigo =textView.getText().toString().trim().split("-")[0];
            final InterfaceApi[] api = new InterfaceApi[1];
            ArrayList<SaveDatosUpload> main = new ArrayList<>();
            SaveDatosUpload upload = new SaveDatosUpload();
            ModelSaveEncuesta encuesta  = new ModelSaveEncuesta();
            ProcedureUpload proce = new ProcedureUpload();
            //##################################
            proce.setDpto("WS_LEGA_CONTEO_INVENTARIO");
            //=================================
            encuesta.setE_1("0");
            encuesta.setE_2(codigo);
            encuesta.setE_3(formato);
            encuesta.setE_4(caracter);
            encuesta.setE_5(prefijo);
            encuesta.setE_6(valor_ini);
            encuesta.setE_7(sufijo);
            encuesta.setE_8(cantidad);
            encuesta.setE_9(funcionario);
            encuesta.setE_10(GetSharedPreferences.getSharedData(getActivity()).get(0));
            encuesta.setE_11(observacion);
            //##################################
            upload.setProcedure(proce);
            upload.setToken(GetSharedPreferences.getSharedData(getActivity()).get(1));
            upload.setValores(encuesta);
            //##################################
            main.add(upload);
            //##################################
            api[0] = GetApiService.ApiService(GetSharedPreferences.getSharedData(getActivity()).get(2));
            api[0].getSave(main).enqueue(new Callback<DatosDownload>() {
                @Override
                public void onResponse(Call<DatosDownload> call, Response<DatosDownload> response) {
                    try {
                        DatosDownload res =  response.body();
                        if (res.getResultado1()!=null) {
                            if (response.isSuccessful()) {
                                if (res.getResultado1().get(0).getCampo0().equalsIgnoreCase("1")) {
                                    Snackbar.make(layout, res.getResultado1().get(0).getCampo1(), Snackbar.LENGTH_LONG).setBackgroundTint(getActivity().getColor(R.color.success)).show();
                                    getFragmentManager().beginTransaction().replace(R.id.layout_materiales, new Materiales());
                                    Clean();
                                } else {
                                    ViewSnackBar.SnackBarDanger(layout, res.getResultado1().get(0).getCampo1(), getActivity());
                                }
                            }
                        }else {
                            ViewSnackBar.SnackBarDanger(layout,"Error al enviar los datos", getActivity());
                        }
                    }catch (Exception e){
                        Log.e("TAG", "onResponse: " + e.fillInStackTrace() );
                    }
                }

                @Override
                public void onFailure(Call<DatosDownload> call, Throwable t) {
                    ViewSnackBar.SnackBarDanger(layout,t.getMessage(), getActivity());
                }
            });
        }catch (Exception e){
            Log.e("TAG", "GuardarDatos: " + e.fillInStackTrace() );
        }
    }

    boolean ValidarDatos(){
        getTexts();
        TextView textView = getActivity().findViewById(R.id.search_tv);
        codigo =textView.getText().toString().trim().split("-")[0];
        return !codigo.isEmpty() && !cantidad.isEmpty() && !prefijo.isEmpty() && !valor_ini.isEmpty() || !sufijo.isEmpty() &&  !formato.isEmpty() && !caracter.isEmpty() &&
                !funcionario.isEmpty();
    }

    void Clean(){
        et_cantidad.setText("");
        et_funcionario.setText("");
        et_caracter.setText("");
        et_formato.setText("");
        et_sufijo.setText("");
        et_prefijo.setText("");
        et_funcionario.setTag("");
        et_valor_ini.setText("");
        tv_grupo.setText("");
        et_observacion.setText("");
    }



}