package com.android.kecak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class KecakList extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    ArrayList<Kecak> kecaks = new ArrayList<>();

    @Override
    public void onBackPressed() {
        Intent back = new Intent(KecakList.this, MenuActivity.class);
        startActivity(back);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kecak_list);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        Pengunjung user = SharedPrefManager.getInstance(this).getUser();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kecak Uluwatu");

        listView = findViewById(R.id.listView);
        dataKecak();
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
}
