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

import com.example.priansyah.demo1.Entity.Kategori;

/**
 * Created by Priansyah on 1/23/2016.
 */
public class DetailKategoriActivity extends AppCompatActivity {
	TextView textViewNamaDK;
    TextView textViewDeskripsiDK;
    Button buttonEditDK;
    Button buttonDeleteDK;
    Intent intent;
    Kategori kategori;
    SQLiteDatabase dbDetailKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kategori);

        intent = getIntent();
        textViewNamaDK = (TextView) findViewById(R.id.textViewNamaDK);
        textViewNamaDK.setText(intent.getStringExtra("NAMA"));

        textViewDeskripsiDK = (TextView) findViewById(R.id.textViewDeskripsiDK);

        kategori = intent.getParcelableExtra("Kategori");
        setTexts();

        buttonEditDK = (Button) findViewById(R.id.buttonEditDK);
        buttonDeleteDK = (Button) findViewById(R.id.buttonDeleteDK);

        dbDetailKategori = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        dbDetailKategori.execSQL("Create table if not exists category(name VARCHAR, description VARCHAR);");

        buttonEditDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DetailKategoriActivity.this, "edit clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailKategoriActivity.this, EditKategoriActivity.class);
                intent.putExtra("Kategori", kategori);
                startActivityForResult(intent,getResources().getInteger(R.integer.category_edit_rq_code));
            }
        });

        buttonDeleteDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(DetailKategoriActivity.this);

                adb.setTitle("Hapus");
                adb.setMessage("Apakah anda yakin?");

                adb.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbDetailKategori.execSQL("delete from category where name = '"+kategori.getTextNama()+"' ");
                        dialog.dismiss();
                        setResult(RESULT_OK);
                        DetailKategoriActivity.this.finish();
                    }
                });
                adb.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DetailKategoriActivity.this, "hapus cancelled", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                adb.show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == getResources().getInteger(R.integer.category_edit_rq_code))
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                kategori = intent.getParcelableExtra("UPDATEDKATEGORI");
                setTexts();
            }
    }

    public void setTexts(){
        textViewNamaDK.setText(kategori.getTextNama());
        textViewDeskripsiDK.setText(kategori.getTextDeskripsi());
    }
}
