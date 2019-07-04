package com.android.kecak;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KecakActivity extends AppCompatActivity {

    TextView judulKecak, txtJadwal, txtHarga;
    TextView txtIdKecak, txtIdPengunjung;
    TextView txtNamaPengunjung;
    ImageView fotoKecak;
    Toolbar toolbar;
    Button btnPemesanan;
    JustifiedTextView txtDeskripsi;

    EditText editJumlah;
    TextView txtTotal;
    TextView txtTanggal;
    Button btnPenjumlahan;
    Button btnTanggal;

    SimpleDateFormat dateFormat;

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
        editJumlah.setText("0");

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        txtTanggal = findViewById(R.id.txtTanggal);
        btnTanggal = findViewById(R.id.btnTanggal);
        btnTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        txtTotal = findViewById(R.id.txtTotal);
        btnPenjumlahan = findViewById(R.id.btnPenjumlahan);

        btnPenjumlahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String perhitungan;
                String pesanan;

                pesanan = editJumlah.getText().toString().trim();
                perhitungan = txtHarga.getText().toString().trim();

                long total = Long.parseLong(pesanan) * Long.parseLong(perhitungan);
                txtTotal.setText(String.valueOf(total));
            }
        });

        btnPemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent konfirmasi = new Intent(KecakActivity.this, PemesananActivity.class);
                startActivity(konfirmasi);
                finishAffinity();
                pemesanan();
            }
        });
    }

    private void showDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(KecakActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                txtTanggal.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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
