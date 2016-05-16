package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priansyah.demo1.Entity.Kategori;

/**
 * Created by Priansyah on 2/20/2016.
 */
public class EditKategoriActivity extends AppCompatActivity {
    TextView textUpdateNamaEK;
    EditText editTextUpdateDeskripsi;
    Button buttonUpdateEK;
    SQLiteDatabase dbEditKategori;
    Kategori kategori;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kategori);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Edit Kategori");

        textUpdateNamaEK = (TextView) findViewById(R.id.textUpdateNamaEK);
        editTextUpdateDeskripsi = (EditText) findViewById(R.id.editTextUpdateDeskripsi);

        buttonUpdateEK = (Button) findViewById(R.id.buttonUpdateEK);
        intent = getIntent();
        kategori = intent.getParcelableExtra("Kategori");

        textUpdateNamaEK.setText(kategori.getTextNama());
        editTextUpdateDeskripsi.setText(kategori.getTextDeskripsi());

        dbEditKategori = openOrCreateDatabase("POS", MODE_PRIVATE, null);

        buttonUpdateEK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();            }
        });
    }

    public void checkInput() {
        if (textUpdateNamaEK.getText().toString().equals("") || editTextUpdateDeskripsi.getText().toString().equals("")) {
            Toast.makeText(EditKategoriActivity.this, "data kategori tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        else {
            Cursor name = dbEditKategori.rawQuery("select * from category where name = '" + kategori.getTextNama() + "' ", null);
            if (name.getCount()==1) {
                dbEditKategori.execSQL("update category set description = '"+editTextUpdateDeskripsi.getText()+"' where name = '" + kategori.getTextNama() + "' ");
                Intent returnIntent = new Intent();
                Kategori newKategori = new Kategori(kategori.getTextNama(),editTextUpdateDeskripsi.getText().toString());
                returnIntent.putExtra("UPDATEDKATEGORI", newKategori);
                setResult(RESULT_OK,returnIntent);
                Toast.makeText(EditKategoriActivity.this, "data tersimpan", Toast.LENGTH_SHORT).show();
            }
//            else{
//                Toast.makeText(TambahKategoriActivity.this,"kategori telah terdaftar",Toast.LENGTH_SHORT).show();
//            }
        }
    }
}
