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
import android.widget.Toast;

import com.example.priansyah.demo1.Adapter.ListKategoriAdapter;
import com.example.priansyah.demo1.Entity.Item;
import com.example.priansyah.demo1.Entity.Kategori;

import java.util.ArrayList;

/**
 * Created by Priansyah on 1/22/2016.
 */
public class ListKategoriFragment extends Fragment {

	RecyclerView recList;
    ListKategoriAdapter adapter;
    Button buttonTambahKategori;

    ArrayList<Kategori> listOfCategories;
    SQLiteDatabase dbListKategori;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_kategori, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dbListKategori = getActivity().openOrCreateDatabase("POS", getActivity().MODE_PRIVATE, null);
        super.onViewCreated(view, savedInstanceState);

        buttonTambahKategori = (Button) getActivity().findViewById(R.id.buttonTambahKategori);

        buttonTambahKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TambahKategoriActivity.class);
                getActivity().startActivityForResult(intent,getResources().getInteger(R.integer.category_new_rq_code));
            }
        });

        recList = (RecyclerView) getView().findViewById(R.id.listViewKategori);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        setList();
    }
    public void setList(){
        listOfCategories = new ArrayList<>();
        dbListKategori.execSQL("Create table if not exists category(name VARCHAR, description VARCHAR);");
        Cursor categories = dbListKategori.rawQuery("select * from category",null);
        if(categories != null)
            if(categories.moveToFirst())
                do{
                    listOfCategories.add(new Kategori(categories.getString(0),categories.getString(1)));
                }while(categories.moveToNext());
//        listOfCategories.add(new Kategori("nama1","deskripsi1"));
//        listOfCategories.add(new Kategori("nama2","deskripsi2"));

        adapter = new ListKategoriAdapter(getActivity(),getContext(),listOfCategories);
        adapter.setOnItemClickListener(new ListKategoriAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DetailKategoriActivity.class);
                intent.putExtra("Kategori", listOfCategories.get(position));
                getActivity().startActivityForResult(intent,getResources().getInteger(R.integer.category_update_rq_code));
            }
        });
        recList.setAdapter(adapter);
    }
}
