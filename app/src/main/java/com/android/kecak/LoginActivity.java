package com.android.kecak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LoginActivity extends AppCompatActivity {

    EditText editUsername, editPassword;
    Button btnMasuk, btnRegistrasi;

    private static final int MULTIPLE_PERMISSION_REQUEST = 1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnMasuk = findViewById(R.id.btnMasuk);
        btnRegistrasi = findViewById(R.id.btnRegistrasi);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MenuActivity.class));
        }

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
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

        if (checkingPermission()){
            Toast.makeText(this, "Akses telah diaktifkan", Toast.LENGTH_SHORT).show();
        } else {
            requestMultiplePermission();
        }
    }

    private void requestMultiplePermission() {
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]
                {
                        READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
                }, MULTIPLE_PERMISSION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MULTIPLE_PERMISSION_REQUEST:
                if (grantResults.length > 0){
                    boolean ReadPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WritePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (ReadPermission && WritePermission){
                        Toast.makeText(this, "Akses telah diaktifkan", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Akses tidak diaktifkan", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private boolean checkingPermission() {
        int onePermission = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int secondPermission = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return onePermission == PackageManager.PERMISSION_GRANTED && secondPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void userLogin() {
        final String email = editUsername.getText().toString().trim();
        final String password = editPassword.getText().toString().trim();

        String urlAddress = getString(R.string.urlAddress);
        final String loginAddress = urlAddress + "/api/auth/login";

        if (TextUtils.isEmpty(email)) {
            editUsername.setError("Mohon Periksa kembali email anda");
            editUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Masukkan kembali password anda");
            editPassword.requestFocus();
            return;
        }

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

                                    Pengunjung user = new Pengunjung(
                                            object.getLong("id"),
                                            object.getString("nama"),
                                            object.getString("alamat"),
                                            object.getString("email")
                                    );

                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                    finish();
                                    Toast.makeText(LoginActivity.this, "Selamat Datang "+object.getString("nama"), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "Mohon email dan password diperiksa dengan baik", Toast.LENGTH_SHORT).show();
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

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
