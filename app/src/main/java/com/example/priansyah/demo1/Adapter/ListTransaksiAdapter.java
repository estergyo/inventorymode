package com.example.priansyah.demo1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.priansyah.demo1.Entity.Transaksi;
import com.example.priansyah.demo1.R;

import java.util.ArrayList;

/**
 * Created by Priansyah on 3/16/2016.
 */
public class ListTransaksiAdapter extends RecyclerView.Adapter<ListTransaksiAdapter.TransaksiViewHolder> {
    Activity activity;
    Context context;
    ArrayList<Transaksi> listOfTransaction;

    String textTransId;
    String textTanggalTrans;

    OnItemClickListener mItemClickListener;

    public ListTransaksiAdapter(Activity activity, Context context, ArrayList<Transaksi> listOfTransaction){
        this.activity = activity;
        this.context = context;
        this.listOfTransaction = listOfTransaction;
    }

    @Override
    public TransaksiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View transaksiView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.listitem_transaksi, parent, false);
        return new TransaksiViewHolder(transaksiView);
    }

    @Override
    public void onBindViewHolder(TransaksiViewHolder holder, int position) {
        final Transaksi transaksi = listOfTransaction.get(position);
        textTransId = transaksi.getTextTransId();
        textTanggalTrans = transaksi.getTextTanggalTrans();
        holder.textViewTransId.setText(textTransId);
        holder.textViewTanggalTrans.setText(textTanggalTrans);
    }

    @Override
    public int getItemCount() {
        return listOfTransaction.size();
    }



    public class TransaksiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewTransId;
        protected TextView textViewTanggalTrans;

        public TransaksiViewHolder(View v) {
            super(v);
            textViewTransId = (TextView) v.findViewById(R.id.text_transaksi_trans_id);
            textViewTanggalTrans = (TextView) v.findViewById(R.id.text_transaksi_tanggal);
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
