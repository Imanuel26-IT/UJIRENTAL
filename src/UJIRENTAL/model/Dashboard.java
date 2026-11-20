package UJIRENTAL.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Dashboard {
    private BigDecimal pendapatanHariIni = BigDecimal.ZERO;
    private BigDecimal pendapatanBulanIni = BigDecimal.ZERO;
    private BigDecimal totalPendapatan = BigDecimal.ZERO;
    private int totalMobil;
    private int mobilTersedia;
    private int mobilDisewa;
    private int mobilPerawatan; 
    private int totalPelanggan;
    private int transaksiAktif;
    private int transaksiSelesai;
    private int transaksiTerlambat;

    private List<Transaksi> transaksiTerbaru = new ArrayList<>();
    private List<PromoStatus> daftarPromoPelanggan = new ArrayList<>();

    public BigDecimal getPendapatanHariIni() { return pendapatanHariIni; }
    public void setPendapatanHariIni(BigDecimal pendapatanHariIni) { this.pendapatanHariIni = pendapatanHariIni; }

    public BigDecimal getPendapatanBulanIni() { return pendapatanBulanIni; }
    public void setPendapatanBulanIni(BigDecimal pendapatanBulanIni) { this.pendapatanBulanIni = pendapatanBulanIni; }

    public BigDecimal getTotalPendapatan() { return totalPendapatan; }
    public void setTotalPendapatan(BigDecimal totalPendapatan) { this.totalPendapatan = totalPendapatan; }

    public int getTotalMobil() { return totalMobil; }
    public void setTotalMobil(int totalMobil) { this.totalMobil = totalMobil; }

    public int getMobilTersedia() { return mobilTersedia; }
    public void setMobilTersedia(int mobilTersedia) { this.mobilTersedia = mobilTersedia; }

    public int getMobilDisewa() { return mobilDisewa; }
    public void setMobilDisewa(int mobilDisewa) { this.mobilDisewa = mobilDisewa; }

    public int getMobilPerawatan() { return mobilPerawatan; }
    public void setMobilPerawatan(int mobilPerawatan) { this.mobilPerawatan = mobilPerawatan; }

    public int getTotalPelanggan() { return totalPelanggan; }
    public void setTotalPelanggan(int totalPelanggan) { this.totalPelanggan = totalPelanggan; }

    public int getTransaksiAktif() { return transaksiAktif; }
    public void setTransaksiAktif(int transaksiAktif) { this.transaksiAktif = transaksiAktif; }

    public int getTransaksiSelesai() { return transaksiSelesai; }
    public void setTransaksiSelesai(int transaksiSelesai) { this.transaksiSelesai = transaksiSelesai; }

    public int getTransaksiTerlambat() { return transaksiTerlambat; }
    public void setTransaksiTerlambat(int transaksiTerlambat) { this.transaksiTerlambat = transaksiTerlambat; }

    public List<Transaksi> getTransaksiTerbaru() { return transaksiTerbaru; }
    public void setTransaksiTerbaru(List<Transaksi> transaksiTerbaru) { this.transaksiTerbaru = transaksiTerbaru; }

    public List<PromoStatus> getDaftarPromoPelanggan() { return daftarPromoPelanggan; }
    public void setDaftarPromoPelanggan(List<PromoStatus> daftarPromoPelanggan) { this.daftarPromoPelanggan = daftarPromoPelanggan; }
}