package com.android.kecak;

public class Kecak{

    long id_kecak;
    String nama_kecak, deskripsi, jadwal, harga;
    String foto_kecak;

    public long getId_kecak() {
        return id_kecak;
    }

    public void setId_kecak(long id_kecak) {
        this.id_kecak = id_kecak;
    }

    public String getNama_kecak() {
        return nama_kecak;
    }

    public void setNama_kecak(String nama_kecak) {
        this.nama_kecak = nama_kecak;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getJadwal() {
        return jadwal;
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getFoto_kecak() {
        return foto_kecak;
    }

    public void setFoto_kecak(String foto_kecak) {
        this.foto_kecak = foto_kecak;
    }

    long id_pengunjung;
    String nama_pengunjung, alamat, email;

    public long getId_pengunjung() {
        return id_pengunjung;
    }

    public void setId_pengunjung(long id_pengunjung) {
        this.id_pengunjung = id_pengunjung;
    }

    public String getNama_pengunjung() {
        return nama_pengunjung;
    }

    public void setNama_pengunjung(String nama_pengunjung) {
        this.nama_pengunjung = nama_pengunjung;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
