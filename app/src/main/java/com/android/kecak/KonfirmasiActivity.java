package com.android.kecak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class KonfirmasiActivity extends AppCompatActivity {

    TextView txtNamaPengunjung, txtTanggalPesan, txtJumlah, txtHarga, txtTotal;
    TextView txtIdPemesanan, txtIdPengunjung, txtIdKecak;
    TextView txtNamaBank, txtNoRekening;
    Button btnGaleri, btnKonfirmasi;

    @Override
    public void onBackPressed() {
        Intent back = new Intent(KonfirmasiActivity.this, PemesananActivity.class);
        startActivity(back);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi);

        txtIdPemesanan = findViewById(R.id.txtIdPemesanan);
        txtIdPengunjung = findViewById(R.id.txtIdPengunjung);
        txtIdKecak = findViewById(R.id.txtIdKecak);

        txtNamaPengunjung = findViewById(R.id.txtNamaPengunjung);
        txtTanggalPesan = findViewById(R.id.txtTanggalPesan);
        txtJumlah = findViewById(R.id.txtJumlah);
        txtHarga = findViewById(R.id.txtHarga);
        txtTotal = findViewById(R.id.txtTotal);

        txtNamaBank = findViewById(R.id.txtNamaBank);
        txtNoRekening = findViewById(R.id.txtNoRekening);

        Pengunjung user = SharedPrefManager.getInstance(this).getUser();
        txtNamaPengunjung.setText(user.getNama_pengunjung());

        Intent pemesanan = getIntent();
        final long id_pemesanan = pemesanan.getLongExtra("id_pemesanan",0);



        txtIdPemesanan.setText(String.valueOf(id_pemesanan));

        btnGaleri = findViewById(R.id.btnGaleri);
        btnGaleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galeri = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galeri, 100);
            }
        });

        btnKonfirmasi = findViewById(R.id.btnKonfirmasi);
        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
