package UJIRENTAL.model;

import java.sql.Timestamp;

public class PromoStatus {
    private int idPelanggan;
    private String namaPelanggan;
    private int minimalSewa;
    private int periodeBulan;
    private String hadiah;
    private int jumlahSewa;
    private boolean dapatGratis;
    private boolean sudahDipakai;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    public PromoStatus() {
    }

    public PromoStatus(int idPelanggan, String namaPelanggan, int jumlahSewa,
                       int minimalSewa, int periodeBulan, String hadiah) {
        this.idPelanggan = idPelanggan;
        this.namaPelanggan = namaPelanggan;
        this.jumlahSewa = jumlahSewa;
        this.minimalSewa = minimalSewa;
        this.periodeBulan = periodeBulan;
        this.hadiah = hadiah;
        this.dapatGratis = jumlahSewa >= minimalSewa;
        this.sudahDipakai = false;
    }

    public int getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(int idPelanggan) { this.idPelanggan = idPelanggan; }

    public String getNamaPelanggan() { return namaPelanggan; }
    public void setNamaPelanggan(String namaPelanggan) { this.namaPelanggan = namaPelanggan; }

    public int getMinimalSewa() { return minimalSewa; }
    public void setMinimalSewa(int minimalSewa) { 
        this.minimalSewa = minimalSewa;
        this.dapatGratis = this.jumlahSewa >= this.minimalSewa;
    }

    public int getPeriodeBulan() { return periodeBulan; }
    public void setPeriodeBulan(int periodeBulan) { this.periodeBulan = periodeBulan; }

    public String getHadiah() { return hadiah; }
    public void setHadiah(String hadiah) { this.hadiah = hadiah; }

    public int getJumlahSewa() { return jumlahSewa; }
    public void setJumlahSewa(int jumlahSewa) { 
        this.jumlahSewa = jumlahSewa;
        this.dapatGratis = jumlahSewa >= this.minimalSewa;
    }

    public boolean isDapatGratis() { return dapatGratis; }
    public void setDapatGratis(boolean dapatGratis) { this.dapatGratis = dapatGratis; }

    public boolean isSudahDipakai() { return sudahDipakai; }
    public void setSudahDipakai(boolean sudahDipakai) { this.sudahDipakai = sudahDipakai; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public int getProgressValue() {
        if (jumlahSewa >= minimalSewa) {
            return minimalSewa;
        }
        return jumlahSewa;
    }

    public int getPersenProgress() {
        if (minimalSewa == 0) return 0;
        int persen = (jumlahSewa * 100) / minimalSewa;
        if (persen > 100) persen = 100;
        return persen;
    }

    public int getSisaSewa() {
        int sisa = minimalSewa - jumlahSewa;
        if (sisa < 0) sisa = 0;
        return sisa;
    }

    public String getLabelStatus() {
        if (sudahDipakai) {
            return "Promo sedang digunakan.";
        }
        if (dapatGratis) {
            return "GRATIS 1x SEWA!";
        }
        return jumlahSewa + " / " + minimalSewa + " Sewa - " + getSisaSewa() + "x lagi GRATIS!";
    }

    @Override
    public String toString() {
        return namaPelanggan + " - " + getLabelStatus();
    }
}