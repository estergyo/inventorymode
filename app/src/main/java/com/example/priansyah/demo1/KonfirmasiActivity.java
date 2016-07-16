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
    TextView textViewPajakKfm;
    SQLiteDatabase dbTransaction;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Konfirmasi");

        textViewDiskonKfm = (TextView) findViewById(R.id.textViewDiskonKfm);
        textViewTotalKfm = (TextView) findViewById(R.id.textViewTotalKfm);
        textViewTanggal = (TextView) findViewById(R.id.textViewTanggal);
        textViewPajakKfm = (TextView) findViewById(R.id.textViewPajakKfm);

        discountKonfirmasi = (Double) getIntent().getSerializableExtra("discount");
        listOfTransactionDetail = getIntent().getParcelableArrayListExtra("TRANSDETLIST");

        textViewTanggal.setText(listOfTransactionDetail.get(listOfTransactionDetail.size()-1).getTextTanggalTrans());

        recList = (RecyclerView) findViewById(R.id.listViewItemKonfirmasi);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        double harga = 0.0;
        for (int i=0; i<listOfTransactionDetail.size(); i++) {
            String helper = listOfTransactionDetail.get(i).getTextHarga().substring(2, listOfTransactionDetail.get(i).getTextHarga().length()-2);
            harga += Integer.parseInt(helper);
        }

        total = harga - (discountKonfirmasi * harga);
        discountKonfirmasi= 100*discountKonfirmasi;
        final double tax = (0.1 * total);
        final Double finalPrice = harga + tax;

        adapter = new ListDetilTransaksiAdapter(this, getBaseContext(), listOfTransactionDetail);
        recList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recList.setAdapter(adapter);

        textViewDiskonKfm.setText(String.format("%.0f%%", discountKonfirmasi));
        textViewTotalKfm.setText("Rp " + finalPrice + ",-");
        textViewPajakKfm.setText("Rp " + tax + ",-");


        buttonLanjutKonfirmasi = (Button) findViewById(R.id.buttonLanjutKonfirmasi);
        buttonLanjutKonfirmasi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                EditText paymentView = (EditText) findViewById(R.id.nominal);
//                Log.v("Payment", "" + Integer.parseInt(paymentView.getText().toString()));
//                int paymentValue = Integer.parseInt(paymentView.getText().toString());
                Intent intent = new Intent(getBaseContext(), PembayaranActivity.class);
//                intent.putExtra("LIST", listOfItemsKfm);
                intent.putExtra("TOTAL", finalPrice);
                intent.putExtra("DISKON", discountKonfirmasi);
                intent.putExtra("PAJAK", tax);
//                intent.putExtra("PAYMENT_VALUE", paymentValue);
                intent.putExtra("TRANSDETLIST", listOfTransactionDetail);

