package com.example.priansyah.demo1.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Priansyah on 1/22/2016.
 */
public class Item implements Parcelable {

    private String textNama;
    private String textSKU;
    private String textJumlah;
    private String textHarga;
    private String textSupplier;
    private String textKategori;
    private String image;

    public Item(){
        textNama = "";
        textSKU = "";
        textJumlah = "";
        textHarga = "";
        textSupplier = "";
        textKategori = "";
        image = "";
    }

    public Item(String textNama, String textSKU, String textJumlah, String textHarga, String textSupplier, String textKategori, String image){
        this.textNama = textNama;
        this.textSKU = textSKU;
        this.textJumlah = textJumlah;
        this.textHarga = textHarga;
        this.textSupplier = textSupplier;
        this.textKategori = textKategori;
        this.image = image;
    }
    public Item(Parcel in){
        String[] data = new String[7];

        in.readStringArray(data);
        this.textNama = data[0];
        this.textSKU = data[1];
        this.textJumlah = data[2];
        this.textHarga = data[3];
        this.textSupplier = data[4];
        this.textKategori = data[5];
        this.image = data[6];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.textNama,
        this.textSKU,
        this.textJumlah,
        this.textHarga,
        this.textSupplier,
        this.textKategori,
        this.image});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getTextNama() {
        return textNama;
    }

    public void setTextNama(String textNama) {
        this.textNama = textNama;
    }

    public String getTextSKU() {
        return textSKU;
    }

    public void setTextSKU(String textSKU) {
        this.textSKU = textSKU;
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

    public String getTextSupplier() {
        return textSupplier;
    }

    public void setTextSupplier(String textSupplier) {
        this.textSupplier = textSupplier;
    }

    public String getTextKategori() {
        return textKategori;
    }

    public void setTextKategori(String textKategori) {
        this.textKategori = textKategori;
    }

    public String getTextImage() {
        return image;
    }

    public void setTextImage(String image) {
        this.image = image;
    }
}
