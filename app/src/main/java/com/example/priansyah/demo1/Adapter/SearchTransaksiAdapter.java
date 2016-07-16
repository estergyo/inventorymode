package com.example.priansyah.demo1.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.priansyah.demo1.Entity.Transaksi;
import com.example.priansyah.demo1.R;

/**
 * Created by ester gyo fanny on 31/05/2016.
 */
public class SearchTransaksiAdapter extends SimpleCursorAdapter {
    private static final String[] mFields  = { "_id", "idtrans", "harga", "tanggal" };
    private static final String[] mVisible = { "idtrans", "harga", "tanggal" };
    private static final int[]    mViewIds = { R.id.textViewIdChild, R.id.textViewHargaChild, R.id.textViewWaktuChild};

    public SearchTransaksiAdapter(Context context, String[] columns) {
        super(context, R.layout.listitem_transaksi_child, null, columns, null, -1000);
    }

    public Transaksi get(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        return new Transaksi(""+cursor.getInt(0),""+cursor.getInt(1),""+cursor.getInt(2),cursor.getString(3),cursor.getString(4), ""+cursor.getInt(5), cursor.getString(6));
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewSearchId=(TextView)view.findViewById(R.id.textViewIdChild);
        TextView textViewSearchHarga=(TextView)view.findViewById(R.id.textViewHargaChild);
        TextView textViewSearchWaktu=(TextView)view.findViewById(R.id.textViewWaktuChild);
        textViewSearchId.setText(cursor.getString(1));
        textViewSearchHarga.setText(cursor.getString(2));
        textViewSearchWaktu.setText(cursor.getString(3));
    }
}
