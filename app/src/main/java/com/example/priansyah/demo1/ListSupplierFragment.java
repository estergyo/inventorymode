package com.example.priansyah.demo1;

import android.app.Activity;
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
import com.example.priansyah.demo1.Adapter.ListSupplierAdapter;
import com.example.priansyah.demo1.Entity.Item;
import com.example.priansyah.demo1.Entity.Kategori;
import com.example.priansyah.demo1.Entity.Supplier;

import java.util.ArrayList;

/**
 * Created by Priansyah on 1/22/2016.
 */
public class ListSupplierFragment extends Fragment {

	RecyclerView recList;
    ListSupplierAdapter adapter;
    Button buttonTambahSupplier;

    ArrayList<Supplier> listOfSuppliers;
    SQLiteDatabase dbListSupplier;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_supplier, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dbListSupplier = getActivity().openOrCreateDatabase("POS", getActivity().MODE_PRIVATE, null);
        super.onViewCreated(view, savedInstanceState);

        buttonTambahSupplier = (Button) getActivity().findViewById(R.id.buttonTambahSupplier);

        buttonTambahSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TambahSupplierActivity.class);
                getParentFragment().startActivityForResult(intent, getResources().getInteger(R.integer.supplier_new_rq_code));
            }
        });

        recList = (RecyclerView) getView().findViewById(R.id.listViewSupplier);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        setList();

    }
    public void setList(){
        listOfSuppliers = new ArrayList<>();
        dbListSupplier.execSQL("Create table if not exists supplier(name VARCHAR, address VARCHAR, contact INTEGER);");
        Cursor suppliers = dbListSupplier.rawQuery("select * from supplier",null);
        if(suppliers != null)
            if(suppliers.moveToFirst())
                do{
                    listOfSuppliers.add(new Supplier(suppliers.getString(0),suppliers.getString(1),""+suppliers.getInt(2)));
                }while(suppliers.moveToNext());
//        listOfSuppliers.add(new Supplier("nama1","alamat1","kontak1"));
//        listOfSuppliers.add(new Supplier("nama2","alamat2","kontak2"));

        adapter = new ListSupplierAdapter(getActivity(),getContext(),listOfSuppliers);
        adapter.setOnItemClickListener(new ListSupplierAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DetailSupplierActivity.class);
                intent.putExtra("Supplier", listOfSuppliers.get(position));
                getParentFragment().startActivityForResult(intent, getResources().getInteger(R.integer.supplier_update_rq_code));
            }
        });

        recList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recList.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode,resultCode,intent);
    }
}
