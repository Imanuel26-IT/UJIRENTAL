/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UJIRENTAL.controller;

import UJIRENTAL.model.Pelanggan;
import java.util.List;

/**
 *
 * @author IMANUEL VALEN LINGGI
 */
public class TestPelangganController {

    public static void main(String[] args) {
        PelangganController customerCtrl = new PelangganController();

        // 1. Cek koneksi database & ambil daftar pelanggan awal
        List<Pelanggan> initialCustomers = customerCtrl.getAllPelanggan();
        System.out.println("=== 1. DATA PELANGGAN AWAL ===");
        System.out.println("Total pelanggan awal: " + initialCustomers.size());
        for (Pelanggan cust : initialCustomers) {
            System.out.println(" - [" + cust.getIdPelanggan() + "] "
                    + cust.getNik() + " | " + cust.getNamaPelanggan()
                    + " | " + cust.getNoTelepon());
        }

        // 2. Uji tambah data pelanggan baru
        Pelanggan draftCustomer = new Pelanggan();
        draftCustomer.setNik("3373011205980001");
        draftCustomer.setNamaPelanggan("Rizky Pratama");
        draftCustomer.setAlamat("Jl. Diponegoro No. 45, Salatiga");
        draftCustomer.setNoTelepon("081229887766");
        draftCustomer.setEmail("rizky.pratama@example.com");
        draftCustomer.setFotoKtp("ktp_rizky_pratama.jpg");

        boolean isInserted = customerCtrl.tambahPelanggan(draftCustomer);
        System.out.println("\n=== 2. TAMBAH PELANGGAN ===");
        System.out.println("Status tambah: " + isInserted);

        // 3. Uji pencarian pelanggan berdasarkan kata kunci
        List<Pelanggan> searchResults = customerCtrl.cariPelanggan("Rizky");
        System.out.println("\n=== 3. CARI PELANGGAN ('Rizky') ===");
        System.out.println("Jumlah hasil ditemukan: " + searchResults.size());

        if (!searchResults.isEmpty()) {
            Pelanggan targetCustomer = searchResults.get(0);
            int targetId = targetCustomer.getIdPelanggan();
            System.out.println("Target ID: " + targetId + " | Nama: " + targetCustomer.getNamaPelanggan());

            // 4. Uji perbarui data pelanggan
            targetCustomer.setNoTelepon("085640112233");
            targetCustomer.setAlamat("Jl. Pattimura No. 18, Salatiga");
            boolean isUpdated = customerCtrl.updatePelanggan(targetCustomer);
            System.out.println("\n=== 4. UPDATE PELANGGAN ===");
            System.out.println("Status update: " + isUpdated);

            // 5. Uji ambil data berdasarkan ID
            Pelanggan fetchedById = customerCtrl.getPelangganById(targetId);
            System.out.println("\n=== 5. GET BY ID (" + targetId + ") ===");
            if (fetchedById != null) {
                System.out.println("Nama: " + fetchedById.getNamaPelanggan()
                        + " | No Telp baru: " + fetchedById.getNoTelepon()
                        + " | Alamat baru: " + fetchedById.getAlamat());
            } else {
                System.out.println("Data tidak ditemukan.");
            }

            // 6. Uji hapus data pelanggan uji agar tabel tetap bersih
            boolean isDeleted = customerCtrl.hapusPelanggan(targetId);
            System.out.println("\n=== 6. HAPUS PELANGGAN UJI ===");
            System.out.println("Status hapus: " + isDeleted);
        }

        // 7. Cek jumlah data akhir setelah pengujian
        List<Pelanggan> finalCustomers = customerCtrl.getAllPelanggan();
        System.out.println("\n=== 7. DATA PELANGGAN AKHIR ===");
        System.out.println("Total pelanggan akhir: " + finalCustomers.size());
    }
}