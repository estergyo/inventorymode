package com.example.priansyah.demo1;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.priansyah.demo1.Adapter.ListItemAdapter;
import com.example.priansyah.demo1.Adapter.SearchItemAdapter;
import com.example.priansyah.demo1.Entity.Item;

import java.util.ArrayList;

/**
 * Created by Priansyah on 2/26/2016.
 */
public class TransaksiActivity extends AppCompatActivity {

    RecyclerView recList;
    ListItemAdapter adapter;
    Button buttonTambahItemTr;
    Button buttonLanjutTransaksi;

    ArrayList<Item> listOfItemsTr;
    SQLiteDatabase dbSearchItem;
    SQLiteDatabase dbListItemTr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarTransaksi);
        setSupportActionBar(myToolbar);

        buttonTambahItemTr = (Button) findViewById(R.id.buttonTambahItemTr);

        buttonTambahItemTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TransaksiActivity.this, "tambah clicked", Toast.LENGTH_SHORT).show();
            }
        });

        recList = (RecyclerView) findViewById(R.id.listViewItemTransaksi);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        setList();

        buttonLanjutTransaksi = (Button) findViewById(R.id.buttonLanjutTransaksi);
        buttonLanjutTransaksi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), DiskonActivity.class);
                intent.putExtra("LIST", listOfItemsTr);
                startActivity(intent);
            }
        });
        dbSearchItem = openOrCreateDatabase("POS", MODE_PRIVATE, null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_transaksi, menu);

        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setSuggestionsAdapter(new SearchItemAdapter(this, dbSearchItem));
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                Toast.makeText(TransaksiActivity.this, "Position: " + position, Toast.LENGTH_SHORT).show();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Toast.makeText(TransaksiActivity.this, query, Toast.LENGTH_SHORT).show();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    public void setList(){
        listOfItemsTr = new ArrayList<>();
        dbListItemTr.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
        Cursor items = dbListItemTr.rawQuery("select * from stock",null);
        if(items != null)
            if(items.moveToFirst())
                do{
                    listOfItemsTr.add(new Item(items.getString(1),items.getString(0),""+items.getInt(2),""+items.getInt(3),items.getString(5),items.getString(4),items.getString(6)));
                }while(items.moveToNext());
        listOfItemsTr.add(new Item("nama1","sku1","jumlah1","harga1","supplier1","kategori1","image1"));
        listOfItemsTr.add(new Item("nama2", "sku2", "jumlah2", "harga2", "supplier2", "kategori2","image2"));
//        dbListDetailTr.execSQL("Create table if not exists transaction_detail(id INT, transaction_id INT,product_id INT, discount_id INT, state_id INT, coupon VARCHAR, price VARCHAR, note VARCHAR, created_by INT, date_created VARCHAR, modified_by INT, date_modified TIMESTAMP);");


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
