package UJIRENTAL.view;

import UJIRENTAL.model.Mobil;
import UJIRENTAL.controller.MobilController;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
/**
 *
 * @author IMANUEL VALEN LINGGI
 */
public class MenuMobil extends javax.swing.JPanel {

    private final MobilController mobilController = new MobilController();
    private final DefaultTableModel modelTabelMobil = new DefaultTableModel(
            new Object[]{"Plat Nomor", "Merek", "Tipe", "Harga / Hari", "Warna",
                "Transmisi", "Status", "Tahun", "Kapasitas"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final List<Mobil> mobilDitampilkan = new ArrayList<>();
    private Mobil mobilTerpilih;
    private boolean formSiap;

    /**
     * Creates new form MenuMobil
     */
    public MenuMobil() {
        initComponents();
        aturPilihanForm();
        tblMobil.setModel(modelTabelMobil);
        txtCariMobil.setText("");
        bersihkanForm();
        formSiap = true;
        muatDataMobil();
    }

    private void aturPilihanForm() {
        cmbWarnaMobil.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Hitam", "Putih", "Silver", "Abu-abu", "Merah", "Biru", "Lainnya"}));
        cmbTransmisiMobil.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Manual", "Otomatis"}));
        cmbStatusMobil.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Tersedia", "Disewa", "Perawatan"}));
        cmbFilterStatusMobil.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Semua", "Tersedia", "Disewa", "Perawatan"}));
    }

    private void muatDataMobil() {
        List<Mobil> semuaMobil = mobilController.getAllMobil();
        String kataKunci = txtCariMobil.getText().trim().toLowerCase();
        String statusFilter = (String) cmbFilterStatusMobil.getSelectedItem();

        lblTotalMobil.setText(String.valueOf(semuaMobil.size()));
        modelTabelMobil.setRowCount(0);
        mobilDitampilkan.clear();
        mobilTerpilih = null;
        btnEditFormMobil.setEnabled(false);
        btnHapusFormMobil.setEnabled(false);

        for (Mobil mobil : semuaMobil) {
            boolean cocokStatus = "Semua".equals(statusFilter)
                    || mobil.getStatus().equalsIgnoreCase(statusFilter);
            String teksMobil = (mobil.getPlatNomor() + " " + mobil.getMerekMobil()
                    + " " + mobil.getTipeMobil()).toLowerCase();
            boolean cocokKataKunci = teksMobil.contains(kataKunci);

            if (cocokStatus && cocokKataKunci) {
                mobilDitampilkan.add(mobil);
                modelTabelMobil.addRow(new Object[]{
                    mobil.getPlatNomor(), mobil.getMerekMobil(), mobil.getTipeMobil(),
                    mobil.getHargaSewa(), mobil.getWarna(), mobil.getTransmisi(),
                    mobil.getStatus(), mobil.getTahun(), mobil.getKapasitas()
                });
            }
        }
    }

    private Mobil bacaMobilDariForm() {
        String platNomor = txtPlatNomor.getText().trim();
        String merek = txtMerekMobil.getText().trim();
        String tipe = txtTipeMobil.getText().trim();
        String teksHarga = txtHargaSewa.getText().trim();
        String teksTahun = txtTahunMobil.getText().trim();
        String teksKapasitas = txtKapasitasMobil.getText().trim();

        if (platNomor.isEmpty() || merek.isEmpty() || tipe.isEmpty()
                || teksHarga.isEmpty() || teksTahun.isEmpty() || teksKapasitas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lengkapi semua data mobil terlebih dahulu.");
            return null;
        }

        try {
            BigDecimal harga = new BigDecimal(teksHarga);
            int tahun = Integer.parseInt(teksTahun);
            int kapasitas = Integer.parseInt(teksKapasitas);

            if (harga.signum() <= 0 || harga.scale() > 2
                    || harga.precision() - harga.scale() > 10 || tahun <= 0 || kapasitas <= 0) {
                JOptionPane.showMessageDialog(this, "Harga, tahun, dan kapasitas harus bernilai positif dan valid.");
                return null;
            }

            Mobil mobil = new Mobil(
                    platNomor,
                    merek,
                    tipe,
                    harga,
                    (String) cmbWarnaMobil.getSelectedItem(),
                    (String) cmbTransmisiMobil.getSelectedItem(),
                    (String) cmbStatusMobil.getSelectedItem(),
                    tahun,
                    kapasitas,
                    mobilTerpilih == null ? null : mobilTerpilih.getNoRangka(),
                    mobilTerpilih == null ? null : mobilTerpilih.getNoMesin()
            );
            return mobil;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus angka desimal, sedangkan tahun dan kapasitas harus angka bulat.");
            return null;
        }
    }

    private void bersihkanForm() {
        mobilTerpilih = null;
        txtPlatNomor.setText("");
        txtMerekMobil.setText("");
        txtTipeMobil.setText("");
        txtHargaSewa.setText("");
        txtTahunMobil.setText("");
        txtKapasitasMobil.setText("");
        cmbWarnaMobil.setSelectedIndex(0);
        cmbTransmisiMobil.setSelectedIndex(0);
        cmbStatusMobil.setSelectedItem("Tersedia");
        tblMobil.clearSelection();
        btnEditFormMobil.setEnabled(false);
        btnHapusFormMobil.setEnabled(false);
        jLabel3.setText("Form Input Data Mobil Baru");
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
        txtCariMobil = new javax.swing.JTextField();
        cmbFilterStatusMobil = new javax.swing.JComboBox<>();
        pnlTotalMobil = new javax.swing.JPanel();
        lblTotalMobil = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pnlInputMobil = new javax.swing.JPanel();
        pnlJudulForm = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        pnlInputPlat = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtPlatNomor = new javax.swing.JTextField();
        pnlInputMerek = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtMerekMobil = new javax.swing.JTextField();
        pnlInputTipe = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtTipeMobil = new javax.swing.JTextField();
        pnlInputHargaSewa = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtHargaSewa = new javax.swing.JTextField();
        pnlInputWarna = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cmbWarnaMobil = new javax.swing.JComboBox<>();
        pnlInputTransmisi = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cmbTransmisiMobil = new javax.swing.JComboBox<>();
        pnlInputStatus = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        cmbStatusMobil = new javax.swing.JComboBox<>();
        pnlInputTahun = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtTahunMobil = new javax.swing.JTextField();
        pnlInputKapasitas = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtKapasitasMobil = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnHapusFormMobil = new javax.swing.JButton();
        btnClearFormMobil = new javax.swing.JButton();
        btnEditFormMobil = new javax.swing.JButton();
        btnTambahFormMobil = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMobil = new javax.swing.JTable();

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtCariMobil.setText("No Plat / Merek");
        txtCariMobil.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariMobilKeyReleased(evt);
            }
        });

