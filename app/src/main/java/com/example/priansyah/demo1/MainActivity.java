package com.example.priansyah.demo1;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;


public class MainActivity extends AppCompatActivity {
//    Button buttonLogin;
//    Button buttonSignUp;
//    Intent intent;
//    SessionManager session;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        buttonLogin = (Button) findViewById(R.id.buttonLogin);
//        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
//
//        session = new SessionManager(getApplicationContext());
//
//        if (session.isLoggedIn()) {
//            intent = new Intent(MainActivity.this, HomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }
//
//        buttonLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent1);
//            }
//        });
//        buttonSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent2 = new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(intent2);
//            }
//        });
//    }
    EditText editTextUserNameLogin;
    EditText editTextPasswordLogin;
    Button buttonToLogin;
    TextView textViewSignUp;

    Intent intent;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        editTextUserNameLogin = (EditText) findViewById(R.id.editTextUserNameLogin);
        editTextPasswordLogin = (EditText) findViewById(R.id.editTextPasswordLogin);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);

        textViewSignUp.setPaintFlags(textViewSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Linkify.addLinks(textViewSignUp, Linkify.ALL);

        buttonToLogin=(Button)findViewById(R.id.buttonToLogin);
        buttonToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = openOrCreateDatabase("POS", MODE_PRIVATE, null);
                db.execSQL("Create table if not exists user(store_name VARCHAR, name VARCHAR, email VARCHAR, username VARCHAR, password VARCHAR);");
                Cursor uname = db.rawQuery("select * from user where username = '" + editTextUserNameLogin.getText() + "' ", null);
                if (uname.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "username tidak terdaftar", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor passwd = db.rawQuery("select password from user where username = '" + editTextUserNameLogin.getText() + "' ", null);
                    if (passwd.moveToFirst()) {
                        if (passwd.getString(0).equals(editTextPasswordLogin.getText().toString())) {

                            session.createLoginSession(editTextUserNameLogin.getText().toString());

                            intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "password salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LoginActivity", "Sign Up Activity activated.");
                // this is where you should start the signup Activity
                 MainActivity.this.startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
