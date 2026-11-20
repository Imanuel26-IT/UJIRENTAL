/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UJIRENTAL.controller;

import UJIRENTAL.model.Dashboard;
import UJIRENTAL.model.Transaksi;
import UJIRENTAL.util.Koneksi;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IMANUEL VALEN LINGGI
 */
public class TransaksiController {

    private final PromoServis promoServis = new PromoServis();

    // Ambil semua transaksi lengkap dengan nama mobil & nama pelanggan (untuk JTable MenuTransaksi)
    public List<Transaksi> getAllTransaksi() {
        List<Transaksi> listTransaksi = new ArrayList<>();
        String sql = "SELECT t.*, "
                   + "CONCAT(m.merek_mobil, ' ', m.tipe_mobil, ' - ', m.plat_nomor) AS nama_mobil, "
                   + "p.nama_pelanggan "
                   + "FROM transaksi t "
                   + "JOIN mobil m ON t.plat_nomor = m.plat_nomor "
                   + "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                   + "ORDER BY t.created_at DESC";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                listTransaksi.add(mapResultSetToTransaksi(rs));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data transaksi: " + e.getMessage());
        }
        return listTransaksi;
    }

    // Filter transaksi berdasarkan status ('Sedang Berjalan', 'Selesai', 'Dibatalkan')
    public List<Transaksi> getTransaksiByStatus(String statusTransaksi) {
        List<Transaksi> listTransaksi = new ArrayList<>();
        String sql = "SELECT t.*, "
                   + "CONCAT(m.merek_mobil, ' ', m.tipe_mobil, ' - ', m.plat_nomor) AS nama_mobil, "
                   + "p.nama_pelanggan "
                   + "FROM transaksi t "
                   + "JOIN mobil m ON t.plat_nomor = m.plat_nomor "
                   + "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                   + "WHERE t.status_transaksi = ? "
                   + "ORDER BY t.created_at DESC";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statusTransaksi);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listTransaksi.add(mapResultSetToTransaksi(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal memfilter transaksi: " + e.getMessage());
        }
        return listTransaksi;
    }

    // Pencarian transaksi berdasarkan plat nomor, nama pelanggan, atau ID transaksi
    public List<Transaksi> cariTransaksi(String keyword) {
        List<Transaksi> listHasil = new ArrayList<>();
        if (keyword == null) {
            System.err.println("Pencarian transaksi gagal: kata kunci tidak boleh null.");
            return listHasil;
        }

        String sql = "SELECT t.*, "
                   + "CONCAT(m.merek_mobil, ' ', m.tipe_mobil, ' - ', m.plat_nomor) AS nama_mobil, "
                   + "p.nama_pelanggan "
                   + "FROM transaksi t "
                   + "JOIN mobil m ON t.plat_nomor = m.plat_nomor "
                   + "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                   + "WHERE t.plat_nomor LIKE ? OR p.nama_pelanggan LIKE ? "
                   + "OR CAST(t.id_transaksi AS CHAR) LIKE ? "
                   + "ORDER BY t.created_at DESC";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pola = "%" + keyword + "%";
            ps.setString(1, pola);
            ps.setString(2, pola);
            ps.setString(3, pola);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listHasil.add(mapResultSetToTransaksi(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mencari transaksi: " + e.getMessage());
        }
        return listHasil;
    }

    /**
     * Menyimpan transaksi baru dan otomatis memakai promo jika pelanggan memenuhi syarat.
     */
    public boolean tambahTransaksi(Transaksi trx) {
        if (trx == null || trx.getPlatNomor() == null || trx.getPlatNomor().trim().isEmpty()
                || trx.getIdPelanggan() <= 0 || trx.getTanggalSewa() == null
                || trx.getTanggalKembali() == null || trx.getTanggalKembali().compareTo(trx.getTanggalSewa()) <= 0
                || trx.getLamaSewa() <= 0
                || ChronoUnit.DAYS.between(trx.getTanggalSewa().toLocalDate(), trx.getTanggalKembali().toLocalDate())
                        != trx.getLamaSewa()
                || trx.getTotalHarga() == null
                || trx.getTotalHarga().signum() < 0
                || (trx.getDenda() != null && trx.getDenda().signum() < 0)) {
            System.err.println("Transaksi gagal: data transaksi tidak valid.");
            return false;
        }

        String sqlInsert = "INSERT INTO transaksi (plat_nomor, id_pelanggan, tanggal_sewa, tanggal_kembali, "
                         + "lama_sewa, total_harga, denda, gratis_promo, status_transaksi) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlKunciPelanggan = "SELECT id_pelanggan FROM pelanggan WHERE id_pelanggan = ? FOR UPDATE";
        String sqlUpdateMobil = "UPDATE mobil SET status = 'Disewa' WHERE plat_nomor = ? AND status = 'Tersedia'";

        try (Connection conn = Koneksi.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psPelanggan = conn.prepareStatement(sqlKunciPelanggan);
                 PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
                 PreparedStatement psMobil = conn.prepareStatement(sqlUpdateMobil)) {

                psPelanggan.setInt(1, trx.getIdPelanggan());
                try (ResultSet rs = psPelanggan.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        System.err.println("Transaksi dibatalkan: Pelanggan tidak ditemukan.");
                        return false;
                    }
                }

                boolean pakaiGratisPromo = promoServis.isPromoAvailable(conn, trx.getIdPelanggan());

                // Cek apakah mobil masih tersedia sebelum dikunci transaksi
                psMobil.setString(1, trx.getPlatNomor());
                int mobilUpdated = psMobil.executeUpdate();
                if (mobilUpdated == 0) {
                    conn.rollback();
                    System.err.println("Transaksi dibatalkan: Mobil tidak tersedia atau sedang disewa.");
                    return false;
                }

                psInsert.setString(1, trx.getPlatNomor());
                psInsert.setInt(2, trx.getIdPelanggan());
                psInsert.setDate(3, trx.getTanggalSewa());
                psInsert.setDate(4, trx.getTanggalKembali());
                psInsert.setInt(5, trx.getLamaSewa());
                psInsert.setBigDecimal(6, pakaiGratisPromo ? BigDecimal.ZERO : trx.getTotalHarga());
                psInsert.setBigDecimal(7, trx.getDenda() != null ? trx.getDenda() : BigDecimal.ZERO);
                psInsert.setBoolean(8, pakaiGratisPromo);
                psInsert.setString(9, "Sedang Berjalan");

                psInsert.executeUpdate();
                conn.commit();
                trx.setGratisPromo(pakaiGratisPromo);
                if (pakaiGratisPromo) {
                    trx.setTotalHarga(BigDecimal.ZERO);
                }
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan transaksi baru: " + e.getMessage());
            return false;
        }
    }

    /**
     * Menyelesaikan transaksi (pengembalian mobil) secara atomik:
     * 1. Update status_transaksi = 'Selesai' beserta nominal denda (jika terlambat)
     * 2. Kembalikan status mobil menjadi 'Tersedia'
     */
    public boolean selesaikanTransaksi(int idTransaksi, String platNomor, BigDecimal denda) {
        if (idTransaksi <= 0 || platNomor == null || platNomor.trim().isEmpty()
                || (denda != null && denda.signum() < 0)) {
            System.err.println("Pengembalian gagal: data transaksi tidak valid.");
            return false;
        }

        String sqlTransaksi = "UPDATE transaksi SET status_transaksi = 'Selesai', denda = ? "
                            + "WHERE id_transaksi = ? AND plat_nomor = ? AND status_transaksi = 'Sedang Berjalan'";
        String sqlMobil = "UPDATE mobil SET status = 'Tersedia' "
                        + "WHERE plat_nomor = ? AND status = 'Disewa'";

        try (Connection conn = Koneksi.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psTrx = conn.prepareStatement(sqlTransaksi);
                 PreparedStatement psMobil = conn.prepareStatement(sqlMobil)) {

                psTrx.setBigDecimal(1, denda != null ? denda : BigDecimal.ZERO);
                psTrx.setInt(2, idTransaksi);
                psTrx.setString(3, platNomor);
                if (psTrx.executeUpdate() == 0) {
                    conn.rollback();
                    System.err.println("Pengembalian gagal: transaksi tidak aktif atau data mobil tidak cocok.");
                    return false;
                }

                psMobil.setString(1, platNomor);
                if (psMobil.executeUpdate() == 0) {
                    conn.rollback();
                    System.err.println("Pengembalian gagal: status mobil bukan 'Disewa'.");
                    return false;
                }

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            System.err.println("Gagal menyelesaikan transaksi: " + e.getMessage());
            return false;
        }
    }

    /**
     * Membatalkan transaksi aktif dan mengembalikan status mobil menjadi 'Tersedia'.
     */
    public boolean batalkanTransaksi(int idTransaksi, String platNomor) {
        if (idTransaksi <= 0 || platNomor == null || platNomor.trim().isEmpty()) {
            System.err.println("Pembatalan gagal: data transaksi tidak valid.");
            return false;
        }

        String sqlTransaksi = "UPDATE transaksi SET status_transaksi = 'Dibatalkan' "
                            + "WHERE id_transaksi = ? AND plat_nomor = ? AND status_transaksi = 'Sedang Berjalan'";
        String sqlMobil = "UPDATE mobil SET status = 'Tersedia' "
                        + "WHERE plat_nomor = ? AND status = 'Disewa'";

        try (Connection conn = Koneksi.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psTrx = conn.prepareStatement(sqlTransaksi);
                 PreparedStatement psMobil = conn.prepareStatement(sqlMobil)) {

                psTrx.setInt(1, idTransaksi);
                psTrx.setString(2, platNomor);
                if (psTrx.executeUpdate() == 0) {
                    conn.rollback();
                    System.err.println("Pembatalan gagal: transaksi tidak aktif atau data mobil tidak cocok.");
                    return false;
                }

                psMobil.setString(1, platNomor);
                if (psMobil.executeUpdate() == 0) {
                    conn.rollback();
                    System.err.println("Pembatalan gagal: status mobil bukan 'Disewa'.");
                    return false;
                }

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            System.err.println("Gagal membatalkan transaksi: " + e.getMessage());
            return false;
        }
    }

    /**
     * Mengambil seluruh data ringkasan statistik untuk ditampilkan pada MenuDashboard.
     */
    public Dashboard getDashboardData() {
        Dashboard dashboard = new Dashboard();

        String sqlMobil = "SELECT "
                        + "COUNT(*) AS total, "
                        + "COALESCE(SUM(CASE WHEN status = 'Tersedia' THEN 1 ELSE 0 END), 0) AS tersedia, "
                        + "COALESCE(SUM(CASE WHEN status = 'Disewa' THEN 1 ELSE 0 END), 0) AS disewa, "
                        + "COALESCE(SUM(CASE WHEN status = 'Perawatan' THEN 1 ELSE 0 END), 0) AS perawatan "
                        + "FROM mobil";

        String sqlPelanggan = "SELECT COUNT(*) AS total FROM pelanggan";

        String sqlTransaksi = "SELECT "
                            + "COALESCE(SUM(CASE WHEN status_transaksi = 'Sedang Berjalan' THEN 1 ELSE 0 END), 0) AS aktif, "
                            + "COALESCE(SUM(CASE WHEN status_transaksi = 'Selesai' THEN 1 ELSE 0 END), 0) AS selesai, "
                            + "COALESCE(SUM(CASE WHEN status_transaksi = 'Sedang Berjalan' AND tanggal_kembali < CURDATE() THEN 1 ELSE 0 END), 0) AS terlambat, "
                            + "COALESCE(SUM(CASE WHEN status_transaksi = 'Selesai' AND DATE(updated_at) = CURDATE() THEN (total_harga + denda) ELSE 0 END), 0) AS hari_ini, "
                            + "COALESCE(SUM(CASE WHEN status_transaksi = 'Selesai' AND MONTH(updated_at) = MONTH(CURDATE()) AND YEAR(updated_at) = YEAR(CURDATE()) THEN (total_harga + denda) ELSE 0 END), 0) AS bulan_ini, "
                            + "COALESCE(SUM(CASE WHEN status_transaksi = 'Selesai' THEN (total_harga + denda) ELSE 0 END), 0) AS total_pendapatan "
                            + "FROM transaksi";

        String sqlTerbaru = "SELECT t.*, "
                          + "CONCAT(m.merek_mobil, ' ', m.tipe_mobil, ' - ', m.plat_nomor) AS nama_mobil, "
                          + "p.nama_pelanggan "
                          + "FROM transaksi t "
                          + "JOIN mobil m ON t.plat_nomor = m.plat_nomor "
                          + "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                          + "ORDER BY t.created_at DESC LIMIT 10";

        try (Connection conn = Koneksi.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlMobil);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dashboard.setTotalMobil(rs.getInt("total"));
                    dashboard.setMobilTersedia(rs.getInt("tersedia"));
                    dashboard.setMobilDisewa(rs.getInt("disewa"));
                    dashboard.setMobilPerawatan(rs.getInt("perawatan"));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlPelanggan);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dashboard.setTotalPelanggan(rs.getInt("total"));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlTransaksi);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dashboard.setTransaksiAktif(rs.getInt("aktif"));
                    dashboard.setTransaksiSelesai(rs.getInt("selesai"));
                    dashboard.setTransaksiTerlambat(rs.getInt("terlambat"));
                    dashboard.setPendapatanHariIni(rs.getBigDecimal("hari_ini"));
                    dashboard.setPendapatanBulanIni(rs.getBigDecimal("bulan_ini"));
                    dashboard.setTotalPendapatan(rs.getBigDecimal("total_pendapatan"));
                }
            }

            List<Transaksi> terbaru = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(sqlTerbaru);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    terbaru.add(mapResultSetToTransaksi(rs));
                }
            }
            dashboard.setTransaksiTerbaru(terbaru);
            dashboard.setDaftarPromoPelanggan(promoServis.getAllPromoStatus());

        } catch (SQLException e) {
            System.err.println("Gagal memuat statistik dashboard: " + e.getMessage());
        }

        return dashboard;
    }

    private Transaksi mapResultSetToTransaksi(ResultSet rs) throws SQLException {
        Transaksi t = new Transaksi();
        t.setIdTransaksi(rs.getInt("id_transaksi"));
        t.setPlatNomor(rs.getString("plat_nomor"));
        t.setIdPelanggan(rs.getInt("id_pelanggan"));
        t.setNamaMobil(rs.getString("nama_mobil"));
        t.setNamaPelanggan(rs.getString("nama_pelanggan"));
        t.setTanggalSewa(rs.getDate("tanggal_sewa"));
        t.setTanggalKembali(rs.getDate("tanggal_kembali"));
        t.setLamaSewa(rs.getInt("lama_sewa"));
        t.setTotalHarga(rs.getBigDecimal("total_harga"));
        t.setDenda(rs.getBigDecimal("denda"));
        t.setGratisPromo(rs.getBoolean("gratis_promo"));
        t.setStatusTransaksi(rs.getString("status_transaksi"));
        t.setCreatedAt(rs.getTimestamp("created_at"));
        t.setUpdatedAt(rs.getTimestamp("updated_at"));
        return t;
    }
}