package com.example.inventarioapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.inventarioapp.Interface.AdminDB;
import com.example.inventarioapp.Interface.SyncDatos;
import com.example.inventarioapp.Interface.ViewSnackBar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventarioapp.ui.main.SectionsPagerAdapter;
import com.example.inventarioapp.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FrameLayout layout;
    private ActivityMainBinding binding;
    ArrayList<String> lista;
    ArrayList<String> lista_fun;
    TextView textView;
    TabLayout tabs;
    Dialog dialog;
    Toolbar toolbar;
    ViewPager viewPager;
    ProgressDialog progressDialog;
    ArrayAdapter<String> adapter_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        layout = findViewById(R.id.layout_main);
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager= binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = binding.tabs;
        textView = findViewById(R.id.search_tv);
        tabs.setupWithViewPager(viewPager);


        if (ConexionInternet()) {
            new BackGroundMain().execute();
        }
        textView.setOnClickListener(v -> {
            dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.item);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            EditText et_text = dialog.findViewById(R.id.et_text);
            ListView listView = dialog.findViewById(R.id.list_view_item);
            adapter_item = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, lista);
            listView.setAdapter(adapter_item);
            et_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter_item.getFilter().filter(s);
                    Log.e("TAG", "onTextChanged: " + s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listView.setOnItemClickListener((parent, view, position, id) -> {
                textView.setText(adapter_item.getItem(position));
                dialog.dismiss();
                String tag = textView.getText().toString().trim();
                if (!tag.isEmpty()) {
                    if (tag.contains("(S)")) {
                        Toast.makeText(MainActivity.this, tag, Toast.LENGTH_LONG).show();
                        tabs.selectTab(tabs.getTabAt(1));
                        TextView grupo = viewPager.findViewById(R.id.grupo_material);
                        // Log.e("TAG", "onCreate: "+ grupo );

                        SQLiteDatabase db = AdminDB.Connection(MainActivity.this);
                        Cursor cursor = db.rawQuery("SELECT GRUPO, NOM_GRUPO FROM MATERIALES_API WHERE CODIGO LIKE '" + tag.split("-")[0].trim() + "' ", null);
                        if (cursor.getCount() > 0) {
                            cursor.moveToPosition(0);
                            grupo.setText(cursor.getString(1));
                            grupo.setTag(cursor.getString(0));
                        }

                    }
                }
            });
        });

    }

    void AddFuncionario(){
            dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.item);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            EditText et_text = dialog.findViewById(R.id.et_text);
            ListView listView = dialog.findViewById(R.id.list_view_item);
            adapter_item = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, lista_fun);
            listView.setAdapter(adapter_item);
            et_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter_item.getFilter().filter(s);
                    Log.e("TAG", "onTextChanged: " + s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listView.setOnItemClickListener((parent, view, position, id) -> {
                dialog.dismiss();
                String tag = adapter_item.getItem(position);
                String material = textView.getText().toString().trim();
                if (!tag.isEmpty() && material.contains("(S)")) {
                        Toast.makeText(MainActivity.this, tag, Toast.LENGTH_LONG).show();
                    tabs.selectTab(tabs.getTabAt(1));
                    TextView funcioario = viewPager.findViewById(R.id.funcionario_s);
                    funcioario.setText(tag.split("-")[1]);
                    funcioario.setTag(tag.split("-")[0]);
                }else {
                    tabs.selectTab(tabs.getTabAt(0));
                    TextView funcioario = viewPager.findViewById(R.id.funcionario);
                    funcioario.setText(tag.split("-")[1]);
                    funcioario.setTag(tag.split("-")[0]);
                }
            });

    }


    void CargarListaMateriales() {
        SQLiteDatabase db = AdminDB.Connection(MainActivity.this);
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM MATERIALES_API", null);
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                lista.add(cursor.getString(0) + "-" + cursor.getString(2) + " ("+ cursor.getString(4)+")");
            }
            Log.e("TAG", "CargarLista: lista Cargada"+ lista.size() );
        }
    }
    void CargarListaTecnicos() {
        SQLiteDatabase db = AdminDB.Connection(MainActivity.this);
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM CUADRILLAS", null);
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                lista_fun.add(cursor.getString(0) + "-" + cursor.getString(1));
            }
            Log.e("TAG", "CargarLista: lista Cargada"+ lista_fun.size() );
        }
    }


    // METODO QUE CONSULTADO EL ESTADO DEL INTERNET EN UN MOMENTO ESPECIFICO
    public boolean ConexionInternet() {
        ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.getState() == NetworkInfo.State.CONNECTED;
        } else {
            ViewSnackBar.SnackBarDanger(layout, "No tienes acceso a internet, verifica e intenta nuevamente", MainActivity.this);
        }
        return false;
    }

    public class BackGroundMain extends AsyncTask<String, Void, String> {
        int con =0;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Sincronizando...");
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Verficando Información...\nPor favor espere unos minutos");
            progressDialog.setProgress(0);
            progressDialog.show();
            super.onPreExecute();
            lista = new ArrayList<>();
            lista_fun = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                for (int i = 1; i <= 2; i++) {
                 con  = SyncDatos.SinronzarDatos(i, MainActivity.this, layout);
                    Thread.sleep(5000);
                }
            } catch (Exception e) {
                Log.e("TAG", "doInBackground: " + e.fillInStackTrace());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

                CargarListaMateriales();
                CargarListaTecnicos();
            super.onPostExecute(s);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_funcionario:
                String material = textView.getText().toString().trim();
                if (material.isEmpty()){
                    ViewSnackBar.SnackBarDanger(layout, "Selecciona un material e intenta nuevamente", MainActivity.this);
                    return true;
                }
               AddFuncionario();
                break;
            case R.id.logout:
                Toast.makeText(this, "Cerrar Sesión", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sync:
                if (ConexionInternet()) {
                    new BackGroundMain().execute();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}