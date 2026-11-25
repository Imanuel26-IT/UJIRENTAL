/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UJIRENTAL.controller;

import UJIRENTAL.model.Mobil;
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
public class MobilController {

    public List<Mobil> getAllMobil() throws SQLException {
        List<Mobil> list = new ArrayList<>();
        String sql = """
            SELECT * FROM mobil ORDER BY created_at DESC
            """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToMobil(rs));
            }
        }
        return list;
    }

    public List<Mobil> getMobilTersedia() throws SQLException {
        List<Mobil> list = new ArrayList<>();
        String sql = """
            SELECT * FROM mobil WHERE status = 'Tersedia'
            ORDER BY merek_mobil ASC, tipe_mobil ASC
            """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToMobil(rs));
            }
        }
        return list;
    }

    public Mobil getMobilByPlat(String platNomor) throws SQLException {
        String sql = """
            SELECT * FROM mobil WHERE plat_nomor = ?
            """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, platNomor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMobil(rs);
                }
            }
        }
        return null;
    }

    public List<Mobil> cariMobil(String keyword) throws SQLException {
        List<Mobil> list = new ArrayList<>();
        if (keyword == null) {
            return list;
        }

        String sql = """
            SELECT * FROM mobil
            WHERE plat_nomor LIKE ? OR merek_mobil LIKE ?
            OR tipe_mobil LIKE ? OR status LIKE ?
            ORDER BY created_at DESC
            """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            String pola = "%" + keyword + "%";
            ps.setString(1, pola);
            ps.setString(2, pola);
            ps.setString(3, pola);
            ps.setString(4, pola);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToMobil(rs));
                }
            }
        }
        return list;
    }

    public boolean tambahMobil(Mobil mobil) throws SQLException {
        String sql = """
            INSERT INTO mobil (plat_nomor, merek_mobil, tipe_mobil, harga_sewa,
                            warna, transmisi, status, tahun, kapasitas, no_rangka, no_mesin)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mobil.getPlatNomor());
            ps.setString(2, mobil.getMerekMobil());
            ps.setString(3, mobil.getTipeMobil());
            ps.setBigDecimal(4, mobil.getHargaSewa());
            ps.setString(5, mobil.getWarna());
            ps.setString(6, mobil.getTransmisi());
            ps.setString(7, mobil.getStatus() != null ? mobil.getStatus() : "Tersedia");
            ps.setInt(8, mobil.getTahun());
            ps.setInt(9, mobil.getKapasitas());
            ps.setString(10, mobil.getNoRangka());
            ps.setString(11, mobil.getNoMesin());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateMobil(Mobil mobil, String platNomorLama) throws SQLException {
        String sql = """
            UPDATE mobil SET plat_nomor = ?, merek_mobil = ?, tipe_mobil = ?,
                            harga_sewa = ?, warna = ?, transmisi = ?, status = ?, tahun = ?,
                            kapasitas = ?, no_rangka = ?, no_mesin = ?
            WHERE plat_nomor = ?
            """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mobil.getPlatNomor());
            ps.setString(2, mobil.getMerekMobil());
            ps.setString(3, mobil.getTipeMobil());
            ps.setBigDecimal(4, mobil.getHargaSewa());
            ps.setString(5, mobil.getWarna());
            ps.setString(6, mobil.getTransmisi());
            ps.setString(7, mobil.getStatus());
            ps.setInt(8, mobil.getTahun());
            ps.setInt(9, mobil.getKapasitas());
            ps.setString(10, mobil.getNoRangka());
            ps.setString(11, mobil.getNoMesin());
            ps.setString(12, platNomorLama);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateStatusMobil(String platNomor, String statusBaru) throws SQLException {
        String sql = """
            UPDATE mobil SET status = ? WHERE plat_nomor = ?
            """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statusBaru);
            ps.setString(2, platNomor);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean hapusMobil(String platNomor) throws SQLException {
        String sql = """
            DELETE FROM mobil WHERE plat_nomor = ?
            """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, platNomor);
            return ps.executeUpdate() > 0;
        }
    }

    private Mobil mapResultSetToMobil(ResultSet rs) throws SQLException {
        Mobil mobil = new Mobil();
        mobil.setPlatNomor(rs.getString("plat_nomor"));
        mobil.setMerekMobil(rs.getString("merek_mobil"));
        mobil.setTipeMobil(rs.getString("tipe_mobil"));
        mobil.setHargaSewa(rs.getBigDecimal("harga_sewa"));
        mobil.setWarna(rs.getString("warna"));
        mobil.setTransmisi(rs.getString("transmisi"));
        mobil.setStatus(rs.getString("status"));
        mobil.setTahun(rs.getInt("tahun"));
        mobil.setKapasitas(rs.getInt("kapasitas"));
        mobil.setNoRangka(rs.getString("no_rangka"));
        mobil.setNoMesin(rs.getString("no_mesin"));
        mobil.setCreatedAt(rs.getTimestamp("created_at"));
        mobil.setUpdatedAt(rs.getTimestamp("updated_at"));
        return mobil;
    }
}