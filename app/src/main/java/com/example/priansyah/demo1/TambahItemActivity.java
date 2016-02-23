package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by black on 23/01/2016.
 */
public class TambahItemActivity extends AppCompatActivity{
    EditText editTextNama;
    EditText editTextSKU;
    EditText editTextJumlah;
    EditText editTextHarga;
    Spinner spinnerSupplier;
    Spinner spinnerKategori;
    Button buttonSimpan;
    Button buttonKameraTI;
    ImageView imageTambahItem;

    SQLiteDatabase db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_item);

        editTextNama = (EditText) findViewById(R.id.editTextNamaProduk);
        editTextHarga = (EditText) findViewById(R.id.editTextHarga);
        editTextSKU = (EditText) findViewById(R.id.editTextSKU);
        editTextJumlah = (EditText) findViewById(R.id.editTextJumlah);

        spinnerKategori = (Spinner) findViewById(R.id.spinnerKategori);
        spinnerSupplier = (Spinner) findViewById(R.id.spinnerSupplier);

        buttonSimpan = (Button) findViewById(R.id.simpanItem);
        buttonKameraTI = (Button) findViewById(R.id.buttonKameraTI);
        imageTambahItem = (ImageView) findViewById(R.id.imageTambahItem);

        buttonKameraTI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        db = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        db.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
        db.execSQL("Create table if not exists category(name VARCHAR, description VARCHAR);");
        db.execSQL("Create table if not exists supplier(name VARCHAR, address VARCHAR, contact VARCHAR);");

        spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                //String item = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();

        Cursor cats = db.rawQuery("select * from category",null);
        if(cats != null)
            if(cats.moveToFirst())
                do{
                    categories.add(cats.getString(0));
                }while(cats.moveToNext());

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerKategori.setAdapter(dataAdapter);

        spinnerSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                //String item = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        // Spinner Drop down elements
        List<String> suppliers = new ArrayList<String>();
        Cursor sups = db.rawQuery("select * from supplier",null);
        if(sups != null)
            if(sups.moveToFirst())
                do{
                    suppliers.add(sups.getString(0));
                }while(sups.moveToNext());

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, suppliers);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerSupplier.setAdapter(dataAdapter1);

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            imageTambahItem.setImageBitmap(bp);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void checkInput() {
        if (editTextNama.getText().toString().equals("") || editTextSKU.getText().toString().equals("") || editTextHarga.getText().toString().equals("") ||editTextJumlah.getText().toString().equals("") || imageTambahItem.getDrawable()== null) {
            Toast.makeText(TambahItemActivity.this,"data produk tidak boleh kosong",Toast.LENGTH_SHORT).show();
        }
        else {
            Cursor name = db.rawQuery("select * from stock where sku = '" + editTextSKU.getText() + "' ", null);
            if (name.getCount()==0) {
                db.execSQL("insert into stock values('"+editTextSKU.getText()+"', '"+editTextNama.getText()+"', "+editTextJumlah.getText()+", "+editTextHarga.getText()+", '"+spinnerKategori.getSelectedItem().toString()+"', '"+spinnerSupplier.getSelectedItem().toString()+"', '"+ encodeTobase64(((BitmapDrawable)imageTambahItem.getDrawable()).getBitmap()) +"')");
                setResult(RESULT_OK);
                finish();
            }else{
                Toast.makeText(TambahItemActivity.this,"produk telah terdaftar",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
