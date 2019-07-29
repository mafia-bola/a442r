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

public class TiketActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    ArrayList<Konfirmasi> pesan = new ArrayList<>();

    @Override
    public void onBackPressed() {
        Intent back = new Intent(TiketActivity.this, MenuActivity.class);
        startActivity(back);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiket);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        Pengunjung user = SharedPrefManager.getInstance(this).getUser();

        listView = findViewById(R.id.listView);
        dataPemesanan();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tiket Kecak");
    }

    private void dataPemesanan() {
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        Pengunjung user = SharedPrefManager.getInstance(this).getUser();

        String urlAddress = getString(R.string.urlAddress);
        final String pemesananAddress = urlAddress + "api/catatan/" + user.getId_pengunjung();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, pemesananAddress,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String info = jsonObject.getString("status");

                            JSONArray data = jsonObject.getJSONArray("data");
                            Konfirmasi konfirmasi;

                            if (info.equals("tersedia")){
                                for (int i=0; i<data.length(); i++){
                                    JSONObject pemesanan = data.getJSONObject(i);

                                    long id_pemesanan = pemesanan.getLong("id");
                                    long pengunjung_id = pemesanan.getLong("pengunjung_id");
                                    long kecak_id = pemesanan.getLong("kecak_id");
                                    String tanggal_pesan = pemesanan.getString("tanggal_pesan");
                                    long jumlah = pemesanan.getLong("jumlah");
                                    long harga = pemesanan.getLong("harga");
                                    long total = pemesanan.getLong("total");
                                    String bukti_transfer = pemesanan.getString("bukti_transfer");
                                    String no_rekening = pemesanan.getString("no_rekening");
                                    String nama_bank = pemesanan.getString("nama_bank");
                                    String status = pemesanan.getString("status");
                                    String foto_kecak = pemesanan.getString("foto");
                                    String nama_kecak = pemesanan.getString("nama_kecak");

                                    String urlAddress = getString(R.string.imageAddress);
                                    foto_kecak = urlAddress+foto_kecak;

                                    String link = getString(R.string.imageAddress);
                                    bukti_transfer = link+bukti_transfer;

                                    konfirmasi = new Konfirmasi();
                                    konfirmasi.setId_pemesanan(id_pemesanan);
                                    konfirmasi.setPengunjung_id(pengunjung_id);
                                    konfirmasi.setKecak_id(kecak_id);
                                    konfirmasi.setTanggal_pesan(tanggal_pesan);
                                    konfirmasi.setJumlah(jumlah);
                                    konfirmasi.setHarga(harga);
                                    konfirmasi.setTotal(total);
                                    konfirmasi.setBukti_transfer(bukti_transfer);
                                    konfirmasi.setNo_rekening(no_rekening);
                                    konfirmasi.setNama_bank(nama_bank);
                                    konfirmasi.setStatus(status);
                                    konfirmasi.setFoto_kecak(foto_kecak);
                                    konfirmasi.setNama_kecak(nama_kecak);

                                    pesan.add(konfirmasi);
                                }
                            }

                            TiketAdapter adapter = new TiketAdapter(getApplicationContext(), pesan);
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
