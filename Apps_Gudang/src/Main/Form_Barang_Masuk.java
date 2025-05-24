package Main;
import java.sql.*;
import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import koneksi.Koneksi;
import javax.swing.ButtonGroup;
import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;


public class Form_Barang_Masuk extends javax.swing.JPanel {
    private Connection conn = new Koneksi().connect();
    private ButtonGroup buttonGroup1;
    private DefaultTableModel tabmode;
    private List<String[]> daftarBarang = new ArrayList<>();
    
    public Form_Barang_Masuk() {
        initComponents();
        buttonGroup1 = new ButtonGroup();
        setStaffInfo();
        aktif();
        autonumber();
        setupComboBoxes();
        kosong();
        datatable();

    }
    
    private void setStaffInfo() {
        System.out.println("setStaffInfo() called");
        
        if (UserSession.isLoggedIn()) {
            String idStaff = UserSession.getUserId();
            String namaStaff = UserSession.getNamaUser();
            
            System.out.println("Setting labels - ID: " + idStaff + ", Nama: " + namaStaff);
            
            jIdStaff.setText(idStaff);
            jNamaStaff.setText(namaStaff);
            
            System.out.println("Labels set - jLabel11: " + jIdStaff.getText() + ", jLabel10: " + jNamaStaff.getText());
        } else {
            System.out.println("User not logged in, setting default values");
            jIdStaff.setText("UNKNOWN");
            jNamaStaff.setText("Guest User");
        }
    }
    
    public void refreshUserInfo() {
        System.out.println("refreshUserInfo() called");
        setStaffInfo();
    }
    
    private void setupComboBoxes() {
        setupSupplierCombo();
        setupMobilCombo();
    }
    
