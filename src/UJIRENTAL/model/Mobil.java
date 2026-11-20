package UJIRENTAL.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Mobil {
    private String platNomor;
    private String merekMobil;
    private String tipeMobil;
    private BigDecimal hargaSewa;
    private String warna;
    private String transmisi;
    private String status;
    private int tahun;
    private int kapasitas;
    private String noRangka;
    private String noMesin;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Mobil() {
    }

    public Mobil(String platNomor, String merekMobil, String tipeMobil, BigDecimal hargaSewa,
                 String warna, String transmisi, String status, int tahun, int kapasitas,
                 String noRangka, String noMesin) {
        this.platNomor = platNomor;
        this.merekMobil = merekMobil;
        this.tipeMobil = tipeMobil;
        this.hargaSewa = hargaSewa;
        this.warna = warna;
        this.transmisi = transmisi;
        this.status = status;
        this.tahun = tahun;
        this.kapasitas = kapasitas;
        this.noRangka = noRangka;
        this.noMesin = noMesin;
    }

    public String getPlatNomor() { return platNomor; }
    public void setPlatNomor(String platNomor) { this.platNomor = platNomor; }

    public String getMerekMobil() { return merekMobil; }
    public void setMerekMobil(String merekMobil) { this.merekMobil = merekMobil; }

    public String getTipeMobil() { return tipeMobil; }
    public void setTipeMobil(String tipeMobil) { this.tipeMobil = tipeMobil; }

    public BigDecimal getHargaSewa() { return hargaSewa; }
    public void setHargaSewa(BigDecimal hargaSewa) { this.hargaSewa = hargaSewa; }

    public String getWarna() { return warna; }
    public void setWarna(String warna) { this.warna = warna; }

    public String getTransmisi() { return transmisi; }
    public void setTransmisi(String transmisi) { this.transmisi = transmisi; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getTahun() { return tahun; }
    public void setTahun(int tahun) { this.tahun = tahun; }

    public int getKapasitas() { return kapasitas; }
    public void setKapasitas(int kapasitas) { this.kapasitas = kapasitas; }

    public String getNoRangka() { return noRangka; }
    public void setNoRangka(String noRangka) { this.noRangka = noRangka; }

    public String getNoMesin() { return noMesin; }
    public void setNoMesin(String noMesin) { this.noMesin = noMesin; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return merekMobil + " " + tipeMobil + " - " + platNomor;
    }
}