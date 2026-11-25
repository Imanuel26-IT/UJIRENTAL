/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UJIRENTAL.controller;

import UJIRENTAL.model.Promo;
import UJIRENTAL.util.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IMANUEL VALEN LINGGI
 */
public class PromoController {

    public List<Promo> getAllPromo() throws SQLException {
        List<Promo> list = new ArrayList<>();
        String sql = """
            SELECT * FROM promo ORDER BY id_promo DESC
            """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToPromo(rs));
            }
        }
        return list;
    }

    public Promo getPromoAktif() throws SQLException {
        String sql = """
            SELECT * FROM promo WHERE status_promo = 'Aktif'
            ORDER BY id_promo DESC LIMIT 1
            """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapResultSetToPromo(rs);
            }
        }
        return null;
    }

    public boolean tambahPromo(Promo promo) throws SQLException {
        String sql = """
            INSERT INTO promo (nama_promo, minimal_sewa, periode_bulan, hadiah, status_promo)
            VALUES (?, ?, ?, ?, ?)
         """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, promo.getNamaPromo());
            ps.setInt(2, promo.getMinimalSewa());
            ps.setInt(3, promo.getPeriodeBulan());
            ps.setString(4, promo.getHadiah());
            ps.setString(5, promo.getStatusPromo() != null ? promo.getStatusPromo() : "Aktif");
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updatePromo(Promo promo) throws SQLException {
        String sql = """
            UPDATE promo SET nama_promo = ?, minimal_sewa = ?, periode_bulan = ?,
                            hadiah = ?, status_promo = ?
            WHERE id_promo = ?
            """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, promo.getNamaPromo());
            ps.setInt(2, promo.getMinimalSewa());
            ps.setInt(3, promo.getPeriodeBulan());
            ps.setString(4, promo.getHadiah());
            ps.setString(5, promo.getStatusPromo());
            ps.setInt(6, promo.getIdPromo());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean hapusPromo(int idPromo) throws SQLException {
        String sql = """
            DELETE FROM promo WHERE id_promo = ?
            """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPromo);
            return ps.executeUpdate() > 0;
        }
    }

    private Promo mapResultSetToPromo(ResultSet rs) throws SQLException {
        Promo promo = new Promo();
        promo.setIdPromo(rs.getInt("id_promo"));
        promo.setNamaPromo(rs.getString("nama_promo"));
        promo.setMinimalSewa(rs.getInt("minimal_sewa"));
        promo.setPeriodeBulan(rs.getInt("periode_bulan"));
        promo.setHadiah(rs.getString("hadiah"));
        promo.setStatusPromo(rs.getString("status_promo"));
        promo.setCreatedAt(rs.getTimestamp("created_at"));
        promo.setUpdatedAt(rs.getTimestamp("updated_at"));
        return promo;
    }
}