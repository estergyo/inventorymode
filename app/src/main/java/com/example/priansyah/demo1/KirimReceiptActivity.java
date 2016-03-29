package com.example.priansyah.demo1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.priansyah.demo1.Entity.TransDetail;

import java.util.ArrayList;

/**
 * Created by Priansyah on 3/6/2016.
 */
public class KirimReceiptActivity extends AppCompatActivity {

    Button buttonKirimReceipt;
    EditText editTextEmailPembeli;
    EditText editTextPonselPembeli;
    RadioButton radioSend1;
    RadioButton radioSend2;
    ArrayList<TransDetail> listOfTransactionDetail;
    String textBody ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kirim_receipt);

        editTextEmailPembeli = (EditText) findViewById(R.id.editTextEmailPembeli);
        editTextPonselPembeli = (EditText) findViewById(R.id.editTextPonselPembeli);
        radioSend1 = (RadioButton) findViewById(R.id.radioSend1);
        radioSend2 = (RadioButton) findViewById(R.id.radioSend2);

        radioSend1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioSend2.setChecked(false);
                    findViewById(R.id.layoutSMS).setVisibility(View.GONE);
                    findViewById(R.id.layoutEmail).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.layoutEmail).setVisibility(View.GONE);
                }
            }
        });
        radioSend2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioSend1.setChecked(false);
                    findViewById(R.id.layoutEmail).setVisibility(View.GONE);
                    findViewById(R.id.layoutSMS).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.layoutSMS).setVisibility(View.GONE);
                }
            }
        });

        buttonKirimReceipt = (Button) findViewById(R.id.buttonKirimReceipt);
        buttonKirimReceipt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (radioSend1.isChecked()) {
                    if(editTextEmailPembeli.getText() != null)
                        sendEmail();
                    else
                        Toast.makeText(KirimReceiptActivity.this,"Alamat email masih kosong",Toast.LENGTH_SHORT);
                }
                else if(radioSend2.isChecked()) {
                    if(editTextPonselPembeli.getText() != null)
                        sendSMS();
                    else
                        Toast.makeText(KirimReceiptActivity.this,"Nomor hp masih kosong",Toast.LENGTH_SHORT);
                }
            }
        });
    }
    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {editTextEmailPembeli.getText().toString()};
        Double paymentValue = Double.parseDouble(getIntent().getSerializableExtra("PAYMENT_VALUE").toString());
        Double total = Double.parseDouble(getIntent().getSerializableExtra("TOTAL").toString());
        Double change = Double.parseDouble(getIntent().getSerializableExtra("CHANGE").toString());
//        int total = (int) getIntent().getSerializableExtra("TOTAL");
//        int change = (int) getIntent().getSerializableExtra("CHANGE");

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);

        listOfTransactionDetail = getIntent().getParcelableArrayListExtra("TRANSDETLIST");
        for (int i=0; i<listOfTransactionDetail.size(); i++) {
            textBody += listOfTransactionDetail.get(i).getTextTransId() + "\t" +
                    listOfTransactionDetail.get(i).getTextNama() + "\t" +
                    listOfTransactionDetail.get(i).getTextJumlah() + "\t" +
                    listOfTransactionDetail.get(i).getTextHarga() + "\t" +
                    listOfTransactionDetail.get(i).getTextTanggalTrans() + "\t" +
                     "\n";
        }
        textBody += "Total: \t" + total + "\n" +
               "Pembayaran: \t" + paymentValue + "\n" +
                "Kembalian: \t" + change;
        emailIntent.putExtra(Intent.EXTRA_TEXT, textBody);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(KirimReceiptActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void sendSMS() {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

//        int paymentValue = (int) getIntent().getSerializableExtra("PAYMENT_VALUE");
//        int total = (int) getIntent().getSerializableExtra("TOTAL");
//        int change = (int) getIntent().getSerializableExtra("CHANGE");
        Double paymentValue = Double.parseDouble(getIntent().getSerializableExtra("PAYMENT_VALUE").toString());
        Double total = Double.parseDouble(getIntent().getSerializableExtra("TOTAL").toString());
        Double change = Double.parseDouble(getIntent().getSerializableExtra("CHANGE").toString());
        listOfTransactionDetail = getIntent().getParcelableArrayListExtra("TRANSDETLIST");
        for (int i=0; i<listOfTransactionDetail.size(); i++) {
            textBody += listOfTransactionDetail.get(i).getTextTransId() + "\t" +
                    listOfTransactionDetail.get(i).getTextNama() + "\t" +
                    listOfTransactionDetail.get(i).getTextJumlah() + "\t" +
                    listOfTransactionDetail.get(i).getTextHarga() + "\t" +
                    listOfTransactionDetail.get(i).getTextTanggalTrans() + "\t" +
                    "\n";
        }
        textBody += "Total: \t" + total + "\n" +
                "Pembayaran: \t" + paymentValue + "\n" +
                "Kembalian: \t" + change;

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", new String(editTextPonselPembeli.getText().toString()));
        smsIntent.putExtra("sms_body"  , textBody);

        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(KirimReceiptActivity.this,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}
