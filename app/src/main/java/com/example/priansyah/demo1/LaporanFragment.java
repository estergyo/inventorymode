package com.example.priansyah.demo1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.priansyah.demo1.Adapter.ListKategoriAdapter;
import com.example.priansyah.demo1.Adapter.ListLaporanAdapter;
import com.example.priansyah.demo1.Entity.TransDetail;

import java.util.ArrayList;

/**
 * Created by ester gyo fanny on 28/05/2016.
 */
public class LaporanFragment extends Fragment {
    ArrayList<String> titles;
    RecyclerView recList;
    ListLaporanAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_laporan, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titles = new ArrayList<>();
        titles.add("Penjualan Harian");
        titles.add("Menampilkan ringkasan penjualan harian berdasarkan tanggal");
        titles.add("Penjualan Berdasarkan Kategori");
        titles.add("Menampilkan ringkasan penjualan harian berdasarkan kategori produk");
        titles.add("Pembayaran Harian");
        titles.add("Menampilkan ringkasan pembayaran harian berdasarkan tanggal");
        titles.add("Penjualan Teratas");
        titles.add("Menampilkan daftar produk terlaris");
        titles.add("Inventaris");
        titles.add("Menampilkan ringkasan stok barang yang tersedia");
        titles.add("Transaksi");
        titles.add("Menampilkan transaksi inventaris");
        titles.add("Profit");
        titles.add("Menampilkan profit penjualan");
        titles.add("Grafik Profit");
        titles.add("Menampilkan grafik keuntungan penjualan");
        titles.add("Struk Harian/Pembelian");
        titles.add("Menampilkan struk penjualan/pembelian harian berdasarkan tanggal");

        recList = (RecyclerView) getActivity().findViewById(R.id.listViewLaporan);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(adapter);
        recList.setHasFixedSize(true);
        adapter = new ListLaporanAdapter(getActivity(),getContext(),titles);
        recList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recList.setAdapter(adapter);
    }
}
