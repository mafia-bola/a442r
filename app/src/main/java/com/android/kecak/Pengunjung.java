package com.android.kecak;

public class Pengunjung {

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

    public Pengunjung(long id_pengunjung, String nama_pengunjung, String alamat, String email) {
        this.id_pengunjung = id_pengunjung;
        this.nama_pengunjung = nama_pengunjung;
        this.alamat = alamat;
        this.email = email;
    }
}
