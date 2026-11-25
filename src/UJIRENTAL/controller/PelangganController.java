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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author IMANUEL VALEN LINGGI
 */
public class PelangganController {

    public List<Pelanggan> getAllPelanggan() throws SQLException {
        List<Pelanggan> list = new ArrayList<>();
        String sql = """
            SELECT * FROM pelanggan ORDER BY nama_pelanggan
            """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapPelanggan(rs));
            }
        }
        return list;
    }

    public Pelanggan getPelangganById(int idPelanggan) throws SQLException {
        String sql = """
            SELECT * FROM pelanggan WHERE id_pelanggan = ?
            """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPelanggan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPelanggan(rs);
                }   
            }
        }
        return null;
    }

    private Pelanggan mapPelanggan(ResultSet rs) throws SQLException {
        Pelanggan pelanggan = new Pelanggan();
        pelanggan.setIdPelanggan(rs.getInt("id_pelanggan"));
        pelanggan.setNik(rs.getString("nik"));
        pelanggan.setNamaPelanggan(rs.getString("nama_pelanggan"));
        pelanggan.setAlamat(getOptionalString(rs, "alamat"));
        pelanggan.setNoTelepon(getOptionalString(rs, "no_telepon"));
        if (pelanggan.getNoTelepon() == null) {
            pelanggan.setNoTelepon(getOptionalString(rs, "no_hp"));
        }
        pelanggan.setEmail(getOptionalString(rs, "email"));
        pelanggan.setFotoKtp(getOptionalString(rs, "foto_ktp"));
        pelanggan.setFotoSim(getOptionalString(rs, "foto_sim"));
        pelanggan.setFotoPelanggan(getOptionalString(rs, "foto_pelanggan"));
        return pelanggan;
    }

    private String getOptionalString(ResultSet rs, String column) throws SQLException {
        try {
            return rs.getString(column);
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Pelanggan> cariPelanggan(String keyword) throws SQLException {
        List<Pelanggan> hasil = new ArrayList<>();
        if (keyword == null) {
            return hasil;
        }

        try (Connection conn = Koneksi.getConnection()) {
            String kolomTelp = getColumns(conn).contains("no_telepon") ? "no_telepon" : "no_hp";
            String sql = """
                SELECT * FROM pelanggan
                WHERE nama_pelanggan LIKE ? OR nik LIKE ? OR %s LIKE ?
                ORDER BY nama_pelanggan
                """.formatted(kolomTelp);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                String pola = "%" + keyword + "%";
                ps.setString(1, pola);
                ps.setString(2, pola);
                ps.setString(3, pola);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        hasil.add(mapPelanggan(rs));
                    }
                }
            }
        }
        return hasil;
    }

    public boolean tambahPelanggan(Pelanggan pelanggan) throws SQLException {
        try (Connection conn = Koneksi.getConnection()) {
            Set<String> kolom = getColumns(conn);
            List<String> namaKolom = new ArrayList<>();
            List<Object> nilai = new ArrayList<>();
            tambahKolomJikaAda(kolom, namaKolom, nilai, "nik", pelanggan.getNik());
            tambahKolomJikaAda(kolom, namaKolom, nilai, "nama_pelanggan", pelanggan.getNamaPelanggan());
            tambahKolomJikaAda(kolom, namaKolom, nilai, "alamat", pelanggan.getAlamat());
            if (kolom.contains("no_telepon")) {
                tambahKolomJikaAda(kolom, namaKolom, nilai, "no_telepon", pelanggan.getNoTelepon());
            } else {
                tambahKolomJikaAda(kolom, namaKolom, nilai, "no_hp", pelanggan.getNoTelepon());
            }
            tambahKolomJikaAda(kolom, namaKolom, nilai, "foto_ktp", pelanggan.getFotoKtp());
            tambahKolomJikaAda(kolom, namaKolom, nilai, "foto_sim", pelanggan.getFotoSim());
            tambahKolomJikaAda(kolom, namaKolom, nilai, "foto_pelanggan", pelanggan.getFotoPelanggan());

            String sql = "INSERT INTO pelanggan (" + String.join(", ", namaKolom) + ") VALUES ("
                + "?, ".repeat(nilai.size()).replaceAll(", $", "") + ")";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int i = 0; i < nilai.size(); i++) {
                    ps.setObject(i + 1, nilai.get(i));
                }
                return ps.executeUpdate() > 0;
            }
        }
    }

    public boolean updatePelanggan(Pelanggan pelanggan) throws SQLException {
        try (Connection conn = Koneksi.getConnection()) {
            Set<String> kolom = getColumns(conn);
            List<String> namaKolom = new ArrayList<>();
            List<Object> nilai = new ArrayList<>();
            tambahKolomJikaAda(kolom, namaKolom, nilai, "nik", pelanggan.getNik());
            tambahKolomJikaAda(kolom, namaKolom, nilai, "nama_pelanggan", pelanggan.getNamaPelanggan());
            tambahKolomJikaAda(kolom, namaKolom, nilai, "alamat", pelanggan.getAlamat());
            if (kolom.contains("no_telepon")) {
                tambahKolomJikaAda(kolom, namaKolom, nilai, "no_telepon", pelanggan.getNoTelepon());
            } else {
                tambahKolomJikaAda(kolom, namaKolom, nilai, "no_hp", pelanggan.getNoTelepon());
            }
            tambahKolomJikaAda(kolom, namaKolom, nilai, "email", pelanggan.getEmail());
            tambahKolomJikaAda(kolom, namaKolom, nilai, "foto_ktp", pelanggan.getFotoKtp());
            tambahKolomJikaAda(kolom, namaKolom, nilai, "foto_sim", pelanggan.getFotoSim());
            tambahKolomJikaAda(kolom, namaKolom, nilai, "foto_pelanggan", pelanggan.getFotoPelanggan());

            StringBuilder sql = new StringBuilder("UPDATE pelanggan SET ");
            for (int i = 0; i < namaKolom.size(); i++) {
                if (i > 0) sql.append(", ");
                sql.append(namaKolom.get(i)).append(" = ?");
            }
            sql.append(" WHERE id_pelanggan = ?");

            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < nilai.size(); i++) {
                    ps.setObject(i + 1, nilai.get(i));
                }
                ps.setInt(nilai.size() + 1, pelanggan.getIdPelanggan());
                return ps.executeUpdate() > 0;
            }
        }
    }

    public boolean hapusPelanggan(int idPelanggan) throws SQLException {
        String sql = """
            DELETE FROM pelanggan WHERE id_pelanggan = ?
            """;

        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPelanggan);
            return ps.executeUpdate() > 0;
        }
    }

    private Set<String> getColumns(Connection conn) throws SQLException {
        Set<String> kolom = new HashSet<>();
        try (ResultSet rs = conn.getMetaData().getColumns(conn.getCatalog(), null, "pelanggan", null)) {
            while (rs.next()) {
                kolom.add(rs.getString("COLUMN_NAME").toLowerCase());
            }
        }
        return kolom;
    }

    private void tambahKolomJikaAda(Set<String> kolom, List<String> namaKolom,
                                List<Object> nilai, String namaKolomDb, Object nilaiKolom) {
        if (kolom.contains(namaKolomDb)) {
            namaKolom.add(namaKolomDb);
            nilai.add(nilaiKolom);
        }
    }
}
