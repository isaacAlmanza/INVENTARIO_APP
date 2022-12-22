package com.example.inventarioapp.Fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.inventarioapp.Interface.AdminDB;
import com.example.inventarioapp.Interface.GetApiService;
import com.example.inventarioapp.Interface.GetSharedPreferences;
import com.example.inventarioapp.Interface.InterfaceApi;
import com.example.inventarioapp.Interface.ViewSnackBar;
import com.example.inventarioapp.Models.DatosDownload;
import com.example.inventarioapp.Models.ModelLista;
import com.example.inventarioapp.Models.ModelSaveEncuesta;
import com.example.inventarioapp.Models.ProcedureUpload;
import com.example.inventarioapp.Models.SaveDatosUpload;
import com.example.inventarioapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Materiales extends Fragment {

    EditText  et_cantidad, et_funcionario, et_observacion;
    Button btn_agregar;
    FrameLayout layout;
    String codigo, cantidad, funcionario, observacion;
    RecyclerView recyclerView;
    ArrayList<String> lista;

    public Materiales() {
        // Required empty public constructor
    }

    public static Materiales newInstance(String param1, String param2) {
        Materiales fragment = new Materiales();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_materiales, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views(view);
        lista= new ArrayList<>();
        recyclerView.setLayoutManager( new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        btn_agregar.setOnClickListener(v -> {
           if (ValidarDatos()){
               GuardarDatos();
           }else {
               ViewSnackBar.SnackBarDanger(layout,"Todos los datos son requeridos", getActivity() );
           }
        });

    }

    void Views(View view){
        et_observacion = view.findViewById(R.id.observacion);
        recyclerView = view.findViewById(R.id.recycler);
        et_cantidad =view.findViewById(R.id.cantidad);
        et_funcionario =  view.findViewById(R.id.funcionario);
        btn_agregar = view.findViewById(R.id.agregar);
        layout = getActivity().findViewById(R.id.layout_main);

    }

    void getTexts(){
        observacion = et_observacion.getText().toString().trim().replace("'", "''");
        cantidad = et_cantidad.getText().toString().trim();
        funcionario = et_funcionario.getTag().toString().trim();
    }
    String UTF_8_Converter(String str){

        ByteBuffer bytes = StandardCharsets.UTF_8.encode(str);
        String utf8EncodedString = StandardCharsets.UTF_8.decode(bytes).toString();
        Log.e("TAG", "UTF_8_Converter: " + utf8EncodedString );
        return utf8EncodedString;
    }


    void GuardarDatos() {
     try {
            getTexts();
         Log.e("TAG", "GuardarDatos: entra"  );
            TextView textView = getActivity().findViewById(R.id.search_tv);
            codigo =textView.getText().toString().trim().split("-")[0];
            // SendData.SendFormulario(getActivity(), codigo, formato,caracter,prefijo,valor_ini, sufijo, cantidad,"Tecnico", 0, "");
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
            encuesta.setE_3("0");
            encuesta.setE_4("");
            encuesta.setE_5("");
            encuesta.setE_6("0");
            encuesta.setE_7("");
            encuesta.setE_8(cantidad);
            encuesta.setE_9(funcionario);
            encuesta.setE_10(GetSharedPreferences.getSharedData(getActivity()).get(0));
            encuesta.setE_11(UTF_8_Converter(observacion));
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
                        if (response.isSuccessful()){
                            if (res.getResultado1().get(0).getCampo0().equalsIgnoreCase("1")){
                                ViewSnackBar.SnackBarSuccess(layout, res.getResultado1().get(0).getCampo1(), getActivity());
                                Clean();
                            }else {
                                ViewSnackBar.SnackBarDanger(layout,res.getResultado1().get(0).getCampo1(), getActivity());
                            }
                        }
                    }catch (Exception e){
                        Log.e("TAG", "onResponse: " + e.fillInStackTrace() );
                    }
                }

                @Override
                public void onFailure(Call<DatosDownload> call, Throwable t) {
                    ViewSnackBar.SnackBarDanger(layout, t.getMessage(), getActivity());
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
        Log.e("TAG", "ValidarDatos: " + codigo + "  "+cantidad+ "  " + funcionario );
        return !codigo.isEmpty() && !cantidad.isEmpty() && !funcionario.isEmpty();
    }
    public void MostrarInfo(){
        try {
            getTexts();
            TextView textView = getActivity().findViewById(R.id.search_tv);
            codigo =textView.getText().toString().trim().split("-")[0];
            // SendData.SendFormulario(getActivity(), codigo, formato,caracter,prefijo,valor_ini, sufijo, cantidad,"Tecnico", 0, "");
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
            encuesta.setE_3("");
            encuesta.setE_4("");
            encuesta.setE_5("");
            encuesta.setE_6("");
            encuesta.setE_7("");
            encuesta.setE_8(cantidad);
            encuesta.setE_9("");
            encuesta.setE_10(GetSharedPreferences.getSharedData(getActivity()).get(0));
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
                        if (response.isSuccessful()){
                            if (res.getResultado1().get(0).getCampo0().equalsIgnoreCase("1")){
                             //   Snackbar.make(layout, res.getResultado1().get(0).getCampo1(),Snackbar.LENGTH_LONG).setBackgroundTint(getActivity().getColor(R.color.success)).show();
                                 ViewSnackBar.SnackBarSuccess(layout,res.getResultado1().get(0).getCampo1(), getActivity() );
                            }else {

                            }
                        }
                    }catch (Exception e){
                        Log.e("TAG", "onResponse: " + e.fillInStackTrace() );
                    }
                }

                @Override
                public void onFailure(Call<DatosDownload> call, Throwable t) {
                    ViewSnackBar.SnackBarDanger(layout,t.getMessage(), getActivity() );
                }
            });
        }catch (Exception e){
            Log.e("TAG", "GuardarDatos: " + e.fillInStackTrace() );
        }
    }
    void Clean(){
        et_cantidad.setText("");
        et_funcionario.setText("");
        et_observacion.setText("");
    }
}