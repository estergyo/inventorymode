package com.example.priansyah.demo1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.priansyah.demo1.Entity.Item;
import com.example.priansyah.demo1.Entity.TransDetail;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Priansyah on 2/26/2016.
 */
public class DiskonActivity extends AppCompatActivity {
    RadioGroup radioGroupDiskon;
    RadioButton radioDisc1;
    RadioButton radioDisc2;
    RadioButton radioDisc3;
    RadioButton radioDisc4;
    ArrayList<TransDetail> listOfTransactionDetail;
    Button buttonLanjutDiskon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diskon);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Diskon");

        radioGroupDiskon = (RadioGroup) findViewById(R.id.myRadioGroup);
        radioDisc1 = (RadioButton) findViewById(R.id.radioDisc1);
        radioDisc2 = (RadioButton) findViewById(R.id.radioDisc2);
        radioDisc3 = (RadioButton) findViewById(R.id.radioDisc3);
        radioDisc4 = (RadioButton) findViewById(R.id.radioDisc4);
//        listOfItemsDisc = getIntent().getParcelableArrayListExtra("LIST");
        listOfTransactionDetail = getIntent().getParcelableArrayListExtra("TRANSDETLIST");

        buttonLanjutDiskon = (Button) findViewById(R.id.buttonLanjutDiskon);
        buttonLanjutDiskon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                double disc;
                int selectedId = radioGroupDiskon.getCheckedRadioButtonId();
                if (selectedId == radioDisc1.getId()) {
                    disc = 0.1;
                } else if (selectedId == radioDisc2.getId()) {
                    disc = 0.15;
                } else if (selectedId == radioDisc3.getId()) {
                    disc = 0.2;
                }
                else {
                    disc = 0.0;
                }

                Intent intent = new Intent(getBaseContext(), KonfirmasiActivity.class);
//                intent.putExtra("LIST", listOfItemsDisc);
                intent.putExtra("TRANSDETLIST", listOfTransactionDetail);
                intent.putExtra("discount", disc);
                startActivity(intent);
            }
        });
    }
}
