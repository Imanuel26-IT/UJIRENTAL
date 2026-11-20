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

    // Ambil semua data mobil untuk ditampilkan di JTable MenuMobil
    public List<Mobil> getAllMobil() {
        List<Mobil> listMobil = new ArrayList<>();
        String sql = "SELECT * FROM mobil ORDER BY created_at DESC";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                listMobil.add(mapResultSetToMobil(rs));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data mobil: " + e.getMessage());
        }
        return listMobil;
    }

    // Ambil hanya mobil berstatus 'Tersedia' untuk JComboBox di MenuTransaksi
    public List<Mobil> getMobilTersedia() {
        List<Mobil> listTersedia = new ArrayList<>();
        String sql = "SELECT * FROM mobil WHERE status = 'Tersedia' ORDER BY merek_mobil ASC, tipe_mobil ASC";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                listTersedia.add(mapResultSetToMobil(rs));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil mobil tersedia: " + e.getMessage());
        }
        return listTersedia;
    }

    // Cari satu mobil berdasarkan Plat Nomor
    public Mobil getMobilByPlat(String platNomor) {
        String sql = "SELECT * FROM mobil WHERE plat_nomor = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, platNomor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMobil(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mencari detail mobil: " + e.getMessage());
        }
        return null;
    }

    // Pencarian dinamis berdasarkan plat nomor, merek, tipe, atau status
    public List<Mobil> cariMobil(String keyword) {
        List<Mobil> listHasil = new ArrayList<>();
        String sql = "SELECT * FROM mobil WHERE plat_nomor LIKE ? OR merek_mobil LIKE ? "
                   + "OR tipe_mobil LIKE ? OR status LIKE ? ORDER BY created_at DESC";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pola = "%" + keyword + "%";
            ps.setString(1, pola);
            ps.setString(2, pola);
            ps.setString(3, pola);
            ps.setString(4, pola);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listHasil.add(mapResultSetToMobil(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal melakukan pencarian mobil: " + e.getMessage());
        }
        return listHasil;
    }

    // Simpan data mobil baru
    public boolean tambahMobil(Mobil mobil) {
        String sql = "INSERT INTO mobil (plat_nomor, merek_mobil, tipe_mobil, harga_sewa, "
                   + "warna, transmisi, status, tahun, kapasitas, no_rangka, no_mesin) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
        } catch (SQLException e) {
            System.err.println("Gagal menambah mobil: " + e.getMessage());
            return false;
        }
    }

    // Update data mobil (mendukung perubahan plat nomor menggunakan platNomorLama)
    public boolean updateMobil(Mobil mobil, String platNomorLama) {
        String sql = "UPDATE mobil SET plat_nomor = ?, merek_mobil = ?, tipe_mobil = ?, "
                   + "harga_sewa = ?, warna = ?, transmisi = ?, status = ?, tahun = ?, "
                   + "kapasitas = ?, no_rangka = ?, no_mesin = ? WHERE plat_nomor = ?";

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
        } catch (SQLException e) {
            System.err.println("Gagal memperbarui data mobil: " + e.getMessage());
            return false;
        }
    }

    // Update khusus status mobil (dipanggil otomatis saat transaksi mulai/selesai)
    public boolean updateStatusMobil(String platNomor, String statusBaru) {
        String sql = "UPDATE mobil SET status = ? WHERE plat_nomor = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statusBaru);
            ps.setString(2, platNomor);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal mengubah status mobil: " + e.getMessage());
            return false;
        }
    }

    // Hapus data mobil berdasarkan Plat Nomor
    public boolean hapusMobil(String platNomor) {
        String sql = "DELETE FROM mobil WHERE plat_nomor = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, platNomor);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal menghapus mobil (pastikan tidak terikat transaksi): " + e.getMessage());
            return false;
        }
    }

    // Helper mapping ResultSet -> Object Mobil
    private Mobil mapResultSetToMobil(ResultSet rs) throws SQLException {
        Mobil m = new Mobil();
        m.setPlatNomor(rs.getString("plat_nomor"));
        m.setMerekMobil(rs.getString("merek_mobil"));
        m.setTipeMobil(rs.getString("tipe_mobil"));
        m.setHargaSewa(rs.getBigDecimal("harga_sewa"));
        m.setWarna(rs.getString("warna"));
        m.setTransmisi(rs.getString("transmisi"));
        m.setStatus(rs.getString("status"));
        m.setTahun(rs.getInt("tahun"));
        m.setKapasitas(rs.getInt("kapasitas"));
        m.setNoRangka(rs.getString("no_rangka"));
        m.setNoMesin(rs.getString("no_mesin"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        m.setUpdatedAt(rs.getTimestamp("updated_at"));
        return m;
    }
}