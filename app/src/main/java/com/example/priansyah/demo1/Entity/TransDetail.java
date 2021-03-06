package com.example.priansyah.demo1.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Priansyah on 3/1/2016.
 */
public class TransDetail implements Parcelable {
    private String textTransId;
    private String textSKU;
    private String textNama;
    private String textJumlah;
    private String textHarga;
    private String textTanggalTrans;

    public TransDetail(){
        textTransId = "";
        textSKU = "";
        textNama = "";
        textJumlah = "";
        textHarga = "";
        textTanggalTrans = "";
    }

    public TransDetail(String textTransId, String textSKU, String textNama, String textJumlah, String textHarga, String textTanggalTrans){
        this.textTransId = textTransId;
        this.textSKU = textSKU;
        this.textNama = textNama;
        this.textJumlah = textJumlah;
        this.textHarga = textHarga;
        this.textTanggalTrans = textTanggalTrans;
    }

    public TransDetail(Parcel in){
        String[] data = new String[6];

        in.readStringArray(data);
        this.textTransId = data[0];
        this.textSKU = data[1];
        this.textNama = data[2];
        this.textJumlah = data[3];
        this.textHarga = data[4];
        this.textTanggalTrans = data[5];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.textTransId,
                this.textSKU,
                this.textNama,
                this.textJumlah,
                this.textHarga,
                this.textTanggalTrans});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TransDetail createFromParcel(Parcel in) {
            return new TransDetail(in);
        }

        public TransDetail[] newArray(int size) {
            return new TransDetail[size];
        }
    };

    public String getTextTransId() {
        return textTransId;
    }

    public void setTextTransId(String textTransId) {
        this.textTransId = textTransId;
    }

    public String getTextSKU() {
        return textSKU;
    }

    public void setTextSKU(String textSKU) {
        this.textSKU = textSKU;
    }

    public String getTextNama() {
        return textNama;
    }

    public void setTextNama(String textNama) {
        this.textNama = textNama;
    }

    public String getTextJumlah() {
        return textJumlah;
    }

    public void setTextJumlah(String textJumlah) {
        this.textJumlah = textJumlah;
    }

    public String getTextHarga() {
        return textHarga;
    }

    public void setTextHarga(String textHarga) {
        this.textHarga = textHarga;
    }

    public String getTextTanggalTrans() {
        return textTanggalTrans;
    }

    public void setTextTanggalTrans(String textTanggalTrans) {
        this.textTanggalTrans = textTanggalTrans;
    }
}
