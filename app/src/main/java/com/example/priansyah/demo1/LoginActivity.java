package com.example.priansyah.demo1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

    SharedPreferences sharedpreferences;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserNameLogin = (EditText) findViewById(R.id.editTextUserNameLogin);
        editTextPasswordLogin = (EditText) findViewById(R.id.editTextPasswordLogin);

        buttonToLogin=(Button)findViewById(R.id.buttonToLogin);
        sharedpreferences = getSharedPreferences(getResources().getString(R.string.USER_PREFERENCES), Context.MODE_PRIVATE);
        buttonToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                SharedPreferences.Editor editor = sharedpreferences.edit();
//
//                editor.putString(Username, uname);
//                editor.putString(Password, passwd);
//                editor.commit();
                check();

                intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void check() {
        String uname = editTextUserNameLogin.getText().toString();
//        String passwd = editTextPasswordLogin.getText().toString();

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", uname);
//        params.put("password", passwd);

        CustomRequest request = new CustomRequest(Request.Method.POST,
                getResources().getString(R.string.URL_GET_USER),
                params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getInt("status")==0){
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(getResources().getString(R.string.user_nama), (response.getString("data")));
//                                editor.putString(getResources().getString(R.string.user_password), (response.getString("data")));

                                editor.commit();

                            }else{
                                Toast.makeText(LoginActivity.this, "Pengguna belum terdaftar", Toast.LENGTH_LONG).show();
                            }
                            resetTexts();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                resetTexts();
                Toast.makeText(LoginActivity.this, "Connection problem occured, please try again", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void resetTexts(){
        editTextUserNameLogin.setText("");
        editTextPasswordLogin.setText("");
    }
}
