package com.example.priansyah.demo1.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ester gyo fanny on 30/03/2016.
 */
public class UserCheckResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("notifikasi")
    private String notifikasi;
    @SerializedName("data")
    private String data;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNotifikasi() {
        return notifikasi;
    }

    public void setNotifikasi(String notifikasi) {
        this.notifikasi = notifikasi;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
