package com.example.priansyah.demo1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priansyah.demo1.Adapter.ListItemAdapter;
import com.example.priansyah.demo1.Entity.Item;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Priansyah on 2/26/2016.
 */
public class KonfirmasiActivity extends AppCompatActivity {
    Double discountKonfirmasi;
    RecyclerView recList;
    ArrayList<Item> listOfItemsKfm;
    ListItemAdapter adapter;
    Button buttonLanjutKonfirmasi;
    int total;
    TextView textViewDiskonKfm;
    TextView textViewTotalKfm;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi);

        textViewDiskonKfm = (TextView) findViewById(R.id.textViewDiskonKfm);
        textViewTotalKfm = (TextView) findViewById(R.id.textViewTotalKfm);

        discountKonfirmasi = (Double) getIntent().getSerializableExtra("discount");

        recList = (RecyclerView) findViewById(R.id.listViewItemKonfirmasi);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        listOfItemsKfm = getIntent().getParcelableArrayListExtra("LIST");
        int t = 20000;
        total = t - (int)(discountKonfirmasi*t);
        discountKonfirmasi= 100*discountKonfirmasi;

        adapter = new ListItemAdapter(this, getBaseContext(), listOfItemsKfm);
        recList.setAdapter(adapter);

        textViewDiskonKfm.setText(String.format("%.0f%%", discountKonfirmasi));
        textViewTotalKfm.setText("" + total);


        buttonLanjutKonfirmasi = (Button) findViewById(R.id.buttonLanjutKonfirmasi);
        buttonLanjutKonfirmasi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText paymentView = (EditText) findViewById(R.id.nominal);
                Log.v("Payment", "" + Integer.parseInt(paymentView.getText().toString()));
                int paymentValue = Integer.parseInt(paymentView.getText().toString());
                Intent intent = new Intent(getBaseContext(), ReceiptActivity.class);
                intent.putExtra("LIST", listOfItemsKfm);
                intent.putExtra("TOTAL", total);
                intent.putExtra("PAYMENT_VALUE", paymentValue);
                startActivity(intent);
            }
        });
    }
}
