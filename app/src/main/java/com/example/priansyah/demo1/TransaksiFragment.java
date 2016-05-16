package com.example.priansyah.demo1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.priansyah.demo1.Adapter.ListDetilTransaksiAdapter;
import com.example.priansyah.demo1.Adapter.SearchItemAdapter;
import com.example.priansyah.demo1.Entity.Item;
import com.example.priansyah.demo1.Entity.TransDetail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Priansyah on 3/21/2016.
 */
public class TransaksiFragment extends Fragment {
    RecyclerView recList;
    ListDetilTransaksiAdapter adapter;
    Button buttonTambahItemTr;
    Button buttonLanjutTransaksi;

    ArrayList<TransDetail> listOfTransactionDetail;
    SQLiteDatabase dbSearchItem;
    SQLiteDatabase dbListItemTr;
    SearchView search;
    SimpleCursorAdapter searchCursorAdapter;
    private static final String[] columns = { "_id" , "sku" , "image" , "nama" , "harga" };
    int amount = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaksi, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listOfTransactionDetail = new ArrayList<>();

        buttonTambahItemTr = (Button) getActivity().findViewById(R.id.buttonTambahItemTr);

        buttonTambahItemTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), PilihItemTransaksiActivity.class);
                startActivityForResult(intent, getResources().getInteger(R.integer.item_transaksi_rq_code));
            }
        });

        dbListItemTr = getActivity().openOrCreateDatabase("POS", getActivity().MODE_PRIVATE, null);
        dbListItemTr.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");

