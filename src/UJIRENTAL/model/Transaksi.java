package UJIRENTAL.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Transaksi {

    private int idTransaksi;
    private String platNomor;
    private int idPelanggan;

    private String namaMobil;
    private String namaPelanggan;

    private Date tanggalSewa;
    private Date tanggalKembali;
    private int lamaSewa;
    private BigDecimal totalHarga;
    private BigDecimal denda;
    private boolean gratisPromo;
    private String statusTransaksi;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Transaksi() {
    }

    public Transaksi(int idTransaksi, String platNomor, int idPelanggan, Date tanggalSewa,
                     Date tanggalKembali, int lamaSewa, BigDecimal totalHarga,
                     BigDecimal denda, String statusTransaksi) {
        this.idTransaksi = idTransaksi;
        this.platNomor = platNomor;
        this.idPelanggan = idPelanggan;
        this.tanggalSewa = tanggalSewa;
        this.tanggalKembali = tanggalKembali;
        this.lamaSewa = lamaSewa;
        this.totalHarga = totalHarga;
        this.denda = denda;
        this.statusTransaksi = statusTransaksi;
    }

    // Identitas
    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getPlatNomor() {
        return platNomor;
    }

    public void setPlatNomor(String platNomor) {
        this.platNomor = platNomor;
    }

    public int getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(int idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    // Nama Untuk Tampilan
    public String getNamaMobil() {
        return namaMobil;
    }

    public void setNamaMobil(String namaMobil) {
        this.namaMobil = namaMobil;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    // Detail Sewa
    public Date getTanggalSewa() {
        return tanggalSewa;
    }

    public void setTanggalSewa(Date tanggalSewa) {
        this.tanggalSewa = tanggalSewa;
    }

    public Date getTanggalKembali() {
        return tanggalKembali;
    }

    public void setTanggalKembali(Date tanggalKembali) {
        this.tanggalKembali = tanggalKembali;
    }

    public int getLamaSewa() {
        return lamaSewa;
    }

    public void setLamaSewa(int lamaSewa) {
        this.lamaSewa = lamaSewa;
    }

    public BigDecimal getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(BigDecimal totalHarga) {
        this.totalHarga = totalHarga;
    }

    public BigDecimal getDenda() {
        return denda;
    }

    public void setDenda(BigDecimal denda) {
        this.denda = denda;
    }

    public boolean isGratisPromo() {
        return gratisPromo;
    }

    public void setGratisPromo(boolean gratisPromo) {
        this.gratisPromo = gratisPromo;
    }

    public String getStatusTransaksi() {
        return statusTransaksi;
    }

    public void setStatusTransaksi(String statusTransaksi) {
        this.statusTransaksi = statusTransaksi;
    }

    // Waktu
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "TRX-" + idTransaksi + " - " + (namaPelanggan != null ? namaPelanggan : ("Pelanggan ID: " + idPelanggan));
    }
}