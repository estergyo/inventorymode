package com.example.priansyah.demo1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Priansyah on 3/26/2016.
 */
public class RegisterActivity extends AppCompatActivity {
    EditText editTextNamaTokoRegister;
    EditText editTextNamaUserRegister;
    EditText editTextEmailRegister;
    EditText editTextUnameRegister;
    EditText editTextPasswordRegister;
    EditText editTextConfirmPasswordRegister;
    Button buttonDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextNamaTokoRegister = (EditText) findViewById(R.id.editTextNamaTokoRegister);
        editTextNamaUserRegister = (EditText) findViewById(R.id.editTextNamaUserRegister);
        editTextEmailRegister = (EditText) findViewById(R.id.editTextEmailRegister);
        editTextUnameRegister = (EditText) findViewById(R.id.editTextUnameRegister);
        editTextPasswordRegister = (EditText) findViewById(R.id.editTextPasswordRegister);
        editTextConfirmPasswordRegister = (EditText) findViewById(R.id.editTextConfirmPasswordRegister);
        buttonDaftar = (Button) findViewById(R.id.buttonDaftar);

        buttonDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });
    }

    public void checkInput() {
        if (editTextNamaTokoRegister.getText().toString().equals("") || editTextNamaUserRegister.getText().toString().equals("")
                || editTextEmailRegister.getText().toString().equals("") || editTextUnameRegister.getText().toString().equals("")
                || editTextPasswordRegister.getText().toString().equals("") || editTextConfirmPasswordRegister.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "data pengguna tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        else {
            SQLiteDatabase db = openOrCreateDatabase("POS", MODE_PRIVATE, null);
            db.execSQL("Create table if not exists user(store_name VARCHAR, name VARCHAR, email VARCHAR, username VARCHAR, password VARCHAR);");

            Cursor name = db.rawQuery("select * from user where username = '" + editTextUnameRegister.getText() + "' ", null);
            if (name.getCount()==0) {
                db.execSQL("insert into user values('" + editTextNamaTokoRegister.getText() + "', '" + editTextNamaUserRegister.getText() + "', '" + editTextEmailRegister.getText() + "', '" + editTextUnameRegister.getText() + "', '" + editTextPasswordRegister.getText() + "')");
                setResult(RESULT_OK);
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(RegisterActivity.this,"pengguna telah terdaftar",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
