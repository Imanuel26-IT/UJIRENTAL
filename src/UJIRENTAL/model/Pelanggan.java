package UJIRENTAL.model;

import java.sql.Timestamp;

public class Pelanggan {

    private int idPelanggan;
    private String nik;
    private String namaPelanggan;

    private String alamat;
    private String noTelepon;
    private String email;

    private String fotoKtp;
    private String fotoSim;
    private String fotoPelanggan;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Pelanggan() {
    }

    public Pelanggan(int idPelanggan, String nik, String namaPelanggan, String alamat,
                     String noTelepon, String email, String fotoKtp) {
        this.idPelanggan = idPelanggan;
        this.nik = nik;
        this.namaPelanggan = namaPelanggan;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
        this.email = email;
        this.fotoKtp = fotoKtp;
    }

    // Identitas
    public int getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(int idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    // Kontak
    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Foto Dokumen
    public String getFotoKtp() {
        return fotoKtp;
    }

    public void setFotoKtp(String fotoKtp) {
        this.fotoKtp = fotoKtp;
    }

    public String getFotoSim() {
        return fotoSim;
    }

    public void setFotoSim(String fotoSim) {
        this.fotoSim = fotoSim;
    }

    public String getFotoPelanggan() {
        return fotoPelanggan;
    }

    public void setFotoPelanggan(String fotoPelanggan) {
        this.fotoPelanggan = fotoPelanggan;
    }

    // Waktu
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return namaPelanggan + " (" + nik + ")";
    }
}