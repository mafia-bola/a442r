package com.android.kecak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class TiketDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imageKecak, imageBuktiTransfer;
    TextView txtNamaPengunjung, txtTanggalPesan, txtJumlah, txtHarga, txtTotal, txtNoRekening, txtNamaBank;
    TextView txtIdPemesanan, txtIdPengunjung, txtIdKecak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        imageKecak = findViewById(R.id.imageKecak);
        imageBuktiTransfer = findViewById(R.id.imageBuktiTransfer);
        txtNamaPengunjung = findViewById(R.id.txtNamaPengunjung);
        txtTanggalPesan = findViewById(R.id.txtTanggalPesan);
        txtJumlah = findViewById(R.id.txtJumlah);
        txtHarga = findViewById(R.id.txtHarga);
        txtTotal = findViewById(R.id.txtTotal);
        txtNoRekening = findViewById(R.id.txtNoRekening);
        txtNamaBank = findViewById(R.id.txtNamaBank);

        txtIdPemesanan = findViewById(R.id.txtIdPemesanan);
        txtIdPengunjung = findViewById(R.id.txtIdPengunjung);
        txtIdKecak = findViewById(R.id.txtIdKecak);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("History Pemesanan");

        Pengunjung user = SharedPrefManager.getInstance(this).getUser();
        txtNamaPengunjung.setText(user.getNama_pengunjung());

        Intent history = getIntent();
        final long id_pemesanan = history.getLongExtra("id_pemesanan",0);
        final long pengunjung_id = history.getLongExtra("pengunjung_id", 0);
        final long kecak_id = history.getLongExtra("kecak_id",0);
        final String tanggal_pesan = history.getStringExtra("tanggal_pesan");
        final long jumlah = history.getLongExtra("jumlah",0);
        final long harga = history.getLongExtra("harga",0);
        final long total = history.getLongExtra("total",0);
        final String no_rekening = history.getStringExtra("no_rekening");
        final String nama_bank = history.getStringExtra("nama_bank");

        final String bukti_transfer = history.getStringExtra("bukti_transfer");
        final String foto_kecak = history.getStringExtra("foto");

        txtIdPemesanan.setText(String.valueOf(id_pemesanan));
        txtIdPengunjung.setText(String.valueOf(pengunjung_id));
        txtIdKecak.setText(String.valueOf(kecak_id));
        txtTanggalPesan.setText(tanggal_pesan);
        txtJumlah.setText(String.valueOf(jumlah));
        txtHarga.setText(String.valueOf(harga));
        txtTotal.setText(String.valueOf(total));
        txtNoRekening.setText(no_rekening);
        txtNamaBank.setText(nama_bank);

        PicassoClient.downloadImage(this, foto_kecak, imageKecak);
        PicassoClient.downloadImage(this, bukti_transfer, imageBuktiTransfer);
    }
}
