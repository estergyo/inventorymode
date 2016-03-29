package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.priansyah.demo1.Adapter.ListItemAdapter;
import com.example.priansyah.demo1.Entity.Item;

import java.util.ArrayList;

/**
 * Created by Priansyah on 3/7/2016.
 */
public class PilihItemTransaksiActivity extends AppCompatActivity {
    RecyclerView recList;
    ListItemAdapter adapter;

    ArrayList<Item> listOfItems;
    SQLiteDatabase dbListItem;
    Button buttonTambahKeList;
    View popupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_item_transaksi);

        dbListItem = openOrCreateDatabase("POS",MODE_PRIVATE, null);
        recList = (RecyclerView) findViewById(R.id.listViewItem);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        listOfItems = new ArrayList<>();
        dbListItem.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
        Cursor items = dbListItem.rawQuery("select * from stock",null);
        if(items != null)
            if(items.moveToFirst())
                do{
                    listOfItems.add(new Item(items.getString(1),items.getString(0),""+0,""+items.getInt(3),items.getString(5),items.getString(4),items.getString(6)));
                }while(items.moveToNext());

        setList();

        buttonTambahKeList = (Button) findViewById(R.id.buttonTambahKeList);
        buttonTambahKeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("PILIHITEMLIST", listOfItems);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void setList(){
        adapter = new ListItemAdapter(this, getBaseContext(),listOfItems);
        adapter.setOnItemClickListener(new ListItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                LayoutInflater layoutInflater
                        = (LayoutInflater)getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.popup_pilih_item_transaksi, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.update();

                Button buttonBatalPopup = (Button)popupView.findViewById(R.id.buttonBatalPopup);
                buttonBatalPopup.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }
                });

                Button buttonLanjutPopup = (Button)popupView.findViewById(R.id.buttonLanjutPopup);
                buttonLanjutPopup.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        EditText editTextJumlahItemTransaksi = (EditText)popupView.findViewById(R.id.editTextJumlahItemTransaksi);
                        int jumlah = Integer.parseInt(editTextJumlahItemTransaksi.getText().toString());
                        listOfItems.get(position).setTextJumlah((Integer.parseInt(listOfItems.get(position).getTextJumlah()) + jumlah) +"");
                        setList();
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAsDropDown(view, 50, -30);
            }
        });
        recList.setAdapter(adapter);
    }
}
