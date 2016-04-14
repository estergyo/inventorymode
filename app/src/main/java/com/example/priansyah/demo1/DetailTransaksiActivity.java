package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priansyah.demo1.Adapter.ListDetilTransaksiAdapter;
import com.example.priansyah.demo1.Entity.TransDetail;
import com.example.priansyah.demo1.Entity.Transaksi;

import java.util.ArrayList;

/**
 * Created by Priansyah on 3/16/2016.
 */
public class DetailTransaksiActivity extends AppCompatActivity {
    TextView textViewIdTransaksi;
    TextView textViewTanggalTransaksi;
    RecyclerView RecViewDetailTransaksi;
    TextView textViewDiskonTransaksi;
    TextView textViewPajakTransaksi;
    TextView textViewTotalTransaksi;
    Button buttonRefund;
    Intent intent;
    Transaksi transaksi;
    SQLiteDatabase dbDetailTransaksi;
    RecyclerView recList;
    ArrayList<TransDetail> listOfDetTrans;
    ListDetilTransaksiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);

        intent = getIntent();
        textViewIdTransaksi = (TextView) findViewById(R.id.textViewIdTransaksi);
//        textViewIdTransaksi.setText(intent.getStringExtra("Transaksi"));

        textViewTanggalTransaksi = (TextView) findViewById(R.id.textViewTanggalTransaksi);
        RecViewDetailTransaksi = (RecyclerView) findViewById(R.id.recViewDetailTransaksi);
        textViewDiskonTransaksi = (TextView) findViewById(R.id.textViewDiskonTransaksi);
        textViewPajakTransaksi = (TextView) findViewById(R.id.textViewPajakTransaksi);
        textViewTotalTransaksi = (TextView) findViewById(R.id.textViewTotalTransaksi);

        transaksi = intent.getParcelableExtra("Transaksi");
        buttonRefund = (Button) findViewById(R.id.buttonRefund);

        dbDetailTransaksi = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        dbDetailTransaksi.execSQL("Create table if not exists stock_transaction(transaction_id INT, discount INT, tax VARCHAR, date_created VARCHAR);");
        dbDetailTransaksi.execSQL("Create table if not exists transaction_detail(trans_detail_id INT, transaction_id INT, product_sku INT, price VARCHAR, date_created VARCHAR);");

        recList = (RecyclerView) findViewById(R.id.recViewDetailTransaksi);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        setList();
        setTexts();

        buttonRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DetailKategoriActivity.this, "edit clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailTransaksiActivity.this, RefundActivity.class);
                intent.putExtra("Transaksi", transaksi);
                startActivity(intent);
//                startActivityForResult(intent, getResources().getInteger(R.integer.category_edit_rq_code));
            }
        });

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if(requestCode == getResources().getInteger(R.integer.category_edit_rq_code))
//            if (resultCode == RESULT_OK) {
//                setResult(RESULT_OK);
//                transaksi = intent.getParcelableExtra("UPDATEDKATEGORI");
//                setTexts();
//            }
//    }

    public void setTexts(){
        textViewIdTransaksi.setText(transaksi.getTextTransId());
        textViewTanggalTransaksi.setText(transaksi.getTextTanggalTrans());
        textViewDiskonTransaksi.setText(transaksi.getTextDiskon());
        textViewPajakTransaksi.setText(transaksi.getTextPajak());
        textViewTotalTransaksi.setText(transaksi.getTextTotalTrans());

    }

    public void setList(){
        listOfDetTrans = new ArrayList<>();
        int transId = Integer.parseInt(transaksi.getTextTransId().toString());
        Log.v("int", "" + transId);
        Cursor transDet = dbDetailTransaksi.rawQuery("select * from transaction_detail where transaction_id = '" + transId  + "' ", null);
        if(transDet != null) {
            Log.v("coba", "" + transDet.getCount());
            if (transDet.moveToFirst())
                do {
//                    dbDetailTransaksi.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
//                    Cursor item = dbDetailTransaksi.rawQuery("select name from stock where sku = '" + transDet.getInt(2) + "' ", null);
//                    String nama = item.getString(0);
                    if (transDet.moveToFirst())
                        listOfDetTrans.add(new TransDetail("" + transDet.getInt(0), "nama", transDet.getString(1), transDet.getString(2), transDet.getString(3), transDet.getString(4)));
//                    Log.v("dalem", "" + transDet.getString(2));
                } while (transDet.moveToNext());
        }
        adapter = new ListDetilTransaksiAdapter(DetailTransaksiActivity.this, getBaseContext(), listOfDetTrans);
        recList.setAdapter(adapter);
    }
}
