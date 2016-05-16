package com.example.priansyah.demo1;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priansyah.demo1.Entity.Item;

import java.util.ArrayList;

/**
 * Created by Priansyah on 1/22/2016.
 */
public class DetailItemActivity extends AppCompatActivity {

    TextView textViewNamaDI;
    Button buttonEditDI;
    Button buttonDeleteDI;
    ImageView imageItemDI;
    TextView textSkuDI;
    TextView textJumlahDI;
    TextView textHargaDI;
    TextView textKategoriDI;
    TextView textSupplierDI;

    Intent intent;

    Item item;
    SQLiteDatabase dbDetailItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Detail Barang");

        intent = getIntent();

        textViewNamaDI = (TextView) findViewById(R.id.textViewNamaDI);
        textSkuDI = (TextView) findViewById(R.id.textSkuDI);
        textJumlahDI = (TextView) findViewById(R.id.textJumlahDI);
        textHargaDI = (TextView) findViewById(R.id.textHargaDI);
        textKategoriDI = (TextView) findViewById(R.id.textKategoriDI);
        textSupplierDI = (TextView) findViewById(R.id.textSupplierDI);
        imageItemDI = (ImageView) findViewById(R.id.imageItemDI);

        item = intent.getParcelableExtra("Item");

        setTexts();

        buttonEditDI = (Button) findViewById(R.id.buttonEditDI);
        buttonDeleteDI = (Button) findViewById(R.id.buttonDeleteDI);

        dbDetailItem = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        dbDetailItem.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");

        buttonEditDI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DetailItemActivity.this, "edit clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailItemActivity.this, EditItemActivity.class);
                intent.putExtra("Item", item);
                startActivityForResult(intent,getResources().getInteger(R.integer.item_edit_rq_code));
            }
        });

        buttonDeleteDI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(DetailItemActivity.this);

                adb.setTitle("Hapus");
                adb.setMessage("Apakah anda yakin?");

                adb.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbDetailItem.execSQL("delete from stock where sku = '"+item.getTextSKU()+"' ");
                        dialog.dismiss();
                        setResult(RESULT_OK);
                        DetailItemActivity.this.finish();
                    }
                });
                adb.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DetailItemActivity.this, "hapus cancelled", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                adb.show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == getResources().getInteger(R.integer.item_edit_rq_code))
           if (resultCode == RESULT_OK) {
               setResult(RESULT_OK);
               item = intent.getParcelableExtra("UPDATEDITEM");
               setTexts();
           }
    }

    public void setTexts(){
        textViewNamaDI.setText(item.getTextNama());
        textSkuDI.setText(item.getTextSKU());
        textJumlahDI.setText(item.getTextJumlah());
        textHargaDI.setText(item.getTextHarga());
        textKategoriDI.setText(item.getTextKategori());
        textSupplierDI.setText(item.getTextSupplier());
        imageItemDI.setImageBitmap(decodeBase64(item.getTextImage()));
    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}
