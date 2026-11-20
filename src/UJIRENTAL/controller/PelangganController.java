/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UJIRENTAL.controller;

import UJIRENTAL.model.Pelanggan;
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
public class PelangganController {

    public List<Pelanggan> getAllPelanggan() {
        List<Pelanggan> pelanggan = new ArrayList<>();
        String sql = "SELECT id_pelanggan, nik, nama_pelanggan FROM pelanggan ORDER BY nama_pelanggan";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pelanggan.add(mapPelanggan(rs));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data pelanggan: " + e.getMessage());
        }
        return pelanggan;
    }

    public Pelanggan getPelangganById(int idPelanggan) {
        String sql = "SELECT id_pelanggan, nik, nama_pelanggan FROM pelanggan WHERE id_pelanggan = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPelanggan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPelanggan(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mencari data pelanggan: " + e.getMessage());
        }
        return null;
    }

    private Pelanggan mapPelanggan(ResultSet rs) throws SQLException {
        Pelanggan pelanggan = new Pelanggan();
        pelanggan.setIdPelanggan(rs.getInt("id_pelanggan"));
        pelanggan.setNik(rs.getString("nik"));
        pelanggan.setNamaPelanggan(rs.getString("nama_pelanggan"));
        return pelanggan;
    }
}
