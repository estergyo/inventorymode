package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priansyah.demo1.Entity.TransDetail;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ester gyo fanny on 23/04/2016.
 */
public class PembayaranActivity extends AppCompatActivity {
    TextView textViewHargaPembayaran;
    EditText nominal;
    Button buttonLanjutPembayaran;
    ArrayList<TransDetail> listOfTransactionDetail;
    SQLiteDatabase dbTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Pembayaran");

        textViewHargaPembayaran = (TextView) findViewById(R.id.textViewHargaPembayaran);
        nominal = (EditText) findViewById(R.id.nominal);
        buttonLanjutPembayaran = (Button) findViewById(R.id.buttonLanjutPembayaran);

        final Double finalPrice = (Double) getIntent().getSerializableExtra("TOTAL");
        final Double diskon = (Double) getIntent().getSerializableExtra("DISKON");
        final Double pajak = (Double) getIntent().getSerializableExtra("PAJAK");
        listOfTransactionDetail = getIntent().getParcelableArrayListExtra("TRANSDETLIST");

        textViewHargaPembayaran.setText(""+finalPrice);

        buttonLanjutPembayaran.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("nominalgettext", nominal.getText().toString());
                if (nominal.getText().toString().equals("")) {
                    Toast.makeText(PembayaranActivity.this, "Masukkan nominal pembayaran", Toast.LENGTH_SHORT).show();
                }
                else {
                    int paymentValue = Integer.parseInt(nominal.getText().toString());
                    if (paymentValue < finalPrice) {
                        Toast.makeText(PembayaranActivity.this, "Nominal tidak memenuhi pembayaran", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(getBaseContext(), ReceiptActivity.class);
//                intent.putExtra("LIST", listOfItemsKfm);
                        intent.putExtra("TOTAL", finalPrice);
                        intent.putExtra("PAYMENT_VALUE", paymentValue);
                        intent.putExtra("TRANSDETLIST", listOfTransactionDetail);

                        dbTransaction = openOrCreateDatabase("POS", MODE_PRIVATE, null);
                        dbTransaction.execSQL("Create table if not exists transaction_detail(trans_detail_id INT, transaction_id INT, product_sku INT, amount INT, price VARCHAR, date_created VARCHAR);");
                        dbTransaction.execSQL("Create table if not exists stock_transaction(transaction_id INT, total INT, discount INT, tax VARCHAR, date_created VARCHAR, payment INT);");


                        Calendar c = Calendar.getInstance();
                        String date = c.get(Calendar.DATE) +"/" + c.get(Calendar.MONTH) +"/" + c.get(Calendar.YEAR);

                        Cursor maxTrans = dbTransaction.rawQuery("SELECT MAX(transaction_id) FROM stock_transaction", null);
                        Log.v("jmlidmaxawal", "" + maxTrans.getCount());

                        Cursor maxTransDetail = dbTransaction.rawQuery("SELECT MAX(trans_detail_id) FROM transaction_detail", null);
                        boolean isCursorToFirst = false;

                        if(maxTrans!=null) {
                            if (maxTrans.moveToFirst()) {
                                isCursorToFirst = true;
                                if (maxTrans.getInt(0) == -1) {
                                    Log.v("tostring", "" + maxTrans.getString(0));
                                    dbTransaction.execSQL("insert into stock_transaction values(1, '" + finalPrice + "', '" + diskon + "', '" + pajak + "', '" + date + "')");
                                    maxTrans = dbTransaction.rawQuery("SELECT MAX(transaction_id) FROM stock_transaction", null);
                                } else {
                                    if (maxTrans.moveToFirst())
                                        Log.v("tostring2", "" + maxTrans.getString(0));
                                    dbTransaction.execSQL("insert into stock_transaction values('" + (maxTrans.getInt(0) + 1) + "', '" + finalPrice + "', '" + diskon + "', '" + pajak + "', '" + date + "', '" + paymentValue + "')");
                                }
                            }
                        }

//                if(maxTransDetail!=null)
//                    if(maxTransDetail.moveToFirst()){
//                        if (maxTransDetail.getInt(0)==-1) {
//                            if(!isCursorToFirst)
//                                maxTrans.moveToFirst();
//                            for (int i=1; i<listOfTransactionDetail.size()+1; i++){
//                                dbTransaction.execSQL("insert into transaction_detail values('"+i+"', '"+maxTrans.getInt(0)+"', '"+listOfTransactionDetail.get(i-1).getTextSKU()+"', '"+listOfTransactionDetail.get(i-1).getTextHarga()+"', '"+date+"')");
//                            }
//                        }
//                        else {
//                            if(!isCursorToFirst)
//                                maxTrans.moveToFirst();
//                            if(maxTransDetail.moveToFirst())
//                                for (int i=1; i<=listOfTransactionDetail.size(); i++){
//                                    Log.v("list", "" + maxTransDetail.getString(0));
//                                    dbTransaction.execSQL("insert into transaction_detail values('"+(maxTransDetail.getInt(0)+i)+"', '"+maxTrans.getInt(0)+"', '"+listOfTransactionDetail.get(i-1).getTextSKU()+"', '"+listOfTransactionDetail.get(i-1).getTextHarga()+"', '"+date+"')");
//                                    maxTransDetail.moveToNext();
//                                }
//                        }
//                    }
//                if (maxTrans != null && maxTrans.moveToFirst()) {
//                    if (maxTrans.getString(maxTrans.getColumnIndex("transaction_id")).equals(-1)) {
//                        do {
//                            dbTransaction.execSQL("insert into stock_transaction values(1, '" + finalPrice + "', '" + discountKonfirmasi + "', '" + tax + "', '" + date + "')");
//                        } while (maxTrans.moveToNext());
//                        maxTrans = dbTransaction.rawQuery("SELECT MAX(transaction_id) FROM stock_transaction", null);
////                        Log.v("idtrans1", "" + maxTrans.getInt(0));
//                        Log.v("cobaliatisi", "" + maxTrans.getString(maxTrans.getColumnIndex("transaction_id")));
//                    } else {
//                        do {
//                            dbTransaction.execSQL("insert into stock_transaction values('" + (maxTrans.getInt(0) + 1) + "', '" + finalPrice + "', '" + discountKonfirmasi + "', '" + tax + "', '" + date + "')");
//                        } while (maxTrans.moveToNext());
//                        maxTrans = dbTransaction.rawQuery("SELECT MAX(transaction_id) FROM stock_transaction", null);
//                        Log.v("idtranslanjut", "" + maxTrans.getInt(0));
//                        Log.v("cobaliatisi", "" + maxTrans.getColumnIndex("transaction_id"));
//                    }
//                }
//
                        for (int i = 1; i < listOfTransactionDetail.size() + 1; i++) {
                            maxTrans.moveToFirst();
                            Log.v("idtrans", "" + maxTrans.getInt(0));
                            if(maxTransDetail==null) {
                                dbTransaction.execSQL("insert into transaction_detail values('" + i + "', '" + (maxTrans.getInt(0) +1) + "', '" + listOfTransactionDetail.get(i - 1).getTextSKU() + "', '" + listOfTransactionDetail.get(i - 1).getTextHarga() + "', '" + date + "')");
                                Log.v("detiltras1", "" + maxTransDetail.getInt(0));
                            } else {
                                if (maxTransDetail.moveToFirst())
                                    dbTransaction.execSQL("insert into transaction_detail values('" + (maxTransDetail.getInt(0) + i) + "', '" + (maxTrans.getInt(0) +1) + "', '" + listOfTransactionDetail.get(i - 1).getTextSKU() + "', '" + listOfTransactionDetail.get(i - 1).getTextJumlah() + "', '" + listOfTransactionDetail.get(i - 1).getTextHarga() + "', '" + date + "')");
                                Log.v("detiltrans1", "" + maxTransDetail.getInt(0));
                            }
                        }

                        startActivity(intent);
                    }
                }
            }
        });
    }
}
