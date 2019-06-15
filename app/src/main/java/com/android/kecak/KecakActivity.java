package com.android.kecak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;


public class KecakActivity extends AppCompatActivity {

    TextView judulKecak, txtJadwal, txtHarga, txtDeskripsi;
    ImageView fotoKecak;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kecak);

        judulKecak = findViewById(R.id.judulKecak);
        txtJadwal = findViewById(R.id.txtJadwal);
        txtHarga = findViewById(R.id.txtHarga);
        txtDeskripsi = findViewById(R.id.txtDeskripsi);
        fotoKecak = findViewById(R.id.fotoKecak);

        Intent kecak = getIntent();
        String judul = kecak.getStringExtra("nama_kecak");
        String deskripsi = kecak.getStringExtra("deskripsi");
        String jadwal = kecak.getStringExtra("jadwal");
        String harga = kecak.getStringExtra("harga");
        String foto_kecak = kecak.getStringExtra("foto_kecak");

        judulKecak.setText(judul);
        txtJadwal.setText(jadwal);
        txtHarga.setText(harga);
        txtDeskripsi.setText(deskripsi);
        PicassoClient.downloadImage(getApplicationContext(), foto_kecak, fotoKecak);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(judul);

    }
}
