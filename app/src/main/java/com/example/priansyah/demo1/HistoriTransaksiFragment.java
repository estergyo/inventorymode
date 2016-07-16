package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.FilterQueryProvider;
import android.widget.TextView;

import com.example.priansyah.demo1.Adapter.ExpandableListTransaksiAdapter;
import com.example.priansyah.demo1.Adapter.ListItemAdapter;
import com.example.priansyah.demo1.Adapter.ListTransaksiAdapter;
import com.example.priansyah.demo1.Adapter.SearchItemAdapter;
import com.example.priansyah.demo1.Adapter.SearchTransaksiAdapter;
import com.example.priansyah.demo1.Entity.Item;
import com.example.priansyah.demo1.Entity.TransDetail;
import com.example.priansyah.demo1.Entity.Transaksi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Priansyah on 3/16/2016.
 */
public class HistoriTransaksiFragment extends Fragment {
    TextView history_empty_view;
    RecyclerView recList;
    ExpandableListTransaksiAdapter adapter;

    List<ExpandableListTransaksiAdapter.Item> data;
    SQLiteDatabase dbListTransaksi;
    SQLiteDatabase dbSearchTransaksi;
    AutoCompleteTextView search;
    SimpleCursorAdapter searchCursorAdapter;
    private static final String[] columns = { "_id" , "idtrans", "harga", "tanggal" };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_histori_transaksi, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dbListTransaksi = getActivity().openOrCreateDatabase("POS", getActivity().MODE_PRIVATE, null);
        super.onViewCreated(view, savedInstanceState);

        history_empty_view = (TextView) getActivity().findViewById(R.id.history_empty_view);
        recList = (RecyclerView) getView().findViewById(R.id.listViewTransaksi);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        setList();

        dbSearchTransaksi = getActivity().openOrCreateDatabase("POS", getActivity().MODE_PRIVATE, null);

        search = (AutoCompleteTextView) getActivity().findViewById(R.id.searchViewHistoriTransaksi);
        search.setHint("Search...");
        search.setThreshold(1);
        searchCursorAdapter = new SearchTransaksiAdapter(getActivity(), columns);
        searchCursorAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(cursor.getColumnIndex("idtrans"));
            }
        });
        search.setAdapter(searchCursorAdapter);

        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                suggestionClick((Cursor) parent.getItemAtPosition(position));
            }
        });
        searchCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return convertToCursor(constraint.toString());
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString();
                if (query.length() > 0) {
                    searchCursorAdapter.getFilter().filter(s.toString());
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from", 1);
                startActivity(intent);
            }
        });
    }

    private MatrixCursor convertToCursor(String constraint) {
        MatrixCursor cursor = new MatrixCursor(columns);
        dbSearchTransaksi.execSQL("Create table if not exists stock_transaction(transaction_id INT, total INT, discount INT, tax VARCHAR, date_created VARCHAR, payment INT, time_created VARCHAR);");
        Cursor itms = dbSearchTransaksi.rawQuery("select * from stock_transaction",null);
        ArrayList<Transaksi> mResults = new ArrayList<Transaksi>();
        if(itms != null)
            if(itms.moveToFirst())
                do{
                    if(!TextUtils.isEmpty(constraint)){
                        String constraintString = constraint.toString().toLowerCase(Locale.ROOT);
                        String help = ""+itms.getInt(0);
                        if(!help.toLowerCase(Locale.ROOT).startsWith(constraintString)){
                        }else {
                            mResults.add(new Transaksi(""+itms.getInt(0),""+itms.getInt(1),""+itms.getInt(2),itms.getString(3),itms.getString(4), ""+itms.getInt(5), itms.getString(6)));
                        }
                    }
                }while(itms.moveToNext());
        for (int i = 0; i < mResults.size(); i++) {
            Transaksi item = mResults.get(i);
            cursor.addRow(item.convertToRow(i));
        }
        return cursor;
    }

    public boolean suggestionClick(Cursor cursor) {
        search.clearFocus();

        dbSearchTransaksi.execSQL("Create table if not exists stock_transaction(transaction_id INT, total INT, discount INT, tax VARCHAR, date_created VARCHAR, payment INT, time_created VARCHAR);");
        String idHelper = cursor.getString(1).substring(1);

        Cursor items = dbSearchTransaksi.rawQuery("select * from stock_transaction where transaction_id = '" + idHelper + "' ", null);
        Intent intent = new Intent(getActivity(), DetailTransaksiActivity.class);
        if (items != null) {
            if (items.moveToFirst()) {
                Transaksi transaksi = new Transaksi("" + items.getInt(0), "" + items.getInt(1), "" + items.getInt(2), items.getString(3), items.getString(4), "" + items.getInt(5), items.getString(6));
                intent.putExtra("Transaksi", transaksi);
            }
        }
        startActivity(intent);
        return true;
    }

    public void setList(){
//        listOfTransaction = new ArrayList<>();
        data = new ArrayList<>();
        dbListTransaksi.execSQL("Create table if not exists stock_transaction(transaction_id INT, total INT, discount INT, tax VARCHAR, date_created VARCHAR, payment INT, time_created VARCHAR);");
        Cursor items = dbListTransaksi.rawQuery("select distinct date_created from stock_transaction",null);
        Cursor items2 = dbListTransaksi.rawQuery("select * from stock_transaction",null);
        if(items != null)
            if(items.moveToFirst())
                do{
//                    listOfTransaction.add(new Transaksi(""+items.getInt(0),""+items.getInt(1),""+items.getInt(2),items.getString(3),items.getString(4), ""+items.getInt(5)));
                    data.add(new ExpandableListTransaksiAdapter.Item(ExpandableListTransaksiAdapter.HEADER, new Transaksi(null, null, null, null,items.getString(0), null, null)));
                    if(items2 != null)
                        if(items2.moveToFirst())
                            do {
                                if (items.getString(0).equals(items2.getString(4)))
                                    data.add(new ExpandableListTransaksiAdapter.Item(ExpandableListTransaksiAdapter.CHILD, new Transaksi(""+items2.getInt(0),""+items2.getInt(1),""+items2.getInt(2),items2.getString(3),items2.getString(4), ""+items2.getInt(5), items2.getString(6))));
                            } while (items2.moveToNext());
                }while(items.moveToNext());

        adapter = new ExpandableListTransaksiAdapter(data);
        if (adapter.getItemCount() == 0) {
            recList.setVisibility(View.GONE);
            history_empty_view.setVisibility(View.VISIBLE);
        }
        else {
            recList.setVisibility(View.VISIBLE);
            history_empty_view.setVisibility(View.GONE);
        }
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
