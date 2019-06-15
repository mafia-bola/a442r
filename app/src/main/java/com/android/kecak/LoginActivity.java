package com.android.kecak;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText editUsername, editPassword;
    Button btnMasuk, btnRegistrasi;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setMessage("Apakah anda mau menutup Aplikasi ?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.this.finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkValue();
        }
    };

    void checkValue(){
        String user = editUsername.getText().toString();
        String pass = editPassword.getText().toString();

        if (user.equals("") || pass.equals("")){
            btnMasuk.setEnabled(false);
        } else {
            btnMasuk.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnMasuk = findViewById(R.id.btnMasuk);
        btnRegistrasi = findViewById(R.id.btnRegistrasi);

        editUsername.addTextChangedListener(textWatcher);
        editPassword.addTextChangedListener(textWatcher);
        checkValue();

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editUsername.getText().toString().trim();
                final String password = editPassword.getText().toString().trim();
                String urlAddress = getString(R.string.urlAddress);
                final String loginAddress = urlAddress + "/api/auth/login";

                if (!email.isEmpty() && !password.isEmpty()){
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, loginAddress,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    JSONArray pengunjung = jsonObject.getJSONArray("pengunjung");

                                    if (status.equals("1")){
                                        for (int i=0; i<pengunjung.length(); i++){
                                            JSONObject object = pengunjung.getJSONObject(i);
                                            long id = object.getLong("id");
                                            String nama = object.getString("nama").trim();
                                            String alamat = object.getString("alamat").trim();
                                            String email = object.getString("email").trim();

                                            finish();
                                            Toast.makeText(LoginActivity.this, "Selamat Datang "+nama, Toast.LENGTH_SHORT).show();
                                            Intent menu = new Intent(LoginActivity.this, MenuActivity.class);
                                            menu.putExtra("id", id);
                                            menu.putExtra("nama", nama);
                                            menu.putExtra("alamat", alamat);
                                            menu.putExtra("email", email);
                                            startActivity(menu);
                                        }
                                    }

                                } catch (JSONException e){
                                    e.printStackTrace();
                                    Toast.makeText(LoginActivity.this, "Mohon email dan password diperiksa dengan baik", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LoginActivity.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", email);
                            params.put("password", password);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    requestQueue.add(stringRequest);
                } else {
                    Toast.makeText(LoginActivity.this, "Mohon email dan password diisi dengan benar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrasi = new Intent(LoginActivity.this, RegistrasiActivity.class);
                startActivity(registrasi);
                finish();
            }
        });
    }
}
