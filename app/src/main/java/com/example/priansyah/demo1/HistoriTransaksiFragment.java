package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.priansyah.demo1.Adapter.ListItemAdapter;
import com.example.priansyah.demo1.Adapter.ListTransaksiAdapter;
import com.example.priansyah.demo1.Entity.Item;
import com.example.priansyah.demo1.Entity.Transaksi;

import java.util.ArrayList;

/**
 * Created by Priansyah on 3/16/2016.
 */
public class HistoriTransaksiFragment extends Fragment {

    RecyclerView recList;
    ListTransaksiAdapter adapter;

    ArrayList<Transaksi> listOfTransaction;
    SQLiteDatabase dbListTransaksi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_histori_transaksi, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dbListTransaksi = getActivity().openOrCreateDatabase("POS", getActivity().MODE_PRIVATE, null);
        super.onViewCreated(view, savedInstanceState);

        recList = (RecyclerView) getView().findViewById(R.id.listViewTransaksi);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        setList();
    }

    public void setList(){
        listOfTransaction = new ArrayList<>();
        dbListTransaksi.execSQL("Create table if not exists stock_transaction(transaction_id INT, discount INT, tax VARCHAR, date_created VARCHAR, total VARCHAR);");
        Cursor items = dbListTransaksi.rawQuery("select * from stock_transaction",null);
        if(items != null)
            if(items.moveToFirst())
                do{
                    listOfTransaction.add(new Transaksi(""+items.getInt(0),""+items.getInt(1),items.getString(2),items.getString(3)));
                }while(items.moveToNext());

        adapter = new ListTransaksiAdapter(getActivity(),getContext(),listOfTransaction);
        adapter.setOnItemClickListener(new ListTransaksiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DetailTransaksiActivity.class);
                intent.putExtra("Transaksi", listOfTransaction.get(position));
                getActivity().startActivityForResult(intent, getResources().getInteger(R.integer.item_update_rq_code));
            }
        });
        recList.setAdapter(adapter);
    }
}
