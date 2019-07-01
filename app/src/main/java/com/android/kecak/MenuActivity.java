package com.android.kecak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class MenuActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ListView listView;
    ArrayList<Kecak> kecaks = new ArrayList<>();

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
        alert
                .setMessage("Apakah anda akan Logout ?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        SharedPrefManager.getInstance(getApplicationContext()).logout();
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
        setContentView(R.layout.activity_menu);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        Pengunjung user = SharedPrefManager.getInstance(this).getUser();

        listView = findViewById(R.id.listView);
        dataKecak();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kecak Uluwatu");
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        JustifiedTextView txtNama = headerView.findViewById(R.id.txtNama);
        JustifiedTextView txtEmail = headerView.findViewById(R.id.txtEmail);
        JustifiedTextView txtAlamat = headerView.findViewById(R.id.txtAlamat);

        txtNama.setText(user.getNama_pengunjung());
        txtEmail.setText(user.getEmail());
        txtAlamat.setText(user.getAlamat());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.logOut:
                        AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
                        alert
                                .setMessage("Apakah anda akan Logout ?")
                                .setCancelable(false)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        SharedPrefManager.getInstance(getApplicationContext()).logout();
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
                        return true;
                    default:
                        return true;
                }
            }
        });

        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void dataKecak() {
        String urlAddress = getString(R.string.urlAddress);
        final String kecakAddress = urlAddress + "api/tiket/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, kecakAddress,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String info = jsonObject.getString("info");

                            JSONArray infoKecak = jsonObject.getJSONArray("kecak");
                            Kecak kecak;

                            if (info.equals("tersedia")){
                                for (int i=0; i<infoKecak.length(); i++) {
                                    JSONObject dataKecak = infoKecak.getJSONObject(i);

                                    long id_kecak = dataKecak.getLong("id");
                                    String nama_kecak = dataKecak.getString("nama_kecak");
                                    String deskripsi = dataKecak.getString("deskripsi");
                                    String jadwal = dataKecak.getString("jadwal");
                                    String foto_kecak = dataKecak.getString("foto");
                                    String harga = dataKecak.getString("harga");

                                    String urlAddress = getString(R.string.urlAddress);
                                    foto_kecak = urlAddress+foto_kecak;

                                    kecak = new Kecak();
                                    kecak.setId_kecak(id_kecak);
                                    kecak.setNama_kecak(nama_kecak);
                                    kecak.setDeskripsi(deskripsi);
                                    kecak.setJadwal(jadwal);
                                    kecak.setFoto_kecak(foto_kecak);
                                    kecak.setHarga(harga);

                                    kecaks.add(kecak);
                                }
                            }
                            KecakAdapter adapter = new KecakAdapter(getApplicationContext(), kecaks);
                            listView.setAdapter(adapter);

                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        Pengunjung user = SharedPrefManager.getInstance(this).getUser();

        switch (item.getItemId()) {
            case R.id.btnPesan:
                Intent konfirmasi = new Intent(MenuActivity.this, KonfirmasiActivity.class);
                startActivity(konfirmasi);
            case R.id.btnProfil:
                AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
                alert
                        .setTitle("Login")
                        .setMessage("Nama : "+user.getNama_pengunjung()+"\n"+
                                "Email : "+user.getEmail()+"\n"+"Alamat : "+user.getAlamat())
                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
