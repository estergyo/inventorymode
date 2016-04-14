package com.example.priansyah.demo1.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Priansyah on 3/16/2016.
 */
public class Transaksi implements Parcelable {

    private String textTransId;
    private String textDiskon;
    private String textPajak;
    private String textTanggalTrans;
    private String textTotalTrans;

    public Transaksi(){
        textTransId = "";
        textTotalTrans="";
        textDiskon = "";
        textPajak = "";
        textTanggalTrans = "";
    }

    public Transaksi(String textTransId, String textTotalTrans, String textDiskon, String textPajak, String textTanggalTrans){
        this.textTransId = textTransId;
        this.textTotalTrans = textTotalTrans;
        this.textDiskon = textDiskon;
        this.textPajak = textPajak;
        this.textTanggalTrans = textTanggalTrans;
    }

    public Transaksi(Parcel in){
        String[] data = new String[5];

        in.readStringArray(data);
        this.textTransId = data[0];
        this.textTotalTrans = data[1];
        this.textDiskon = data[2];
        this.textPajak = data[3];
        this.textTanggalTrans = data[4];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.textTransId,
                this.textTotalTrans,
                this.textDiskon,
                this.textPajak,
                this.textTanggalTrans,
//                textTotalTrans
        });
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Transaksi createFromParcel(Parcel in) {
            return new Transaksi(in);
        }

        public Transaksi[] newArray(int size) {
            return new Transaksi[size];
        }
    };

    public String getTextTransId() {
        return textTransId;
    }

    public void setTextTransId(String textTransId) {
        this.textTransId = textTransId;
    }

    public String getTextDiskon() {
        return textDiskon;
    }

    public void setTextDiskon(String textDiskon) {
        this.textDiskon = textDiskon;
    }

    public String getTextPajak() {
        return textPajak;
    }

    public void setTextPajak(String textNama) {
        this.textPajak = textPajak;
    }

    public String getTextTanggalTrans() {
        return textTanggalTrans;
    }

    public void setTextTanggalTrans(String textTanggalTrans) {
        this.textTanggalTrans = textTanggalTrans;
    }

    public String getTextTotalTrans() {
        return textTotalTrans;
    }

    public void setTextTotalTrans(String textTotalTrans) {
        this.textTotalTrans = textTotalTrans;
    }
}
