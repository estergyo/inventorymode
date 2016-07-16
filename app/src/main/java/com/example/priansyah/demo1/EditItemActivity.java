package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
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

import com.example.priansyah.demo1.Entity.Item;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priansyah on 2/19/2016.
 */
public class EditItemActivity extends AppCompatActivity {
    EditText editTextUpdateNamaEI;
    EditText editTextUpdateSKU;
    EditText editTextUpdateJumlah;
    EditText editTextUpdateHarga;
    Spinner spinnerUpdateSupplier;
    Spinner spinnerUpdateKategori;
    Button buttonUpdateEI;
    Button buttonKameraEI;
    ImageView imageEditItem;

    SQLiteDatabase dbEditItem;
    Item item;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Edit Barang");

        intent = getIntent();

        editTextUpdateNamaEI = (EditText) findViewById(R.id.editTextUpdateNamaEI);
        editTextUpdateHarga = (EditText) findViewById(R.id.editTextUpdateHarga);
        editTextUpdateSKU = (EditText) findViewById(R.id.editTextUpdateSKU);
        editTextUpdateJumlah = (EditText) findViewById(R.id.editTextUpdateJumlah);

        spinnerUpdateKategori = (Spinner) findViewById(R.id.spinnerUpdateKategori);
        spinnerUpdateSupplier = (Spinner) findViewById(R.id.spinnerUpdateSupplier);

        buttonUpdateEI = (Button) findViewById(R.id.buttonUpdateEI);
        buttonKameraEI = (Button) findViewById(R.id.buttonKameraEI);
        imageEditItem = (ImageView) findViewById(R.id.imageEditItem);

        item = intent.getParcelableExtra("Item");

        buttonKameraEI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        editTextUpdateNamaEI.setText(item.getTextNama());
        if (item.getTextHarga().substring(0,1).equals("R")) {
            String harga = item.getTextHarga().substring(3,item.getTextHarga().length()-2);
            editTextUpdateHarga.setText(harga);
            Log.d("harga", item.getTextHarga());
        }
        else {
            editTextUpdateHarga.setText(item.getTextHarga());
            Log.d("harga", item.getTextHarga());
        }

        editTextUpdateSKU.setText(item.getTextSKU());
        editTextUpdateJumlah.setText(item.getTextJumlah());
        imageEditItem.setImageBitmap(decodeBase64(item.getTextImage()));

        dbEditItem = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        dbEditItem.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
        dbEditItem.execSQL("Create table if not exists category(name VARCHAR, description VARCHAR);");
        dbEditItem.execSQL("Create table if not exists supplier(name VARCHAR, address VARCHAR, contact VARCHAR);");

        spinnerUpdateKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        Cursor cats = dbEditItem.rawQuery("select * from category",null);
        if(cats != null)
            if (cats.moveToFirst())
                do {
                    categories.add(cats.getString(0));
                } while (cats.moveToNext());

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerUpdateKategori.setAdapter(dataAdapter);

        for (int i = 0; i < spinnerUpdateKategori.getCount(); i++){
            if(spinnerUpdateKategori.getItemAtPosition(i).toString().equalsIgnoreCase(item.getTextKategori()))
                spinnerUpdateKategori.setSelection(i);
        }

        spinnerUpdateSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        Cursor sups = dbEditItem.rawQuery("select * from supplier",null);
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
        spinnerUpdateSupplier.setAdapter(dataAdapter1);

        for (int i = 0; i < spinnerUpdateSupplier.getCount(); i++){
            if(spinnerUpdateSupplier.getItemAtPosition(i).toString().equalsIgnoreCase(item.getTextKategori()))
                spinnerUpdateSupplier.setSelection(i);
        }

        buttonUpdateEI.setOnClickListener(new View.OnClickListener() {
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
            imageEditItem.setImageBitmap(bp);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void checkInput() {
        if (item.getTextNama().toString().equals("") || item.getTextSKU().toString().equals("") || item.getTextHarga().toString().equals("") ||item.getTextJumlah().toString().equals("") || imageEditItem.getDrawable()== null) {
            Toast.makeText(EditItemActivity.this, "data produk tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        else {
            Cursor name = dbEditItem.rawQuery("select * from stock where sku = '" + item.getTextSKU() + "' ", null);
            if (name.getCount()==1) {
                dbEditItem.execSQL("update stock set name = '" + editTextUpdateNamaEI.getText() + "', amount = " + editTextUpdateJumlah.getText() + ", price = "+editTextUpdateHarga.getText()+", category = '" + spinnerUpdateKategori.getSelectedItem().toString() + "', supplier = '" + spinnerUpdateSupplier.getSelectedItem().toString() + "', image = '"+ encodeTobase64(((BitmapDrawable)imageEditItem.getDrawable()).getBitmap()) +"' where sku = '" + item.getTextSKU() + "' ");
                Intent returnIntent = new Intent();
                Item newItem = new Item(editTextUpdateNamaEI.getText().toString(),item.getTextSKU(),editTextUpdateJumlah.getText().toString(),editTextUpdateHarga.getText().toString(),spinnerUpdateSupplier.getSelectedItem().toString(),spinnerUpdateKategori.getSelectedItem().toString(), encodeTobase64(((BitmapDrawable)imageEditItem.getDrawable()).getBitmap()));
                returnIntent.putExtra("UPDATEDITEM", newItem);
                setResult(RESULT_OK,returnIntent);
                Toast.makeText(EditItemActivity.this, "data tersimpan", Toast.LENGTH_SHORT).show();
            }
//          else{
//                Toast.makeText(TambahItemActivity.this,"produk telah terdaftar",Toast.LENGTH_SHORT).show();
//            }
        }
    }
}
