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

    // Ambil seluruh aturan promo
    public List<Promo> getAllPromo() {
        List<Promo> listPromo = new ArrayList<>();
        String sql = "SELECT * FROM promo ORDER BY id_promo DESC";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                listPromo.add(mapResultSetToPromo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data promo: " + e.getMessage());
        }
        return listPromo;
    }

    // Ambil promo aktif utama (digunakan oleh PromoServis untuk menghitung kelayakan gratis)
    public Promo getPromoAktif() {
        String sql = "SELECT * FROM promo WHERE status_promo = 'Aktif' ORDER BY id_promo DESC LIMIT 1";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return mapResultSetToPromo(rs);
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil promo aktif: " + e.getMessage());
        }
        return null;
    }

    // Tambah aturan promo baru
    public boolean tambahPromo(Promo promo) {
        String sql = "INSERT INTO promo (nama_promo, minimal_sewa, periode_bulan, hadiah, status_promo) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, promo.getNamaPromo());
            ps.setInt(2, promo.getMinimalSewa());
            ps.setInt(3, promo.getPeriodeBulan());
            ps.setString(4, promo.getHadiah());
            ps.setString(5, promo.getStatusPromo() != null ? promo.getStatusPromo() : "Aktif");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal menambah promo: " + e.getMessage());
            return false;
        }
    }

    // Perbarui aturan promo
    public boolean updatePromo(Promo promo) {
        String sql = "UPDATE promo SET nama_promo = ?, minimal_sewa = ?, periode_bulan = ?, "
                   + "hadiah = ?, status_promo = ? WHERE id_promo = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, promo.getNamaPromo());
            ps.setInt(2, promo.getMinimalSewa());
            ps.setInt(3, promo.getPeriodeBulan());
            ps.setString(4, promo.getHadiah());
            ps.setString(5, promo.getStatusPromo());
            ps.setInt(6, promo.getIdPromo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal memperbarui promo: " + e.getMessage());
            return false;
        }
    }

    // Hapus promo
    public boolean hapusPromo(int idPromo) {
        String sql = "DELETE FROM promo WHERE id_promo = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPromo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal menghapus promo: " + e.getMessage());
            return false;
        }
    }

    private Promo mapResultSetToPromo(ResultSet rs) throws SQLException {
        Promo p = new Promo();
        p.setIdPromo(rs.getInt("id_promo"));
        p.setNamaPromo(rs.getString("nama_promo"));
        p.setMinimalSewa(rs.getInt("minimal_sewa"));
        p.setPeriodeBulan(rs.getInt("periode_bulan"));
        p.setHadiah(rs.getString("hadiah"));
        p.setStatusPromo(rs.getString("status_promo"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }
}