    private void setupSupplierCombo() {
        cSupplier.removeAllItems();
        cSupplier.addItem("Pilih Supplier");
        
        try {
            String query = "SELECT id_supplier, nama_supplier FROM supplier ORDER BY nama_supplier";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                String idSupplier = rs.getString("id_supplier");
                String namaSupplier = rs.getString("nama_supplier");
                cSupplier.addItem(idSupplier + " - " + namaSupplier);
            }
            
            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error mengambil data supplier: " + e.getMessage());
        }
    }
    
    private void setupMobilCombo() {
        cMobil.removeAllItems();
        cMobil.addItem("Pilih Mobil");
        
        try {
            String query = "SELECT id_mobil, plat_mobil FROM mobil ORDER BY plat_mobil";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                String idMobil = rs.getString("id_mobil");
                String platNomor = rs.getString("plat_mobil");
                cMobil.addItem(idMobil + " - " + platNomor);
            }
            
            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error mengambil data mobil: " + e.getMessage());
        }
    }
    
    protected void autonumber() {
        try {
            String sql = "SELECT id_masuk FROM barang_masuk ORDER BY id_masuk DESC LIMIT 1";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            String nextId = "BM0001";
            
            if (rs.next()) {
                String lastId = rs.getString("id_masuk");
                if (lastId != null && lastId.length() >= 2) {
                    String numPart = lastId.substring(2); 
                    int nextNum = Integer.parseInt(numPart) + 1;
                    
                    String zeros = "";
                    if (nextNum < 10) {
                        zeros = "000";
                    } else if (nextNum < 100) {
                        zeros = "00";
                    } else if (nextNum < 1000) {
                        zeros = "0";
                    }
                    
                    nextId = "BM" + zeros + nextNum;
                }
            }
            
            jIdTrans.setText(nextId);
            rs.close();
            pst.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error generating auto number: " + e.getMessage());
            txtFaktur.setText("BM0001");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error parsing auto number: " + e.getMessage());
            txtFaktur.setText("BM0001");
        }
    }
    
    protected void aktif() {
        txtFaktur.requestFocus();
        jTggl.setModel(new javax.swing.SpinnerDateModel());
        jTggl.setEditor(new JSpinner.DateEditor(jTggl, "yyyy/MM/dd"));
        jTggl.setValue(new Date());
        
        String[] columnHeaders = {"Id Barang", "Nama Barang", "Jumlah"};
        tabmode = new DefaultTableModel(columnHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        tblTR_BrgMsk.setModel(tabmode);
        
        if (tblTR_BrgMsk.getColumnModel().getColumnCount() >= 3) {
            tblTR_BrgMsk.getColumnModel().getColumn(0).setPreferredWidth(100); 
            tblTR_BrgMsk.getColumnModel().getColumn(1).setPreferredWidth(200); 
            tblTR_BrgMsk.getColumnModel().getColumn(2).setPreferredWidth(80);  
        }
    }

    protected void kosong() {
        if (cSupplier.getItemCount() > 0) {
            cSupplier.setSelectedIndex(0); 
        }
        txtNamaSppl.setText("");
        
        if (cMobil.getItemCount() > 0) {
            cMobil.setSelectedIndex(0); 
        }
        txtNamaDriver.setText("");
        
        jTggl.setValue(new Date());
        
        if (buttonGroup1 != null) {
            buttonGroup1.clearSelection();
        }
        
        if (tabmode != null) {
            tabmode.setRowCount(0);
        }
        daftarBarang.clear();
    }
    
    public void tambahItemBarang(String idBarang, String namaBarang, int jumlah) {
        try {
            for (int i = 0; i < tabmode.getRowCount(); i++) {
                if (tabmode.getValueAt(i, 0).toString().equals(idBarang)) {
                    int currentJumlah = Integer.parseInt(tabmode.getValueAt(i, 2).toString());
                    int newJumlah = currentJumlah + jumlah;
                    tabmode.setValueAt(newJumlah, i, 2);
                    
                    for (String[] barang : daftarBarang) {
                        if (barang[0].equals(idBarang)) {
                            barang[2] = String.valueOf(newJumlah);
                            break;
                        }
                    }
                    return;
                }
            }
            
            tabmode.addRow(new Object[]{idBarang, namaBarang, jumlah});
            
            daftarBarang.add(new String[]{idBarang, namaBarang, String.valueOf(jumlah)});
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error menambah item barang: " + e.getMessage());
        }
    }
    
    public void datatable() {
    try {
        if (tabmode != null) {
            tabmode.setRowCount(0);
            
            for (String[] barang : daftarBarang) {
                tabmode.addRow(new Object[]{barang[0], barang[1], barang[2]});
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error refresh table: " + e.getMessage());
    }
}
        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        navbar = new javax.swing.JPanel();
        Barang = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTR_BrgMsk = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        bTambah = new javax.swing.JButton();
        bUbah = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        bSimpan = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtFaktur = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtNamaSppl = new javax.swing.JTextField();
        txtNamaDriver = new javax.swing.JTextField();
        jTggl = new javax.swing.JSpinner();
        cSupplier = new javax.swing.JComboBox<>();
        cMobil = new javax.swing.JComboBox<>();
        jNamaStaff = new javax.swing.JLabel();
        jIdStaff = new javax.swing.JLabel();
        jIdTrans = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 255, 255));

        navbar.setBackground(new java.awt.Color(0, 0, 153));

        Barang.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        Barang.setForeground(new java.awt.Color(255, 255, 255));
        Barang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Barang.setText("Transaksi Barang Masuk");

        javax.swing.GroupLayout navbarLayout = new javax.swing.GroupLayout(navbar);
        navbar.setLayout(navbarLayout);
        navbarLayout.setHorizontalGroup(
            navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, navbarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Barang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        navbarLayout.setVerticalGroup(
            navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navbarLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(Barang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        tblTR_BrgMsk.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblTR_BrgMsk);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("ID Staff");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Nama Staff");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("ID Transaksi");

        bTambah.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bTambah.setText("Tambah");
        bTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTambahActionPerformed(evt);
            }
        });

        bUbah.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bUbah.setText("Ubah");
        bUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bUbahActionPerformed(evt);
            }
        });

        bHapus.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bHapus.setText("Hapus");
        bHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHapusActionPerformed(evt);
            }
        });

        bSimpan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        bSimpan.setText("Simpan");
        bSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSimpanActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaksi"));

        jLabel4.setText("No Faktur");

        jLabel5.setText("Tanggal Masuk");

        jLabel6.setText("ID Supplier");

        jLabel7.setText("Id Mobil");

        jLabel8.setText("Nama Driver");

        txtFaktur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFakturActionPerformed(evt);
            }
        });

        jLabel9.setText("Nama Supplier");

        jTggl.setModel(new javax.swing.SpinnerDateModel());

        cSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cSupplier.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cSupplierItemStateChanged(evt);
            }
        });

        cMobil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cMobil.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cMobilItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTggl))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNamaSppl, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cMobil, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNamaDriver, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFaktur)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNamaSppl, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTggl)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cMobil, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cSupplier)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNamaDriver, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );

        jNamaStaff.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jIdStaff.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jNamaStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jIdStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 246, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jIdTrans, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(bSimpan))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(235, 235, 235)
                        .addComponent(bTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bUbah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bHapus)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(navbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addComponent(jIdTrans, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addComponent(jIdStaff, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jNamaStaff, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(bSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtFakturActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFakturActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFakturActionPerformed

    private void bTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTambahActionPerformed
        try {
            PopupTambahMasuk popup = new PopupTambahMasuk(this);
            popup.setLocationRelativeTo(this);
            popup.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error membuka popup: " + e.getMessage());
        }
    }//GEN-LAST:event_bTambahActionPerformed

    private void bHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHapusActionPerformed
    int selectedRow = tblTR_BrgMsk.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Pilih barang yang akan dihapus!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Apakah Anda yakin ingin menghapus barang ini?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (selectedRow < daftarBarang.size()) {
                    daftarBarang.remove(selectedRow);
                }
                
                datatable();
                
                JOptionPane.showMessageDialog(null, "Barang berhasil dihapus!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error menghapus data: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_bHapusActionPerformed

    private void bUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUbahActionPerformed
        int selectedRow = tblTR_BrgMsk.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Pilih barang yang akan diubah!");
            return;
        }
        
        try {
            String idBarang = tabmode.getValueAt(selectedRow, 0).toString();
            String namaBarang = tabmode.getValueAt(selectedRow, 1).toString();
            int currentJumlah = Integer.parseInt(tabmode.getValueAt(selectedRow, 2).toString());
            
            String newJumlahStr = JOptionPane.showInputDialog(this, 
                "Nama Barang: " + namaBarang + "\nJumlah Baru:", currentJumlah);
            
            if (newJumlahStr != null && !newJumlahStr.trim().isEmpty()) {
                int newJumlah = Integer.parseInt(newJumlahStr.trim());
                if (newJumlah <= 0) {
                    JOptionPane.showMessageDialog(null, "Jumlah harus lebih dari 0!");
                    return;
                }
                
                tabmode.setValueAt(newJumlah, selectedRow, 2);
                
                for (String[] barang : daftarBarang) {
                    if (barang[0].equals(idBarang)) {
                        barang[2] = String.valueOf(newJumlah);
                        break;
                    }
                }
                
                JOptionPane.showMessageDialog(null, "Jumlah barang berhasil diubah!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Jumlah harus berupa angka!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error mengubah data: " + e.getMessage());
        }
    }//GEN-LAST:event_bUbahActionPerformed

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        if (txtFaktur.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Faktur harus diisi!");
            return;
        }
        
        if (daftarBarang.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Belum ada barang yang ditambahkan!");
            return;
        }
        
        try {
            conn.setAutoCommit(false);
            
            String idMasuk = jIdTrans.getText().trim();
            String noFaktur = txtFaktur.getText().trim();
            Date tanggalMasuk = (Date) jTggl.getValue();
            java.sql.Date sqlDate = new java.sql.Date(tanggalMasuk.getTime());
            String idStaff = jIdStaff.getText(); 
            
            String idSupplier = null;
            String idMobil = null;
            
            if (cSupplier.getSelectedIndex() > 0) {
                String selectedSupplier = cSupplier.getSelectedItem().toString();
                if (!selectedSupplier.equals("Pilih Supplier")) {
                    idSupplier = selectedSupplier.split(" - ")[0];
                }
            }
            
            if (cMobil.getSelectedIndex() > 0) {
                String selectedMobil = cMobil.getSelectedItem().toString();
                if (!selectedMobil.equals("Pilih Mobil")) {
                    idMobil = selectedMobil.split(" - ")[0];
                }
            }
            
            String sqlHeader = "INSERT INTO barang_masuk (id_masuk, no_faktur, tanggal_masuk, id_supplier, id_mobil, id_user) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstHeader = conn.prepareStatement(sqlHeader);
            pstHeader.setString(1, idMasuk);
            pstHeader.setString(2, noFaktur);
            pstHeader.setDate(3, sqlDate);
            pstHeader.setString(4, idSupplier);
            pstHeader.setString(5, idMobil);
            pstHeader.setString(6, idStaff);
            pstHeader.executeUpdate();
            pstHeader.close();
            
            for (String[] barang : daftarBarang) {
                String idBarang = barang[0];
                int jumlah = Integer.parseInt(barang[2]);
                
                String sqlDetail = "INSERT INTO detail_barang_masuk (id_masuk, id_barang, jumlah) VALUES (?, ?, ?)";
                PreparedStatement pstDetail = conn.prepareStatement(sqlDetail);
                pstDetail.setString(1, idMasuk);
                pstDetail.setString(2, idBarang);
                pstDetail.setInt(3, jumlah);
                pstDetail.executeUpdate();
                pstDetail.close();
                
                String sqlCheckStok = "SELECT jumlah FROM stok_barang WHERE id_barang = ?";
                PreparedStatement pstCheck = conn.prepareStatement(sqlCheckStok);
                pstCheck.setString(1, idBarang);
                ResultSet rsCheck = pstCheck.executeQuery();
                
                if (rsCheck.next()) {
                    int currentStok = rsCheck.getInt("jumlah");
                    int newStok = currentStok + jumlah;
                    
                    String sqlUpdateStok = "UPDATE stok_barang SET jumlah = ?, tanggal_update = NOW() WHERE id_barang = ?";
                    PreparedStatement pstUpdate = conn.prepareStatement(sqlUpdateStok);
                    pstUpdate.setInt(1, newStok);
                    pstUpdate.setString(2, idBarang);
                    pstUpdate.executeUpdate();
                    pstUpdate.close();
                } else {
                    String sqlInsertStok = "INSERT INTO stok_barang (id_barang, jumlah) VALUES (?, ?)";
                    PreparedStatement pstInsert = conn.prepareStatement(sqlInsertStok);
                    pstInsert.setString(1, idBarang);
                    pstInsert.setInt(2, jumlah);
                    pstInsert.executeUpdate();
                    pstInsert.close();
                }
                
                rsCheck.close();
                pstCheck.close();
            }
            
            conn.commit(); 
            JOptionPane.showMessageDialog(null, "Transaksi berhasil disimpan!");
            
            kosong();
            autonumber();
            setStaffInfo();
            
        } catch (SQLException e) {
            try {
                conn.rollback(); 
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error rollback: " + ex.getMessage());
            }
            JOptionPane.showMessageDialog(null, "Error menyimpan transaksi: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true); 
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error reset auto commit: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_bSimpanActionPerformed

    private void cSupplierItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cSupplierItemStateChanged
    if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED 
                && cSupplier.getSelectedItem() != null) {
            
            String selectedItem = cSupplier.getSelectedItem().toString();
            
            if (selectedItem.equals("Pilih Supplier")) {
                txtNamaSppl.setText("");
                return;
            }
            
            try {
                String idSupplier = selectedItem.split(" - ")[0];
                
                String sql = "SELECT nama_supplier FROM supplier WHERE id_supplier = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, idSupplier);
                ResultSet rs = pst.executeQuery();
                
                if (rs.next()) {
                    txtNamaSppl.setText(rs.getString("nama_supplier"));
                }
                
                rs.close();
                pst.close();
            } catch (Exception e) {
                txtNamaSppl.setText("");
                JOptionPane.showMessageDialog(null, "Error mengambil nama supplier: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_cSupplierItemStateChanged

    private void cMobilItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cMobilItemStateChanged
    if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED 
                && cMobil.getSelectedItem() != null) {
            
            String selectedItem = cMobil.getSelectedItem().toString();
            
            if (selectedItem.equals("Pilih Mobil")) {
                txtNamaDriver.setText("");
                return;
            }
            
            try {
                String idMobil = selectedItem.split(" - ")[0];
                
                String sql = "SELECT nama_driver FROM mobil WHERE id_mobil = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, idMobil);
                ResultSet rs = pst.executeQuery();
                
                if (rs.next()) {
                    txtNamaDriver.setText(rs.getString("nama_driver"));
                }
                
                rs.close();
                pst.close();
            } catch (Exception e) {
                txtNamaDriver.setText("");
                JOptionPane.showMessageDialog(null, "Error mengambil nama driver: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_cMobilItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Barang;
    private javax.swing.JButton bHapus;
    private javax.swing.JButton bSimpan;
    private javax.swing.JButton bTambah;
    private javax.swing.JButton bUbah;
    private javax.swing.JComboBox<String> cMobil;
    private javax.swing.JComboBox<String> cSupplier;
    private javax.swing.JLabel jIdStaff;
    private javax.swing.JLabel jIdTrans;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jNamaStaff;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jTggl;
    private javax.swing.JPanel navbar;
    private javax.swing.JTable tblTR_BrgMsk;
    private javax.swing.JTextField txtFaktur;
    private javax.swing.JTextField txtNamaDriver;
    private javax.swing.JTextField txtNamaSppl;
    // End of variables declaration//GEN-END:variables

}

