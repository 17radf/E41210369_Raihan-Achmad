/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import javax.swing.JOptionPane;
import Controller.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Raihan
 */

public class Dashboard extends javax.swing.JFrame {
	Connection conn;
	ResultSet rs = null;
	PreparedStatement pst = null;
	String id;
	String idBuah;
	String idPelanggan;
	int hargaBuah;
	int totalHarga;

    /**
     * Creates new form Dashboard
	 * @throws java.sql.SQLException
	 * @throws java.lang.ClassNotFoundException
     */
    public Dashboard() throws SQLException, ClassNotFoundException {
	this.conn = Koneksi.getKoneksi();
        initComponents();
    }
    public Dashboard(String id) throws SQLException, ClassNotFoundException {
	this.conn = Koneksi.getKoneksi();
	this.id = id;
        initComponents();
	initThings();
	tabelKaryawan();
	tabelPelanggan();
	tabelPenjualan();
	tabelBuah();
	listId();
	listBuah();
	listPelanggan();
	whoPelanggan();
	howCost();
    }

    public void initThings(){
	    try{
		    // transaksi hari ini
	    String sql = "select count(*) from tb_transaksi where tanggal = curdate()";
	    pst = conn.prepareStatement(sql);
	    rs = pst.executeQuery();
	    while(rs.next()){
		    jLabel8.setText(rs.getString(1));
	    }
		    // total transaksi
	    sql = "select count(*) from tb_transaksi";
	    pst = conn.prepareStatement(sql);
	    rs = pst.executeQuery();
	    while(rs.next()){
		    jLabel18.setText(rs.getString(1));
	    }
		    // stok buah
	    sql = "select sum(stok) from tb_buah";
	    pst = conn.prepareStatement(sql);
	    rs = pst.executeQuery();
	    while(rs.next()){
		    jLabel34.setText(rs.getString(1));
	    }
	sql = "select nama from tb_user where id = " + id;
	System.out.println(sql);
	pst=conn.prepareStatement(sql);
	rs=pst.executeQuery();
	if(rs.next()){
	jLabel24.setText("<html><center>Hi, " + rs.getString(1) + "</center></html>");
	}else{
	System.out.println("ur mom gay");
	}
	    }catch(SQLException ex){
		    System.out.println("gagal gan");
	    }
    }

