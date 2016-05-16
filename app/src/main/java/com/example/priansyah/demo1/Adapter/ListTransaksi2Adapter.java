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
 * Created by ester gyo fanny on 22/04/2016.
 */
public class ListTransaksi2Adapter extends RecyclerView.Adapter<ListTransaksi2Adapter.TransaksiViewHolder> {
    Activity activity;
    Context context;
    ArrayList<Transaksi> listOfTransaction;

    String textTransId;
    String textTotalTrans;

    OnItemClickListener mItemClickListener;

    public ListTransaksi2Adapter(Activity activity, Context context, ArrayList<Transaksi> listOfTransaction){
        this.activity = activity;
        this.context = context;
        this.listOfTransaction = listOfTransaction;
    }

    @Override
    public TransaksiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View transaksiView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.listitem_transaksi2, parent, false);
        return new TransaksiViewHolder(transaksiView);
    }

    @Override
    public void onBindViewHolder(TransaksiViewHolder holder, int position) {
        final Transaksi transaksi = listOfTransaction.get(position);
        textTransId = transaksi.getTextTransId();
        textTotalTrans = transaksi.getTextTotalTrans();
        holder.textViewIdTrans.setText(textTransId);
        holder.textViewTotalTrans.setText(textTotalTrans);
    }

    @Override
    public int getItemCount() {
        return listOfTransaction.size();
    }



    public class TransaksiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewIdTrans;
        protected TextView textViewTotalTrans;

        public TransaksiViewHolder(View v) {
            super(v);
            textViewIdTrans = (TextView) v.findViewById(R.id.textViewIdTrans);
            textViewTotalTrans = (TextView) v.findViewById(R.id.textViewTotalTrans);
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
