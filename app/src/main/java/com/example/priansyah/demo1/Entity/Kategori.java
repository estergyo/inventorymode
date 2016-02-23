package com.example.priansyah.demo1.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Priansyah on 1/23/2016.
 */
public class Kategori implements Parcelable {

    private String textNama;
    private String textDeskripsi;

    public Kategori(){
        textNama = "";
        textDeskripsi = "";
    }

    public Kategori(String textNama, String textDeskripsi){
        this.textNama = textNama;
        this.textDeskripsi = textDeskripsi;
    }

    public Kategori(Parcel in){
        String[] data = new String[2];

        in.readStringArray(data);
        this.textNama = data[0];
        this.textDeskripsi = data[1];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.textNama,
                this.textDeskripsi});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Kategori createFromParcel(Parcel in) {
            return new Kategori(in);
        }

        public Kategori[] newArray(int size) {
            return new Kategori[size];
        }
    };

    public String getTextNama() {
        return textNama;
    }

    public void setTextNama(String textNama) {
        this.textNama = textNama;
    }

    public String getTextDeskripsi() {
        return textDeskripsi;
    }

    public void setTextDeskripsi(String textDeskripsi) {
        this.textDeskripsi = textDeskripsi ;
    }
}
