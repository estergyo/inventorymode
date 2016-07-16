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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priansyah.demo1.Entity.Transaksi;

import java.util.Calendar;

/**
 * Created by Priansyah on 3/17/2016.
 */
public class RefundActivity extends AppCompatActivity {
    EditText editTextJumlahRefund;
//    EditText editTextAlasanRefund;
//    EditText editTextNilaiRefund;
    RadioGroup radioGroupAlasan;
    RadioButton radioAlasan1;
    RadioButton radioAlasan2;
    RadioButton radioAlasan3;
    EditText editTextAlasan3;
    Button buttonProsesRefund;
    SQLiteDatabase dbRefund;
    Intent intent;
    Transaksi transaksi;
    String alasan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Pengembalian Dana");

        intent = getIntent();
        transaksi = intent.getParcelableExtra("Transaksi");

        editTextJumlahRefund = (EditText) findViewById(R.id.editTextJumlahRefund);
        radioGroupAlasan = (RadioGroup) findViewById(R.id.radioGroupAlasan);
        radioAlasan1 = (RadioButton) findViewById(R.id.radioAlasan1);
        radioAlasan2 = (RadioButton) findViewById(R.id.radioAlasan2);
        radioAlasan3 = (RadioButton) findViewById(R.id.radioAlasan3);
        editTextAlasan3 = (EditText) findViewById(R.id.editTextAlasan3);

        editTextJumlahRefund.setText(transaksi.getTextTotalTrans());
//        editTextAlasanRefund = (EditText) findViewById(R.id.editTextAlasanRefund);
//        editTextNilaiRefund = (EditText) findViewById(R.id.editTextNilaiRefund);

//        int selectedId = radioGroupAlasan.getCheckedRadioButtonId();
//        if (selectedId == radioAlasan1.getId()) {
//            alasan = "Barang rusak";
//            findViewById(R.id.editTextAlasan3).setVisibility(View.GONE);
//        } else if (selectedId == radioAlasan2.getId()) {
//            findViewById(R.id.editTextAlasan3).setVisibility(View.GONE);
//            alasan = "Barang tidak sesuai";
//        } else if (selectedId == radioAlasan3.getId()) {
//            findViewById(R.id.editTextAlasan3).setVisibility(View.VISIBLE);
//            alasan = editTextAlasan3.getText().toString();
//        }
        radioAlasan1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioAlasan2.setChecked(false);
                    radioAlasan3.setChecked(false);
                    alasan = "Barang rusak";
                    findViewById(R.id.editTextAlasan3).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.editTextAlasan3).setVisibility(View.GONE);
                }
            }
        });
        radioAlasan2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioAlasan1.setChecked(false);
                    radioAlasan3.setChecked(false);
                    findViewById(R.id.editTextAlasan3).setVisibility(View.GONE);
                    alasan = "Barang tidak sesuai";
                }else{
                    findViewById(R.id.editTextAlasan3).setVisibility(View.GONE);
                }
            }
        });
        radioAlasan3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioAlasan1.setChecked(false);
                    radioAlasan2.setChecked(false);
                    findViewById(R.id.editTextAlasan3).setVisibility(View.VISIBLE);
                    alasan = editTextAlasan3.getText().toString();
                }else{
                    findViewById(R.id.editTextAlasan3).setVisibility(View.GONE);
                }
            }
        });

        buttonProsesRefund = (Button) findViewById(R.id.buttonProsesRefund);

        dbRefund = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        dbRefund.execSQL("Create table if not exists transaction_refund_statement(id INT, description VARCHAR, date_created VARCHAR);");
        dbRefund.execSQL("Create table if not exists transaction_refund(id INT, transaction_id INT, transaction_refund_statement_id INT,value INT, date_created VARCHAR);");

        buttonProsesRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                String date = c.get(Calendar.DATE) +"/" + c.get(Calendar.MONTH) +"/" + c.get(Calendar.YEAR);
                Cursor maxRefundStatementId = dbRefund.rawQuery("SELECT MAX(id) FROM transaction_refund_statement", null);
                Cursor maxRefundId = dbRefund.rawQuery("SELECT MAX(id) FROM transaction_refund", null);
                boolean isCursorToFirst = false;

                if(maxRefundStatementId!=null)
                    if(maxRefundStatementId.moveToFirst()){
                        isCursorToFirst = true;
                        if (maxRefundStatementId.getInt(0)==-1) {
                            dbRefund.execSQL("insert into transaction_refund_statement values(1, '" + alasan + "', '" + date + "')");
                            maxRefundStatementId = dbRefund.rawQuery("SELECT MAX(id) FROM transaction_refund_statement", null);
                        }
                        else {
                            if(maxRefundStatementId.moveToFirst())
                                dbRefund.execSQL("insert into transaction_refund_statement values('"+(maxRefundStatementId.getInt(0)+1)+"', '" + alasan + "', '" + date + "')");
                        }
                    }

                if(maxRefundId!=null)
                    if(maxRefundId.moveToFirst()){
                        if (maxRefundId.getInt(0)==-1) {
                            if(!isCursorToFirst)
                                maxRefundStatementId.moveToFirst();
                            dbRefund.execSQL("insert into transaction_refund values('"+maxRefundId.getInt(0)+"', '"+transaksi.getTextTransId()+"', '"+maxRefundStatementId.getInt(0)+"', '"+editTextJumlahRefund.getText().toString()+"', '"+date+"')");
                        }
                        else {
                            if(!isCursorToFirst)
                                maxRefundStatementId.moveToFirst();
                            if(maxRefundId.moveToFirst())
                                dbRefund.execSQL("insert into transaction_refund values('"+(maxRefundId.getInt(0)+1)+"', '"+transaksi.getTextTransId()+"', '"+maxRefundStatementId.getInt(0)+"', '"+editTextJumlahRefund.getText().toString()+"', '"+date+"')");                }
                    }
                Toast.makeText(RefundActivity.this, "data tersimpan", Toast.LENGTH_SHORT).show();
                Log.d("refund", editTextJumlahRefund.getText().toString());
                finish();
            }
        });

    }

}
