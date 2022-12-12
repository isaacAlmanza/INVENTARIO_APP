package com.example.inventarioapp.Interface;


import com.example.inventarioapp.Models.DatosDownload;
import com.example.inventarioapp.Models.DatosUpload;
import com.example.inventarioapp.Models.LoginAuthReq;
import com.example.inventarioapp.Models.LoginAuthRes;
import com.example.inventarioapp.Models.SaveDatosUpload;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceApi {


    @POST("Token.php")
    Call<LoginAuthRes> getDatosLogin(@Body LoginAuthReq req);


    @POST("Service.php")
    Call<DatosDownload> getDatosUpload(@Body ArrayList<DatosUpload> request);


    @POST("Service.php")
    Call<DatosDownload> getSave(@Body ArrayList<SaveDatosUpload> request);


}
