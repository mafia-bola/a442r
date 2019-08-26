package com.android.kecak;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KonfirmasiActivity extends AppCompatActivity {

    TextView txtNamaPengunjung, txtTanggalPesan, txtJumlah, txtHarga, txtTotal;
    TextView txtIdPemesanan, txtIdPengunjung, txtIdKecak;
    TextView txtNamaBank, txtNoRekening;
    Button btnGaleri, btnKonfirmasi;
    Toolbar toolbar;
    ImageView imageView;

    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;

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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Konfirmasi Pemesanan");

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
        final long kecak_id = pemesanan.getLongExtra("kecak_id",0);
        final String tanggal_pesan = pemesanan.getStringExtra("tanggal_pesan");
        final long jumlah = pemesanan.getLongExtra("jumlah",0);
        final long harga = pemesanan.getLongExtra("harga",0);
        final long total = pemesanan.getLongExtra("total",0);

        txtIdPemesanan.setText(String.valueOf(id_pemesanan));
        txtIdPengunjung.setText(String.valueOf(user.getId_pengunjung()));
        txtIdKecak.setText(String.valueOf(kecak_id));
        txtTanggalPesan.setText(tanggal_pesan);
        txtJumlah.setText(String.valueOf(jumlah));
        txtHarga.setText(String.valueOf(harga));
        txtTotal.setText(String.valueOf(total));

        imageView = findViewById(R.id.imageView);
        btnGaleri = findViewById(R.id.btnGaleri);
        btnGaleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnKonfirmasi = findViewById(R.id.btnKonfirmasi);
        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(KonfirmasiActivity.this);
                alert
                        .setMessage("Apakah anda akan melakukan konfirmasi pemesanan ?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent konfirmasi = new Intent(KonfirmasiActivity.this, MenuActivity.class);
                                startActivity(konfirmasi);
                                finishAffinity();
                                uploadBitmap();
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
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadBitmap() {
        final long id_pemesanan = Long.parseLong(txtIdPemesanan.getText().toString());
        final String no_rekening = txtNoRekening.getText().toString();
        final String nama_bank = txtNamaBank.getText().toString();

        String urlAddress = getString(R.string.urlAddress);
        final String konfirmasiAddress = urlAddress+"api/konfirmasi/"+id_pemesanan;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, konfirmasiAddress,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("sukses")){
                                Toast.makeText(KonfirmasiActivity.this, "Konfirmasi telah dikirim", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(KonfirmasiActivity.this, "Gagal melakukan konfirmasi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KonfirmasiActivity.this, "Gagal melakukan konfirmasi", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("no_rekening", no_rekening);
                params.put("nama_bank", nama_bank);
                params.put("bukti_transfer", getStringImage(bitmap));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_cancel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnCancel:
                AlertDialog.Builder alert = new AlertDialog.Builder(KonfirmasiActivity.this);
                alert
                        .setTitle("Batalkan Pemesanan")
                        .setMessage("Apakan anda akan membatalkan Pemesanan ini ?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               batalPemesanan();
                               Intent batal = new Intent(KonfirmasiActivity.this, PemesananActivity.class);
                               startActivity(batal);
                               finish();
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
                return super.onOptionsItemSelected(item);
        }
    }

    private void batalPemesanan() {
        final long id_pemesanan = Long.parseLong(txtIdPemesanan.getText().toString());

        String urlAddress = getString(R.string.urlAddress);
        final String hapusPemesanan = urlAddress+"api/batalpesan/"+id_pemesanan;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, hapusPemesanan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("sukses")){
                                Toast.makeText(KonfirmasiActivity.this, "Pemesanan telah dibatalkan", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(KonfirmasiActivity.this, "Gagal melakukan pembatalan pesanan", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KonfirmasiActivity.this, "Gagal melakukan pemesanan", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id_pemesanan));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
