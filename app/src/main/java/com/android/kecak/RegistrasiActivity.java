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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrasiActivity extends AppCompatActivity {

    private EditText txtNama, txtAlamat, txtUsername, txtPassword;
    Button btnDaftar, btnClean;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setMessage("Apakah anda akan kembali ke Login ?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent login = new Intent(RegistrasiActivity.this, LoginActivity.class);
                        startActivity(login);
                        finish();
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
            checkPengisian();
        }
    };

    void checkPengisian(){
        String nama = txtNama.getText().toString();
        String alamat = txtAlamat.getText().toString();
        String email = txtUsername.getText().toString();
        String pass = txtPassword.getText().toString();

        if (nama.equals("") || alamat.equals("") || email.equals("") || pass.equals("")){
            btnDaftar.setEnabled(false);
        } else {
            btnDaftar.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        txtNama = findViewById(R.id.txtNama);
        txtAlamat = findViewById(R.id.txtAlamat);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnDaftar = findViewById(R.id.btnDaftar);

        txtNama.addTextChangedListener(textWatcher);
        txtAlamat.addTextChangedListener(textWatcher);
        txtUsername.addTextChangedListener(textWatcher);
        txtPassword.addTextChangedListener(textWatcher);
        checkPengisian();

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(RegistrasiActivity.this);
                alert
                        .setMessage("Apakah anda akan yakin dengan Registrasi Data berikut ?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent login = new Intent(RegistrasiActivity.this, LoginActivity.class);
                                startActivity(login);
                                finish();
                                registrasi();
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
        });
    }

    private void registrasi() {
        final String nama = this.txtNama.getText().toString().trim();
        final String alamat = this.txtAlamat.getText().toString().trim();
        final String email = this.txtUsername.getText().toString().trim();
        final String password = this.txtPassword.getText().toString().trim();

        String urlAddress = getString(R.string.urlAddress);
        final String registerAddress = urlAddress + "/api/auth/register";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, registerAddress,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("sukses")){
                                Toast.makeText(RegistrasiActivity.this, "Registrasi berhasil, silahkan kembali ke menu login", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(RegistrasiActivity.this, "Mohon periksa kembali data yang diisi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistrasiActivity.this, "Mohon periksa kembali data yang diisi", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("alamat", alamat);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