    public void tabelPenjualan(){
	    DefaultTableModel model = new DefaultTableModel();
	    model.addColumn("ID");
	    model.addColumn("No Faktur");
	    model.addColumn("Buah");
	    model.addColumn("QTY");
	    model.addColumn("Pelanggan");
	    model.addColumn("Kasir");
	    model.addColumn("Tanggal");
	    try{
		    String sql = "select dt.id, t.no_faktur, b.nama, dt.qty, p.nama, u.nama, t.tanggal from tb_detail_transaksi as dt join tb_transaksi as t on dt.id_transaksi = t.no_faktur join tb_buah as b on dt.id_buah = b.id join tb_pelanggan as p on t.id_pelanggan = p.id join tb_user as u on dt.id_user = u.id order by t.tanggal desc";
		    pst=conn.prepareStatement(sql);
		    rs=pst.executeQuery();
		    while(rs.next()){
			    model.addRow(new Object[] {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)});
		    }
		    jTable3.setModel(model);
		    jTable3.setEnabled(false);
	    }catch(SQLException e){
	    }
    }

    public void tabelKaryawan(){
	    DefaultTableModel model = new DefaultTableModel();
	    model.addColumn("No");
	    model.addColumn("Nama");
	    model.addColumn("Bagian");
	    try{
		    String sql = "select k.id, k.nama, b.nama from tb_karyawan as k join tb_bagianrole as b on k.id = b.id";
		    pst=conn.prepareStatement(sql);
		    rs=pst.executeQuery();
		    while(rs.next()){
			    model.addRow(new Object[] {rs.getString(1), rs.getString(2), rs.getString(3)});
		    }
		    jTable1.setModel(model);
	    }catch(SQLException e){
	    }
    }

    public void tabelPelanggan(){
	    DefaultTableModel model = new DefaultTableModel();
	    model.addColumn("No");
	    model.addColumn("Nama");
	    model.addColumn("Alamat");
	    model.addColumn("No HP");
	    try{
		    String sql = "select * from tb_pelanggan";
		    pst=conn.prepareStatement(sql);
		    rs=pst.executeQuery();
		    while(rs.next()){
			    model.addRow(new Object[] {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
		    }
		    jTable2.setModel(model);
	    }catch(SQLException e){
	    }
    }

    public void tabelBuah(){
	    DefaultTableModel model = new DefaultTableModel();
	    model.addColumn("No");
	    model.addColumn("Nama");
	    model.addColumn("Stok");
	    model.addColumn("Harga");
	    try{
		    String sql = "select * from tb_buah";
		    pst=conn.prepareStatement(sql);
		    rs=pst.executeQuery();
		    while(rs.next()){
			    model.addRow(new Object[] {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
		    }
		    jTable4.setModel(model);
	    }catch(SQLException e){
	    }
    }

    public void listId(){
	jComboBox1.removeAllItems();
	    try{
		    String sql = "select no_faktur from tb_transaksi";
		    pst=conn.prepareStatement(sql);
		    rs=pst.executeQuery();
		    while(rs.next()){
			jComboBox1.addItem(rs.getString(1));
		    }
	    }catch(SQLException e){
	    }
    }

    public void listBuah(){
	jComboBox2.removeAllItems();
	jComboBox3.removeAllItems();
	    try{
		    String sql = "select nama from tb_buah";
		    pst=conn.prepareStatement(sql);
		    rs=pst.executeQuery();
		    while(rs.next()){
			jComboBox2.addItem(rs.getString(1));
			jComboBox3.addItem(rs.getString(1));
		    }
	    }catch(SQLException e){
	    }
    }

    public void listPelanggan(){
	jComboBox4.removeAllItems();
	    try{
		    String sql = "select nama from tb_pelanggan";
		    pst=conn.prepareStatement(sql);
		    rs=pst.executeQuery();
		    while(rs.next()){
			jComboBox4.addItem(rs.getString(1));
		    }
	    }catch(SQLException e){
	    }
    }

    public void updateHarga() throws SQLException{
	System.out.println(hargaBuah);
	try{
		int um = Integer.parseInt(jTextField6.getText());
		if( um > 0){
			totalHarga = um * hargaBuah;
			jTextField8.setText(Integer.toString(totalHarga));
		}else{
			System.out.println("salah value");
		}
	}catch(NumberFormatException ex){
		jTextField8.setText("0");
	}
    }
    public void whoPelanggan() throws SQLException {
		String sql = "select p.nama from tb_pelanggan as p join tb_transaksi as t on p.id = t.id_pelanggan WHERE t.no_faktur = '" + jComboBox1.getSelectedItem() +"'";
		try{
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while(rs.next()){
				jTextField9.setText(rs.getString(1));
				jTextField9.setEditable(false);
			}
		}catch(SQLException ex){
		}
    }

	public void howCost(){
	String sql = "select harga from tb_buah where nama = '" + jComboBox2.getSelectedItem() + "'";
	try{
		pst=conn.prepareStatement(sql);
		rs=pst.executeQuery();
		while(rs.next()){
			hargaBuah = rs.getInt("harga");
			updateHarga();
		}
	}catch(SQLException ex){
		System.out.println("netnot");
	}
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jDialog1 = new javax.swing.JDialog();
                jPanel1 = new javax.swing.JPanel();
                jPanel2 = new javax.swing.JPanel();
                jLabel6 = new javax.swing.JLabel();
                jPanel11 = new javax.swing.JPanel();
                jLabel12 = new javax.swing.JLabel();
                jPanel9 = new javax.swing.JPanel();
                jLabel21 = new javax.swing.JLabel();
                jPanel6 = new javax.swing.JPanel();
                home = new javax.swing.JPanel();
                jPanel4 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                jPanel3 = new javax.swing.JPanel();
                jLabel2 = new javax.swing.JLabel();
                jPanel5 = new javax.swing.JPanel();
                jLabel3 = new javax.swing.JLabel();
                jPanel7 = new javax.swing.JPanel();
                jLabel4 = new javax.swing.JLabel();
                jPanel8 = new javax.swing.JPanel();
                jLabel5 = new javax.swing.JLabel();
                jPanel12 = new javax.swing.JPanel();
                jLabel8 = new javax.swing.JLabel();
                jLabel10 = new javax.swing.JLabel();
                jLabel23 = new javax.swing.JLabel();
                jPanel13 = new javax.swing.JPanel();
                jLabel18 = new javax.swing.JLabel();
                jLabel26 = new javax.swing.JLabel();
                jLabel25 = new javax.swing.JLabel();
                jPanel14 = new javax.swing.JPanel();
                jLabel34 = new javax.swing.JLabel();
                jLabel35 = new javax.swing.JLabel();
                jLabel28 = new javax.swing.JLabel();
                jLabel13 = new javax.swing.JLabel();
                transaksi = new javax.swing.JPanel();
                jLabel14 = new javax.swing.JLabel();
                jLabel16 = new javax.swing.JLabel();
                jLabel17 = new javax.swing.JLabel();
                jLabel19 = new javax.swing.JLabel();
                jLabel20 = new javax.swing.JLabel();
                jTextField6 = new javax.swing.JTextField();
                jTextField8 = new javax.swing.JTextField();
                jLabel22 = new javax.swing.JLabel();
                jComboBox1 = new javax.swing.JComboBox<>();
                jComboBox2 = new javax.swing.JComboBox<>();
                button1 = new java.awt.Button();
                button2 = new java.awt.Button();
                jTextField9 = new javax.swing.JTextField();
                newTransaksi = new javax.swing.JPanel();
                jLabel7 = new javax.swing.JLabel();
                jLabel9 = new javax.swing.JLabel();
                jPanel10 = new javax.swing.JPanel();
                jLabel11 = new javax.swing.JLabel();
                jComboBox4 = new javax.swing.JComboBox<>();
                cekStokBuah = new javax.swing.JPanel();
                jLabel15 = new javax.swing.JLabel();
                jScrollPane4 = new javax.swing.JScrollPane();
                jTable4 = new javax.swing.JTable();
                jComboBox3 = new javax.swing.JComboBox<>();
                jButton1 = new javax.swing.JButton();
                datapenjualan = new javax.swing.JPanel();
                jLabel32 = new javax.swing.JLabel();
                jScrollPane3 = new javax.swing.JScrollPane();
                jTable3 = new javax.swing.JTable();
                datapelanggan = new javax.swing.JPanel();
                jLabel27 = new javax.swing.JLabel();
                jScrollPane2 = new javax.swing.JScrollPane();
                jTable2 = new javax.swing.JTable();
                datakaryawan = new javax.swing.JPanel();
                jLabel33 = new javax.swing.JLabel();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTable1 = new javax.swing.JTable();
                about = new javax.swing.JPanel();
                jLabel36 = new javax.swing.JLabel();
                jLabel30 = new javax.swing.JLabel();
                jLabel24 = new javax.swing.JLabel();
                jLabel29 = new javax.swing.JLabel();

                javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
                jDialog1.getContentPane().setLayout(jDialog1Layout);
                jDialog1Layout.setHorizontalGroup(
                        jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
                );
                jDialog1Layout.setVerticalGroup(
                        jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
                );

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                jPanel1.setBackground(new java.awt.Color(30, 81, 40));
                jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jPanel2.setBackground(new java.awt.Color(78, 159, 61));

                jLabel6.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel6.setForeground(new java.awt.Color(255, 255, 255));
                jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-logout-20.png"))); // NOI18N
                jLabel6.setText(" Logout");
                jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel6MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(jLabel6)
                                .addContainerGap(71, Short.MAX_VALUE))
                );
                jPanel2Layout.setVerticalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                );

                jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 190, 50));

                jPanel11.setBackground(new java.awt.Color(78, 159, 61));

                jLabel12.setBackground(new java.awt.Color(54, 33, 89));
                jLabel12.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel12.setForeground(new java.awt.Color(255, 255, 255));
                jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-graph-20.png"))); // NOI18N
                jLabel12.setText(" Dashboard");
                jLabel12.setToolTipText("");
                jLabel12.setMaximumSize(new java.awt.Dimension(120, 20));
                jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel12MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
                jPanel11.setLayout(jPanel11Layout);
                jPanel11Layout.setHorizontalGroup(
                        jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(47, Short.MAX_VALUE))
                );
                jPanel11Layout.setVerticalGroup(
                        jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                );

                jPanel1.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 190, 50));

                jPanel9.setBackground(new java.awt.Color(78, 159, 61));

                jLabel21.setBackground(new java.awt.Color(255, 255, 255));
                jLabel21.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel21.setForeground(new java.awt.Color(255, 255, 255));
                jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-about-20.png"))); // NOI18N
                jLabel21.setText(" About");
                jLabel21.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel21MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
                jPanel9.setLayout(jPanel9Layout);
                jPanel9Layout.setHorizontalGroup(
                        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(38, Short.MAX_VALUE))
                );
                jPanel9Layout.setVerticalGroup(
                        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                );

                jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 190, 50));

                jPanel6.setBackground(new java.awt.Color(186, 79, 84));
                jPanel6.setLayout(new java.awt.CardLayout());

                home.setBackground(new java.awt.Color(216, 233, 168));
                home.setForeground(new java.awt.Color(25, 26, 25));
                home.addContainerListener(new java.awt.event.ContainerAdapter() {
                        public void componentAdded(java.awt.event.ContainerEvent evt) {
                                homeComponentAdded(evt);
                        }
                });
                home.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jPanel4.setBackground(new java.awt.Color(220, 237, 171));
                jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(78, 159, 61), 2, true));
                jPanel4.setToolTipText("");
                jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jPanel4MouseClicked(evt);
                        }
                });

                jLabel1.setBackground(new java.awt.Color(25, 26, 25));
                jLabel1.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel1.setForeground(new java.awt.Color(25, 26, 25));
                jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-apple-30.png"))); // NOI18N
                jLabel1.setText("<html>Check Stock Buah</html>");
                jLabel1.setMaximumSize(new java.awt.Dimension(50, 50));
                jLabel1.setMinimumSize(new java.awt.Dimension(50, 50));
                jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel1MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(29, Short.MAX_VALUE))
                );
                jPanel4Layout.setVerticalGroup(
                        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                );

                home.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 150, 70));

                jPanel3.setBackground(new java.awt.Color(220, 237, 171));
                jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(78, 159, 61), 2, true));

                jLabel2.setBackground(new java.awt.Color(25, 26, 25));
                jLabel2.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel2.setForeground(new java.awt.Color(25, 26, 25));
                jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-lightning-bolt-30.png"))); // NOI18N
                jLabel2.setText("<html>Quick Transaksi</html>");
                jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel2MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap(20, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14))
                );
                jPanel3Layout.setVerticalGroup(
                        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                );

                home.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 260, 150, 70));

                jPanel5.setBackground(new java.awt.Color(220, 237, 171));
                jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(78, 159, 61), 2, true));

                jLabel3.setBackground(new java.awt.Color(25, 26, 25));
                jLabel3.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel3.setForeground(new java.awt.Color(25, 26, 25));
                jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-detail-30.png"))); // NOI18N
                jLabel3.setText("<html>Detail Transaksi</html>");
                jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel3MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
                jPanel5.setLayout(jPanel5Layout);
                jPanel5Layout.setHorizontalGroup(
                        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39))
                );
                jPanel5Layout.setVerticalGroup(
                        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                );

                home.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 260, 150, 70));

                jPanel7.setBackground(new java.awt.Color(220, 237, 171));
                jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(78, 159, 61), 2, true));

                jLabel4.setBackground(new java.awt.Color(25, 26, 25));
                jLabel4.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel4.setForeground(new java.awt.Color(25, 26, 25));
                jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-person-30.png"))); // NOI18N
                jLabel4.setText("<html>Data Pelanggan</html>");
                jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel4MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
                jPanel7.setLayout(jPanel7Layout);
                jPanel7Layout.setHorizontalGroup(
                        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(14, Short.MAX_VALUE))
                );
                jPanel7Layout.setVerticalGroup(
                        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                );

                home.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 150, 70));

                jPanel8.setBackground(new java.awt.Color(220, 237, 171));
                jPanel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(78, 159, 61), 2, true));

                jLabel5.setBackground(new java.awt.Color(25, 26, 25));
                jLabel5.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel5.setForeground(new java.awt.Color(25, 26, 25));
                jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-worker-30.png"))); // NOI18N
                jLabel5.setText("<html>Data Karyawan</html>");
                jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel5MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
                jPanel8.setLayout(jPanel8Layout);
                jPanel8Layout.setHorizontalGroup(
                        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                                .addGap(17, 17, 17))
                );
                jPanel8Layout.setVerticalGroup(
                        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                );

                home.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 350, 150, 70));

                jPanel12.setBackground(new java.awt.Color(78, 159, 61));

                jLabel8.setFont(new java.awt.Font("Cascadia Mono", 0, 24)); // NOI18N
                jLabel8.setForeground(new java.awt.Color(255, 255, 255));
                jLabel8.setText("bruh");

                jLabel10.setFont(new java.awt.Font("Cascadia Mono", 0, 14)); // NOI18N
                jLabel10.setForeground(new java.awt.Color(255, 255, 255));
                jLabel10.setText("Transaksi Hari Ini");

                jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-transaction-48.png"))); // NOI18N

                javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
                jPanel12.setLayout(jPanel12Layout);
                jPanel12Layout.setHorizontalGroup(
                        jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                                                .addComponent(jLabel23)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(29, Short.MAX_VALUE))
                );
                jPanel12Layout.setVerticalGroup(
                        jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                                .addContainerGap(22, Short.MAX_VALUE)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel10)
                                .addGap(20, 20, 20))
                );

                home.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 210, 120));

                jPanel13.setBackground(new java.awt.Color(78, 159, 61));

                jLabel18.setFont(new java.awt.Font("Cascadia Mono", 0, 24)); // NOI18N
                jLabel18.setForeground(new java.awt.Color(255, 255, 255));
                jLabel18.setText("bruh");

                jLabel26.setFont(new java.awt.Font("Cascadia Mono", 0, 14)); // NOI18N
                jLabel26.setForeground(new java.awt.Color(255, 255, 255));
                jLabel26.setText("Total Transaksi");

                jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-bill-48.png"))); // NOI18N

                javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
                jPanel13.setLayout(jPanel13Layout);
                jPanel13Layout.setHorizontalGroup(
                        jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel13Layout.createSequentialGroup()
                                                .addComponent(jLabel25)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(29, Short.MAX_VALUE))
                );
                jPanel13Layout.setVerticalGroup(
                        jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel13Layout.createSequentialGroup()
                                .addContainerGap(22, Short.MAX_VALUE)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel26)
                                .addGap(20, 20, 20))
                );

                home.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 110, 210, 120));

                jPanel14.setBackground(new java.awt.Color(78, 159, 61));

                jLabel34.setFont(new java.awt.Font("Cascadia Mono", 0, 24)); // NOI18N
                jLabel34.setForeground(new java.awt.Color(255, 255, 255));
                jLabel34.setText("bruh");

                jLabel35.setFont(new java.awt.Font("Cascadia Mono", 0, 14)); // NOI18N
                jLabel35.setForeground(new java.awt.Color(255, 255, 255));
                jLabel35.setText("Stok Buah");

                jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-fruits-48.png"))); // NOI18N

                javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
                jPanel14.setLayout(jPanel14Layout);
                jPanel14Layout.setHorizontalGroup(
                        jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                                                .addComponent(jLabel28)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(29, Short.MAX_VALUE))
                );
                jPanel14Layout.setVerticalGroup(
                        jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel14Layout.createSequentialGroup()
                                .addContainerGap(22, Short.MAX_VALUE)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel35)
                                .addGap(20, 20, 20))
                );

                home.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 110, 210, 120));

                jLabel13.setBackground(new java.awt.Color(25, 26, 25));
                jLabel13.setFont(new java.awt.Font("Cascadia Mono", 1, 24)); // NOI18N
                jLabel13.setForeground(new java.awt.Color(25, 26, 25));
                jLabel13.setText("Toko Buah-Buahan");
                home.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 260, 60));

                jPanel6.add(home, "card4");

                transaksi.setBackground(new java.awt.Color(216, 233, 168));
                transaksi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                jLabel14.setForeground(new java.awt.Color(25, 26, 25));
                jLabel14.setText("TRANSAKSI");
                transaksi.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 30, 90, -1));

                jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
                jLabel16.setForeground(new java.awt.Color(25, 26, 25));
                jLabel16.setText("Buah");
                transaksi.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 130, 100, 30));

                jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
                jLabel17.setForeground(new java.awt.Color(25, 26, 25));
                jLabel17.setText("Jumlah");
                transaksi.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, 90, 30));

                jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
                jLabel19.setForeground(new java.awt.Color(25, 26, 25));
                jLabel19.setText("Harga");
                transaksi.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, 90, 30));

                jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
                jLabel20.setForeground(new java.awt.Color(25, 26, 25));
                jLabel20.setText("Pelanggan");
                transaksi.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 250, 100, 30));

                jTextField6.setBackground(new java.awt.Color(255, 255, 255));
                jTextField6.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jTextField6ActionPerformed(evt);
                        }
                });
                jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyTyped(java.awt.event.KeyEvent evt) {
                                jTextField6KeyTyped(evt);
                        }
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                jTextField6KeyReleased(evt);
                        }
                });
                transaksi.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 170, 140, 30));

                jTextField8.setEditable(false);
                jTextField8.setBackground(new java.awt.Color(255, 255, 255));
                jTextField8.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jTextField8ActionPerformed(evt);
                        }
                });
                transaksi.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 210, 140, 30));

                jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
                jLabel22.setForeground(new java.awt.Color(25, 26, 25));
                jLabel22.setText("No Faktur");
                transaksi.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 90, 100, 30));

                jComboBox1.setBackground(new java.awt.Color(216, 233, 168));
                jComboBox1.setForeground(new java.awt.Color(25, 26, 25));
                jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
                jComboBox1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
                        public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                                jComboBox1PopupMenuWillBecomeVisible(evt);
                        }
                        public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                                jComboBox1PopupMenuWillBecomeInvisible(evt);
                        }
                        public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                        }
                });
                jComboBox1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jComboBox1ActionPerformed(evt);
                        }
                });
                transaksi.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 90, 140, 30));

                jComboBox2.setBackground(new java.awt.Color(216, 233, 168));
                jComboBox2.setForeground(new java.awt.Color(25, 26, 25));
                jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
                jComboBox2.addItemListener(new java.awt.event.ItemListener() {
                        public void itemStateChanged(java.awt.event.ItemEvent evt) {
                                jComboBox2ItemStateChanged(evt);
                        }
                });
                jComboBox2.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
                        public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                        }
                        public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                                jComboBox2PopupMenuWillBecomeInvisible(evt);
                        }
                        public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                        }
                });
                jComboBox2.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jComboBox2ActionPerformed(evt);
                        }
                });
                jComboBox2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                        public void propertyChange(java.beans.PropertyChangeEvent evt) {
                                jComboBox2PropertyChange(evt);
                        }
                });
                transaksi.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 130, 140, 30));

                button1.setActionCommand("Transaksi Baru");
                button1.setBackground(new java.awt.Color(216, 233, 168));
                button1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                button1.setForeground(new java.awt.Color(25, 26, 25));
                button1.setLabel("Kirim Transaksi");
                button1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                button1ActionPerformed(evt);
                        }
                });
                transaksi.add(button1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 290, 250, 40));
                button1.getAccessibleContext().setAccessibleName("Transaksi Baru");

                button2.setActionCommand("Buat Transaksi Baru");
                button2.setBackground(new java.awt.Color(216, 233, 168));
                button2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                button2.setForeground(new java.awt.Color(25, 26, 25));
                button2.setLabel("Buat Transaksi Baru");
                button2.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                button2MouseClicked(evt);
                        }
                });
                button2.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                button2ActionPerformed(evt);
                        }
                });
                transaksi.add(button2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 340, 250, 40));

                jTextField9.setEditable(false);
                jTextField9.setBackground(new java.awt.Color(255, 255, 255));
                jTextField9.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jTextField9ActionPerformed(evt);
                        }
                });
                transaksi.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 250, 140, 30));

                jPanel6.add(transaksi, "card3");

                newTransaksi.setBackground(new java.awt.Color(216, 233, 168));
                newTransaksi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                jLabel7.setForeground(new java.awt.Color(255, 255, 255));
                jLabel7.setText("Transaksi Baru");
                newTransaksi.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 120, -1));

                jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
                jLabel9.setForeground(new java.awt.Color(255, 255, 255));
                jLabel9.setText("Nama Pelanggan :");
                newTransaksi.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, -1, -1));

                jPanel10.setBackground(new java.awt.Color(54, 33, 89));
                jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

                jLabel11.setBackground(new java.awt.Color(54, 33, 89));
                jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
                jLabel11.setForeground(new java.awt.Color(255, 255, 255));
                jLabel11.setText("Buat");
                jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel11MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
                jPanel10.setLayout(jPanel10Layout);
                jPanel10Layout.setHorizontalGroup(
                        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabel11)
                                .addContainerGap(26, Short.MAX_VALUE))
                );
                jPanel10Layout.setVerticalGroup(
                        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                );

                newTransaksi.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 90, 30));

                jComboBox4.setBackground(new java.awt.Color(54, 33, 89));
                jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
                jComboBox4.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jComboBox4ActionPerformed(evt);
                        }
                });
                newTransaksi.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 90, 140, 40));

                jPanel6.add(newTransaksi, "card2");

                cekStokBuah.setBackground(new java.awt.Color(216, 233, 168));
                cekStokBuah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                jLabel15.setForeground(new java.awt.Color(25, 26, 25));
                jLabel15.setText("Stok Buah");
                cekStokBuah.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 80, -1));

                jTable4.setBackground(new java.awt.Color(216, 233, 168));
                jTable4.setForeground(new java.awt.Color(25, 26, 25));
                jTable4.setModel(new javax.swing.table.DefaultTableModel(
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
                jScrollPane4.setViewportView(jTable4);

                cekStokBuah.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 640, 310));

                jComboBox3.setBackground(new java.awt.Color(216, 233, 168));
                jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
                cekStokBuah.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 520, -1));

                jButton1.setBackground(new java.awt.Color(216, 233, 168));
                jButton1.setForeground(new java.awt.Color(25, 26, 25));
                jButton1.setText("Cari");
                jButton1.setToolTipText("");
                jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jButton1MouseClicked(evt);
                        }
                });
                cekStokBuah.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 60, 100, -1));

                jPanel6.add(cekStokBuah, "card2");

                datapenjualan.setBackground(new java.awt.Color(216, 233, 168));
                datapenjualan.setForeground(new java.awt.Color(255, 255, 255));

                jLabel32.setBackground(new java.awt.Color(255, 255, 255));
                jLabel32.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
                jLabel32.setForeground(new java.awt.Color(25, 26, 25));
                jLabel32.setText("Data Penjualan");

                jTable3.setBackground(new java.awt.Color(216, 233, 168));
                jTable3.setForeground(new java.awt.Color(25, 26, 25));
                jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
                jScrollPane3.setViewportView(jTable3);

                javax.swing.GroupLayout datapenjualanLayout = new javax.swing.GroupLayout(datapenjualan);
                datapenjualan.setLayout(datapenjualanLayout);
                datapenjualanLayout.setHorizontalGroup(
                        datapenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(datapenjualanLayout.createSequentialGroup()
                                .addGap(308, 308, 308)
                                .addComponent(jLabel32)
                                .addContainerGap(330, Short.MAX_VALUE))
                        .addGroup(datapenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(datapenjualanLayout.createSequentialGroup()
                                        .addGap(38, 38, 38)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(58, Short.MAX_VALUE)))
                );
                datapenjualanLayout.setVerticalGroup(
                        datapenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(datapenjualanLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jLabel32)
                                .addContainerGap(457, Short.MAX_VALUE))
                        .addGroup(datapenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(datapenjualanLayout.createSequentialGroup()
                                        .addGap(74, 74, 74)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(63, Short.MAX_VALUE)))
                );

                jPanel6.add(datapenjualan, "card5");

                datapelanggan.setBackground(new java.awt.Color(216, 233, 168));
                datapelanggan.setForeground(new java.awt.Color(255, 255, 255));

                jLabel27.setBackground(new java.awt.Color(255, 255, 255));
                jLabel27.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
                jLabel27.setForeground(new java.awt.Color(25, 26, 25));
                jLabel27.setText("Data Pelanggan");

                jTable2.setBackground(new java.awt.Color(216, 233, 168));
                jTable2.setForeground(new java.awt.Color(25, 26, 25));
                jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
                jScrollPane2.setViewportView(jTable2);

                javax.swing.GroupLayout datapelangganLayout = new javax.swing.GroupLayout(datapelanggan);
                datapelanggan.setLayout(datapelangganLayout);
                datapelangganLayout.setHorizontalGroup(
                        datapelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(datapelangganLayout.createSequentialGroup()
                                .addGap(301, 301, 301)
                                .addComponent(jLabel27)
                                .addContainerGap(330, Short.MAX_VALUE))
                        .addGroup(datapelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(datapelangganLayout.createSequentialGroup()
                                        .addGap(38, 38, 38)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 653, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(59, Short.MAX_VALUE)))
                );
                datapelangganLayout.setVerticalGroup(
                        datapelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(datapelangganLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jLabel27)
                                .addContainerGap(458, Short.MAX_VALUE))
                        .addGroup(datapelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(datapelangganLayout.createSequentialGroup()
                                        .addGap(76, 76, 76)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(82, Short.MAX_VALUE)))
                );

                jPanel6.add(datapelanggan, "card5");

                datakaryawan.setBackground(new java.awt.Color(216, 233, 168));
                datakaryawan.setForeground(new java.awt.Color(255, 255, 255));

                jLabel33.setBackground(new java.awt.Color(255, 255, 255));
                jLabel33.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
                jLabel33.setForeground(new java.awt.Color(25, 26, 25));
                jLabel33.setText("Data Karyawan");

                jTable1.setBackground(new java.awt.Color(216, 233, 168));
                jTable1.setForeground(new java.awt.Color(25, 26, 25));
                jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
                jScrollPane1.setViewportView(jTable1);

                javax.swing.GroupLayout datakaryawanLayout = new javax.swing.GroupLayout(datakaryawan);
                datakaryawan.setLayout(datakaryawanLayout);
                datakaryawanLayout.setHorizontalGroup(
                        datakaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(datakaryawanLayout.createSequentialGroup()
                                .addGroup(datakaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(datakaryawanLayout.createSequentialGroup()
                                                .addGap(42, 42, 42)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(datakaryawanLayout.createSequentialGroup()
                                                .addGap(309, 309, 309)
                                                .addComponent(jLabel33)))
                                .addContainerGap(57, Short.MAX_VALUE))
                );
                datakaryawanLayout.setVerticalGroup(
                        datakaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(datakaryawanLayout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jLabel33)
                                .addGap(32, 32, 32)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(65, Short.MAX_VALUE))
                );

                jPanel6.add(datakaryawan, "card5");

                about.setBackground(new java.awt.Color(216, 233, 168));
                about.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jLabel36.setBackground(new java.awt.Color(255, 255, 255));
                jLabel36.setFont(new java.awt.Font("Cascadia Mono", 0, 24)); // NOI18N
                jLabel36.setForeground(new java.awt.Color(25, 26, 25));
                jLabel36.setText("About Us");
                about.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 120, 120, 40));

                jLabel30.setFont(new java.awt.Font("Cascadia Mono", 0, 18)); // NOI18N
                jLabel30.setForeground(new java.awt.Color(25, 26, 25));
                jLabel30.setText("<html>Kami kelompok 1 dari golongan A, yaudah gitu aja mau ngomong apa lagi? aku gatau mau nulis apa jadi ya gitu deh jadinya oh iya kalo mau kasih word wrap di label tinggal ngasih tag html aja trus teksnya dimasukin ke dalem tag itu. ok tq</html>");
                about.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 650, 150));

                jPanel6.add(about, "card7");

                jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 750, 500));

                jLabel24.setBackground(new java.awt.Color(25, 26, 25));
                jLabel24.setFont(new java.awt.Font("Cascadia Mono", 1, 18)); // NOI18N
                jLabel24.setForeground(new java.awt.Color(255, 255, 255));
                jLabel24.setText("<html><center>Welcome</center></html>");
                jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 150, 60));

                jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-user-96.png"))); // NOI18N
                jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 100, 120));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
       int dialogbtn = JOptionPane.YES_NO_OPTION;
       int dialogresult = JOptionPane.showConfirmDialog(this, "Yakin kh?", "Warning", dialogbtn);
       
       if (dialogresult == 0){
           this.setVisible(false);
	       try {
		       new Login_Form().setVisible(true);
	       } catch (SQLException | ClassNotFoundException ex) {
		       Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
	       }
       }
       else {
           
       }
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
      jPanel6.removeAll();
      jPanel6.repaint();
      jPanel6.revalidate();
      
      jPanel6.add(cekStokBuah);
      jPanel6.repaint();
      jPanel6.revalidate();
        
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
      jPanel6.removeAll();
      jPanel6.repaint();
      jPanel6.revalidate();
      
      jPanel6.add(home);
      jPanel6.repaint();
      jPanel6.revalidate();
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
	jPanel6.removeAll();
	jPanel6.repaint();
	jPanel6.revalidate();

	jPanel6.add(transaksi);
	jPanel6.repaint();
	jPanel6.revalidate();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseClicked
	jPanel6.removeAll();
	jPanel6.repaint();
	jPanel6.revalidate();

	jPanel6.add(about);
	jPanel6.repaint();
	jPanel6.revalidate();
    }//GEN-LAST:event_jLabel21MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
	jPanel6.removeAll();
	jPanel6.repaint();
	jPanel6.revalidate();

	jPanel6.add(datapenjualan);
	jPanel6.repaint();
	jPanel6.revalidate();
	tabelPenjualan();
    }//GEN-LAST:event_jLabel3MouseClicked

        private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
	jPanel6.removeAll();
	jPanel6.repaint();
	jPanel6.revalidate();

	jPanel6.add(datapelanggan);
	jPanel6.repaint();
	jPanel6.revalidate();

	tabelPelanggan();
        }//GEN-LAST:event_jLabel4MouseClicked

        private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
	jPanel6.removeAll();
	jPanel6.repaint();
	jPanel6.revalidate();

	jPanel6.add(datakaryawan);
	jPanel6.repaint();
	jPanel6.revalidate();

	tabelKaryawan();
        }//GEN-LAST:event_jLabel5MouseClicked

        private void homeComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_homeComponentAdded

        }//GEN-LAST:event_homeComponentAdded

        private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
		String idTransaksi = String.valueOf(jComboBox1.getSelectedItem());
		String idBuah = String.valueOf(jComboBox2.getSelectedItem());
		String ketiga = jTextField6.getText();
		String idBuahSql = "select id from tb_buah where nama = '" + jComboBox2.getSelectedItem() + "'";
		try{
			pst=conn.prepareStatement(idBuahSql);
			rs=pst.executeQuery();
			while(rs.next()){
				idBuah = rs.getString(1);
			}
			String Sql = "insert into tb_detail_transaksi (id_transaksi, id_buah, qty, id_user) values ('" + idTransaksi + "','" + idBuah + "','" + ketiga + "','" + id + "')";
			System.out.println(Sql);
			pst=conn.prepareStatement(Sql);
			rs=pst.executeQuery();
			JOptionPane.showMessageDialog(null,"Transaksi Terkirim");
		}catch(SQLException ex){
			System.out.println("doesnt work lmao");
		}
        }//GEN-LAST:event_button1ActionPerformed

        private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
		jPanel6.removeAll();
		jPanel6.repaint();
		jPanel6.revalidate();

		jPanel6.add(transaksi);
		jPanel6.repaint();
		jPanel6.revalidate();
		String sql = "insert into tb_transaksi(tanggal) values(CURDATE())";
		try{
		pst=conn.prepareStatement(sql);
		rs=pst.executeQuery();
		JOptionPane.showMessageDialog(null,"Transaksi baru telah dibuat");
		}catch(SQLException ex){
			System.out.println("doesnt work lmao");
		}
        }//GEN-LAST:event_button2ActionPerformed

        private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        }//GEN-LAST:event_jComboBox1ActionPerformed

        private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_jComboBox2ActionPerformed

        private void jTextField6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyTyped
        }//GEN-LAST:event_jTextField6KeyTyped

        private void jComboBox2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jComboBox2PropertyChange
        }//GEN-LAST:event_jComboBox2PropertyChange

        private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        }//GEN-LAST:event_jComboBox2ItemStateChanged

        private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
                // TODO add your handling code here:
		String sql = "select harga from tb_buah where nama = '" + jComboBox2.getSelectedItem() + "'";
		try{
			pst=conn.prepareStatement(sql);
			rs=pst.executeQuery();
			while(rs.next()){
				hargaBuah = rs.getInt("harga");
				updateHarga();
			}
		}catch(SQLException ex){
			System.out.println("netnot");
		}
        }//GEN-LAST:event_jTextField6KeyReleased

        private void jComboBox2PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBox2PopupMenuWillBecomeInvisible
                // TODO add your handling code here:
		howCost();
        }//GEN-LAST:event_jComboBox2PopupMenuWillBecomeInvisible

        private void jComboBox1PopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBox1PopupMenuWillBecomeVisible
                // TODO add your handling code here:
		listId();
        }//GEN-LAST:event_jComboBox1PopupMenuWillBecomeVisible

        private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_jComboBox4ActionPerformed

        private void button2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button2MouseClicked
                // TODO add your handling code here:
		jPanel6.removeAll();
		jPanel6.repaint();
		jPanel6.revalidate();

		jPanel6.add(newTransaksi);
		jPanel6.repaint();
		jPanel6.revalidate();
		listPelanggan();
        }//GEN-LAST:event_button2MouseClicked

        private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
                // TODO add your handling code here:
		String idPelanggan = "select id from tb_pelanggan where nama = '" + jComboBox4.getSelectedItem() + "'";
		try{
			pst = conn.prepareStatement(idPelanggan);
			rs = pst.executeQuery();
			while(rs.next()){
				String sql = "insert into tb_transaksi values(NULL,'" + rs.getString(1) + "',curdate())";
				pst = conn.prepareStatement(sql);
				rs = pst.executeQuery();
			}
			jPanel6.removeAll();
			jPanel6.repaint();
			jPanel6.revalidate();

			jPanel6.add(transaksi);
			jPanel6.repaint();
			jPanel6.revalidate();
		}catch(SQLException ex){
		}


        }//GEN-LAST:event_jLabel11MouseClicked

        private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_jTextField9ActionPerformed

        private void jComboBox1PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBox1PopupMenuWillBecomeInvisible
		try {
			// TODO add your handling code here:
			whoPelanggan();
		} catch (SQLException ex) {
			Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
		}
        }//GEN-LAST:event_jComboBox1PopupMenuWillBecomeInvisible

        private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
                // TODO add your handling code here:
		    DefaultTableModel model = new DefaultTableModel();
		    model.addColumn("Nama");
		    model.addColumn("Stok");
		    model.addColumn("Harga");
		    try{
			    String sql = "select * from tb_buah where nama = '" + jComboBox3.getSelectedItem() + "'";
			    pst=conn.prepareStatement(sql);
			    rs=pst.executeQuery();
			    while(rs.next()){
				    model.addRow(new Object[] {rs.getString(2), rs.getString(3), rs.getString(4)});
			    }
			    jTable4.setModel(model);
		    }catch(SQLException e){
		    }
        }//GEN-LAST:event_jButton1MouseClicked

        private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
                // TODO add your handling code here:
		tabelBuah();
        }//GEN-LAST:event_jPanel4MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        /* Create and display the form */
	System.setProperty("awt.useSystemAAFontSettings", "on");
        java.awt.EventQueue.invokeLater(new Runnable() {
		@Override
		public void run() {
			try {
				new Dashboard().setVisible(true);
			} catch (SQLException | ClassNotFoundException ex) {
				Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	});
    }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JPanel about;
        private java.awt.Button button1;
        private java.awt.Button button2;
        private javax.swing.JPanel cekStokBuah;
        private javax.swing.JPanel datakaryawan;
        private javax.swing.JPanel datapelanggan;
        private javax.swing.JPanel datapenjualan;
        private javax.swing.JPanel home;
        private javax.swing.JButton jButton1;
        private javax.swing.JComboBox<String> jComboBox1;
        private javax.swing.JComboBox<String> jComboBox2;
        private javax.swing.JComboBox<String> jComboBox3;
        private javax.swing.JComboBox<String> jComboBox4;
        private javax.swing.JDialog jDialog1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel10;
        private javax.swing.JLabel jLabel11;
        private javax.swing.JLabel jLabel12;
        private javax.swing.JLabel jLabel13;
        private javax.swing.JLabel jLabel14;
        private javax.swing.JLabel jLabel15;
        private javax.swing.JLabel jLabel16;
        private javax.swing.JLabel jLabel17;
        private javax.swing.JLabel jLabel18;
        private javax.swing.JLabel jLabel19;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel20;
        private javax.swing.JLabel jLabel21;
        private javax.swing.JLabel jLabel22;
        private javax.swing.JLabel jLabel23;
        private javax.swing.JLabel jLabel24;
        private javax.swing.JLabel jLabel25;
        private javax.swing.JLabel jLabel26;
        private javax.swing.JLabel jLabel27;
        private javax.swing.JLabel jLabel28;
        private javax.swing.JLabel jLabel29;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel30;
        private javax.swing.JLabel jLabel32;
        private javax.swing.JLabel jLabel33;
        private javax.swing.JLabel jLabel34;
        private javax.swing.JLabel jLabel35;
        private javax.swing.JLabel jLabel36;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JLabel jLabel8;
        private javax.swing.JLabel jLabel9;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel10;
        private javax.swing.JPanel jPanel11;
        private javax.swing.JPanel jPanel12;
        private javax.swing.JPanel jPanel13;
        private javax.swing.JPanel jPanel14;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JPanel jPanel5;
        private javax.swing.JPanel jPanel6;
        private javax.swing.JPanel jPanel7;
        private javax.swing.JPanel jPanel8;
        private javax.swing.JPanel jPanel9;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JScrollPane jScrollPane4;
        private javax.swing.JTable jTable1;
        private javax.swing.JTable jTable2;
        private javax.swing.JTable jTable3;
        private javax.swing.JTable jTable4;
        private javax.swing.JTextField jTextField6;
        private javax.swing.JTextField jTextField8;
        private javax.swing.JTextField jTextField9;
        private javax.swing.JPanel newTransaksi;
        private javax.swing.JPanel transaksi;
        // End of variables declaration//GEN-END:variables
}
