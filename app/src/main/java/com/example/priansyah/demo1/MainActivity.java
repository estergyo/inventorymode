package com.example.priansyah.demo1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button buttonPOS;
    Button buttonInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPOS = (Button) findViewById(R.id.buttonPOS);
        buttonInventory = (Button) findViewById(R.id.buttonInventory);

        buttonPOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, TransaksiActivity.class);
                startActivity(intent1);
            }
        });
        buttonInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, InventoryActivity.class);
                startActivity(intent2);
            }
        });
    }
}
