package com.example.priansyah.demo1.Entity;

import android.database.Cursor;
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
    private String textJumlahBayar;
    private String textWaktuTrans;

    public Transaksi(){
        textTransId = "";
        textTotalTrans="";
        textDiskon = "";
        textPajak = "";
        textTanggalTrans = "";
        textJumlahBayar = "";
        textWaktuTrans = "";
    }

    public Transaksi(String textTransId, String textTotalTrans, String textDiskon, String textPajak, String textTanggalTrans, String textJumlahBayar, String textWaktuTrans){
        this.textTransId = textTransId;
        this.textTotalTrans = textTotalTrans;
        this.textDiskon = textDiskon;
        this.textPajak = textPajak;
        this.textTanggalTrans = textTanggalTrans;
        this.textJumlahBayar = textJumlahBayar;
        this.textWaktuTrans = textWaktuTrans;
    }

    public Transaksi(Parcel in){
        String[] data = new String[7];

        in.readStringArray(data);
        this.textTransId = data[0];
        this.textTotalTrans = data[1];
        this.textDiskon = data[2];
        this.textPajak = data[3];
        this.textTanggalTrans = data[4];
        this.textJumlahBayar = data[5];
        this.textWaktuTrans = data[6];
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
                this.textJumlahBayar,
                this.textWaktuTrans,
        });
    }

    public String[] convertToRow(int position) {
        String[] temp = new String[4];
        temp[0] = Integer.toString(position);
        temp[1] = "#"+this.getTextTransId();
        temp[2] = "Rp" + this.getTextTotalTrans() + ",-";
        temp[3] = this.getTextWaktuTrans();
        return temp;
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

    public String getTextJumlahBayar() {
        return textJumlahBayar;
    }

    public void setTextJumlahBayar(String textJumlahBayar) {
        this.textJumlahBayar = textJumlahBayar;
    }

    public String getTextTotalTrans() {
        return textTotalTrans;
    }

    public void setTextTotalTrans(String textTotalTrans) {
        this.textTotalTrans = textTotalTrans;
    }

    public String getTextWaktuTrans() {
        return textWaktuTrans;
    }

    public void setTextWaktuTrans(String textWaktuTrans) {
        this.textWaktuTrans = textWaktuTrans;
    }
}
