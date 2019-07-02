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

        TextView txtTanggalPesan = convertView.findViewById(R.id.txtTanggalPesan);
        TextView txtStatus = convertView.findViewById(R.id.txtStatus);

        final Konfirmasi konfirmasi = (Konfirmasi) this.getItem(position);
        txtTanggalPesan.setText(konfirmasi.getTanggal_pesan());
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
