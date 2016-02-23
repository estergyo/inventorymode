package com.example.priansyah.demo1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.priansyah.demo1.Entity.Item;
import com.example.priansyah.demo1.Entity.Kategori;
import com.example.priansyah.demo1.R;

import java.util.ArrayList;

/**
 * Created by Priansyah on 1/23/2016.
 */
public class ListKategoriAdapter extends RecyclerView.Adapter<ListKategoriAdapter.KategoriViewHolder> {

    Activity activity;
    Context context;
    ArrayList<Kategori> listOfCategories;

    String textNama;
    String textDeskripsi;

    OnItemClickListener mItemClickListener;

    public ListKategoriAdapter(Activity activity, Context context, ArrayList<Kategori> listOfCategories){
        this.activity = activity;
        this.context = context;
        this.listOfCategories = listOfCategories;
    }

    @Override
    public KategoriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View kategoriView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.listitem_kategori, parent, false);
        return new KategoriViewHolder(kategoriView);
    }

    @Override
    public void onBindViewHolder(KategoriViewHolder holder, int position) {
        final Kategori kategori = listOfCategories.get(position);
        textNama = kategori.getTextNama();
        textDeskripsi = kategori.getTextDeskripsi();
        holder.textViewNama.setText(textNama);
        holder.textViewDeskripsi.setText(textDeskripsi);
    }

    @Override
    public int getItemCount() {
        return listOfCategories.size();
    }



    public class KategoriViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewNama;
        protected TextView textViewDeskripsi;

        public KategoriViewHolder(View v) {
            super(v);
            textViewNama = (TextView) v.findViewById(R.id.text_kategori_nama);
            textViewDeskripsi = (TextView) v.findViewById(R.id.text_kategori_deskripsi);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
