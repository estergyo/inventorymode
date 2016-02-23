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
public class TambahKategoriActivity extends AppCompatActivity {
	EditText editTextNama;
    EditText editTextDeskripsi;
    Button buttonSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kategori);

        editTextNama = (EditText) findViewById(R.id.editTextNamaKategori);
        editTextDeskripsi = (EditText) findViewById(R.id.editTextDeskripsi);
        
        buttonSimpan = (Button) findViewById(R.id.simpanKategori);

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
checkInput();            }
        });
    }

    public void checkInput() {
        if (editTextNama.getText().toString().equals("") || editTextDeskripsi.getText().toString().equals("")) {
            Toast.makeText(TambahKategoriActivity.this,"data kategori tidak boleh kosong",Toast.LENGTH_SHORT).show();
        }
        else {
            SQLiteDatabase db = openOrCreateDatabase("POS", MODE_PRIVATE, null);
            db.execSQL("Create table if not exists category(name VARCHAR, description VARCHAR);");

            Cursor name = db.rawQuery("select * from category where name = '" + editTextNama.getText() + "' ", null);
            if (name.getCount()==0) {
                db.execSQL("insert into category values('"+editTextNama.getText()+"', '"+editTextDeskripsi.getText()+"')");
                setResult(RESULT_OK);
                finish();
            }else{
                Toast.makeText(TambahKategoriActivity.this,"kategori telah terdaftar",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
