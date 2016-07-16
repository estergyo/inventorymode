package com.example.priansyah.demo1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.priansyah.demo1.Model.UserCheckResponse;
import android.location.LocationListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    SessionManager session;
    protected LocationManager locationManager;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 10; // dalam Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 60000; // dalam Milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Registrasi");

        session = new SessionManager(getApplicationContext());

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

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MINIMUM_TIME_BETWEEN_UPDATES,
                    MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                    new MyLocationListener()
            );

        } catch (SecurityException e) {
            Toast.makeText(RegisterActivity.this,"Ada masalah dengan GPS", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkInput() {
        if (editTextNamaTokoRegister.getText().toString().equals("") || editTextNamaUserRegister.getText().toString().equals("")
                || editTextEmailRegister.getText().toString().equals("") || editTextUnameRegister.getText().toString().equals("")
                || editTextPasswordRegister.getText().toString().equals("") || editTextConfirmPasswordRegister.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "data pengguna tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        else {
            if (editTextPasswordRegister.getText().toString().equals(editTextConfirmPasswordRegister.getText().toString())){
                registerUser();
                Toast.makeText(RegisterActivity.this, "berhasil daftar", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(RegisterActivity.this, "password tidak cocok", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void registerUser() {
        final String toko = editTextNamaTokoRegister.getText().toString().trim();
        final String nama = editTextNamaUserRegister.getText().toString().trim();
        final String email = editTextEmailRegister.getText().toString().trim();
        final String username = editTextUnameRegister.getText().toString().trim();
        final String password = editTextPasswordRegister.getText().toString().trim();

        final SQLiteDatabase db = openOrCreateDatabase("POS", MODE_PRIVATE, null);
        db.execSQL("Create table if not exists user(store_name VARCHAR, name VARCHAR, email VARCHAR, username VARCHAR, password VARCHAR);");
        final Cursor uname = db.rawQuery("select * from user where username = '" + username + "' ", null);
        final Cursor mail = db.rawQuery("select * from user where email = '" + email + "' ", null);

        StringRequest request = new StringRequest(Request.Method.GET, getResources().getString(R.string.URL_GET_USERNAME), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                UserCheckResponse responseModel = MainApplication.gson.fromJson(response, UserCheckResponse.class);
                //silakan diapa-apain ini responsenya :D
                if (responseModel.getData().equals(username) || uname.getCount()!=0) {
                    Toast.makeText(RegisterActivity.this, responseModel.getNotifikasi(), Toast.LENGTH_SHORT).show();
                }
                else if (!responseModel.getData().equals(username) || uname.getCount()==0){
                    StringRequest request2 = new StringRequest(Request.Method.GET, getResources().getString(R.string.URL_GET_USEREMAIL), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response2) {
                            Log.d("response2", response2);
                            UserCheckResponse responseModel2 = MainApplication.gson.fromJson(response2, UserCheckResponse.class);
                            if (responseModel2.getData().equals(email) || mail.getCount()!=0) {
                                Toast.makeText(RegisterActivity.this, responseModel2.getNotifikasi(), Toast.LENGTH_SHORT).show();
                            }
                            else if (!responseModel2.getData().equals(email) && mail.getCount()==0){
                                db.execSQL("insert into user values('" + toko + "', '" + nama + "', '" + email + "', '" + username + "', '" + password + "')");

                                session.createLoginSession(username);
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                //kalo butuh status codenya
                                Log.e("Error code", String.valueOf(error.networkResponse.statusCode));
                                Log.e("Error", new String(error.networkResponse.data));
                            }
                        }
                    });
                    MainApplication.getInstance().getRequestQueue().add(request2);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    //kalo butuh status codenya
                    Log.e("Error code", String.valueOf(error.networkResponse.statusCode));
                    Log.e("Error", new String(error.networkResponse.data));
                }
            }
        });
        MainApplication.getInstance().getRequestQueue().add(request);
    }
    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            String message = String.format(
                    "Deteksi Lokasi Baru \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
            //switchToMap();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(RegisterActivity.this, "Status provider berubah",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(RegisterActivity.this,
                    "Provider dinonaktifkan oleh user, GPS off",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(RegisterActivity.this,
                    "Provider diaktifkan oleh user, GPS on",
                    Toast.LENGTH_LONG).show();
        }

    }
}