//                dbTransaction = openOrCreateDatabase("POS", MODE_PRIVATE, null);
//                dbTransaction.execSQL("Create table if not exists transaction_detail(trans_detail_id INT, transaction_id INT, product_sku INT, amount INT, price VARCHAR, date_created VARCHAR);");
//                dbTransaction.execSQL("Create table if not exists stock_transaction(transaction_id INT, total INT, discount INT, tax VARCHAR, date_created VARCHAR, payment INT);");
//
//
//                Calendar c = Calendar.getInstance();
//                String date = c.get(Calendar.DATE) +"/" + c.get(Calendar.MONTH) +"/" + c.get(Calendar.YEAR);
//
//                Cursor maxTrans = dbTransaction.rawQuery("SELECT MAX(transaction_id) FROM stock_transaction", null);
//                Log.v("jmlidmaxawal", "" + maxTrans.getCount());
//
//                Cursor maxTransDetail = dbTransaction.rawQuery("SELECT MAX(trans_detail_id) FROM transaction_detail", null);
//                boolean isCursorToFirst = false;
//
//                if(maxTrans!=null) {
//                    if (maxTrans.moveToFirst()) {
//                        isCursorToFirst = true;
//                        if (maxTrans.getInt(0) == -1) {
//                            Log.v("tostring", "" + maxTrans.getString(0));
//                            dbTransaction.execSQL("insert into stock_transaction values(1, '" + finalPrice + "', '" + discountKonfirmasi + "', '" + tax + "', '" + date + "')");
//                            maxTrans = dbTransaction.rawQuery("SELECT MAX(transaction_id) FROM stock_transaction", null);
//                        } else {
//                            if (maxTrans.moveToFirst())
//                                Log.v("tostring2", "" + maxTrans.getString(0));
//                                dbTransaction.execSQL("insert into stock_transaction values('" + (maxTrans.getInt(0) + 1) + "', '" + finalPrice + "', '" + discountKonfirmasi + "', '" + tax + "', '" + date + "', '" + paymentValue + "')");
//                        }
//                    }
//                }
//
////                if(maxTransDetail!=null)
////                    if(maxTransDetail.moveToFirst()){
////                        if (maxTransDetail.getInt(0)==-1) {
////                            if(!isCursorToFirst)
////                                maxTrans.moveToFirst();
////                            for (int i=1; i<listOfTransactionDetail.size()+1; i++){
////                                dbTransaction.execSQL("insert into transaction_detail values('"+i+"', '"+maxTrans.getInt(0)+"', '"+listOfTransactionDetail.get(i-1).getTextSKU()+"', '"+listOfTransactionDetail.get(i-1).getTextHarga()+"', '"+date+"')");
////                            }
////                        }
////                        else {
////                            if(!isCursorToFirst)
////                                maxTrans.moveToFirst();
////                            if(maxTransDetail.moveToFirst())
////                                for (int i=1; i<=listOfTransactionDetail.size(); i++){
////                                    Log.v("list", "" + maxTransDetail.getString(0));
////                                    dbTransaction.execSQL("insert into transaction_detail values('"+(maxTransDetail.getInt(0)+i)+"', '"+maxTrans.getInt(0)+"', '"+listOfTransactionDetail.get(i-1).getTextSKU()+"', '"+listOfTransactionDetail.get(i-1).getTextHarga()+"', '"+date+"')");
////                                    maxTransDetail.moveToNext();
////                                }
////                        }
////                    }
////                if (maxTrans != null && maxTrans.moveToFirst()) {
////                    if (maxTrans.getString(maxTrans.getColumnIndex("transaction_id")).equals(-1)) {
////                        do {
////                            dbTransaction.execSQL("insert into stock_transaction values(1, '" + finalPrice + "', '" + discountKonfirmasi + "', '" + tax + "', '" + date + "')");
////                        } while (maxTrans.moveToNext());
////                        maxTrans = dbTransaction.rawQuery("SELECT MAX(transaction_id) FROM stock_transaction", null);
//////                        Log.v("idtrans1", "" + maxTrans.getInt(0));
////                        Log.v("cobaliatisi", "" + maxTrans.getString(maxTrans.getColumnIndex("transaction_id")));
////                    } else {
////                        do {
////                            dbTransaction.execSQL("insert into stock_transaction values('" + (maxTrans.getInt(0) + 1) + "', '" + finalPrice + "', '" + discountKonfirmasi + "', '" + tax + "', '" + date + "')");
////                        } while (maxTrans.moveToNext());
////                        maxTrans = dbTransaction.rawQuery("SELECT MAX(transaction_id) FROM stock_transaction", null);
////                        Log.v("idtranslanjut", "" + maxTrans.getInt(0));
////                        Log.v("cobaliatisi", "" + maxTrans.getColumnIndex("transaction_id"));
////                    }
////                }
////
//                for (int i = 1; i < listOfTransactionDetail.size() + 1; i++) {
//                    maxTrans.moveToFirst();
//                    Log.v("idtrans", "" + maxTrans.getInt(0));
//                    if(maxTransDetail==null) {
//                        dbTransaction.execSQL("insert into transaction_detail values('" + i + "', '" + (maxTrans.getInt(0) +1) + "', '" + listOfTransactionDetail.get(i - 1).getTextSKU() + "', '" + listOfTransactionDetail.get(i - 1).getTextHarga() + "', '" + date + "')");
//                        Log.v("detiltras1", "" + maxTransDetail.getInt(0));
//                    } else {
//                        if (maxTransDetail.moveToFirst())
//                            dbTransaction.execSQL("insert into transaction_detail values('" + (maxTransDetail.getInt(0) + i) + "', '" + (maxTrans.getInt(0) +1) + "', '" + listOfTransactionDetail.get(i - 1).getTextSKU() + "', '" + listOfTransactionDetail.get(i - 1).getTextJumlah() + "', '" + listOfTransactionDetail.get(i - 1).getTextHarga() + "', '" + date + "')");
//                        Log.v("detiltrans1", "" + maxTransDetail.getInt(0));
//                    }
//                }
                startActivity(intent);
            }
        });
    }
}
