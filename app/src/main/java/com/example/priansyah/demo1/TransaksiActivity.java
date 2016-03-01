package com.example.priansyah.demo1;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.priansyah.demo1.Adapter.ListItemAdapter;
import com.example.priansyah.demo1.Adapter.SearchItemAdapter;
import com.example.priansyah.demo1.Entity.Item;
import com.example.priansyah.demo1.Entity.TransDetail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Priansyah on 2/26/2016.
 */
public class TransaksiActivity extends AppCompatActivity {

    RecyclerView recList;
    ListItemAdapter adapter;
    Button buttonTambahItemTr;
    Button buttonLanjutTransaksi;

    ArrayList<Item> listOfItemsTr;
    ArrayList<TransDetail> listOfTransactionDetail;
    SQLiteDatabase dbSearchItem;
    SQLiteDatabase dbListItemTr;
    SQLiteDatabase dbTransactionDetail;
    SearchView search;
    SimpleCursorAdapter searchCursorAdapter;
    private static final String[] columns = { "_id" , "sku" , "image" , "nama" , "harga" };
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarTransaksi);
        setSupportActionBar(myToolbar);

        listOfItemsTr = new ArrayList<>();
        listOfTransactionDetail = new ArrayList<>();

        buttonTambahItemTr = (Button) findViewById(R.id.buttonTambahItemTr);

        buttonTambahItemTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TransaksiActivity.this, "tambah clicked", Toast.LENGTH_SHORT).show();
            }
        });

        dbListItemTr = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        dbListItemTr.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
        dbTransactionDetail = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        dbTransactionDetail.execSQL("Create table if not exists transaction_detail(transaction_id INT, product_sku INT, price VARCHAR, date_created VARCHAR);");


        recList = (RecyclerView) findViewById(R.id.listViewItemTransaksi);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        buttonLanjutTransaksi = (Button) findViewById(R.id.buttonLanjutTransaksi);
        buttonLanjutTransaksi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), DiskonActivity.class);
                intent.putExtra("LIST", listOfItemsTr);
                intent.putExtra("TRANSDETLIST", listOfTransactionDetail);
                startActivity(intent);
            }
        });
        dbSearchItem = openOrCreateDatabase("POS", MODE_PRIVATE, null);

        search = (SearchView) findViewById(R.id.searchViewTransaksi);
        search.setQueryHint("Search...");
        search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Cursor cursor = (Cursor) search.getSuggestionsAdapter().getItem(position);
                String nama = cursor.getString(2);
                search.setQuery(nama, false);
                setList(cursor.getString(1));
                search.clearFocus();
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) search.getSuggestionsAdapter().getItem(position);
                String nama = cursor.getString(2);
                search.setQuery(nama, false);
                setList(cursor.getString(1));
                search.clearFocus();
                return true;
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
        searchCursorAdapter = new SearchItemAdapter(this, columns);
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

    public void setList(String sku){
        dbListItemTr.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
        Cursor items = dbListItemTr.rawQuery("select * from stock where sku = '"+sku+"' ",null);
        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DATE);
        if(items != null)
            if(items.moveToFirst()) {
                listOfItemsTr.add(new Item(items.getString(1), items.getString(0), "" + items.getInt(2), "" + items.getInt(3), items.getString(5), items.getString(4), items.getString(6)));
                listOfTransactionDetail.add(new TransDetail("" + counter, items.getString(1), "" + items.getInt(3), "" + date));
                counter++;
            }

        adapter = new ListItemAdapter(this, getBaseContext(), listOfItemsTr);
//        adapter.setOnItemClickListener(new ListItemAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(getActivity(), DetailItemActivity.class);
//                intent.putExtra("Item", listOfItems.get(position));
//                getActivity().startActivityForResult(intent, getResources().getInteger(R.integer.item_update_rq_code));
//            }
//        });
        recList.setAdapter(adapter);
    }
}
