# 🚗 UJIRENTAL - Sistem Manajemen Rental Mobil

Aplikasi desktop berbasis **Java Swing** dan **MySQL** yang dirancang untuk mengelola operasional rental mobil, mulai dari pendataan armada mobil, pelanggan, pencatatan transaksi sewa dan pengembalian, hingga sistem reward promo loyalty member.

---

## 📋 Daftar Isi
1. [Fitur Utama](#-fitur-utama)
2. [Struktur Proyek (MVC Architecture)](#-struktur-proyek-mvc-architecture)
3. [Prasyarat Sistem](#-prasyarat-sistem)
4. [Panduan Menjalankan Proyek](#-panduan-menjalankan-proyek)
5. [Struktur Database](#-struktur-database)
6. [Panduan Kolaborasi Tim (Git Workflow)](#-panduan-kolaborasi-tim-git-workflow)

---

## ✨ Fitur Utama
- **Dashboard Ringkasan**: Statistik armada mobil (Tersedia, Disewa, Perawatan) dan riwayat aktivitas transaksi terkini.
- **Manajemen Data Mobil**: Tambah, edit, hapus, dan lihat ketersediaan mobil (plat nomor, merek, tipe, harga sewa, transmisi, kapasitas, status).
- **Manajemen Pelanggan**: Pendataan identitas penyewa (NIK, nama, alamat, no telepon, email, foto KTP).
- **Transaksi Sewa & Pengembalian**:
  - Perhitungan otomatis total biaya sewa berdasarkan durasi hari.
  - Perhitungan denda keterlambatan pengembalian.
  - Pengecekan otomatis reward/promo gratis sewa.
- **Sistem Promo Loyalty**: Memberikan gratis 1x sewa untuk pelanggan setia yang mencapai target sewa tertentu (misal: 10x sewa).

---

## 📂 Struktur Proyek (MVC Architecture)
```text
UJIRENTAL/
├── lib/
│   ├── mysql-connector-j-26.10.0.jar      # Driver JDBC MySQL
│   └── nblibraries.properties
├── nbproject/                             # Konfigurasi Apache NetBeans
├── src/
│   ├── Main.java                          # Entry point aplikasi
│   └── UJIRENTAL/
│       ├── controller/                    # Logika bisnis & controller
│       │   ├── MobilController.java
│       │   ├── PelangganController.java
│       │   ├── PromoController.java
│       │   ├── PromoServis.java
│       │   └── TransaksiController.java
│       ├── model/                         # Data model / entitas
│       │   ├── Dashboard.java
│       │   ├── Mobil.java
│       │   ├── Pelanggan.java
│       │   ├── Promo.java
│       │   ├── PromoStatus.java
│       │   └── Transaksi.java
│       ├── util/                          # Utilitas koneksi database
│       │   └── Koneksi.java
│       └── view/                          # GUI Form (Swing)
│           ├── DashboardFrame.java
│           ├── MenuDashboard.java
│           ├── MenuMobil.java
│           ├── MenuTransaksi.java
│           ├── Header.java
│           └── Sidebar.java
├── db_ujirental.sql                       # Database dump (skema & data awal)
├── build.xml                              # Ant build script
└── README.md
```

---

## ⚙️ Prasyarat Sistem
Sebelum menjalankan proyek, pastikan sudah menginstal:
1. **Java Development Kit (JDK)**: JDK 17 atau yang lebih baru (disarankan JDK 21 / JDK 26 sesuai konfigurasi proyek).
2. **Apache NetBeans IDE**: Versi 18 ke atas (atau IDE Java yang mendukung Ant Project).
3. **MySQL Server**: Menggunakan XAMPP, Laragon, atau MySQL Server mandiri.

---

## 🚀 Panduan Menjalankan Proyek

### 1. Clone Repository
```bash
git clone https://github.com/Imanuel26-IT/UJIRENTAL.git
cd UJIRENTAL
```

### 2. Setup Database MySQL
1. Buka XAMPP / Laragon, lalu jalankan service **Apache** dan **MySQL**.
2. Buka browser dan akses **phpMyAdmin** (`http://localhost/phpmyadmin`) atau GUI database client favorit Anda (DBeaver / Navicat / HeidiSQL).
3. Buat database baru bernama:
   ```sql
   CREATE DATABASE db_ujirental;
   ```
4. Import file `db_ujirental.sql` yang berada di root folder proyek ini ke dalam database `db_ujirental`.

### 3. Cek Konfigurasi Koneksi Database
Buka file `src/UJIRENTAL/util/Koneksi.java`:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/db_ujirental?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";
private static final String DB_USER = "root";
private static final String DB_PASS = "";
```
> [!NOTE]
> Sesuaikan `DB_USER` dan `DB_PASS` jika MySQL Anda menggunakan password.

### 4. Buka di Apache NetBeans
1. Buka **Apache NetBeans**.
2. Klik menu **File** > **Open Project...** (`Ctrl + Shift + O`).
3. Cari dan pilih folder **UJIRENTAL**, lalu klik **Open Project**.
4. Jika muncul peringatan library `mysql-connector-j`, klik kanan folder `Libraries` pada proyek di NetBeans > **Add JAR/Folder** > pilih file `lib/mysql-connector-j-26.10.0.jar`.
5. Klik kanan pada project **UJIRENTAL** > **Clean and Build**.
6. Tekan **F6** atau tombol **Run Project** untuk menjalankan aplikasi.

---

## 🗄️ Struktur Database (`db_ujirental`)
- `mobil`: Menyimpan data armada kendaraan beserta status (`Tersedia`, `Disewa`, `Perawatan`).
- `pelanggan`: Menyimpan data penyewa (NIK, nama, kontak, foto KTP).
- `promo`: Menyimpan konfigurasi promo loyalty program.
- `transaksi`: Mencatat transaksi peminjaman, durasi, total tarif, denda, status pembayaran/pengembalian.

---

## 🤝 Panduan Kolaborasi Tim (Git Workflow)
Untuk menjaga kode tetap rapi dan meminimalisir merge conflict antar anggota tim, ikuti alur kerja berikut:

1. **Selalu update branch lokal sebelum mulai coding:**
   ```bash
   git checkout main
   git pull origin main
   ```
2. **Buat branch baru untuk setiap fitur/tugas:**
   ```bash
   git checkout -b fitur/nama-fitur
   # contoh: git checkout -b fitur/tambah-validasi-transaksi
   ```
3. **Commit perubahan secara berkala:**
   ```bash
   git add .
   git commit -m "feat: tambahkan validasi form transaksi"
   ```
4. **Push branch ke GitHub:**
   ```bash
   git push origin fitur/nama-fitur
   ```
5. **Buat Pull Request (PR):**
   - Buka repository di GitHub.
   - Buat **New Pull Request** dari branch Anda ke branch `main`.
   - Diskusikan bersama rekan tim sebelum di-merge.
