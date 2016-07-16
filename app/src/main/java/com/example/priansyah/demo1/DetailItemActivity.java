package com.example.priansyah.demo1;

import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priansyah.demo1.Entity.Item;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Priansyah on 1/22/2016.
 */
public class DetailItemActivity extends AppCompatActivity {

    ShareActionProvider mShareActionProvider;
    TextView textViewNamaDI;
    Button buttonEditDI;
    Button buttonDeleteDI;
    Button buttonTwitterDI;
    Button buttonFacebookDI;
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
        buttonTwitterDI = (Button) findViewById(R.id.buttonTwitterDI);
        buttonFacebookDI = (Button) findViewById(R.id.buttonFacebookDI);

        dbDetailItem = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        dbDetailItem.execSQL("Create table if not exists stock(sku VARCHAR, name VARCHAR, amount INTEGER, price INTEGER, category VARCHAR, supplier VARCHAR, image VARCHAR);");

        buttonEditDI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DetailItemActivity.this, "edit clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailItemActivity.this, EditItemActivity.class);
                intent.putExtra("Item", item);
                startActivityForResult(intent, getResources().getInteger(R.integer.item_edit_rq_code));
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

        buttonTwitterDI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String msg = item.getTextSupplier() + " menjual " + item.getTextNama() +
                            " dengan harga " + item.getTextHarga() + ". Sisa stok " + item.getTextJumlah();
                    Bitmap imageBitmap = decodeBase64(item.getTextImage());
                    Uri uri = bitmapToUriConverter(imageBitmap);
                    Intent twitterIntent = new Intent();
                    twitterIntent.setAction(Intent.ACTION_SEND);
                    twitterIntent.putExtra(Intent.EXTRA_TEXT, msg);
                    twitterIntent.setType("text/plain");
                    twitterIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    twitterIntent.setType("image/jpeg");
                    twitterIntent.setPackage("com.twitter.android");
                    startActivity(twitterIntent);
                } catch (final ActivityNotFoundException e) {
                    Toast.makeText(DetailItemActivity.this, "aplikasi twitter tidak ditemukan", Toast.LENGTH_SHORT).show();
                }

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
        if (item.getTextHarga().substring(0,1).equals("R")) {
            textHargaDI.setText(item.getTextHarga());
        }
        else {
            textHargaDI.setText("Rp " + item.getTextHarga() + ",-");
        }
        textKategoriDI.setText(item.getTextKategori());
        textSupplierDI.setText(item.getTextSupplier());
        imageItemDI.setImageBitmap(decodeBase64(item.getTextImage()));
    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 100, 100);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200,
                    true);
            File file = new File(this.getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = this.openFileOutput(file.getName(),
                    Context.MODE_WORLD_READABLE);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);

        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return uri;
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int stretch_width = Math.round((float)width / (float)reqWidth);
        int stretch_height = Math.round((float)height / (float)reqHeight);

        if (stretch_width <= stretch_height)
            return stretch_height;
        else
            return stretch_width;
    }

}
