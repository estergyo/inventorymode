package com.example.priansyah.demo1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
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
public class ReceiptActivity extends AppCompatActivity {
    RecyclerView recList;
    ArrayList<Item> listOfItemsRcp;
    ListItemAdapter adapter;
    TextView textViewBayarRcp;
    TextView textViewKembalianRcp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        textViewBayarRcp = (TextView) findViewById(R.id.textViewBayarRcp);
        textViewKembalianRcp = (TextView) findViewById(R.id.textViewKembalianRcp);

        recList = (RecyclerView) findViewById(R.id.listViewItemReceipt);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        int paymentValue = (int) getIntent().getSerializableExtra("PAYMENT_VALUE");
        listOfItemsRcp = getIntent().getParcelableArrayListExtra("LIST");

        adapter = new ListItemAdapter(this, getBaseContext(), listOfItemsRcp);
        recList.setAdapter(adapter);
        int total = (int) getIntent().getSerializableExtra("TOTAL");
        int change = paymentValue-total;
        textViewBayarRcp.setText("" + paymentValue);
        textViewKembalianRcp.setText("" + change);

    }
}
