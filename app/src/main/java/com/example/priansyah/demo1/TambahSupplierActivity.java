package com.example.priansyah.demo1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by black on 23/01/2016.
 */
public class TambahSupplierActivity extends AppCompatActivity {
	EditText editTextNama;
    EditText editTextAlamat;
    EditText editTextKontak;
    Button buttonSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_supplier);

        editTextNama = (EditText) findViewById(R.id.editTextNamaSupplier);
        editTextAlamat = (EditText) findViewById(R.id.editTextAlamat);
        editTextKontak = (EditText) findViewById(R.id.editTextKontak);
        
        buttonSimpan = (Button) findViewById(R.id.simpanSupplier);

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });

    }

    public void checkInput() {
        if (editTextNama.getText().toString().equals("") || editTextAlamat.getText().toString().equals("") || editTextKontak.getText().toString().equals("")) {
            Toast.makeText(TambahSupplierActivity.this,"data supplier tidak boleh kosong",Toast.LENGTH_SHORT).show();
        }
        else {
            SQLiteDatabase db = openOrCreateDatabase("POS", MODE_PRIVATE, null);
            db.execSQL("Create table if not exists supplier(name VARCHAR, address VARCHAR, contact VARCHAR);");

            Cursor name = db.rawQuery("select * from supplier where name = '" + editTextNama.getText() + "' ", null);
            if (name.getCount()==0) {
                db.execSQL("insert into supplier values('"+editTextNama.getText()+"', '"+editTextAlamat.getText()+"', '"+editTextKontak.getText()+"')");
                setResult(RESULT_OK);
                finish();
            }else{
                Toast.makeText(TambahSupplierActivity.this,"supplier telah terdaftar",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
