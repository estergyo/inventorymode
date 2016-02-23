package com.example.priansyah.demo1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.priansyah.demo1.Entity.Supplier;
import com.example.priansyah.demo1.R;

import java.util.ArrayList;

/**
 * Created by Priansyah on 1/23/2016.
 */
public class ListSupplierAdapter extends RecyclerView.Adapter<ListSupplierAdapter.SupplierViewHolder> {

    Activity activity;
    Context context;
    ArrayList<Supplier> listOfSuppliers;

    String textNama;
    String textAlamat;
    String textKontak;

    OnItemClickListener mItemClickListener;

    public ListSupplierAdapter(Activity activity, Context context, ArrayList<Supplier> listOfSuppliers){
        this.activity = activity;
        this.context = context;
        this.listOfSuppliers = listOfSuppliers;
    }

    @Override
    public SupplierViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View supplierView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.listitem_supplier, parent, false);
        return new SupplierViewHolder(supplierView);
    }

    @Override
    public void onBindViewHolder(SupplierViewHolder holder, int position) {
        final Supplier supplier = listOfSuppliers.get(position);
        textNama = supplier.getTextNama();
        textAlamat = supplier.getTextAlamat();
        textKontak = supplier.getTextKontak();
        holder.textViewNama.setText(textNama);
        holder.textViewAlamat.setText(textAlamat);
        holder.textViewKontak.setText(textKontak);
    }

    @Override
    public int getItemCount() {
        return listOfSuppliers.size();
    }



    public class SupplierViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewNama;
        protected TextView textViewAlamat;
        protected TextView textViewKontak;

        public SupplierViewHolder(View v) {
            super(v);
            textViewNama = (TextView) v.findViewById(R.id.text_supplier_nama);
            textViewAlamat = (TextView) v.findViewById(R.id.text_supplier_alamat);
            textViewKontak = (TextView) v.findViewById(R.id.text_supplier_kontak);
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
