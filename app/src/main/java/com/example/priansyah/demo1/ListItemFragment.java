package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.priansyah.demo1.Adapter.ListItemAdapter;
import com.example.priansyah.demo1.Entity.Item;

import java.util.ArrayList;

/**
 * Created by Priansyah on 1/22/2016.
 */
public class ListItemFragment extends Fragment {

    RecyclerView recList;
    ListItemAdapter adapter;
    Button buttonTambahItem;

    ArrayList<Item> listOfItems;
    SQLiteDatabase dbListItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_item, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        dbListItem = getActivity().openOrCreateDatabase("POS", getActivity().MODE_PRIVATE, null);
        super.onViewCreated(view, savedInstanceState);

        buttonTambahItem = (Button) getActivity().findViewById(R.id.buttonTambahItem);

        buttonTambahItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TambahItemActivity.class);
                getActivity().startActivityForResult(intent,getResources().getInteger(R.integer.item_new_rq_code));
            }
        });

        recList = (RecyclerView) getView().findViewById(R.id.listViewItem);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        setList();

    }
    public void setList(){
        listOfItems = new ArrayList<>();
        dbListItem.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
        Cursor items = dbListItem.rawQuery("select * from stock",null);
        if(items != null)
            if(items.moveToFirst())
                do{
                    listOfItems.add(new Item(items.getString(1),items.getString(0),""+items.getInt(2),""+items.getInt(3),items.getString(5),items.getString(4),items.getString(6)));
                }while(items.moveToNext());
//        listOfItems.add(new Item("nama1","sku1","jumlah1","harga1","supplier1","kategori1"));
//        listOfItems.add(new Item("nama2","sku2","jumlah2","harga2","supplier2","kategori2"));

        adapter = new ListItemAdapter(getActivity(),getContext(),listOfItems);
        adapter.setOnItemClickListener(new ListItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DetailItemActivity.class);
                intent.putExtra("Item", listOfItems.get(position));
                getActivity().startActivityForResult(intent,getResources().getInteger(R.integer.item_update_rq_code));
            }
        });
        recList.setAdapter(adapter);
    }
}