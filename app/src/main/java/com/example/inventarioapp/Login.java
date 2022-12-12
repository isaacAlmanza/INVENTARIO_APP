package com.example.inventarioapp;

import static java.lang.String.valueOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.inventarioapp.Interface.GetApiService;
import com.example.inventarioapp.Interface.GetSharedPreferences;
import com.example.inventarioapp.Interface.InterfaceApi;
import com.example.inventarioapp.Models.LoginAuthReq;
import com.example.inventarioapp.Models.LoginAuthRes;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    String TAG = "eRROr";
    ConstraintLayout layout;
    InterfaceApi api;
    String imei;
    String url, TOKEN;
    EditText et_server, et_dominio, et_cc, et_cod;
    Button btnAuth;
    private ProgressDialog progressDialog;
    static final Integer PHONESTATS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        consultarPermiso();
        et_server = findViewById(R.id.servidor);
        et_dominio = findViewById(R.id.dominio);
        et_cc = findViewById(R.id.cc_auth);
        et_cod = findViewById(R.id.cod_auth);
        btnAuth = findViewById(R.id.btnAuth);
        layout = findViewById(R.id.layout_auth);
        TOKEN =GetSharedPreferences.getSharedData(Login.this).get(1);

        try {
            if (!TOKEN.isEmpty()){
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
                Log.e(TAG, "doInBackground: entra" );
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        btnAuth.setOnClickListener(view -> new BackgroundLogin().execute());


    }

    public static String getIMEIDeviceId(Context context) {
        String deviceId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
            }
            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    deviceId = mTelephony.getImei();
                }else {
                    deviceId = mTelephony.getDeviceId();
                }
            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        Log.e("deviceId", deviceId);

        return deviceId;
    }

    private void consultarPermiso() {
        int per = ContextCompat.checkSelfPermission(Login.this, Manifest.permission.READ_PHONE_STATE) ;
       int internet =checkSelfPermission(Manifest.permission.INTERNET);
        if (per == PackageManager.PERMISSION_GRANTED && internet ==PackageManager.PERMISSION_GRANTED) {
            imei = getIMEIDeviceId(getApplicationContext());
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.INTERNET },PHONESTATS);
        }
    }
    private void verifyPermission() {
        int permsRequestCode = 100;
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.INTERNET};
        int accessFinePermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int accessInternet = checkSelfPermission(Manifest.permission.INTERNET);
        int accessCoarsePermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        int cameraPermission = checkSelfPermission(Manifest.permission.CAMERA);

        if ( accessInternet!=PackageManager.PERMISSION_GRANTED && cameraPermission != PackageManager.PERMISSION_GRANTED && accessFinePermission != PackageManager.PERMISSION_GRANTED && accessCoarsePermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(perms, permsRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {

                // Validamos si el usuario acepta el permiso para que la aplicación acceda a los datos internos del equipo, si no denegamos el acceso
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    Toast.makeText(Login.this, "Has negado el permiso a la aplicación", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void SaveToken(String token, String url, String imei){
        String user =  et_cod.getText().toString().trim();
        SharedPreferences preferences = (SharedPreferences) getSharedPreferences(valueOf(R.string.token), MODE_PRIVATE);
        SharedPreferences.Editor edit =  preferences.edit();
        edit.putString(valueOf(R.string.token), token);
        edit.putString("user", user);
        edit.putString("url", url);
        edit.putString("imei", imei);
        edit.apply();
    }

    boolean ValidarLogin(){
        String codigo = et_cod.getText().toString().trim();
        String cedula = et_cc.getText().toString().trim();
        String servidor = et_server.getText().toString().trim();
        String dominio = et_dominio.getText().toString().trim();
        if (codigo.isEmpty() || cedula.isEmpty() || servidor.isEmpty() || dominio.isEmpty()){
            Snackbar.make(layout, "Ingrese los datos para continuar", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.danger)).show();
            return true;
        }
        return false;
    }

    private void CargarData() throws NoSuchFieldException {
        try {
            String codigo = et_cod.getText().toString().trim();
            String cedula = et_cc.getText().toString().trim();
            String servidor = et_server.getText().toString().trim();
            String dominio = et_dominio.getText().toString().trim();
            LoginAuthReq req = new LoginAuthReq();
            req.setCodigo(codigo);
            req.setCedula(cedula);
            imei = getIMEIDeviceId(getApplicationContext());
            Log.e(TAG, "CargarData: " + imei );
            req.setImei(imei);
            url = "http://"+servidor+"/"+dominio+"/pages/Api_Token/";
            api = GetApiService.ApiService(url);
            api.getDatosLogin(req).enqueue(new Callback<LoginAuthRes>() {
                @Override
                public void onResponse(Call<LoginAuthRes> call, Response<LoginAuthRes> response) {
                    LoginAuthRes res = response.body();
                    if (response.isSuccessful()) {
                        if (!res.getToken().isEmpty()) {
                            SaveToken(res.getToken(), url, imei);
                            Snackbar.make(layout, res.getMsgError(), Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.success)).show();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                            finish();
                        }else {
                            Snackbar.make(layout, res.getMsgError(), Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.danger)).show();
                        }
                    }else {
                        Snackbar.make(layout, "Inicio de Sesion Fallido, Verifique sus Credenciales", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.danger)).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginAuthRes> call, Throwable t) {
                    Snackbar.make(layout, t.getMessage().toString(), Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.danger)).show();
                }
            });
        }catch (Exception e){
            Snackbar.make(layout, e.getMessage(), Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.danger)).show();
        }

    }


    public class BackgroundLogin extends AsyncTask<Void, String, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setTitle("Iniciando Sesión...");
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Verficando Información...\nPor favor espere unos minutos");
            progressDialog.setProgress(0);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e(TAG, "onCreate: token "+ GetSharedPreferences.getSharedData(Login.this).get(1).isEmpty() );
            if (!ValidarLogin()) {
                try {
                    CargarData();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressDialog.dismiss();

        }
    }


}