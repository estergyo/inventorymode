package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.priansyah.demo1.Entity.Transaksi;

import java.util.Calendar;

/**
 * Created by Priansyah on 3/17/2016.
 */
public class RefundActivity extends AppCompatActivity {
    EditText editTextAlasanRefund;
    EditText editTextNilaiRefund;
    Button buttonProsesRefund;
    SQLiteDatabase dbRefund;
    Intent intent;
    Transaksi transaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);

        editTextAlasanRefund = (EditText) findViewById(R.id.editTextAlasanRefund);
        editTextNilaiRefund = (EditText) findViewById(R.id.editTextNilaiRefund);
        buttonProsesRefund = (Button) findViewById(R.id.buttonProsesRefund);

        intent = getIntent();
        transaksi = intent.getParcelableExtra("Transaksi");

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
                            dbRefund.execSQL("insert into transaction_refund_statement values(1, '" + editTextAlasanRefund.getText().toString() + "', '" + date + "')");
                            maxRefundStatementId = dbRefund.rawQuery("SELECT MAX(id) FROM transaction_refund_statement", null);
                        }
                        else {
                            if(maxRefundStatementId.moveToFirst())
                                dbRefund.execSQL("insert into transaction_refund_statement values('"+(maxRefundStatementId.getInt(0)+1)+"', '" + editTextAlasanRefund.getText().toString() + "', '" + date + "')");
                        }
                    }

                if(maxRefundId!=null)
                    if(maxRefundId.moveToFirst()){
                        if (maxRefundId.getInt(0)==-1) {
                            if(!isCursorToFirst)
                                maxRefundStatementId.moveToFirst();
                            dbRefund.execSQL("insert into transaction_refund values('"+maxRefundId.getInt(0)+"', '"+transaksi.getTextTransId()+"', '"+maxRefundStatementId.getInt(0)+"', '"+editTextNilaiRefund.getText().toString()+"', '"+date+"')");
                        }
                        else {
                            if(!isCursorToFirst)
                                maxRefundStatementId.moveToFirst();
                            if(maxRefundId.moveToFirst())
                                dbRefund.execSQL("insert into transaction_refund values('"+(maxRefundId.getInt(0)+1)+"', '"+transaksi.getTextTransId()+"', '"+maxRefundStatementId.getInt(0)+"', '"+editTextNilaiRefund.getText().toString()+"', '"+date+"')");                }
                    }
                Toast.makeText(RefundActivity.this, "data tersimpan", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

}
