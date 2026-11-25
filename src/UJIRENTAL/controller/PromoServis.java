package UJIRENTAL.controller;

import UJIRENTAL.model.Pelanggan;
import UJIRENTAL.model.PromoStatus;
import UJIRENTAL.util.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PromoServis {

    private static final int MINIMAL_SEWA = 10;
    private static final int BATAS_HARI_KE_BELAKANG = 90;
    private static final int BATAS_AKHIR_HARI_INI = 1;
    private static final String HADIAH_PROMO = "Gratis 1x Sewa";

    private final PelangganController pelangganController = new PelangganController();

    public PromoStatus cekStatusPromo(int idPelanggan) throws SQLException {
        try (Connection conn = Koneksi.getConnection()) {
            return hitungStatusPromo(conn, idPelanggan);
        }
    }

    public boolean isPromoAvailable(Connection conn, int idPelanggan) throws SQLException {
        PromoStatus status = hitungStatusPromo(conn, idPelanggan);
        return status != null && status.isDapatGratis();
    }

    public List<PromoStatus> getAllPromoStatus() throws SQLException {
        List<PromoStatus> daftarStatus = new ArrayList<>();
        for (Pelanggan pelanggan : pelangganController.getAllPelanggan()) {
            PromoStatus status = cekStatusPromo(pelanggan.getIdPelanggan());
            if (status != null) {
                daftarStatus.add(status);
            }
        }
        return daftarStatus;
    }

    private PromoStatus hitungStatusPromo(Connection conn, int idPelanggan) throws SQLException {
        String sql = """
            SELECT p.nama_pelanggan,
                   COALESCE(SUM(CASE WHEN t.status_transaksi = 'Selesai' AND t.gratis_promo = 0 THEN 1 ELSE 0 END), 0) AS berbayar,
                   COALESCE(SUM(CASE WHEN t.status_transaksi = 'Selesai' AND t.gratis_promo = 1 THEN 1 ELSE 0 END), 0) AS promo_selesai,
                   COALESCE(SUM(CASE WHEN t.status_transaksi = 'Sedang Berjalan' AND t.gratis_promo = 1 THEN 1 ELSE 0 END), 0) AS promo_dipakai
            FROM pelanggan p
            LEFT JOIN transaksi t ON t.id_pelanggan = p.id_pelanggan
            AND (t.tanggal_sewa BETWEEN DATE_SUB(CURDATE(), INTERVAL ? DAY)
            AND DATE_SUB(CURDATE(), INTERVAL ? DAY)
            OR (t.status_transaksi = 'Sedang Berjalan' AND t.gratis_promo = 1))
            WHERE p.id_pelanggan = ?
            """;

        String namaPelanggan = null;
        int totalSewaBerbayar = 0;
        int jumlahPromoSelesai = 0;
        boolean promoSedangDipakai = false;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, BATAS_HARI_KE_BELAKANG);
            ps.setInt(2, BATAS_AKHIR_HARI_INI);
            ps.setInt(3, idPelanggan);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    namaPelanggan = rs.getString("nama_pelanggan");
                    totalSewaBerbayar = rs.getInt("berbayar");
                    jumlahPromoSelesai = rs.getInt("promo_selesai");
                    promoSedangDipakai = rs.getInt("promo_dipakai") > 0;
                }
            }
        }

        if (namaPelanggan == null) {
            return null;
        }

        int jumlahSewaEfektif = Math.max(0, totalSewaBerbayar - (jumlahPromoSelesai * MINIMAL_SEWA));
        boolean dapatGratis = jumlahSewaEfektif >= MINIMAL_SEWA && !promoSedangDipakai;

        PromoStatus status = new PromoStatus(
                idPelanggan,
                namaPelanggan,
                jumlahSewaEfektif,
                MINIMAL_SEWA,
                3,
                HADIAH_PROMO
        );
        status.setDapatGratis(dapatGratis);
        status.setSudahDipakai(promoSedangDipakai);
        return status;
    }
}   
