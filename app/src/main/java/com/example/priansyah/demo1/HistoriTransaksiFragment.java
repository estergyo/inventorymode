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
import android.widget.ExpandableListAdapter;

import com.example.priansyah.demo1.Adapter.ExpandableListTransaksiAdapter;
import com.example.priansyah.demo1.Adapter.ListItemAdapter;
import com.example.priansyah.demo1.Adapter.ListTransaksiAdapter;
import com.example.priansyah.demo1.Entity.Item;
import com.example.priansyah.demo1.Entity.Transaksi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priansyah on 3/16/2016.
 */
public class HistoriTransaksiFragment extends Fragment {

    RecyclerView recList;
    ExpandableListTransaksiAdapter adapter;

//    ArrayList<Transaksi> listOfTransaction;
    List<ExpandableListTransaksiAdapter.Item> data;
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
//        listOfTransaction = new ArrayList<>();
        data = new ArrayList<>();
        dbListTransaksi.execSQL("Create table if not exists stock_transaction(transaction_id INT, total INT, discount INT, tax VARCHAR, date_created VARCHAR, payment INT);");
        Cursor items = dbListTransaksi.rawQuery("select distinct date_created from stock_transaction",null);
        Cursor items2 = dbListTransaksi.rawQuery("select * from stock_transaction",null);
        if(items != null)
            if(items.moveToFirst())
                do{
//                    listOfTransaction.add(new Transaksi(""+items.getInt(0),""+items.getInt(1),""+items.getInt(2),items.getString(3),items.getString(4), ""+items.getInt(5)));
                    data.add(new ExpandableListTransaksiAdapter.Item(ExpandableListTransaksiAdapter.HEADER, new Transaksi(null, null, null, null,items.getString(0), null)));
                    if(items2 != null)
                        if(items2.moveToFirst())
                            do {
                                if (items.getString(0).equals(items2.getString(4)))
                                    data.add(new ExpandableListTransaksiAdapter.Item(ExpandableListTransaksiAdapter.CHILD, new Transaksi(""+items2.getInt(0),""+items2.getInt(1),""+items2.getInt(2),items2.getString(3),items2.getString(4), ""+items2.getInt(5))));
                            } while (items2.moveToNext());
                }while(items.moveToNext());

        adapter = new ExpandableListTransaksiAdapter(data);
        adapter.setOnItemClickListener(new ExpandableListTransaksiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DetailTransaksiActivity.class);
                intent.putExtra("Transaksi", data.get(position).transaksi);
                startActivity(intent);
            }
        });
        recList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recList.setAdapter(adapter);
//        recyclerview.setAdapter(new ExpandableListAdapter(data));
    }
}
