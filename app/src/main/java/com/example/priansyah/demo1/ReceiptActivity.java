package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priansyah.demo1.Adapter.ListDetilTransaksiAdapter;
import com.example.priansyah.demo1.Adapter.ListItemAdapter;
import com.example.priansyah.demo1.Entity.Item;
import com.example.priansyah.demo1.Entity.TransDetail;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Priansyah on 2/26/2016.
 */
public class ReceiptActivity extends AppCompatActivity {
    RecyclerView recList;
    ArrayList<TransDetail> listOfTransactionDetail;
    ListDetilTransaksiAdapter adapter;
    TextView textViewBayarRcp;
    TextView textViewKembalianRcp;
    Button buttonKirim;
    double total;
    double paymentValue;
    double change;
    SQLiteDatabase dbTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        textViewBayarRcp = (TextView) findViewById(R.id.textViewBayarRcp);
        textViewKembalianRcp = (TextView) findViewById(R.id.textViewKembalianRcp);
        buttonKirim = (Button) findViewById(R.id.buttonKirim);

        recList = (RecyclerView) findViewById(R.id.listViewItemReceipt);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        listOfTransactionDetail = new ArrayList<>();

        paymentValue = (int) getIntent().getSerializableExtra("PAYMENT_VALUE");
        listOfTransactionDetail = getIntent().getParcelableArrayListExtra("TRANSDETLIST");
        dbTransaction = openOrCreateDatabase("POS", MODE_PRIVATE, null);
//        dbTransaction.execSQL("Create table if not exists transaction_detail(trans_detail_id INT, transaction_id INT, product_sku INT, price VARCHAR, date_created VARCHAR);");
//        Cursor items = dbTransaction.rawQuery("select * from transaction_detail",null);
//        if(items != null)
//            if(items.moveToFirst())
//                do{
//                    listOfTransactionDetail.add(new TransDetail(items.getString(0),""+items.getInt(1), items.getString(1),""+items.getInt(2),items.getString(3),items.getString(4)));
//                }while(items.moveToNext());

        adapter = new ListDetilTransaksiAdapter(this, getBaseContext(), listOfTransactionDetail);
        recList.setAdapter(adapter);

        total = getIntent().getDoubleExtra("TOTAL",0.0);
        change = paymentValue-total;
        textViewBayarRcp.setText("" + paymentValue);
        textViewKembalianRcp.setText("" + change);

        buttonKirim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), KirimReceiptActivity.class);
//                intent.putExtra("LIST", listOfItemsRcp);
                intent.putExtra("TOTAL", total);
                intent.putExtra("PAYMENT_VALUE", paymentValue);
                intent.putExtra("TRANSDETLIST", listOfTransactionDetail);
                intent.putExtra("CHANGE", change);
                startActivity(intent);
            }
        });

    }
}