        cmbFilterStatusMobil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbFilterStatusMobil.addActionListener(this::cmbFilterStatusMobilActionPerformed);

        pnlTotalMobil.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTotalMobil.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        lblTotalMobil.setText("4");

        jLabel2.setText("Total Mobil");

        javax.swing.GroupLayout pnlTotalMobilLayout = new javax.swing.GroupLayout(pnlTotalMobil);
        pnlTotalMobil.setLayout(pnlTotalMobilLayout);
        pnlTotalMobilLayout.setHorizontalGroup(
            pnlTotalMobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTotalMobilLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTotalMobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalMobil, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        pnlTotalMobilLayout.setVerticalGroup(
            pnlTotalMobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTotalMobilLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotalMobil, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTotalMobil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtCariMobil, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbFilterStatusMobil, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCariMobil, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFilterStatusMobil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTotalMobil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlInputMobil.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        pnlJudulForm.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Bell MT", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Form Input Data Mobil Baru");

        javax.swing.GroupLayout pnlJudulFormLayout = new javax.swing.GroupLayout(pnlJudulForm);
        pnlJudulForm.setLayout(pnlJudulFormLayout);
        pnlJudulFormLayout.setHorizontalGroup(
            pnlJudulFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlJudulFormLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlJudulFormLayout.setVerticalGroup(
            pnlJudulFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlJudulFormLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap())
        );

        jLabel4.setText("Plat Nomor");

        txtPlatNomor.setText("A 4474 WAG");

        javax.swing.GroupLayout pnlInputPlatLayout = new javax.swing.GroupLayout(pnlInputPlat);
        pnlInputPlat.setLayout(pnlInputPlatLayout);
        pnlInputPlatLayout.setHorizontalGroup(
            pnlInputPlatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputPlatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPlatNomor)
                .addContainerGap())
        );
        pnlInputPlatLayout.setVerticalGroup(
            pnlInputPlatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputPlatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputPlatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtPlatNomor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jLabel5.setText("Merek");

        txtMerekMobil.setText("Toyota");

        javax.swing.GroupLayout pnlInputMerekLayout = new javax.swing.GroupLayout(pnlInputMerek);
        pnlInputMerek.setLayout(pnlInputMerekLayout);
        pnlInputMerekLayout.setHorizontalGroup(
            pnlInputMerekLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputMerekLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(34, 34, 34)
                .addComponent(txtMerekMobil)
                .addContainerGap())
        );
        pnlInputMerekLayout.setVerticalGroup(
            pnlInputMerekLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputMerekLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputMerekLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtMerekMobil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jLabel6.setText("Tipe");

        txtTipeMobil.setText("Avanza");

        javax.swing.GroupLayout pnlInputTipeLayout = new javax.swing.GroupLayout(pnlInputTipe);
        pnlInputTipe.setLayout(pnlInputTipeLayout);
        pnlInputTipeLayout.setHorizontalGroup(
            pnlInputTipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputTipeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(43, 43, 43)
                .addComponent(txtTipeMobil)
                .addContainerGap())
        );
        pnlInputTipeLayout.setVerticalGroup(
            pnlInputTipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputTipeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputTipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtTipeMobil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jLabel7.setText("Harga Sewa /hari");

        txtHargaSewa.setText("350000");
        txtHargaSewa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHargaSewaKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout pnlInputHargaSewaLayout = new javax.swing.GroupLayout(pnlInputHargaSewa);
        pnlInputHargaSewa.setLayout(pnlInputHargaSewaLayout);
        pnlInputHargaSewaLayout.setHorizontalGroup(
            pnlInputHargaSewaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputHargaSewaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtHargaSewa)
                .addContainerGap())
        );
        pnlInputHargaSewaLayout.setVerticalGroup(
            pnlInputHargaSewaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputHargaSewaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputHargaSewaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtHargaSewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jLabel8.setText("Warna");

        cmbWarnaMobil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout pnlInputWarnaLayout = new javax.swing.GroupLayout(pnlInputWarna);
        pnlInputWarna.setLayout(pnlInputWarnaLayout);
        pnlInputWarnaLayout.setHorizontalGroup(
            pnlInputWarnaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputWarnaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(31, 31, 31)
                .addComponent(cmbWarnaMobil, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlInputWarnaLayout.setVerticalGroup(
            pnlInputWarnaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputWarnaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputWarnaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cmbWarnaMobil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel9.setText("Transmisi");

        cmbTransmisiMobil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout pnlInputTransmisiLayout = new javax.swing.GroupLayout(pnlInputTransmisi);
        pnlInputTransmisi.setLayout(pnlInputTransmisiLayout);
        pnlInputTransmisiLayout.setHorizontalGroup(
            pnlInputTransmisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputTransmisiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbTransmisiMobil, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlInputTransmisiLayout.setVerticalGroup(
            pnlInputTransmisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputTransmisiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputTransmisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cmbTransmisiMobil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel10.setText("Status");

        cmbStatusMobil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout pnlInputStatusLayout = new javax.swing.GroupLayout(pnlInputStatus);
        pnlInputStatus.setLayout(pnlInputStatusLayout);
        pnlInputStatusLayout.setHorizontalGroup(
            pnlInputStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addGap(29, 29, 29)
                .addComponent(cmbStatusMobil, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlInputStatusLayout.setVerticalGroup(
            pnlInputStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(cmbStatusMobil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel11.setText("Tahun");

        txtTahunMobil.setText("2006");

        javax.swing.GroupLayout pnlInputTahunLayout = new javax.swing.GroupLayout(pnlInputTahun);
        pnlInputTahun.setLayout(pnlInputTahunLayout);
        pnlInputTahunLayout.setHorizontalGroup(
            pnlInputTahunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputTahunLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(29, 29, 29)
                .addComponent(txtTahunMobil)
                .addContainerGap())
        );
        pnlInputTahunLayout.setVerticalGroup(
            pnlInputTahunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputTahunLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputTahunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtTahunMobil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel12.setText("Kapasitas");

        txtKapasitasMobil.setText("5");

        javax.swing.GroupLayout pnlInputKapasitasLayout = new javax.swing.GroupLayout(pnlInputKapasitas);
        pnlInputKapasitas.setLayout(pnlInputKapasitasLayout);
        pnlInputKapasitasLayout.setHorizontalGroup(
            pnlInputKapasitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputKapasitasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtKapasitasMobil)
                .addContainerGap())
        );
        pnlInputKapasitasLayout.setVerticalGroup(
            pnlInputKapasitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputKapasitasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputKapasitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtKapasitasMobil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        btnHapusFormMobil.setBackground(new java.awt.Color(255, 0, 0));
        btnHapusFormMobil.setText("Hapus");
        btnHapusFormMobil.addActionListener(this::btnHapusFormMobilActionPerformed);

        btnClearFormMobil.setBackground(new java.awt.Color(204, 204, 204));
        btnClearFormMobil.setText("Clear");
        btnClearFormMobil.addActionListener(this::btnClearFormMobilActionPerformed);

        btnEditFormMobil.setBackground(new java.awt.Color(102, 102, 255));
        btnEditFormMobil.setText("Edit");
        btnEditFormMobil.addActionListener(this::btnEditFormMobilActionPerformed);

        btnTambahFormMobil.setBackground(new java.awt.Color(102, 255, 0));
        btnTambahFormMobil.setText("Tambah");
        btnTambahFormMobil.addActionListener(this::btnTambahFormMobilActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnHapusFormMobil)
                .addGap(18, 18, 18)
                .addComponent(btnEditFormMobil)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(btnClearFormMobil)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnTambahFormMobil)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHapusFormMobil)
                    .addComponent(btnEditFormMobil)
                    .addComponent(btnTambahFormMobil)
                    .addComponent(btnClearFormMobil))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlInputMobilLayout = new javax.swing.GroupLayout(pnlInputMobil);
        pnlInputMobil.setLayout(pnlInputMobilLayout);
        pnlInputMobilLayout.setHorizontalGroup(
            pnlInputMobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputMobilLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputMobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlInputWarna, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlInputPlat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlJudulForm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlInputMerek, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlInputTipe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlInputHargaSewa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlInputTransmisi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlInputStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlInputTahun, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlInputKapasitas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlInputMobilLayout.setVerticalGroup(
            pnlInputMobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputMobilLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlJudulForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlInputPlat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlInputMerek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlInputTipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlInputHargaSewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlInputWarna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlInputTransmisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlInputStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlInputTahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlInputKapasitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tblMobil.setModel(new javax.swing.table.DefaultTableModel(
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
        tblMobil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMobilMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblMobil);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlInputMobil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlInputMobil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnHapusFormMobilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusFormMobilActionPerformed
        if (mobilTerpilih == null) {
            JOptionPane.showMessageDialog(this, "Pilih mobil yang akan dihapus dari tabel.");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Hapus mobil dengan plat nomor " + mobilTerpilih.getPlatNomor() + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );
        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }

        if (!mobilController.hapusMobil(mobilTerpilih.getPlatNomor())) {
            JOptionPane.showMessageDialog(
                    this,
                    "Mobil gagal dihapus. Mobil mungkin masih digunakan dalam transaksi.",
                    "Gagal Menghapus",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(this, "Data mobil berhasil dihapus.");
        bersihkanForm();
        muatDataMobil();
    }//GEN-LAST:event_btnHapusFormMobilActionPerformed

    private void btnEditFormMobilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditFormMobilActionPerformed
        if (mobilTerpilih == null) {
            JOptionPane.showMessageDialog(this, "Pilih mobil yang akan diedit dari tabel.");
            return;
        }

        Mobil mobilBaru = bacaMobilDariForm();
        if (mobilBaru == null) {
            return;
        }

        if (!mobilController.updateMobil(mobilBaru, mobilTerpilih.getPlatNomor())) {
            JOptionPane.showMessageDialog(
                    this,
                    "Data mobil gagal diperbarui. Periksa kembali plat nomor dan koneksi database.",
                    "Gagal Memperbarui",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(this, "Data mobil berhasil diperbarui.");
        bersihkanForm();
        muatDataMobil();
    }//GEN-LAST:event_btnEditFormMobilActionPerformed

    private void btnClearFormMobilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFormMobilActionPerformed
        bersihkanForm();
    }//GEN-LAST:event_btnClearFormMobilActionPerformed

    private void btnTambahFormMobilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahFormMobilActionPerformed
        Mobil mobil = bacaMobilDariForm();
        if (mobil == null) {
            return;
        }

        if (!mobilController.tambahMobil(mobil)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Mobil gagal ditambahkan. Plat nomor mungkin sudah terdaftar atau koneksi database bermasalah.",
                    "Gagal Menambahkan",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(this, "Data mobil berhasil ditambahkan.");
        bersihkanForm();
        muatDataMobil();
    }//GEN-LAST:event_btnTambahFormMobilActionPerformed

    private void tblMobilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMobilMouseClicked
        int barisDipilih = tblMobil.getSelectedRow();
        if (barisDipilih < 0) {
            return;
        }

        int barisModel = tblMobil.convertRowIndexToModel(barisDipilih);
        mobilTerpilih = mobilDitampilkan.get(barisModel);
        txtPlatNomor.setText(mobilTerpilih.getPlatNomor());
        txtMerekMobil.setText(mobilTerpilih.getMerekMobil());
        txtTipeMobil.setText(mobilTerpilih.getTipeMobil());
        txtHargaSewa.setText(mobilTerpilih.getHargaSewa().toPlainString());
        txtTahunMobil.setText(String.valueOf(mobilTerpilih.getTahun()));
        txtKapasitasMobil.setText(String.valueOf(mobilTerpilih.getKapasitas()));
        cmbWarnaMobil.setSelectedItem(mobilTerpilih.getWarna());
        cmbTransmisiMobil.setSelectedItem(mobilTerpilih.getTransmisi());
        cmbStatusMobil.setSelectedItem(mobilTerpilih.getStatus());
        jLabel3.setText("Form Edit Data Mobil");
        btnEditFormMobil.setEnabled(true);
        btnHapusFormMobil.setEnabled(true);
    }//GEN-LAST:event_tblMobilMouseClicked

    private void txtCariMobilKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariMobilKeyReleased
        if (formSiap) {
            if (mobilTerpilih != null) {
                bersihkanForm();
            }
            muatDataMobil();
        }
    }//GEN-LAST:event_txtCariMobilKeyReleased

    private void cmbFilterStatusMobilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFilterStatusMobilActionPerformed
        if (formSiap) {
            if (mobilTerpilih != null) {
                bersihkanForm();
            }
            muatDataMobil();
        }
    }//GEN-LAST:event_cmbFilterStatusMobilActionPerformed

    private void txtHargaSewaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaSewaKeyTyped
        char karakter = evt.getKeyChar();
        if (Character.isISOControl(karakter)) {
            return;
        }

        if (!Character.isDigit(karakter) && karakter != '.') {
            evt.consume();
            return;
        }

        if (karakter == '.' && txtHargaSewa.getText().contains(".")) {
            evt.consume();
        }
    }//GEN-LAST:event_txtHargaSewaKeyTyped
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClearFormMobil;
    private javax.swing.JButton btnEditFormMobil;
    private javax.swing.JButton btnHapusFormMobil;
    private javax.swing.JButton btnTambahFormMobil;
    private javax.swing.JComboBox<String> cmbFilterStatusMobil;
    private javax.swing.JComboBox<String> cmbStatusMobil;
    private javax.swing.JComboBox<String> cmbTransmisiMobil;
    private javax.swing.JComboBox<String> cmbWarnaMobil;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalMobil;
    private javax.swing.JPanel pnlInputHargaSewa;
    private javax.swing.JPanel pnlInputKapasitas;
    private javax.swing.JPanel pnlInputMerek;
    private javax.swing.JPanel pnlInputMobil;
    private javax.swing.JPanel pnlInputPlat;
    private javax.swing.JPanel pnlInputStatus;
    private javax.swing.JPanel pnlInputTahun;
    private javax.swing.JPanel pnlInputTipe;
    private javax.swing.JPanel pnlInputTransmisi;
    private javax.swing.JPanel pnlInputWarna;
    private javax.swing.JPanel pnlJudulForm;
    private javax.swing.JPanel pnlTotalMobil;
    private javax.swing.JTable tblMobil;
    private javax.swing.JTextField txtCariMobil;
    private javax.swing.JTextField txtHargaSewa;
    private javax.swing.JTextField txtKapasitasMobil;
    private javax.swing.JTextField txtMerekMobil;
    private javax.swing.JTextField txtPlatNomor;
    private javax.swing.JTextField txtTahunMobil;
    private javax.swing.JTextField txtTipeMobil;
    // End of variables declaration//GEN-END:variables
}
