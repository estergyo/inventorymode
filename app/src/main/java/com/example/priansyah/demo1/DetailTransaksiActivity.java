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
    TextView textViewBayarTransaksi;
    TextView textViewKembalianTransaksi;
    Button buttonRefund;
    Button buttonReceipt;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Detail Transaksi");

        intent = getIntent();
        textViewIdTransaksi = (TextView) findViewById(R.id.textViewIdTransaksi);
//        textViewIdTransaksi.setText(intent.getStringExtra("Transaksi"));

        textViewTanggalTransaksi = (TextView) findViewById(R.id.textViewTanggalTransaksi);
        RecViewDetailTransaksi = (RecyclerView) findViewById(R.id.recViewDetailTransaksi);
        textViewDiskonTransaksi = (TextView) findViewById(R.id.textViewDiskonTransaksi);
        textViewPajakTransaksi = (TextView) findViewById(R.id.textViewPajakTransaksi);
        textViewTotalTransaksi = (TextView) findViewById(R.id.textViewTotalTransaksi);
        textViewBayarTransaksi = (TextView) findViewById(R.id.textViewBayarTransaksi);
        textViewKembalianTransaksi = (TextView) findViewById(R.id.textViewKembalianTransaksi);

        transaksi = intent.getParcelableExtra("Transaksi");
        Log.d("transaksi",""+ transaksi.getTextTransId());
        buttonRefund = (Button) findViewById(R.id.buttonRefund);
        buttonReceipt = (Button) findViewById(R.id.buttonReceipt);

        dbDetailTransaksi = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        dbDetailTransaksi.execSQL("Create table if not exists stock_transaction(transaction_id INT, total INT, discount INT, tax VARCHAR, date_created VARCHAR, payment INT, time_created VARCHAR);");
        dbDetailTransaksi.execSQL("Create table if not exists transaction_detail(trans_detail_id INT, transaction_id INT, product_sku INT, product_name VARCHAR, amount INT, price VARCHAR, date_created VARCHAR);");

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

        buttonReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), KirimReceiptActivity.class);
                intent.putExtra("TOTAL", transaksi.getTextTotalTrans());
                intent.putExtra("PAYMENT_VALUE", transaksi.getTextJumlahBayar());
                intent.putExtra("TRANSDETLIST", listOfDetTrans);
                int change = Integer.parseInt(transaksi.getTextJumlahBayar()) - Integer.parseInt(transaksi.getTextTotalTrans());
                intent.putExtra("CHANGE", change);
                startActivity(intent);
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
        textViewDiskonTransaksi.setText("Rp "+transaksi.getTextDiskon()+",-");
        textViewPajakTransaksi.setText("Rp "+transaksi.getTextPajak()+",-");
        textViewTotalTransaksi.setText("Rp "+transaksi.getTextTotalTrans()+",-");
        textViewBayarTransaksi.setText("Rp "+transaksi.getTextJumlahBayar()+",-");
        int kembalian = Integer.parseInt(transaksi.getTextJumlahBayar()) - Integer.parseInt(transaksi.getTextTotalTrans());
        textViewKembalianTransaksi.setText("Rp "+ kembalian +",-");

    }

    public void setList(){
        listOfDetTrans = new ArrayList<>();

        int transId = Integer.parseInt(transaksi.getTextTransId().toString());
        Cursor transDet = dbDetailTransaksi.rawQuery("select * from transaction_detail where transaction_id = '" + transId  + "' ", null);
        if(transDet != null) {
            if (transDet.moveToFirst())
                do {
//                    dbDetailTransaksi.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
//                    Cursor item = dbDetailTransaksi.rawQuery("select name from stock where sku = '" + transDet.getInt(2) + "' ", null);
//                    item.moveToFirst();
                    String nama = transDet.getString(3);
                    listOfDetTrans.add(new TransDetail("" + transDet.getInt(0), transDet.getString(2), nama, transDet.getString(4), transDet.getString(5), transDet.getString(6)));
                } while (transDet.moveToNext());
        }
        adapter = new ListDetilTransaksiAdapter(DetailTransaksiActivity.this, getBaseContext(), listOfDetTrans);
        recList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recList.setAdapter(adapter);
    }
}
