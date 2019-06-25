package com.android.kecak;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

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

public class KecakActivity extends AppCompatActivity {

    TextView judulKecak, txtJadwal, txtHarga, txtDeskripsi;
    TextView txtIdKecak, txtIdPengunjung;
    TextView txtNamaPengunjung;
    ImageView fotoKecak;
    Toolbar toolbar;
    Button btnPemesanan;

    EditText editJumlah;
    TextView txtTotal;
    TextView txtTanggal;
    Button btnPenjumlahan;

    long total = 0;
    long perhitungan = 0;
    long pesanan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kecak);

        judulKecak = findViewById(R.id.judulKecak);
        txtJadwal = findViewById(R.id.txtJadwal);
        txtHarga = findViewById(R.id.txtHarga);
        txtDeskripsi = findViewById(R.id.txtDeskripsi);
        fotoKecak = findViewById(R.id.fotoKecak);
        txtNamaPengunjung = findViewById(R.id.txtNamaPengunjung);
        btnPemesanan = findViewById(R.id.btnPemesanan);

        txtIdKecak = findViewById(R.id.txtIdKecak);
        txtIdPengunjung = findViewById(R.id.txtIdPengunjung);

        Intent kecak1 = getIntent();
        final long id = kecak1.getLongExtra("id_kecak",0);
        final String judul = kecak1.getStringExtra("nama_kecak");
        final String deskripsi = kecak1.getStringExtra("deskripsi");
        final String jadwal = kecak1.getStringExtra("jadwal");
        final String harga = kecak1.getStringExtra("harga");
        final String foto_kecak = kecak1.getStringExtra("foto_kecak");

        Pengunjung user = SharedPrefManager.getInstance(this).getUser();

        judulKecak.setText(judul);
        txtJadwal.setText(jadwal);
        txtHarga.setText(harga);
        txtDeskripsi.setText(deskripsi);
        txtNamaPengunjung.setText(user.getNama_pengunjung());
        PicassoClient.downloadImage(getApplicationContext(), foto_kecak, fotoKecak);

        txtIdKecak.setText(Long.toString(id));
        txtIdPengunjung.setText(Long.toString(user.getId_pengunjung()));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(judul);

        editJumlah = findViewById(R.id.editJumlah);
        txtTotal = findViewById(R.id.txtTotal);
        txtTanggal = findViewById(R.id.txtTanggal);
        btnPenjumlahan = findViewById(R.id.btnPenjumlahan);

        btnPenjumlahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesanan = Long.parseLong(editJumlah.getText().toString());
                perhitungan = Long.parseLong(txtHarga.getText().toString());

                total = pesanan * perhitungan;
                txtTotal.setText(String.valueOf(total));
            }
        });

        btnPemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pemesanan();
            }
        });
    }

    private void pemesanan() {
        final String id_pengunjung = txtIdPengunjung.getText().toString();
        final String id_kecak = txtIdKecak.getText().toString();
        final String tanggal_pesan = txtTanggal.getText().toString();
        final String jumlah = editJumlah.getText().toString();
        final String harga = txtHarga.getText().toString();
        final String total = txtTotal.getText().toString();

        String urlAddress = getString(R.string.urlAddress);
        final String pesanAddress = urlAddress + "api/pemesanan";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, pesanAddress,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("sukses")){
                                Toast.makeText(KecakActivity.this, "Pemesanan telah dikirim", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(KecakActivity.this, "Gagal melakukan pemesanan", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KecakActivity.this, "Gagal melakukan pemesanan", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("pengunjung_id", id_pengunjung);
                params.put("kecak_id", id_kecak);
                params.put("tanggal_pesan", tanggal_pesan);
                params.put("jumlah", jumlah);
                params.put("harga", harga);
                params.put("total", total);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
