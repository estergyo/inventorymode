package com.example.priansyah.demo1.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Priansyah on 1/23/2016.
 */
public class Supplier implements Parcelable {

    private String textNama;
    private String textAlamat;
    private String textKontak;

    public Supplier(){
        textNama = "";
        textAlamat = "";
        textKontak = "";
    }

    public Supplier(String textNama, String textAlamat, String textKontak){
        this.textNama = textNama;
        this.textAlamat = textAlamat;
        this.textKontak = textKontak;
    }

    public Supplier(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.textNama = data[0];
        this.textAlamat = data[1];
        this.textKontak = data[2];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.textNama,
                this.textAlamat,
                this.textKontak});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Supplier createFromParcel(Parcel in) {
            return new Supplier(in);
        }

        public Supplier[] newArray(int size) {
            return new Supplier[size];
        }
    };

    public String getTextNama() {
        return textNama;
    }

    public void setTextNama(String textNama) {
        this.textNama = textNama;
    }

    public String getTextAlamat() {
        return textAlamat;
    }

    public void setTextAlamat(String textAlamat) {
        this.textAlamat = textAlamat ;
    }

    public String getTextKontak() {
        return textKontak;
    }

    public void setTextKontak(String textKontak) {
        this.textKontak = textKontak;
    }
}
