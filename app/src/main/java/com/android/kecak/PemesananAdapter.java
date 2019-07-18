package com.android.kecak;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PemesananAdapter extends BaseAdapter {

    Context c;
    ArrayList<Konfirmasi> pesan;

    public PemesananAdapter(Context c, ArrayList<Konfirmasi> pesan) {
        this.c = c;
        this.pesan = pesan;
    }

    @Override
    public int getCount() {
        return pesan.size();
    }

    @Override
    public Object getItem(int position) {
        return pesan.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(c).inflate(R.layout.model_pemesanan, parent, false);
        }

        TextView txtTanggalPesan = convertView.findViewById(R.id.txtTanggalPesan);
        TextView txtStatus = convertView.findViewById(R.id.txtStatus);
        ImageView imageKecak = convertView.findViewById(R.id.imageKecak);
        TextView txtNamaKecak = convertView.findViewById(R.id.txtNamaKecak);

        final Konfirmasi konfirmasi = (Konfirmasi) this.getItem(position);
        txtTanggalPesan.setText(konfirmasi.getTanggal_pesan());
        if (konfirmasi.getStatus().equals("0")){
            txtStatus.setText("Status : "+"Belum terkonfirmasi");
        } else {
            txtStatus.setText("Status : "+"Telah dikonfirmasi");
        }
        PicassoClient.downloadImage(c, konfirmasi.getFoto_kecak(), imageKecak);
        txtNamaKecak.setText(konfirmasi.getNama_kecak());

        konfirmasi.getId_pemesanan();
        konfirmasi.getPengunjung_id();
        konfirmasi.getKecak_id();
        konfirmasi.getTanggal_pesan();
        konfirmasi.getJumlah();
        konfirmasi.getHarga();
        konfirmasi.getTotal();
        konfirmasi.getStatus();

        Pengunjung user = SharedPrefManager.getInstance(c).getUser();
        TextView txtNamaPengunjung = convertView.findViewById(R.id.txtNamaPengunjung);
        txtNamaPengunjung.setText(user.getNama_pengunjung());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKonfirmasi(konfirmasi.getId_pemesanan(), konfirmasi.getPengunjung_id(), konfirmasi.getKecak_id(),
                        konfirmasi.getTanggal_pesan(), konfirmasi.getJumlah(), konfirmasi.getHarga(), konfirmasi.getTotal());
            }
        });

        return convertView;
    }

    private void openKonfirmasi(
            long id_pemesanan, long pengunjung_id, long kecak_id,
            String tanggal_pesan, long jumlah, long harga, long total)
    {
        Intent detailKonfirmasi = new Intent(c, KonfirmasiActivity.class);
        detailKonfirmasi.putExtra("id_pemesanan", id_pemesanan);
        detailKonfirmasi.putExtra("pengunjung_id", pengunjung_id);
        detailKonfirmasi.putExtra("kecak_id", kecak_id);
        detailKonfirmasi.putExtra("tanggal_pesan", tanggal_pesan);
        detailKonfirmasi.putExtra("jumlah", jumlah);
        detailKonfirmasi.putExtra("harga", harga);
        detailKonfirmasi.putExtra("total", total);
        detailKonfirmasi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(detailKonfirmasi);
    }
}
