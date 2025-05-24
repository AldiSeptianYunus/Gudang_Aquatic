package com.gudangapp.form;

import javax.swing.*;
import java.awt.*;

public class FormBarangMasuk extends JPanel {
    public JTextField txtIdMasuk, txtTanggal, txtJumlah;
    public JComboBox<String> cmbBarang, cmbSupplier;
    public JButton btnSimpan, btnReset;

    private JLabel lblIdMasuk, lblTanggal, lblBarang, lblJumlah, lblSupplier;

    public FormBarangMasuk() {
        initComponents();
        setupLayout();
        setupStyle();
        setupActionListeners();
    }

    private void initComponents() {
        txtIdMasuk = new JTextField();
        txtTanggal = new JTextField();
        txtJumlah = new JTextField();
        cmbBarang = new JComboBox<>();
        cmbSupplier = new JComboBox<>();
        btnSimpan = new JButton("Simpan");
        btnReset = new JButton("Reset");

        lblIdMasuk = new JLabel("ID Masuk:");
        lblTanggal = new JLabel("Tanggal:");
        lblBarang = new JLabel("Barang:");
        lblJumlah = new JLabel("Jumlah:");
        lblSupplier = new JLabel("Supplier:");

        cmbBarang.setSelectedIndex(-1);
        cmbSupplier.setSelectedIndex(-1);
    }

    private void setupStyle() {
        // Font
        Font fontForm = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontButton = new Font("Segoe UI", Font.BOLD, 14);

        lblIdMasuk.setFont(fontForm);
        lblTanggal.setFont(fontForm);
        lblBarang.setFont(fontForm);
        lblJumlah.setFont(fontForm);
        lblSupplier.setFont(fontForm);

        txtIdMasuk.setFont(fontForm);
        txtTanggal.setFont(fontForm);
        txtJumlah.setFont(fontForm);
        cmbBarang.setFont(fontForm);
        cmbSupplier.setFont(fontForm);

        btnSimpan.setFont(fontButton);
        btnReset.setFont(fontButton);

        // Warna tombol
        btnSimpan.setBackground(new Color(0, 153, 0)); // hijau gelap
        btnSimpan.setForeground(Color.WHITE);

        btnReset.setBackground(new Color(204, 0, 0)); // merah
        btnReset.setForeground(Color.WHITE);

        // Border
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private void setupLayout() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(lblIdMasuk)
                    .addComponent(lblTanggal)
                    .addComponent(lblBarang)
                    .addComponent(lblJumlah)
                    .addComponent(lblSupplier)
                    .addComponent(btnSimpan))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(txtIdMasuk)
                    .addComponent(txtTanggal)
                    .addComponent(cmbBarang)
                    .addComponent(txtJumlah)
                    .addComponent(cmbSupplier)
                    .addComponent(btnReset))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdMasuk)
                    .addComponent(txtIdMasuk))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTanggal)
                    .addComponent(txtTanggal))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBarang)
                    .addComponent(cmbBarang))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJumlah)
                    .addComponent(txtJumlah))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSupplier)
                    .addComponent(cmbSupplier))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnReset))
        );
    }

    private void setupActionListeners() {
        // Action listener untuk tombol Simpan
        btnSimpan.addActionListener(e -> {
            // Simulasi simpan data
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        });

        // Action listener untuk tombol Reset
        btnReset.addActionListener(e -> clearForm());
    }

    public void clearForm() {
        txtIdMasuk.setText("");
        txtTanggal.setText("");
        txtJumlah.setText("");
        cmbBarang.setSelectedIndex(-1);
        cmbSupplier.setSelectedIndex(-1);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Form Barang Masuk");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 350);

        frame.getContentPane().add(new FormBarangMasuk());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
