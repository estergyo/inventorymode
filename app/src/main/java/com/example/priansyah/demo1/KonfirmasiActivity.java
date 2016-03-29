package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.priansyah.demo1.Adapter.ListDetilTransaksiAdapter;
import com.example.priansyah.demo1.Adapter.ListItemAdapter;
import com.example.priansyah.demo1.Entity.Item;
import com.example.priansyah.demo1.Entity.TransDetail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Priansyah on 2/26/2016.
 */
public class KonfirmasiActivity extends AppCompatActivity {
    Double discountKonfirmasi;
    RecyclerView recList;
    ArrayList<TransDetail> listOfTransactionDetail;
    ListDetilTransaksiAdapter adapter;
    Button buttonLanjutKonfirmasi;
    double total;
    TextView textViewDiskonKfm;
    TextView textViewTotalKfm;
    TextView textViewTanggal;
    SQLiteDatabase dbTransaction;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi);

        textViewDiskonKfm = (TextView) findViewById(R.id.textViewDiskonKfm);
        textViewTotalKfm = (TextView) findViewById(R.id.textViewTotalKfm);
        textViewTanggal = (TextView) findViewById(R.id.textViewTanggal);

        discountKonfirmasi = (Double) getIntent().getSerializableExtra("discount");
        listOfTransactionDetail = getIntent().getParcelableArrayListExtra("TRANSDETLIST");

        textViewTanggal.setText(listOfTransactionDetail.get(listOfTransactionDetail.size()-1).getTextTanggalTrans());

        recList = (RecyclerView) findViewById(R.id.listViewItemKonfirmasi);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

//        listOfItemsKfm = getIntent().getParcelableArrayListExtra("LIST");

        double harga = 0.0;
        for (int i=0; i<listOfTransactionDetail.size(); i++) {
            harga += Integer.parseInt(listOfTransactionDetail.get(i).getTextHarga());
        }

        total = harga - (discountKonfirmasi * harga) + (0.1 * total);
        discountKonfirmasi= 100*discountKonfirmasi;

        adapter = new ListDetilTransaksiAdapter(this, getBaseContext(), listOfTransactionDetail);
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
//                intent.putExtra("LIST", listOfItemsKfm);
                intent.putExtra("TOTAL", total);
                intent.putExtra("PAYMENT_VALUE", paymentValue);
                intent.putExtra("TRANSDETLIST", listOfTransactionDetail);

                dbTransaction = openOrCreateDatabase("POS", MODE_PRIVATE, null);
                dbTransaction.execSQL("Create table if not exists transaction_detail(trans_detail_id INT, transaction_id INT, product_sku INT, price VARCHAR, date_created VARCHAR);");
                dbTransaction.execSQL("Create table if not exists stock_transaction(transaction_id INT, discount INT, tax VARCHAR, date_created VARCHAR);");

                double tax = (0.1 * total);
                Calendar c = Calendar.getInstance();
                String date = c.get(Calendar.DATE) +"/" + c.get(Calendar.MONTH) +"/" + c.get(Calendar.YEAR);

                Cursor maxTrans = dbTransaction.rawQuery("SELECT MAX(transaction_id) FROM stock_transaction", null);
                Cursor maxTransDetail = dbTransaction.rawQuery("SELECT MAX(transaction_id) FROM stock_transaction", null);
                boolean isCursorToFirst = false;

                if(maxTrans!=null)
                    if(maxTrans.moveToFirst()){
                        isCursorToFirst = true;
                        if (maxTrans.getInt(0)==-1) {
                            dbTransaction.execSQL("insert into stock_transaction values(1, '" + discountKonfirmasi + "', '" + tax + "', '" + date + "')");
                            maxTrans = dbTransaction.rawQuery("SELECT MAX(transaction_id) FROM stock_transaction", null);
                        }
                        else {
                            if(maxTrans.moveToFirst())
                                dbTransaction.execSQL("insert into stock_transaction values('"+(maxTrans.getInt(0)+1)+"', '"+discountKonfirmasi+"', '"+tax+"', '"+date+"')");
                        }
                    }

                if(maxTransDetail!=null)
                    if(maxTransDetail.moveToFirst()){
                        if (maxTransDetail.getInt(0)==-1) {
                            if(!isCursorToFirst)
                                maxTrans.moveToFirst();
                            for (int i=1; i<listOfTransactionDetail.size()+1; i++){
                                dbTransaction.execSQL("insert into transaction_detail values('"+i+"', '"+maxTrans.getInt(0)+"', '"+listOfTransactionDetail.get(i-1).getTextSKU()+"', '"+listOfTransactionDetail.get(i-1).getTextHarga()+"', '"+date+"')");
                            }
                        }
                        else {
                            if(!isCursorToFirst)
                                maxTrans.moveToFirst();
                            if(maxTransDetail.moveToFirst())
                                for (int i=1; i<=listOfTransactionDetail.size(); i++){
                                    dbTransaction.execSQL("insert into transaction_detail values('"+(maxTransDetail.getInt(0)+i)+"', '"+maxTrans.getInt(0)+"', '"+listOfTransactionDetail.get(i-1).getTextSKU()+"', '"+listOfTransactionDetail.get(i-1).getTextHarga()+"', '"+date+"')");
                                }
                        }
                    }



                startActivity(intent);
            }
        });
    }
}
