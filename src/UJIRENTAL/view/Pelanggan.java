/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package UJIRENTAL.view;

import UJIRENTAL.controller.PelangganController;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author IMANUEL VALEN LINGGI
 */
public class Pelanggan extends javax.swing.JPanel {

    private static final String PLACEHOLDER_CARI = "ID / Nama Pelanggan";
    private static final String PLACEHOLDER_ID_UPDATE = "Masukan ID Pelanggan yang akan diedit";
    private static final String PLACEHOLDER_NAMA = "Masukan nama";
    private static final String PLACEHOLDER_ALAMAT = "Masukan alamat";
    private static final String PLACEHOLDER_NOMOR = "Masuskan nomor telepon";
    private static final String PLACEHOLDER_EMAIL = "Masukan email";
    private static final String PLACEHOLDER_ID_DELETE = "Masukan ID Pelanggan yang akan di delete";

    private final PelangganController pelangganCtrl;
    private DefaultTableModel modelTabelPelanggan;

    /**
     * Creates new form Pelanggan
     */
    public Pelanggan() {
        initComponents();
        pelangganCtrl = new PelangganController();
        siapkanModelTabel();
        pasangListenerTambahan();
        muatUlangDataPelanggan();
    }

    private void siapkanModelTabel() {
        modelTabelPelanggan = new DefaultTableModel(
                new Object[]{"ID Pelanggan", "NIK", "Nama", "Alamat", "Nomor Telepon", "Email"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePelanggan.setModel(modelTabelPelanggan);
    }

    private void pasangListenerTambahan() {
        btnCariPelanggan.addActionListener(evt -> prosesCariPelanggan());
        btnDeleteDelete.addActionListener(evt -> prosesHapusPelanggan());
    }

    private void tampilkanKeTablePelanggan(List<UJIRENTAL.model.Pelanggan> daftarData) {
        modelTabelPelanggan.setRowCount(0);
        for (UJIRENTAL.model.Pelanggan pelanggan : daftarData) {
            modelTabelPelanggan.addRow(new Object[]{
                pelanggan.getIdPelanggan(),
                pelanggan.getNik(),
                pelanggan.getNamaPelanggan(),
                pelanggan.getAlamat(),
                pelanggan.getNoTelepon(),
                pelanggan.getEmail()
            });
        }
    }

    private String ambilTeksBersih(String nilai, String placeholder) {
        if (nilai == null) {
            return "";
        }
        String hasil = nilai.trim();
        return hasil.isEmpty() || hasil.equalsIgnoreCase(placeholder) ? "" : hasil;
    }

    public void muatUlangDataPelanggan() {
        try {
            List<UJIRENTAL.model.Pelanggan> semuaPelanggan = pelangganCtrl.getAllPelanggan();
            tampilkanKeTablePelanggan(semuaPelanggan);
            lblTotalPelanggan.setText(String.valueOf(semuaPelanggan.size()));
        } catch (SQLException ex) {
            tampilkanKesalahanDatabase("memuat data pelanggan", ex);
        }
    }

    private Integer parsingIdPositif(String teksId, String namaField) {
        if (teksId.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, namaField + " harus diisi.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            int id = Integer.parseInt(teksId);
            if (id > 0) {
                return id;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, namaField + " harus berupa angka.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        JOptionPane.showMessageDialog(
                this, namaField + " harus berupa angka lebih besar dari 0.",
                "Validasi", JOptionPane.WARNING_MESSAGE);
        return null;
    }

    private void prosesCariPelanggan() {
        String kataKunci = ambilTeksBersih(txtCariPelanggan.getText(), PLACEHOLDER_CARI);
        if (kataKunci.isEmpty()) {
            muatUlangDataPelanggan();
            return;
        }

        try {
            List<UJIRENTAL.model.Pelanggan> hasilGabungan = new ArrayList<>();
            try {
                UJIRENTAL.model.Pelanggan ditemukan
                        = pelangganCtrl.getPelangganById(Integer.parseInt(kataKunci));
                if (ditemukan != null) {
                    hasilGabungan.add(ditemukan);
                }
            } catch (NumberFormatException ex) {
                // Kata kunci bukan ID; pencarian nama tetap dijalankan.
            }

            List<UJIRENTAL.model.Pelanggan> hasilNama = pelangganCtrl.cariPelanggan(kataKunci);
            for (UJIRENTAL.model.Pelanggan pelanggan : hasilNama) {
                boolean sudahAda = false;
                for (UJIRENTAL.model.Pelanggan data : hasilGabungan) {
                    if (data.getIdPelanggan() == pelanggan.getIdPelanggan()) {
                        sudahAda = true;
                        break;
                    }
                }
                if (!sudahAda) {
                    hasilGabungan.add(pelanggan);
                }
            }
            tampilkanKeTablePelanggan(hasilGabungan);
        } catch (SQLException ex) {
            tampilkanKesalahanDatabase("mencari pelanggan", ex);
        }
    }

    private void prosesUpdatePelanggan() {
        String idTeks = ambilTeksBersih(txtfieldIDPelangganUpdate.getText(), PLACEHOLDER_ID_UPDATE);
        Integer idTarget = parsingIdPositif(idTeks, "ID Pelanggan");
        if (idTarget == null) {
            return;
        }

        try {
            UJIRENTAL.model.Pelanggan dataLama = pelangganCtrl.getPelangganById(idTarget);
            if (dataLama == null) {
                JOptionPane.showMessageDialog(
                        this, "Pelanggan dengan ID " + idTarget + " tidak ditemukan.",
                        "Tidak Ditemukan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String namaBaru = ambilTeksBersih(txtfieldNamaUpdate.getText(), PLACEHOLDER_NAMA);
            String alamatBaru = ambilTeksBersih(txtfieldAlamatUpdate.getText(), PLACEHOLDER_ALAMAT);
            String nomorBaru = ambilTeksBersih(txtfieldnomorUpdate.getText(), PLACEHOLDER_NOMOR);
            String emailBaru = ambilTeksBersih(txtfieldemailUpdate.getText(), PLACEHOLDER_EMAIL);

            UJIRENTAL.model.Pelanggan dataUpdate = new UJIRENTAL.model.Pelanggan();
            dataUpdate.setIdPelanggan(dataLama.getIdPelanggan());
            dataUpdate.setNik(dataLama.getNik());
            dataUpdate.setNamaPelanggan(namaBaru.isEmpty() ? dataLama.getNamaPelanggan() : namaBaru);
            dataUpdate.setAlamat(alamatBaru.isEmpty() ? dataLama.getAlamat() : alamatBaru);
            dataUpdate.setNoTelepon(nomorBaru.isEmpty() ? dataLama.getNoTelepon() : nomorBaru);
            dataUpdate.setEmail(emailBaru.isEmpty() ? dataLama.getEmail() : emailBaru);
            dataUpdate.setFotoKtp(dataLama.getFotoKtp());

            if (pelangganCtrl.updatePelanggan(dataUpdate)) {
                JOptionPane.showMessageDialog(this, "Data pelanggan ID " + idTarget + " berhasil diupdate.");
                txtfieldIDPelangganUpdate.setText(PLACEHOLDER_ID_UPDATE);
                txtfieldNamaUpdate.setText(PLACEHOLDER_NAMA);
                txtfieldAlamatUpdate.setText(PLACEHOLDER_ALAMAT);
                txtfieldnomorUpdate.setText(PLACEHOLDER_NOMOR);
                txtfieldemailUpdate.setText(PLACEHOLDER_EMAIL);
                muatUlangDataPelanggan();
            } else {
                JOptionPane.showMessageDialog(
                        this, "Gagal mengupdate data pelanggan ID " + idTarget + ".",
                        "Gagal Update", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            tampilkanKesalahanDatabase("mengupdate data pelanggan", ex);
        }
    }

    private void prosesHapusPelanggan() {
        String idTeks = ambilTeksBersih(txtfieldIDDelete.getText(), PLACEHOLDER_ID_DELETE);
        Integer idHapus = parsingIdPositif(idTeks, "ID Pelanggan");
        if (idHapus == null) {
            return;
        }

        try {
            UJIRENTAL.model.Pelanggan dataTarget = pelangganCtrl.getPelangganById(idHapus);
            if (dataTarget == null) {
                JOptionPane.showMessageDialog(
                        this, "Pelanggan dengan ID " + idHapus + " tidak ditemukan.",
                        "Tidak Ditemukan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int konfirmasi = JOptionPane.showConfirmDialog(
                    this,
                    "Hapus pelanggan \"" + dataTarget.getNamaPelanggan() + "\" (ID: " + idHapus + ")?",
                    "Konfirmasi Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (konfirmasi == JOptionPane.YES_OPTION) {
                if (pelangganCtrl.hapusPelanggan(idHapus)) {
                    JOptionPane.showMessageDialog(this, "Pelanggan ID " + idHapus + " berhasil dihapus.");
                    txtfieldIDDelete.setText(PLACEHOLDER_ID_DELETE);
                    muatUlangDataPelanggan();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Gagal menghapus pelanggan ID " + idHapus + ". Periksa apakah data terikat transaksi.",
                            "Gagal Delete",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } catch (SQLException ex) {
            tampilkanKesalahanDatabase("menghapus data pelanggan", ex);
        }
    }

    private void tampilkanKesalahanDatabase(String aksi, SQLException ex) {
        JOptionPane.showMessageDialog(
                this,
                "Gagal " + aksi + ": " + ex.getMessage(),
                "Kesalahan Database",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtCariPelanggan = new javax.swing.JTextField();
        pnlTotalMobil = new javax.swing.JPanel();
        lblTotalPelanggan = new javax.swing.JLabel();
        txtTotalPelanggan = new javax.swing.JLabel();
        btnCariPelanggan = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        txtdeletepelanggan = new javax.swing.JLabel();
        txtIDDelete = new javax.swing.JLabel();
        txtfieldIDDelete = new javax.swing.JTextField();
        btnDeleteDelete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePelanggan = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        txtUpdatePelanggan = new javax.swing.JLabel();
        txtIDUpdate = new javax.swing.JLabel();
        txtnamaUpdate = new javax.swing.JLabel();
        txtalamatUpdate = new javax.swing.JLabel();
        txtnomorUpdate = new javax.swing.JLabel();
        txtemailUpdate = new javax.swing.JLabel();
        txtfieldIDPelangganUpdate = new javax.swing.JTextField();
        txtfieldNamaUpdate = new javax.swing.JTextField();
        txtfieldAlamatUpdate = new javax.swing.JTextField();
        txtfieldnomorUpdate = new javax.swing.JTextField();
        txtfieldemailUpdate = new javax.swing.JTextField();
        btnUpdate = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtCariPelanggan.addActionListener(this::txtCariPelangganActionPerformed);
        txtCariPelanggan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariPelangganKeyReleased(evt);
            }
        });

        pnlTotalMobil.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTotalPelanggan.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        lblTotalPelanggan.setText("4");

        txtTotalPelanggan.setText("Total Pelanggan");

        javax.swing.GroupLayout pnlTotalMobilLayout = new javax.swing.GroupLayout(pnlTotalMobil);
        pnlTotalMobil.setLayout(pnlTotalMobilLayout);
        pnlTotalMobilLayout.setHorizontalGroup(
            pnlTotalMobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTotalMobilLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTotalMobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotalPelanggan))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        pnlTotalMobilLayout.setVerticalGroup(
            pnlTotalMobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTotalMobilLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTotalPelanggan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotalPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnCariPelanggan.setText("Cari");

        jLabel1.setText("Nama Pelanggan");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTotalMobil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtCariPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCariPelanggan))
                    .addComponent(jLabel1))
                .addGap(50, 50, 50))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTotalMobil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCariPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCariPelanggan))
                .addGap(16, 16, 16))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtdeletepelanggan.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtdeletepelanggan.setText("Delete Pelanggan");

        txtIDDelete.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtIDDelete.setText("ID Pelanggan");

        txtfieldIDDelete.addActionListener(this::txtfieldIDDeleteActionPerformed);

        btnDeleteDelete.setText("Delete");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(90, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(txtdeletepelanggan)
                        .addGap(177, 177, 177))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnDeleteDelete)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtIDDelete)
                                .addGap(18, 18, 18)
                                .addComponent(txtfieldIDDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(69, 69, 69))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(txtdeletepelanggan)
                .addGap(43, 43, 43)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtfieldIDDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIDDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDeleteDelete)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablePelanggan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablePelanggan);

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtUpdatePelanggan.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtUpdatePelanggan.setText("Update Pelanggan");

        txtIDUpdate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtIDUpdate.setText("ID Pelanggan");

        txtnamaUpdate.setText("Nama");

        txtalamatUpdate.setText("Alamat");

        txtnomorUpdate.setText("Nomor Telepon");

        txtemailUpdate.setText("Email");

        txtfieldIDPelangganUpdate.addActionListener(this::txtfieldIDPelangganUpdateActionPerformed);

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtIDUpdate)
                    .addComponent(txtnamaUpdate)
                    .addComponent(txtalamatUpdate)
                    .addComponent(txtnomorUpdate)
                    .addComponent(txtemailUpdate))
                .addGap(57, 57, 57)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtfieldemailUpdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtfieldnomorUpdate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                        .addComponent(txtfieldAlamatUpdate, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtfieldNamaUpdate, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtfieldIDPelangganUpdate, javax.swing.GroupLayout.Alignment.LEADING)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnUpdate)
                .addContainerGap(12, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtUpdatePelanggan)
                .addGap(181, 181, 181))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(txtUpdatePelanggan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtIDUpdate)
                    .addComponent(txtfieldIDPelangganUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(txtnamaUpdate))
                            .addComponent(txtfieldNamaUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(txtalamatUpdate))
                            .addComponent(txtfieldAlamatUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(txtnomorUpdate))
                            .addComponent(txtfieldnomorUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(txtemailUpdate))
                            .addComponent(txtfieldemailUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnUpdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtCariPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariPelangganActionPerformed
        // TODO: Cari pelanggan saat Enter ditekan pada txtCariPelanggan
        prosesCariPelanggan();
    }//GEN-LAST:event_txtCariPelangganActionPerformed

    private void txtCariPelangganKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariPelangganKeyReleased
        // TODO: Saring baris tablePelanggan secara langsung berdasarkan ID atau Nama
        prosesCariPelanggan();
    }//GEN-LAST:event_txtCariPelangganKeyReleased

    private void txtfieldIDDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtfieldIDDeleteActionPerformed
        // TODO: Jalankan proses delete pelanggan saat Enter ditekan di txtfieldIDDelete
        prosesHapusPelanggan();
    }//GEN-LAST:event_txtfieldIDDeleteActionPerformed

    private void txtfieldIDPelangganUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtfieldIDPelangganUpdateActionPerformed
        // TODO: Muat data pelanggan berdasarkan ID yang dimasukkan di txtfieldIDPelangganUpdate
        Integer id = parsingIdPositif(
                ambilTeksBersih(txtfieldIDPelangganUpdate.getText(), PLACEHOLDER_ID_UPDATE),
                "ID Pelanggan");
        if (id == null) {
            return;
        }
        try {
            UJIRENTAL.model.Pelanggan ditemukan = pelangganCtrl.getPelangganById(id);
            if (ditemukan != null) {
                txtfieldNamaUpdate.setText(ditemukan.getNamaPelanggan() != null ? ditemukan.getNamaPelanggan() : "");
                txtfieldAlamatUpdate.setText(ditemukan.getAlamat() != null ? ditemukan.getAlamat() : "");
                txtfieldnomorUpdate.setText(ditemukan.getNoTelepon() != null ? ditemukan.getNoTelepon() : "");
                txtfieldemailUpdate.setText(ditemukan.getEmail() != null ? ditemukan.getEmail() : "");
            } else {
                JOptionPane.showMessageDialog(
                        this, "Pelanggan dengan ID " + id + " tidak ditemukan.",
                        "Tidak Ditemukan", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            tampilkanKesalahanDatabase("memuat data pelanggan", ex);
        }
    }//GEN-LAST:event_txtfieldIDPelangganUpdateActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO: Update data pelanggan berdasarkan ID dengan mempertahankan field yang tidak diisi
        prosesUpdatePelanggan();
    }//GEN-LAST:event_btnUpdateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCariPelanggan;
    private javax.swing.JButton btnDeleteDelete;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalPelanggan;
    private javax.swing.JPanel pnlTotalMobil;
    private javax.swing.JTable tablePelanggan;
    private javax.swing.JTextField txtCariPelanggan;
    private javax.swing.JLabel txtIDDelete;
    private javax.swing.JLabel txtIDUpdate;
    private javax.swing.JLabel txtTotalPelanggan;
    private javax.swing.JLabel txtUpdatePelanggan;
    private javax.swing.JLabel txtalamatUpdate;
    private javax.swing.JLabel txtdeletepelanggan;
    private javax.swing.JLabel txtemailUpdate;
    private javax.swing.JTextField txtfieldAlamatUpdate;
    private javax.swing.JTextField txtfieldIDDelete;
    private javax.swing.JTextField txtfieldIDPelangganUpdate;
    private javax.swing.JTextField txtfieldNamaUpdate;
    private javax.swing.JTextField txtfieldemailUpdate;
    private javax.swing.JTextField txtfieldnomorUpdate;
    private javax.swing.JLabel txtnamaUpdate;
    private javax.swing.JLabel txtnomorUpdate;
    // End of variables declaration//GEN-END:variables
}
