package com.example.priansyah.demo1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.priansyah.demo1.Entity.TransDetail;
import com.example.priansyah.demo1.R;

import java.util.ArrayList;

/**
 * Created by Priansyah on 3/6/2016.
 */
public class ListDetilTransaksiAdapter extends RecyclerView.Adapter<ListDetilTransaksiAdapter.DetilTransaksiViewHolder> {
    Activity activity;
    Context context;
    ArrayList<TransDetail> listOfTransactionDetails;

    String textTransId;
    String textSKU;
    String textNama;
    String textJumlah;
    String textHarga;
    String textTanggalTrans;

    OnItemClickListener mItemClickListener;

    public ListDetilTransaksiAdapter(Activity activity, Context context, ArrayList<TransDetail> listOfTransactionDetails){
        this.activity = activity;
        this.context = context;
        this.listOfTransactionDetails = listOfTransactionDetails;
    }

    @Override
    public DetilTransaksiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View detilTransaksiView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.listitem_transdetail, parent, false);
        return new DetilTransaksiViewHolder(detilTransaksiView);
    }

    @Override
    public void onBindViewHolder(DetilTransaksiViewHolder holder, int position) {
        final TransDetail detilTransaksi = listOfTransactionDetails.get(position);
        textTransId = detilTransaksi.getTextTransId();
        textNama = detilTransaksi.getTextNama();
        textJumlah = detilTransaksi.getTextJumlah();
        textHarga = detilTransaksi.getTextHarga();
//        textTanggalTrans = detilTransaksi.getTextTanggalTrans();
        holder.textViewTransId.setText(textTransId);
        holder.textViewNama.setText(textNama);
        holder.textViewJumlah.setText(textJumlah);
        holder.textViewHarga.setText(textHarga);
//        holder.textViewTanggalTrans.setText(textTanggalTrans);
    }

    @Override
    public int getItemCount() {
        return listOfTransactionDetails.size();
    }



    public class DetilTransaksiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewTransId;
        protected TextView textViewNama;
        protected TextView textViewJumlah;
        protected TextView textViewHarga;
//        protected TextView textViewTanggalTrans;

        public DetilTransaksiViewHolder(View v) {
            super(v);
            textViewTransId = (TextView) v.findViewById(R.id.text_transdetail_trans_id);
            textViewNama = (TextView) v.findViewById(R.id.text_transdetail_nama);
            textViewJumlah = (TextView) v.findViewById(R.id.text_transdetail_jumlah);
            textViewHarga = (TextView) v.findViewById(R.id.text_transdetail_harga);
//            textViewTanggalTrans = (TextView) v.findViewById(R.id.text_transdetail_tanggal);
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
