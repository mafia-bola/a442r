package com.android.kecak;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.uncopt.android.widget.text.justify.JustifiedTextView;
import java.util.ArrayList;

public class KecakAdapter extends BaseAdapter {

    Context c;
    ArrayList<Kecak> kecaks;

    public KecakAdapter(Context c, ArrayList<Kecak> kecaks) {
        this.c = c;
        this.kecaks = kecaks;
    }

    @Override
    public int getCount() {
        return kecaks.size();
    }

    @Override
    public Object getItem(int position) {
        return kecaks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(c).inflate(R.layout.kecak_model, parent, false);
        }

        JustifiedTextView txtKecak = convertView.findViewById(R.id.txtKecak);
        ImageView imageList = convertView.findViewById(R.id.imageKecak);

        final Kecak kecak = (Kecak) this.getItem(position);
        txtKecak.setText(kecak.getNama_kecak());
        PicassoClient.downloadImage(c, kecak.getFoto_kecak(), imageList);

        kecak.getId_kecak();
        kecak.getNama_kecak();
        kecak.getDeskripsi();
        kecak.getJadwal();
        kecak.getFoto_kecak();
        kecak.getHarga();

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKecak(kecak.getId_kecak(), kecak.getNama_kecak(), kecak.getDeskripsi(), kecak.getJadwal(),
                        kecak.getFoto_kecak(), kecak.getHarga());
            }
        });

        return convertView;
    }

    private void openKecak(
        long id_kecak,
        String nama_kecak, String deskripsi, String jadwal, String foto_kecak, String harga
    ) {
        Intent detailKecak = new Intent(c, KecakActivity.class);
        detailKecak.putExtra("id_kecak", id_kecak);
        detailKecak.putExtra("nama_kecak", nama_kecak);
        detailKecak.putExtra("deskripsi", deskripsi);
        detailKecak.putExtra("jadwal", jadwal);
        detailKecak.putExtra("foto_kecak", foto_kecak);
        detailKecak.putExtra("harga", harga);
        detailKecak.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(detailKecak);
    }
}
