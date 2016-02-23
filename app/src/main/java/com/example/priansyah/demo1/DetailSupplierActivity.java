package com.example.priansyah.demo1;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priansyah.demo1.Entity.Supplier;

/**
 * Created by Priansyah on 1/23/2016.
 */
public class DetailSupplierActivity extends AppCompatActivity {
	TextView textViewNamaDS;
    TextView textViewAlamatDS;
    TextView textViewKontakDS;
    Button buttonEditDS;
    Button buttonDeleteDS;
    Intent intent;
    Supplier supplier;
    SQLiteDatabase dbDetailSupplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_supplier);

        intent = getIntent();
        textViewNamaDS = (TextView) findViewById(R.id.textViewNamaDS);
        textViewAlamatDS = (TextView) findViewById(R.id.textViewAlamatDS);
        textViewKontakDS = (TextView) findViewById(R.id.textViewKontakDS);

        supplier = intent.getParcelableExtra("Supplier");
        setTexts();

        buttonEditDS = (Button) findViewById(R.id.buttonEditDS);
        buttonDeleteDS = (Button) findViewById(R.id.buttonDeleteDS);

        dbDetailSupplier = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        dbDetailSupplier.execSQL("Create table if not exists supplier(name VARCHAR, address VARCHAR, contact VARCHAR);");

        buttonEditDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DetailSupplierActivity.this, "edit clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailSupplierActivity.this, EditSupplierActivity.class);
                intent.putExtra("Supplier", supplier);
                startActivityForResult(intent,getResources().getInteger(R.integer.supplier_edit_rq_code));
            }
        });

        buttonDeleteDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(DetailSupplierActivity.this);

                adb.setTitle("Hapus");
                adb.setMessage("Apakah anda yakin?");

                adb.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbDetailSupplier.execSQL("delete from supplier where name = '"+supplier.getTextNama()+"' ");
                        dialog.dismiss();
                        setResult(RESULT_OK);
                        DetailSupplierActivity.this.finish();
                    }
                });
                adb.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DetailSupplierActivity.this, "hapus cancelled", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                adb.show();
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == getResources().getInteger(R.integer.supplier_edit_rq_code))
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                supplier = intent.getParcelableExtra("UPDATEDSUPPLIER");
                setTexts();
            }
    }

    public void setTexts() {
        textViewNamaDS.setText(supplier.getTextNama());
        textViewAlamatDS.setText(supplier.getTextAlamat());
        textViewKontakDS.setText(supplier.getTextKontak());
    }
}
