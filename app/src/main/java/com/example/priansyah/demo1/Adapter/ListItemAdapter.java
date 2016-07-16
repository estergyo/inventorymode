package com.example.priansyah.demo1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.priansyah.demo1.Entity.Item;
import com.example.priansyah.demo1.R;

import java.util.ArrayList;

/**
 * Created by Priansyah on 1/22/2016.
 */
public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ItemViewHolder> {

    Activity activity;
    Context context;
    ArrayList<Item> listOfItems;

    String textNama;
    String textSKU;
    String textJumlah;
    String textHarga;
    String textKategori;
    String textSupplier;

    OnItemClickListener mItemClickListener;

    public ListItemAdapter(Activity activity, Context context, ArrayList<Item> listOfItems){
        this.activity = activity;
        this.context = context;
        this.listOfItems = listOfItems;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.listitem_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Item item = listOfItems.get(position);
        textNama = item.getTextNama();
        textSKU = item.getTextSKU();
        textJumlah = item.getTextJumlah();
        textHarga = item.getTextHarga();
//        textKategori = item.getTextKategori();
//        textSupplier = item.getTextSupplier();
        holder.textViewNama.setText(textNama);
        holder.textViewSKU.setText(textSKU);
        holder.textViewJumlah.setText(textJumlah);
        holder.textViewHarga.setText(textHarga);
//        holder.textViewSupplier.setText(textSupplier);
//        holder.textViewKategori.setText(textKategori);
    }

    @Override
    public int getItemCount() {
        return listOfItems.size();
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView textViewNama;
        protected TextView textViewSKU;
        protected TextView textViewJumlah;
        protected TextView textViewHarga;
        protected TextView textViewKategori;
        protected TextView textViewSupplier;

        public ItemViewHolder(View v) {
            super(v);
            textViewNama = (TextView) v.findViewById(R.id.text_item_nama);
            textViewSKU = (TextView) v.findViewById(R.id.text_item_sku);
            textViewJumlah = (TextView) v.findViewById(R.id.text_item_jumlah);
            textViewHarga = (TextView) v.findViewById(R.id.text_item_harga);
//            textViewSupplier = (TextView) v.findViewById(R.id.text_item_supplier);
//            textViewKategori = (TextView) v.findViewById(R.id.text_item_kategori);

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
