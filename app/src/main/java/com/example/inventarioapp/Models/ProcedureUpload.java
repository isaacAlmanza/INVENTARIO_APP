package com.example.inventarioapp.Models;

import com.google.gson.annotations.SerializedName;

public class ProcedureUpload {

    @SerializedName("procedimiento1")
    private  String dpto;

    @SerializedName("procedimiento2")
    private  String mpio;

    @SerializedName("procedimiento3")
    private  String barrio;


    public ProcedureUpload(String dpto, String mpio, String barrio) {
        this.dpto = dpto;
        this.mpio = mpio;
        this.barrio = barrio;
    }
    public ProcedureUpload(){

    }
    public String getDpto() {
        return dpto;
    }

    public void setDpto(String dpto) {
        this.dpto = dpto;
    }

    public String getMpio() {
        return mpio;
    }

    public void setMpio(String mpio) {
        this.mpio = mpio;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }
}
