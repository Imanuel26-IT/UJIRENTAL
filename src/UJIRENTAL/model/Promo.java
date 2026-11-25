package UJIRENTAL.model;

import java.sql.Timestamp;

public class Promo {

    private int idPromo;
    private String namaPromo;

    private int minimalSewa;
    private int periodeBulan;
    private String hadiah;
    private String statusPromo;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Promo() {
    }

    // Identitas
    public int getIdPromo() {
        return idPromo;
    }

    public void setIdPromo(int idPromo) {
        this.idPromo = idPromo;
    }

    public String getNamaPromo() {
        return namaPromo;
    }

    public void setNamaPromo(String namaPromo) {
        this.namaPromo = namaPromo;
    }

    // Aturan Promo
    public int getMinimalSewa() {
        return minimalSewa;
    }

    public void setMinimalSewa(int minimalSewa) {
        this.minimalSewa = minimalSewa;
    }

    public int getPeriodeBulan() {
        return periodeBulan;
    }

    public void setPeriodeBulan(int periodeBulan) {
        this.periodeBulan = periodeBulan;
    }

    public String getHadiah() {
        return hadiah;
    }

    public void setHadiah(String hadiah) {
        this.hadiah = hadiah;
    }

    public String getStatusPromo() {
        return statusPromo;
    }

    public void setStatusPromo(String statusPromo) {
        this.statusPromo = statusPromo;
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
        return namaPromo + " - " + minimalSewa + "x Gratis";
    }
}