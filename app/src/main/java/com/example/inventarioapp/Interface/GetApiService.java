package com.example.inventarioapp.Interface;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public interface GetApiService {

        static InterfaceApi ApiService(String url){
            Retrofit retrofit = null;
            final HttpLoggingInterceptor logging
                    = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);
            final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            Log.e("Error", "getApiService: " + url);
           try{
               if (retrofit == null) {
                   retrofit = new Retrofit.Builder()
                           .baseUrl(url)
                           .addConverterFactory(GsonConverterFactory.create())
                           .client(httpClient.build())
                           .build();
               }
           }catch (Exception e){
               Log.e("TAG", "ApiService: " +e.fillInStackTrace() );
           }
            return retrofit.create(InterfaceApi.class);

        }

}
