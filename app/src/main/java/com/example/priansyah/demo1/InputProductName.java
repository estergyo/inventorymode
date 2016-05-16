package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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


public class InputProductName extends ActionBarActivity implements View.OnClickListener {

    private Button saveBtn;
    private EditText nameTxtFld;
    private EditText priceTxtFld;
    Spinner spinnerSupplierIPN;
    Spinner spinnerKategoriIPN;
    Button buttonKameraIPN;
    ImageView imageItemIPN;
    SQLiteDatabase dbIPN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_product_name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Tambah Barang");

        saveBtn = (Button)findViewById(R.id.buttonSimpanScan);
        nameTxtFld = (EditText)findViewById(R.id.editTextNamaProdukBaru);
        priceTxtFld = (EditText)findViewById(R.id.editTextHargaProdukBaru);
        buttonKameraIPN = (Button) findViewById(R.id.buttonKameraIPN);
        imageItemIPN = (ImageView) findViewById(R.id.imageItemIPN);
        saveBtn.setOnClickListener(this);

        spinnerKategoriIPN = (Spinner) findViewById(R.id.spinnerKategoriIPN);
        spinnerSupplierIPN = (Spinner) findViewById(R.id.spinnerSupplierIPN);

        dbIPN = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        dbIPN.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
        dbIPN.execSQL("Create table if not exists category(name VARCHAR, description VARCHAR);");
        dbIPN.execSQL("Create table if not exists supplier(name VARCHAR, address VARCHAR, contact VARCHAR);");

        buttonKameraIPN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        spinnerKategoriIPN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        Cursor cats = dbIPN.rawQuery("select * from category",null);
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
        spinnerKategoriIPN.setAdapter(dataAdapter);

        spinnerSupplierIPN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        Cursor sups = dbIPN.rawQuery("select * from supplier",null);
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
        spinnerSupplierIPN.setAdapter(dataAdapter1);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input_product_name, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent pName = getIntent();
        String scanContent = pName.getStringExtra(InventoryActivity.EXTRA_MESSAGE);
        SQLiteDatabase dbIPN = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        if (nameTxtFld.getText().length() == 0 || scanContent.length() == 0 || priceTxtFld.getText().equals(0) || imageItemIPN.getDrawable()== null) {
            Toast.makeText(InputProductName.this, "data produk tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        else {
            Cursor name = dbIPN.rawQuery("select * from stock where sku = '" + scanContent + "' ", null);
            if (name.getCount()==0) {
                dbIPN.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");
                dbIPN.execSQL("insert into stock values('"+scanContent+"', '"+nameTxtFld.getText()+"', 1, "+priceTxtFld.getText()+", 'Kategori1', 'Supplier1', '"+ encodeTobase64(((BitmapDrawable)imageItemIPN.getDrawable()).getBitmap()) +"')");
                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
            else {
                Toast.makeText(InputProductName.this,"produk telah terdaftar",Toast.LENGTH_SHORT).show();
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            imageItemIPN.setImageBitmap(bp);
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
}
