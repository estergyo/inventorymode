package com.example.priansyah.demo1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Priansyah on 3/26/2016.
 */
public class LoginActivity extends AppCompatActivity {
    EditText editTextUserNameLogin;
    EditText editTextPasswordLogin;
    Button buttonToLogin;

    Intent intent;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());

        editTextUserNameLogin = (EditText) findViewById(R.id.editTextUserNameLogin);
        editTextPasswordLogin = (EditText) findViewById(R.id.editTextPasswordLogin);

        buttonToLogin=(Button)findViewById(R.id.buttonToLogin);
        buttonToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = openOrCreateDatabase("POS", MODE_PRIVATE, null);
                db.execSQL("Create table if not exists user(store_name VARCHAR, name VARCHAR, email VARCHAR, username VARCHAR, password VARCHAR);");
                Cursor uname = db.rawQuery("select * from user where username = '" + editTextUserNameLogin.getText() + "' ", null);
                if (uname.getCount() == 0) {
                    Toast.makeText(LoginActivity.this, "username tidak terdaftar", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor passwd = db.rawQuery("select password from user where username = '" + editTextUserNameLogin.getText() + "' ", null);
                    if (passwd.moveToFirst()) {
                        if (passwd.getString(0).equals(editTextPasswordLogin.getText().toString())) {

                            session.createLoginSession(editTextUserNameLogin.getText().toString());

                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "password salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
