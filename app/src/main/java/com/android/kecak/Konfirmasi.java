package com.android.kecak;

public class Konfirmasi {
    long id_pemesanan, pengunjung_id, kecak_id, user_id, jumlah, harga, total;
    String tanggal_pesan, bukti_transfer, no_rekening, nama_bank;
    String status;

    String foto_kecak, nama_kecak;

    public String getFoto_kecak() {
        return foto_kecak;
    }

    public void setFoto_kecak(String foto_kecak) {
        this.foto_kecak = foto_kecak;
    }

    public String getNama_kecak() {
        return nama_kecak;
    }

    public void setNama_kecak(String nama_kecak) {
        this.nama_kecak = nama_kecak;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getId_pemesanan() {
        return id_pemesanan;
    }

    public void setId_pemesanan(long id_pemesanan) {
        this.id_pemesanan = id_pemesanan;
    }

    public long getPengunjung_id() {
        return pengunjung_id;
    }

    public void setPengunjung_id(long pengunjung_id) {
        this.pengunjung_id = pengunjung_id;
    }

    public long getKecak_id() {
        return kecak_id;
    }

    public void setKecak_id(long kecak_id) {
        this.kecak_id = kecak_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getJumlah() {
        return jumlah;
    }

    public void setJumlah(long jumlah) {
        this.jumlah = jumlah;
    }

    public long getHarga() {
        return harga;
    }

    public void setHarga(long harga) {
        this.harga = harga;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getTanggal_pesan() {
        return tanggal_pesan;
    }

    public void setTanggal_pesan(String tanggal_pesan) {
        this.tanggal_pesan = tanggal_pesan;
    }

    public String getBukti_transfer() {
        return bukti_transfer;
    }

    public void setBukti_transfer(String bukti_transfer) {
        this.bukti_transfer = bukti_transfer;
    }

    public String getNo_rekening() {
        return no_rekening;
    }

    public void setNo_rekening(String no_rekening) {
        this.no_rekening = no_rekening;
    }

    public String getNama_bank() {
        return nama_bank;
    }

    public void setNama_bank(String nama_bank) {
        this.nama_bank = nama_bank;
    }
}
