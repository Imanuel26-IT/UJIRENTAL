USE db_ujirental;

-- Menambahkan atribut pelanggan yang digunakan oleh panel dan controller.
ALTER TABLE pelanggan
    ADD COLUMN IF NOT EXISTS nik VARCHAR(16) NULL AFTER id_pelanggan,
    ADD COLUMN IF NOT EXISTS nama_pelanggan VARCHAR(100) NULL AFTER nik,
    ADD COLUMN IF NOT EXISTS alamat TEXT NULL AFTER nama_pelanggan,
    ADD COLUMN IF NOT EXISTS no_telepon VARCHAR(15) NULL AFTER alamat,
    ADD COLUMN IF NOT EXISTS email VARCHAR(100) NULL AFTER no_telepon,
    ADD COLUMN IF NOT EXISTS foto_ktp VARCHAR(255) NULL AFTER email,
    ADD COLUMN IF NOT EXISTS foto_sim VARCHAR(255) NULL AFTER foto_ktp,
    ADD COLUMN IF NOT EXISTS foto_pelanggan VARCHAR(255) NULL AFTER foto_sim,
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP AFTER foto_pelanggan,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP AFTER created_at;

-- Menghindari duplikasi NIK dan nomor telepon pada pelanggan baru.
-- Jalankan hanya jika kolom belum memiliki data duplikat.
ALTER TABLE pelanggan
    ADD UNIQUE INDEX uk_pelanggan_nik (nik),
    ADD UNIQUE INDEX uk_pelanggan_no_telepon (no_telepon);

