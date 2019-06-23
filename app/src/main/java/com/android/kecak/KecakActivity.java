package com.android.kecak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

public class KecakActivity extends AppCompatActivity {

    TextView judulKecak, txtJadwal, txtHarga, txtDeskripsi;
    TextView txtNamaPengunjung;
    ImageView fotoKecak;
    Toolbar toolbar;
    Button btnPemesanan;

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

        Intent kecak1 = getIntent();
        final long id = kecak1.getLongExtra("id_kecak",0);
        final String judul = kecak1.getStringExtra("nama_kecak");
        final String deskripsi = kecak1.getStringExtra("deskripsi");
        final String jadwal = kecak1.getStringExtra("jadwal");
        final String harga = kecak1.getStringExtra("harga");
        final String foto_kecak = kecak1.getStringExtra("foto_kecak");
        final long id_pengunjung = kecak1.getLongExtra("id_pengunjung", 0);
        final String nama_pengunjung = kecak1.getStringExtra("nama_pengunjung");
        final String alamat_pengunjung = kecak1.getStringExtra("alamat");
        final String email_pengunjung = kecak1.getStringExtra("email");

        judulKecak.setText("Nama Tiket : "+judul);
        txtJadwal.setText("Jadwal : "+jadwal);
        txtHarga.setText("Harga : Rp "+harga);
        txtDeskripsi.setText("Deskripsi : "+deskripsi);
        txtNamaPengunjung.setText(nama_pengunjung);
        PicassoClient.downloadImage(getApplicationContext(), foto_kecak, fotoKecak);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(judul);

        btnPemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
