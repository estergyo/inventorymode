package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priansyah.demo1.Entity.Item;
import com.example.priansyah.demo1.Entity.Supplier;

/**
 * Created by Priansyah on 2/20/2016.
 */
public class EditSupplierActivity extends AppCompatActivity {
    TextView textUpdateNamaES;
    EditText editTextUpdateAlamat;
    EditText editTextUpdateKontak;
    Button buttonUpdateES;
    SQLiteDatabase dbEditSupplier;
    Supplier supplier;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_supplier);

        textUpdateNamaES = (TextView) findViewById(R.id.textUpdateNamaES);
        editTextUpdateAlamat = (EditText) findViewById(R.id.editTextUpdateAlamat);
        editTextUpdateKontak = (EditText) findViewById(R.id.editTextUpdateKontak);

        buttonUpdateES = (Button) findViewById(R.id.buttonUpdateES);
        intent = getIntent();
        supplier = intent.getParcelableExtra("Supplier");

        textUpdateNamaES.setText(supplier.getTextNama());
        editTextUpdateAlamat.setText(supplier.getTextAlamat());
        editTextUpdateKontak.setText(supplier.getTextKontak());

        dbEditSupplier = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        dbEditSupplier.execSQL("Create table if not exists supplier(name VARCHAR, address VARCHAR, contact VARCHAR);");

        buttonUpdateES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });

    }

    public void checkInput() {
        if (textUpdateNamaES.getText().toString().equals("") || editTextUpdateAlamat.getText().toString().equals("") || editTextUpdateKontak.getText().toString().equals("")) {
            Toast.makeText(EditSupplierActivity.this, "data supplier tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        else {
            Cursor name = dbEditSupplier.rawQuery("select * from supplier where name = '" + supplier.getTextNama() + "' ", null);
            if (name.getCount()==1) {
                dbEditSupplier.execSQL("update supplier set address = '"+editTextUpdateAlamat.getText()+"', contact = '"+editTextUpdateKontak.getText()+"' where name = '" + supplier.getTextNama() + "' ");
                Intent returnIntent = new Intent();
                Supplier newSupplier = new Supplier(supplier.getTextNama(),editTextUpdateAlamat.getText().toString(),editTextUpdateKontak.getText().toString());
                returnIntent.putExtra("UPDATEDSUPPLIER", newSupplier);
                setResult(RESULT_OK,returnIntent);
                Toast.makeText(EditSupplierActivity.this, "data tersimpan", Toast.LENGTH_SHORT).show();
            }
//            else{
//                Toast.makeText(EditSupplierActivity.this,"supplier telah terdaftar",Toast.LENGTH_SHORT).show();
//            }
        }
    }
}
