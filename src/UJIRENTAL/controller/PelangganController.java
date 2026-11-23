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

    // Ambil seluruh data pelanggan untuk ditampilkan pada tabel Swing
    public List<Pelanggan> getAllPelanggan() {
        List<Pelanggan> daftarPelanggan = new ArrayList<>();
        String querySelectAll = "SELECT id_pelanggan, nik, nama_pelanggan, alamat, no_telepon, email, foto_ktp, created_at, updated_at "
                              + "FROM pelanggan ORDER BY nama_pelanggan ASC";

        try (Connection dbConn = Koneksi.getConnection();
             PreparedStatement stmtSelectAll = dbConn.prepareStatement(querySelectAll);
             ResultSet rsData = stmtSelectAll.executeQuery()) {

            while (rsData.next()) {
                daftarPelanggan.add(mapResultSetToPelanggan(rsData));
            }
        } catch (SQLException ex) {
            System.err.println("Gagal mengambil data pelanggan: " + ex.getMessage());
        }
        return daftarPelanggan;
    }

    // Ambil satu data pelanggan berdasarkan ID
    public Pelanggan getPelangganById(int customerId) {
        String querySelectById = "SELECT id_pelanggan, nik, nama_pelanggan, alamat, no_telepon, email, foto_ktp, created_at, updated_at "
                               + "FROM pelanggan WHERE id_pelanggan = ?";

        try (Connection dbConn = Koneksi.getConnection();
             PreparedStatement stmtSelectById = dbConn.prepareStatement(querySelectById)) {

            stmtSelectById.setInt(1, customerId);
            try (ResultSet rsData = stmtSelectById.executeQuery()) {
                if (rsData.next()) {
                    return mapResultSetToPelanggan(rsData);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Gagal mencari data pelanggan: " + ex.getMessage());
        }
        return null;
    }

    // Pencarian data pelanggan berdasarkan NIK, nama, nomor telepon, email, atau alamat
    public List<Pelanggan> cariPelanggan(String searchKeyword) {
        List<Pelanggan> hasilPencarian = new ArrayList<>();
        if (searchKeyword == null) {
            System.err.println("Pencarian pelanggan gagal: kata kunci tidak boleh null.");
            return hasilPencarian;
        }

        String querySearch = "SELECT id_pelanggan, nik, nama_pelanggan, alamat, no_telepon, email, foto_ktp, created_at, updated_at "
                           + "FROM pelanggan "
                           + "WHERE nik LIKE ? OR nama_pelanggan LIKE ? OR no_telepon LIKE ? "
                           + "OR email LIKE ? OR alamat LIKE ? "
                           + "ORDER BY nama_pelanggan ASC";

        try (Connection dbConn = Koneksi.getConnection();
             PreparedStatement stmtSearch = dbConn.prepareStatement(querySearch)) {

            String filterPattern = "%" + searchKeyword.trim() + "%";
            stmtSearch.setString(1, filterPattern);
            stmtSearch.setString(2, filterPattern);
            stmtSearch.setString(3, filterPattern);
            stmtSearch.setString(4, filterPattern);
            stmtSearch.setString(5, filterPattern);

            try (ResultSet rsData = stmtSearch.executeQuery()) {
                while (rsData.next()) {
                    hasilPencarian.add(mapResultSetToPelanggan(rsData));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Gagal mencari daftar pelanggan: " + ex.getMessage());
        }
        return hasilPencarian;
    }

    // Tambah data pelanggan baru ke database
    public boolean tambahPelanggan(Pelanggan newCustomer) {
        if (newCustomer == null
                || newCustomer.getNik() == null || newCustomer.getNik().trim().isEmpty()
                || newCustomer.getNamaPelanggan() == null || newCustomer.getNamaPelanggan().trim().isEmpty()) {
            System.err.println("Tambah pelanggan gagal: NIK dan Nama Pelanggan wajib diisi.");
            return false;
        }

        String queryInsert = "INSERT INTO pelanggan (nik, nama_pelanggan, alamat, no_telepon, email, foto_ktp) "
                           + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection dbConn = Koneksi.getConnection();
             PreparedStatement stmtInsert = dbConn.prepareStatement(queryInsert)) {

            stmtInsert.setString(1, newCustomer.getNik().trim());
            stmtInsert.setString(2, newCustomer.getNamaPelanggan().trim());
            stmtInsert.setString(3, newCustomer.getAlamat());
            stmtInsert.setString(4, newCustomer.getNoTelepon());
            stmtInsert.setString(5, newCustomer.getEmail());
            stmtInsert.setString(6, newCustomer.getFotoKtp());

            return stmtInsert.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Gagal menambah pelanggan: " + ex.getMessage());
            return false;
        }
    }

    // Perbarui data pelanggan berdasarkan id_pelanggan
    public boolean updatePelanggan(Pelanggan updatedCustomer) {
        if (updatedCustomer == null || updatedCustomer.getIdPelanggan() <= 0
                || updatedCustomer.getNik() == null || updatedCustomer.getNik().trim().isEmpty()
                || updatedCustomer.getNamaPelanggan() == null || updatedCustomer.getNamaPelanggan().trim().isEmpty()) {
            System.err.println("Update pelanggan gagal: data pelanggan atau ID tidak valid.");
            return false;
        }

        String queryUpdate = "UPDATE pelanggan SET nik = ?, nama_pelanggan = ?, alamat = ?, "
                           + "no_telepon = ?, email = ?, foto_ktp = ? "
                           + "WHERE id_pelanggan = ?";

        try (Connection dbConn = Koneksi.getConnection();
             PreparedStatement stmtUpdate = dbConn.prepareStatement(queryUpdate)) {

            stmtUpdate.setString(1, updatedCustomer.getNik().trim());
            stmtUpdate.setString(2, updatedCustomer.getNamaPelanggan().trim());
            stmtUpdate.setString(3, updatedCustomer.getAlamat());
            stmtUpdate.setString(4, updatedCustomer.getNoTelepon());
            stmtUpdate.setString(5, updatedCustomer.getEmail());
            stmtUpdate.setString(6, updatedCustomer.getFotoKtp());
            stmtUpdate.setInt(7, updatedCustomer.getIdPelanggan());

            return stmtUpdate.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Gagal memperbarui pelanggan: " + ex.getMessage());
            return false;
        }
    }

    // Hapus data pelanggan berdasarkan id_pelanggan
    public boolean hapusPelanggan(int customerId) {
        if (customerId <= 0) {
            System.err.println("Hapus pelanggan gagal: ID pelanggan tidak valid.");
            return false;
        }

        String queryDelete = "DELETE FROM pelanggan WHERE id_pelanggan = ?";

        try (Connection dbConn = Koneksi.getConnection();
             PreparedStatement stmtDelete = dbConn.prepareStatement(queryDelete)) {

            stmtDelete.setInt(1, customerId);
            return stmtDelete.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Gagal menghapus pelanggan (pastikan tidak terikat transaksi): " + ex.getMessage());
            return false;
        }
    }

    // Mapping baris ResultSet ke objek model Pelanggan
    private Pelanggan mapResultSetToPelanggan(ResultSet rsData) throws SQLException {
        Pelanggan customerObj = new Pelanggan();
        customerObj.setIdPelanggan(rsData.getInt("id_pelanggan"));
        customerObj.setNik(rsData.getString("nik"));
        customerObj.setNamaPelanggan(rsData.getString("nama_pelanggan"));
        customerObj.setAlamat(rsData.getString("alamat"));
        customerObj.setNoTelepon(rsData.getString("no_telepon"));
        customerObj.setEmail(rsData.getString("email"));
        customerObj.setFotoKtp(rsData.getString("foto_ktp"));
        customerObj.setCreatedAt(rsData.getTimestamp("created_at"));
        customerObj.setUpdatedAt(rsData.getTimestamp("updated_at"));
        return customerObj;
    }
}