//        dbListItemTr.execSQL("DROP TABLE IF EXISTS transaction_detail");
//        dbListItemTr.execSQL("DROP TABLE IF EXISTS stock_transaction");



        recList = (RecyclerView) getActivity().findViewById(R.id.listViewItemTransaksi);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        buttonLanjutTransaksi = (Button) getActivity().findViewById(R.id.buttonLanjutTransaksi);
        buttonLanjutTransaksi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listOfTransactionDetail.size() == 0) {
                    Toast.makeText(getActivity(), "Tidak ada barang yang dipilih", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getActivity().getBaseContext(), DiskonActivity.class);
                    intent.putExtra("TRANSDETLIST", listOfTransactionDetail);
                    startActivity(intent);
                }
            }
        });
        dbSearchItem = getActivity().openOrCreateDatabase("POS", getActivity().MODE_PRIVATE, null);

        search = (SearchView) getActivity().findViewById(R.id.searchViewTransaksi);
        search.setQueryHint("Search...");
        search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return suggestionClick(position);
            }

            @Override
            public boolean onSuggestionClick(int position) {
                return suggestionClick(position);
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {
                    MatrixCursor matrixCursor = convertToCursor(query);
                    searchCursorAdapter.changeCursor(matrixCursor);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    MatrixCursor matrixCursor = convertToCursor(newText);
                    searchCursorAdapter.changeCursor(matrixCursor);
                }
                return true;
            }
        });
        searchCursorAdapter = new SearchItemAdapter(getActivity(), columns);
        search.setSuggestionsAdapter(searchCursorAdapter);
    }

    private MatrixCursor convertToCursor(String constraint) {
        MatrixCursor cursor = new MatrixCursor(columns);
        dbSearchItem.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
        Cursor itms = dbSearchItem.rawQuery("select * from stock",null);
        ArrayList<Item> mResults = new ArrayList<Item>();
        if(itms != null)
            if(itms.moveToFirst())
                do{
                    if(!TextUtils.isEmpty(constraint)){
                        String constraintString = constraint.toString().toLowerCase(Locale.ROOT);
                        if(!itms.getString(1).toLowerCase(Locale.ROOT).startsWith(constraintString)){
                        }else {
                            mResults.add(new Item(itms.getString(1), itms.getString(0), "" + itms.getInt(2), "" + itms.getInt(3), itms.getString(5), itms.getString(4), itms.getString(6)));
                        }
                    }
                }while(itms.moveToNext());
        int i = 0;
        for (Item item : mResults) {
            String[] temp = new String[5];
            i = i + 1;
            temp[0] = Integer.toString(i);
            temp[1] = item.getTextSKU();
            temp[2] = item.getTextNama();
            temp[3] = item.getTextHarga();
            temp[4] = item.getTextImage();
            cursor.addRow(temp);
        }
        return cursor;
    }

    public boolean suggestionClick(int position){
        Cursor cursor = (Cursor) search.getSuggestionsAdapter().getItem(position);
        String nama = cursor.getString(2);
        search.setQuery(nama, false);
        search.clearFocus();

        dbListItemTr.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
        Cursor items = dbListItemTr.rawQuery("select * from stock where sku = '"+cursor.getString(1)+"' ",null);
        Calendar c = Calendar.getInstance();
        String date = c.get(Calendar.DATE) +"/" + c.get(Calendar.MONTH) +"/" + c.get(Calendar.YEAR);

        if(items != null)
            if(items.moveToFirst()) {
                Boolean isAvailable = false;
                for (int i=0; i<listOfTransactionDetail.size(); i++) {
                    if (listOfTransactionDetail.get(i).getTextSKU().equals(cursor.getString(1))) {
                        int jumlah =Integer.parseInt(listOfTransactionDetail.get(i).getTextJumlah());
                        listOfTransactionDetail.get(i).setTextJumlah(""+(jumlah+1));
                        isAvailable = true;
                        break;
                    }
                }
                if (!isAvailable)
                    listOfTransactionDetail.add(new TransDetail("" + (listOfTransactionDetail.size()+1), items.getString(0), items.getString(1), "" + amount, "" + items.getInt(3) * amount, date));
            }
        setList();

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == getResources().getInteger(R.integer.item_transaksi_rq_code))
            if(resultCode == Activity.RESULT_OK){
                Calendar c = Calendar.getInstance();
                String date = c.get(Calendar.DATE) +"/" + c.get(Calendar.MONTH) +"/" + c.get(Calendar.YEAR);
                ArrayList<Item> listItem = intent.getParcelableArrayListExtra("PILIHITEMLIST");
                for (int i=0; i<listItem.size(); i++) {
                    int jml = Integer.parseInt(listItem.get(i).getTextJumlah().toString());
                    if(jml != 0) {
                        boolean isItem = false;
                        for (int j = 0; j < listOfTransactionDetail.size(); j++) {
                            if (listItem.get(i).getTextSKU().equals(listOfTransactionDetail.get(j).getTextSKU())) {
                                isItem = true;
                                listOfTransactionDetail.get(j).setTextJumlah((jml + Integer.parseInt(listOfTransactionDetail.get(j).getTextJumlah())) + "");
                                listOfTransactionDetail.get(j).setTextHarga("" + (jml * Integer.parseInt(listOfTransactionDetail.get(j).getTextHarga())));
                                break;
                            }
                        }
                        if (!isItem)
                            listOfTransactionDetail.add(new TransDetail("" + (listOfTransactionDetail.size() + 1), listItem.get(i).getTextSKU(), listItem.get(i).getTextNama(), "" + listItem.get(i).getTextJumlah(), "" + (Integer.parseInt(listItem.get(i).getTextHarga()) * Integer.parseInt(listItem.get(i).getTextJumlah())), date));
                    }
                }
                setList();
            }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        // Inflate menu to add items to action bar if it is present.
//        inflater.inflate(R.menu.menu_transaksi, menu);
//
//        return true;
//    }
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu)
//    {
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        final SearchView searchView =
//                (SearchView) menu.findItem(R.id.menu_search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//
//        searchView.setSuggestionsAdapter(new SearchItemAdapter(this, dbSearchItem));
//        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
//            @Override
//            public boolean onSuggestionClick(int position) {
//                Toast.makeText(TransaksiActivity.this, "Position: " + position, Toast.LENGTH_SHORT).show();
//                searchView.clearFocus();
//                return true;
//            }
//
//            @Override
//            public boolean onSuggestionSelect(int position) {
//                return false;
//            }
//        });
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
//        {
//            @Override
//            public boolean onQueryTextSubmit(String query)
//            {
//                Toast.makeText(TransaksiActivity.this, query, Toast.LENGTH_SHORT).show();
//                searchView.clearFocus();
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText)
//            {
//                return false;
//            }
//        });
//        return super.onPrepareOptionsMenu(menu);
//    }

    public void setList(){
        adapter = new ListDetilTransaksiAdapter(getActivity(), getContext(), listOfTransactionDetail);
//        adapter.setOnItemClickListener(new ListItemAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(getActivity(), DetailItemActivity.class);
//                intent.putExtra("Item", listOfItems.get(position));
//                getActivity().startActivityForResult(intent, getResources().getInteger(R.integer.item_update_rq_code));
//            }
//        });
        recList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recList.setAdapter(adapter);
    }
}
