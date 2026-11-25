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

    private static final String BERJALAN = "Sedang Berjalan";
    private static final String SELESAI = "Selesai";
    private static final String DIBATALKAN = "Dibatalkan";

    private static final String SQL_DASAR = """
        SELECT t.*,
               CONCAT(m.merek_mobil, ' ', m.tipe_mobil, ' - ', m.plat_nomor) AS nama_mobil,
               p.nama_pelanggan
        FROM transaksi t
        JOIN mobil m ON t.plat_nomor = m.plat_nomor
        JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan
        """;

    private final PromoServis promoServis = new PromoServis();

    public List<Transaksi> getAllTransaksi() throws SQLException {
        List<Transaksi> list = new ArrayList<>();
        String sql = SQL_DASAR + "ORDER BY t.created_at DESC";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToTransaksi(rs));
            }
        }
        return list;
    }

    public List<Transaksi> getTransaksiByStatus(String statusTransaksi) throws SQLException {
        List<Transaksi> list = new ArrayList<>();
        String sql = SQL_DASAR + "WHERE t.status_transaksi = ? ORDER BY t.created_at DESC";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statusTransaksi);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTransaksi(rs));
                }
            }
        }
        return list;
    }

    public List<Transaksi> cariTransaksi(String keyword) throws SQLException {
        List<Transaksi> list = new ArrayList<>();
        if (keyword == null) {
            return list;
        }
        String sql = SQL_DASAR + """
            WHERE t.plat_nomor LIKE ?
               OR p.nama_pelanggan LIKE ?
               OR CAST(t.id_transaksi AS CHAR) LIKE ?
            ORDER BY t.created_at DESC
            """;

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pola = "%" + keyword + "%";
            ps.setString(1, pola);
            ps.setString(2, pola);
            ps.setString(3, pola);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTransaksi(rs));
                }
            }
        }
        return list;
    }

    public boolean tambahTransaksi(Transaksi trx) throws SQLException {
        if (!isValid(trx)) {
            return false;
        }

        String sqlInsert = """
            INSERT INTO transaksi (plat_nomor, id_pelanggan, tanggal_sewa, tanggal_kembali,
                                   lama_sewa, total_harga, denda, gratis_promo, status_transaksi)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        String sqlKunciPelanggan = """
            SELECT id_pelanggan FROM pelanggan WHERE id_pelanggan = ? FOR UPDATE
            """;
        String sqlUpdateMobil = """
            UPDATE mobil SET status = 'Disewa' WHERE plat_nomor = ? AND status = 'Tersedia'
            """;

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement psPelanggan = conn.prepareStatement(sqlKunciPelanggan);
             PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
             PreparedStatement psMobil = conn.prepareStatement(sqlUpdateMobil)) {

            conn.setAutoCommit(false);

            psPelanggan.setInt(1, trx.getIdPelanggan());
            try (ResultSet rs = psPelanggan.executeQuery()) {
                if (!rs.next()) {
                    conn.rollback();
                    return false;
                }
            }

            boolean pakaiGratisPromo = promoServis.isPromoAvailable(conn, trx.getIdPelanggan());

            psMobil.setString(1, trx.getPlatNomor());
            if (psMobil.executeUpdate() == 0) {
                conn.rollback();
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
            psInsert.setString(9, BERJALAN);
            psInsert.executeUpdate();

            conn.commit();
            trx.setGratisPromo(pakaiGratisPromo);
            if (pakaiGratisPromo) {
                trx.setTotalHarga(BigDecimal.ZERO);
            }
            return true;
        }
    }

    public boolean selesaikanTransaksi(int idTransaksi, String platNomor, BigDecimal denda) throws SQLException {
        if (idTransaksi <= 0 || platNomor == null || platNomor.trim().isEmpty()
                || (denda != null && denda.signum() < 0)) {
            return false;
        }

        String sqlTransaksi = """
            UPDATE transaksi SET status_transaksi = 'Selesai', denda = ?
            WHERE id_transaksi = ? AND plat_nomor = ? AND status_transaksi = 'Sedang Berjalan'
            """;
        String sqlMobil = """
            UPDATE mobil SET status = 'Tersedia'
            WHERE plat_nomor = ? AND status = 'Disewa'
            """;

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement psTrx = conn.prepareStatement(sqlTransaksi);
             PreparedStatement psMobil = conn.prepareStatement(sqlMobil)) {

            conn.setAutoCommit(false);

            psTrx.setBigDecimal(1, denda != null ? denda : BigDecimal.ZERO);
            psTrx.setInt(2, idTransaksi);
            psTrx.setString(3, platNomor);
            if (psTrx.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }

            psMobil.setString(1, platNomor);
            if (psMobil.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;
        }
    }

    public boolean batalkanTransaksi(int idTransaksi, String platNomor) throws SQLException {
        if (idTransaksi <= 0 || platNomor == null || platNomor.trim().isEmpty()) {
            return false;
        }

        String sqlTransaksi = """
            UPDATE transaksi SET status_transaksi = 'Dibatalkan'
            WHERE id_transaksi = ? AND plat_nomor = ? AND status_transaksi = 'Sedang Berjalan'
            """;
        String sqlMobil = """
            UPDATE mobil SET status = 'Tersedia'
            WHERE plat_nomor = ? AND status = 'Disewa'
            """;

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement psTrx = conn.prepareStatement(sqlTransaksi);
             PreparedStatement psMobil = conn.prepareStatement(sqlMobil)) {

            conn.setAutoCommit(false);

            psTrx.setInt(1, idTransaksi);
            psTrx.setString(2, platNomor);
            if (psTrx.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }

            psMobil.setString(1, platNomor);
            if (psMobil.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;
        }
    }

    public Dashboard getDashboardData() throws SQLException {
        Dashboard dashboard = new Dashboard();

        String sqlMobil = """
            SELECT COUNT(*) AS total,
                   COALESCE(SUM(CASE WHEN status = 'Tersedia' THEN 1 ELSE 0 END), 0) AS tersedia,
                   COALESCE(SUM(CASE WHEN status = 'Disewa' THEN 1 ELSE 0 END), 0) AS disewa,
                   COALESCE(SUM(CASE WHEN status = 'Perawatan' THEN 1 ELSE 0 END), 0) AS perawatan
            FROM mobil
            """;
        String sqlPelanggan = """
            SELECT COUNT(*) AS total FROM pelanggan
            """;
        String sqlTransaksi = """
            SELECT COALESCE(SUM(CASE WHEN status_transaksi = 'Sedang Berjalan' THEN 1 ELSE 0 END), 0) AS aktif,
                   COALESCE(SUM(CASE WHEN status_transaksi = 'Selesai' THEN 1 ELSE 0 END), 0) AS selesai,
                   COALESCE(SUM(CASE WHEN status_transaksi = 'Sedang Berjalan' AND tanggal_kembali < CURDATE() THEN 1 ELSE 0 END), 0) AS terlambat,
                   COALESCE(SUM(CASE WHEN status_transaksi = 'Selesai' AND DATE(updated_at) = CURDATE() THEN (total_harga + denda) ELSE 0 END), 0) AS hari_ini,
                   COALESCE(SUM(CASE WHEN status_transaksi = 'Selesai' AND MONTH(updated_at) = MONTH(CURDATE()) AND YEAR(updated_at) = YEAR(CURDATE()) THEN (total_harga + denda) ELSE 0 END), 0) AS bulan_ini,
                   COALESCE(SUM(CASE WHEN status_transaksi = 'Selesai' THEN (total_harga + denda) ELSE 0 END), 0) AS total_pendapatan
            FROM transaksi
            """;
        String sqlTerbaru = SQL_DASAR + "ORDER BY t.created_at DESC LIMIT 10";

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
        }
        return dashboard;
    }

    private Transaksi mapResultSetToTransaksi(ResultSet rs) throws SQLException {
        Transaksi trx = new Transaksi();
        trx.setIdTransaksi(rs.getInt("id_transaksi"));
        trx.setPlatNomor(rs.getString("plat_nomor"));
        trx.setIdPelanggan(rs.getInt("id_pelanggan"));
        trx.setNamaMobil(rs.getString("nama_mobil"));
        trx.setNamaPelanggan(rs.getString("nama_pelanggan"));
        trx.setTanggalSewa(rs.getDate("tanggal_sewa"));
        trx.setTanggalKembali(rs.getDate("tanggal_kembali"));
        trx.setLamaSewa(rs.getInt("lama_sewa"));
        trx.setTotalHarga(rs.getBigDecimal("total_harga"));
        trx.setDenda(rs.getBigDecimal("denda"));
        trx.setGratisPromo(rs.getBoolean("gratis_promo"));
        trx.setStatusTransaksi(rs.getString("status_transaksi"));
        trx.setCreatedAt(rs.getTimestamp("created_at"));
        trx.setUpdatedAt(rs.getTimestamp("updated_at"));
        return trx;
    }

    private boolean isValid(Transaksi trx) {
        if (trx == null) return false;
        if (trx.getPlatNomor() == null || trx.getPlatNomor().trim().isEmpty()) return false;
        if (trx.getIdPelanggan() <= 0) return false;
        if (trx.getTanggalSewa() == null || trx.getTanggalKembali() == null) return false;
        if (!trx.getTanggalKembali().after(trx.getTanggalSewa())) return false;
        if (trx.getLamaSewa() <= 0) return false;

        long hitungHari = ChronoUnit.DAYS.between(
            trx.getTanggalSewa().toLocalDate(), trx.getTanggalKembali().toLocalDate());
        if (hitungHari != trx.getLamaSewa()) return false;

        if (trx.getTotalHarga() == null || trx.getTotalHarga().signum() < 0) return false;
        if (trx.getDenda() != null && trx.getDenda().signum() < 0) return false;
        return true;
    }
}