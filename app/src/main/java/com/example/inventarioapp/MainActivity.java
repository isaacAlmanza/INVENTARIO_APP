package com.example.inventarioapp;

import static android.widget.SearchView.*;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.example.inventarioapp.Adapters.Adapter;
import com.example.inventarioapp.Interface.AdminDB;
import com.example.inventarioapp.Interface.SyncDatos;
import com.example.inventarioapp.Models.ModelLista;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventarioapp.ui.main.SectionsPagerAdapter;
import com.example.inventarioapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    CoordinatorLayout layout;
    private ActivityMainBinding binding;
    SearchView searchView;
    ArrayList<String> copia;
    ArrayList<String> lista;
    ModelLista modelLista;
    TextView textView;
    Dialog dialog;
    ListView listView_item;
    ListView listView;
    ArrayAdapter<String> adapter_item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        layout = findViewById(R.id.layout_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // listView = findViewById(R.id.listView);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        textView = findViewById(R.id.search_tv);
        tabs.setupWithViewPager(viewPager);
        lista = new ArrayList<>();
        CargarLista();
        if (ConexionInternet()){
          //  new BackGroundMain().execute();
        }

        textView.setOnClickListener(v -> {
            dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.item);
            dialog.getWindow().setLayout(650, 800);
            dialog.getWindow().setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            EditText et_text = dialog.findViewById(R.id.et_text);
            listView = dialog.findViewById(R.id.list_view_item);
            Log.e("TAG", "onCreate: " + listView_item );
            adapter_item = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, lista);
            listView_item.setAdapter(adapter_item);
            et_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter_item.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listView_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    textView.setText(adapter_item.getItem(position));
                    dialog.dismiss();
                }
            });
        });

    }

    void CargarLista(){
        SQLiteDatabase db = AdminDB.Connection(MainActivity.this);
        Cursor cursor = db.rawQuery("SELECT * FROM MATERIALES_API", null);
       if ( cursor.getCount()>0){
          while (cursor.moveToNext()){
              lista.add(cursor.getString(0)+"-"+cursor.getString(2));
          }
       }
     //   adapter = new Adapter(MainActivity.this, cursor, 0);
      //  listView.setAdapter(adapter);
       // copia = new ArrayList<>();
        // copia.addAll(lista);
    }



    // METODO QUE CONSULTADO EL ESTADO DEL INTERNET EN UN MOMENTO ESPECIFICO
    public boolean ConexionInternet(){
        ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =  conn.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.getState() == NetworkInfo.State.CONNECTED;
        }else {
            Snackbar.make(layout,"No tienes acceso a internet, verifica e intenta nuevamente",Snackbar.LENGTH_LONG).setBackgroundTint(getColor(R.color.danger)).show();
        }
        return false;
    }
    public  class BackGroundMain extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

          try {
              for (int i = 1; i <= 2; i++) {
                  SyncDatos.SinronzarDatos(i, MainActivity.this);
              }
          }catch (Exception e){
              Log.e("TAG", "doInBackground: " +e.fillInStackTrace() );
          }
            return null;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        HandlerIntent(intent);
    }
    void HandlerIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
          if (query!=null){
              Filtrado_Codigo(query);
          }
        }
    }

    public void Filtrado_Codigo(final String buscar){
        Log.e("TAG", "Filtrado_Codigo: enra" + buscar );
        if (buscar.length()==0) {
            lista.clear();
            lista.addAll(copia);
        }else {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
                lista.clear();
                List<String> collect = copia.stream().
                        filter(i ->String.valueOf(i).
                                toLowerCase().contains(buscar))
                        .collect(Collectors
                                .toList());

               lista.addAll(collect);
            }else {
                lista.clear();
                for (String  i: copia) {
                    if (String.valueOf(i).toLowerCase().contains(buscar)){
                        lista.add(i);
                    }
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchManager manager = (SearchManager) getSystemService(SEARCH_SERVICE);
        final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        //permite modificar el hint que el EditText muestra por defecto
        searchView.setSearchableInfo( manager.getSearchableInfo( new ComponentName(this, Busqueda.class)));
       /* searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                //se oculta el EditText
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        */
        return super.onCreateOptionsMenu(menu);
    }
}