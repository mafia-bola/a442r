package com.android.kecak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
            convertView = LayoutInflater.from(c).inflate(R.layout.pemesanan_model, parent, false);
        }

//        TextView txtIdPemesanan = convertView.findViewById(R.id.txtIdPemesanan);
//        TextView txtIdPengunjung = convertView.findViewById(R.id.txtIdPengunjung);
//        TextView txtIdKecak = convertView.findViewById(R.id.txtIdKecak);
        TextView txtTanggalPesan = convertView.findViewById(R.id.txtTanggalPesan);
        TextView txtJumlah = convertView.findViewById(R.id.txtJumlah);
        TextView txtHarga = convertView.findViewById(R.id.txtHarga);
        TextView txtTotal = convertView.findViewById(R.id.txtTotal);
        TextView txtStatus = convertView.findViewById(R.id.txtStatus);

        final Konfirmasi konfirmasi = (Konfirmasi) this.getItem(position);
//        txtIdPemesanan.setText(String.valueOf(konfirmasi.getId_pemesanan()));
//        txtIdPengunjung.setText(String.valueOf(konfirmasi.getPengunjung_id()));
//        txtIdKecak.setText(String.valueOf(konfirmasi.getKecak_id()));
        txtTanggalPesan.setText(konfirmasi.getTanggal_pesan());
//        txtJumlah.setText(String.valueOf(konfirmasi.getJumlah()));
//        txtHarga.setText(String.valueOf(konfirmasi.getHarga()));
        txtTotal.setText(String.valueOf(konfirmasi.getTotal()));
//        txtStatus.setText(konfirmasi.getStatus());

        if (konfirmasi.getStatus().equals("0")){
            txtStatus.setText("Belum terkonfirmasi");
        } else {
            txtStatus.setText("Telah dikonfirmasi");
        }

        Pengunjung user = SharedPrefManager.getInstance(c).getUser();
        TextView txtNamaPengunjung = convertView.findViewById(R.id.txtNamaPengunjung);
        txtNamaPengunjung.setText(user.getNama_pengunjung());

        return convertView;
    }
}
