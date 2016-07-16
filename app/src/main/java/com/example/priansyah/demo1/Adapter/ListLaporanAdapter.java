package com.example.priansyah.demo1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.priansyah.demo1.R;

import java.util.ArrayList;

/**
 * Created by ester gyo fanny on 02/06/2016.
 */
public class ListLaporanAdapter extends RecyclerView.Adapter<ListLaporanAdapter.LaporanViewHolder>{
    Activity activity;
    Context context;
    ArrayList<String> listOfLaporan;

    String textJudul;
    String textKeterangan;

    OnItemClickListener mItemClickListener;

    public ListLaporanAdapter(Activity activity, Context context, ArrayList<String> listOfLaporan){
        this.activity = activity;
        this.context = context;
        this.listOfLaporan = listOfLaporan;
    }

    @Override
    public LaporanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View laporanView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.listitem_laporan, parent, false);
        return new LaporanViewHolder(laporanView);
    }

    @Override
    public void onBindViewHolder(LaporanViewHolder holder, int position) {
        if (position%2==0) {
            textJudul = listOfLaporan.get(position);
            holder.textViewJudul.setText(textJudul);
        }
        else {
            textKeterangan = listOfLaporan.get(position);
            holder.textViewKeterangan.setText(textJudul);
        }
    }

    @Override
    public int getItemCount() {
        return listOfLaporan.size();
    }



    public class LaporanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewJudul;
        protected TextView textViewKeterangan;

        public LaporanViewHolder(View v) {
            super(v);
            textViewJudul = (TextView) v.findViewById(R.id.textViewJudul);
            textViewKeterangan = (TextView) v.findViewById(R.id.textViewKeterangan);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
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
