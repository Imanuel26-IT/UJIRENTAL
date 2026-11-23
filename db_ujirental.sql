-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Nov 20, 2026 at 02:28 PM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_ujirental`
--

-- --------------------------------------------------------

--
-- Table structure for table `mobil`
--

CREATE TABLE `mobil` (
  `plat_nomor` varchar(20) NOT NULL,
  `merek_mobil` varchar(50) NOT NULL,
  `tipe_mobil` varchar(50) NOT NULL,
  `harga_sewa` decimal(12,2) NOT NULL,
  `warna` varchar(30) DEFAULT NULL,
  `transmisi` varchar(20) DEFAULT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'Tersedia',
  `tahun` int DEFAULT NULL,
  `kapasitas` int DEFAULT NULL,
  `no_rangka` varchar(50) DEFAULT NULL,
  `no_mesin` varchar(50) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mobil`
--

INSERT INTO `mobil` (`plat_nomor`, `merek_mobil`, `tipe_mobil`, `harga_sewa`, `warna`, `transmisi`, `status`, `tahun`, `kapasitas`, `no_rangka`, `no_mesin`, `created_at`, `updated_at`) VALUES
('A 4474 WAG', 'Honda', 'CRV', '750000.00', 'Silver', 'Manual', 'Tersedia', 2025, 7, NULL, NULL, '2026-11-18 09:07:03', '2026-11-18 09:07:03'),
('H 1234 AB', 'Toyota', 'Avanza', '350000.00', 'Putih', 'Manual', 'Tersedia', 2021, 7, 'MHKM5F1JKMX12301', '1NR1234561', '2026-11-18 08:42:32', '2026-11-18 08:42:32'),
('H 2345 CD', 'Daihatsu', 'Xenia', '325000.00', 'Silver', 'Manual', 'Tersedia', 2020, 7, 'MHKM5F1JKMX12302', '1NR1234562', '2026-11-18 08:42:32', '2026-11-18 08:42:32'),
('H 3456 EF', 'Honda', 'Brio', '275000.00', 'Merah', 'Matic', 'Tersedia', 2022, 5, 'MHRDD1850MX12303', 'L12B1234563', '2026-11-18 08:42:32', '2026-11-18 08:42:32'),
('H 5678 IJ', 'Mitsubishi', 'Xpander', '450000.00', 'Abu-abu', 'Matic', 'Perawatan', 2023, 7, 'MHMNC1LMKMX12305', '4A911234565', '2026-11-18 08:42:32', '2026-11-18 08:42:32'),
('H 6789 KL', 'Suzuki', 'Ertiga', '350000.00', 'Putih', 'Manual', 'Tersedia', 2019, 7, 'MHYGC41SMX12306', 'K15B1234566', '2026-11-18 08:42:32', '2026-11-18 08:42:32');

-- --------------------------------------------------------

--
-- Table structure for table `pelanggan`
--

CREATE TABLE `pelanggan` (
  `id_pelanggan` int NOT NULL,
  `nik` varchar(20) NOT NULL,
  `nama_pelanggan` varchar(100) NOT NULL,
  `alamat` text,
  `no_telepon` varchar(20) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `foto_ktp` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `promo`
--

CREATE TABLE `promo` (
  `id_promo` int NOT NULL,
  `nama_promo` varchar(100) NOT NULL,
  `minimal_sewa` int NOT NULL DEFAULT '10',
  `periode_bulan` int NOT NULL DEFAULT '12',
  `hadiah` varchar(100) NOT NULL,
  `status_promo` varchar(20) NOT NULL DEFAULT 'Aktif',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `promo`
--

INSERT INTO `promo` (`id_promo`, `nama_promo`, `minimal_sewa`, `periode_bulan`, `hadiah`, `status_promo`, `created_at`, `updated_at`) VALUES
(1, 'Loyalty Member 10x Sewa', 10, 3, 'Gratis 1x Sewa', 'Aktif', '2026-11-18 05:21:36', '2026-11-18 07:54:53');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id_transaksi` int NOT NULL,
  `plat_nomor` varchar(20) NOT NULL,
  `id_pelanggan` int NOT NULL,
  `tanggal_sewa` date NOT NULL,
  `tanggal_kembali` date NOT NULL,
  `lama_sewa` int NOT NULL,
  `total_harga` decimal(12,2) NOT NULL,
  `denda` decimal(12,2) NOT NULL DEFAULT '0.00',
  `gratis_promo` tinyint(1) NOT NULL DEFAULT '0',
  `status_transaksi` varchar(30) NOT NULL DEFAULT 'Sedang Berjalan',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `mobil`
--
ALTER TABLE `mobil`
  ADD PRIMARY KEY (`plat_nomor`);

--
-- Indexes for table `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`id_pelanggan`),
  ADD UNIQUE KEY `nik` (`nik`);

--
-- Indexes for table `promo`
--
ALTER TABLE `promo`
  ADD PRIMARY KEY (`id_promo`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `fk_transaksi_mobil` (`plat_nomor`),
  ADD KEY `fk_transaksi_pelanggan` (`id_pelanggan`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `pelanggan`
--
ALTER TABLE `pelanggan`
  MODIFY `id_pelanggan` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `promo`
--
ALTER TABLE `promo`
  MODIFY `id_promo` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id_transaksi` int NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD CONSTRAINT `fk_transaksi_mobil` FOREIGN KEY (`plat_nomor`) REFERENCES `mobil` (`plat_nomor`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_transaksi_pelanggan` FOREIGN KEY (`id_pelanggan`) REFERENCES `pelanggan` (`id_pelanggan`) ON DELETE RESTRICT ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


CREATE DATABASE IF NOT EXISTS db_ujirental CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci

USE db_ujirental;

CREATE TABLE IF NOT EXISTS `pelanggan` (
  `id_pelanggan` int NOT NULL AUTO_INCREMENT,
  `nik` varchar(20) NOT NULL,
  `nama_pelanggan` varchar(100) NOT NULL,
  `alamat` text,
  `no_telepon` varchar(20) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `foto_ktp` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_pelanggan`),
  UNIQUE KEY `nik` (`nik`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

USE db_ujirental;

INSERT INTO `pelanggan` (`nik`, `nama_pelanggan`, `alamat`, `no_telepon`, `email`, `foto_ktp`) VALUES
('3373011504950002', 'Nadia Kusuma', 'Jl. Osamaliki No. 22, Salatiga', '081325678910', 'nadia.kusuma@example.com', 'ktp_nadia_kusuma.jpg'),
('3373022008920003', 'Dimas Hermawan', 'Jl. Pemuda No. 88, Salatiga', '085740991122', 'dimas.hermawan@example.com', 'ktp_dimas_hermawan.jpg'),
('3373031011990004', 'Salsa Wijaya', 'Jl. Kartini No. 14, Salatiga', '082136778899', 'salsa.wijaya@example.com', 'ktp_salsa_wijaya.jpg');