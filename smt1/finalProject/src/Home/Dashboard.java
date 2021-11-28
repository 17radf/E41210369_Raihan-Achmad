/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import javax.swing.JOptionPane;
import Controller.Koneksi;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


/**
 *
 * @author Raihan
 */

public final class Dashboard extends javax.swing.JFrame {
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
    }

    public void initThings(){
	    try{
		    // disable fields by default
			jTextField7.disable();

			jTextField11.disable();
			jTextField10.disable();
			jTextField9.disable();

			jTextField15.disable();
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
	    // list id
	sql = "select nama from tb_user where id = " + id;
	System.out.println(sql);
	pst=conn.prepareStatement(sql);
	rs=pst.executeQuery();
	if(rs.next()){
	jLabel24.setText("<html><center>Hi, " + rs.getString(1) + "</center></html>");
	jLabel11.setText(rs.getString(1));
	// init qty
	jTextField2.setText("0");
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
	    model.addColumn("Pelanggan");
	    model.addColumn("Buah");
	    model.addColumn("QTY");
	    model.addColumn("Kasir");
	    try{
		    String sql = "select dt.id, p.nama, b.nama, dt.qty, u.nama from tb_detail_transaksi as dt join tb_transaksi as t on dt.id_transaksi = t.no_faktur join tb_buah as b on dt.id_buah = b.id join tb_pelanggan as p on t.id_pelanggan = p.id join tb_user as u on dt.id_user = u.id order by dt.id desc";
		    pst=conn.prepareStatement(sql);
		    rs=pst.executeQuery();
		    while(rs.next()){
			    model.addRow(new Object[] {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)});
		    }
		    jTable3.setModel(model);
	    }catch(SQLException e){
	    }
    }

    public void tabelDetailPenjualan(String id){
	    DefaultTableModel model = new DefaultTableModel();
	    model.addColumn("ID");
	    model.addColumn("Buah");
	    model.addColumn("QTY");
	    model.addColumn("Harga");
	    model.addColumn("Kasir");
	    try{
		    String sql = "select dt.id, b.nama, dt.qty, sum(dt.qty * b.harga), u.nama from tb_detail_transaksi as dt join tb_transaksi as t on dt.id_transaksi = t.no_faktur join tb_buah as b on dt.id_buah = b.id join tb_pelanggan as p on t.id_pelanggan = p.id join tb_user as u on dt.id_user = u.id where t.no_faktur = " + id + " group by dt.id order by t.tanggal desc";
		    pst=conn.prepareStatement(sql);
		    rs=pst.executeQuery();
		    while(rs.next()){
			    model.addRow(new Object[] {rs.getString(1), rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5)});
		    }
		    jTable5.setModel(model);
		    sql = "select sum(dt.qty * b.harga) from tb_detail_transaksi as dt join tb_transaksi as t on dt.id_transaksi = t.no_faktur join tb_buah as b on dt.id_buah = b.id join tb_pelanggan as p on t.id_pelanggan = p.id join tb_user as u on dt.id_user = u.id where t.no_faktur = " + id;
		    pst=conn.prepareStatement(sql);
		    rs=pst.executeQuery();
		    while(rs.next()){
			    if(rs.getString(1) == null){
				    jLabel46.setText("Rp.0,00");
			    }else{
				    Locale locale = new Locale("id", "ID");
				    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
				    jLabel46.setText(currencyFormatter.format(Integer.parseInt(rs.getString(1))));
			    }
		    }
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
	    model.addColumn("ID");
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
	    model.addColumn("ID");
	    model.addColumn("Nama");
	    model.addColumn("Stok");
	    model.addColumn("Harga");
	    try{
		    String sql = "select * from tb_buah";
		    pst=conn.prepareStatement(sql);
		    rs=pst.executeQuery();
		    while(rs.next()){
			    model.addRow(new Object[] {rs.getString(1),rs.getString(2), rs.getString(3), rs.getString(4)});
		    }
		    jTable4.setModel(model);
	    }catch(SQLException e){
	    }
    }

    public void listId(){
	jComboBox3.removeAllItems();
	    try{
		    String sql = "select no_faktur from tb_transaksi";
		    pst=conn.prepareStatement(sql);
		    rs=pst.executeQuery();
		    while(rs.next()){
			jComboBox3.addItem(rs.getString(1));
		    }
			sql = "select t.tanggal, p.nama from tb_transaksi as t join tb_pelanggan as p on t.id_pelanggan = p.id where t.no_faktur = " + jComboBox3.getSelectedItem();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while(rs.next()){
				jLabel9.setText(rs.getString(1));
				jLabel47.setText(rs.getString(2));
				tabelDetailPenjualan((String) jComboBox3.getSelectedItem());
			}
	    }catch(SQLException e){
	    }
    }

    public void listBuah(){
	jComboBox1.removeAllItems();
	jComboBox4.removeAllItems();
	    try{
		    String sql = "select nama from tb_buah";
		    pst=conn.prepareStatement(sql);
		    rs=pst.executeQuery();
		    while(rs.next()){
			jComboBox1.addItem(rs.getString(1));
			jComboBox4.addItem(rs.getString(1));
		    }
	    }catch(SQLException e){
	    }
    }

    public void listPelanggan(){
	jComboBox5.removeAllItems();
	    try{
		    String sql = "select nama from tb_pelanggan";
		    pst=conn.prepareStatement(sql);
		    rs=pst.executeQuery();
		    while(rs.next()){
			jComboBox5.addItem(rs.getString(1));
		    }
	    }catch(SQLException e){
	    }
    }

	public void clear(){

		jTextField3.setText("");
		jTextField4.setText("");
		jTextField5.setText("");
		jTextField3.enable();
		jTextField11.setText("");
		jTextField10.setText("");
		jTextField9.setText("");
		jTextField8.setText("");
		jComboBox3.setSelectedItem(this);

		jTextField15.setText("");
		jTextField14.setText("");
		jTextField13.setText("");
		jTextField12.setText("");

		tabelPelanggan();
		tabelBuah();
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
                jPanel2 = new javax.swing.JPanel();
                jLabel6 = new javax.swing.JLabel();
                jPanel11 = new javax.swing.JPanel();
                jLabel12 = new javax.swing.JLabel();
                jPanel9 = new javax.swing.JPanel();
                jLabel21 = new javax.swing.JLabel();
                jPanel6 = new javax.swing.JPanel();
                home = new javax.swing.JPanel();
                jLabel13 = new javax.swing.JLabel();
                jPanel12 = new javax.swing.JPanel();
                jLabel8 = new javax.swing.JLabel();
                jLabel23 = new javax.swing.JLabel();
                jPanel10 = new javax.swing.JPanel();
                jLabel10 = new javax.swing.JLabel();
                jPanel13 = new javax.swing.JPanel();
                jLabel18 = new javax.swing.JLabel();
                jLabel25 = new javax.swing.JLabel();
                jPanel23 = new javax.swing.JPanel();
                jLabel26 = new javax.swing.JLabel();
                jPanel14 = new javax.swing.JPanel();
                jLabel34 = new javax.swing.JLabel();
                jLabel28 = new javax.swing.JLabel();
                jPanel24 = new javax.swing.JPanel();
                jLabel35 = new javax.swing.JLabel();
                jPanel20 = new javax.swing.JPanel();
                jPanel4 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                jPanel5 = new javax.swing.JPanel();
                jLabel3 = new javax.swing.JLabel();
                jPanel7 = new javax.swing.JPanel();
                jLabel4 = new javax.swing.JLabel();
                jPanel8 = new javax.swing.JPanel();
                jLabel5 = new javax.swing.JLabel();
                jPanel15 = new javax.swing.JPanel();
                jLabel31 = new javax.swing.JLabel();
                cekStokBuah = new javax.swing.JPanel();
                jScrollPane4 = new javax.swing.JScrollPane();
                jTable4 = new javax.swing.JTable();
                jSeparator2 = new javax.swing.JSeparator();
                jTextField1 = new javax.swing.JTextField();
                jLabel38 = new javax.swing.JLabel();
                jLabel51 = new javax.swing.JLabel();
                jPanel26 = new javax.swing.JPanel();
                jLabel15 = new javax.swing.JLabel();
                jLabel52 = new javax.swing.JLabel();
                jLabel53 = new javax.swing.JLabel();
                jPanel27 = new javax.swing.JPanel();
                jLabel54 = new javax.swing.JLabel();
                jLabel58 = new javax.swing.JLabel();
                jTextField3 = new javax.swing.JTextField();
                jTextField4 = new javax.swing.JTextField();
                jTextField5 = new javax.swing.JTextField();
                jPanel28 = new javax.swing.JPanel();
                jLabel55 = new javax.swing.JLabel();
                jPanel29 = new javax.swing.JPanel();
                jLabel56 = new javax.swing.JLabel();
                jPanel30 = new javax.swing.JPanel();
                jLabel57 = new javax.swing.JLabel();
                jTextField7 = new javax.swing.JTextField();
                jPanel46 = new javax.swing.JPanel();
                jLabel17 = new javax.swing.JLabel();
                buatTransaksi = new javax.swing.JPanel();
                jPanel45 = new javax.swing.JPanel();
                jLabel14 = new javax.swing.JLabel();
                jPanel17 = new javax.swing.JPanel();
                jLabel2 = new javax.swing.JLabel();
                jLabel39 = new javax.swing.JLabel();
                jLabel41 = new javax.swing.JLabel();
                jLabel42 = new javax.swing.JLabel();
                jComboBox3 = new javax.swing.JComboBox<>();
                jLabel9 = new javax.swing.JLabel();
                jLabel11 = new javax.swing.JLabel();
                jLabel47 = new javax.swing.JLabel();
                jPanel18 = new javax.swing.JPanel();
                jLabel43 = new javax.swing.JLabel();
                jLabel44 = new javax.swing.JLabel();
                jPanel22 = new javax.swing.JPanel();
                jLabel7 = new javax.swing.JLabel();
                jComboBox4 = new javax.swing.JComboBox<>();
                jTextField2 = new javax.swing.JTextField();
                jScrollPane5 = new javax.swing.JScrollPane();
                jTable5 = new javax.swing.JTable();
                jPanel16 = new javax.swing.JPanel();
                jPanel3 = new javax.swing.JPanel();
                jLabel45 = new javax.swing.JLabel();
                jLabel46 = new javax.swing.JLabel();
                jPanel19 = new javax.swing.JPanel();
                jLabel49 = new javax.swing.JLabel();
                jPanel21 = new javax.swing.JPanel();
                jLabel48 = new javax.swing.JLabel();
                jPanel25 = new javax.swing.JPanel();
                jLabel50 = new javax.swing.JLabel();
                jComboBox5 = new javax.swing.JComboBox<>();
                dataPenjualan = new javax.swing.JPanel();
                jScrollPane3 = new javax.swing.JScrollPane();
                jTable3 = new javax.swing.JTable();
                jPanel31 = new javax.swing.JPanel();
                jLabel16 = new javax.swing.JLabel();
                jLabel59 = new javax.swing.JLabel();
                jLabel60 = new javax.swing.JLabel();
                jPanel32 = new javax.swing.JPanel();
                jLabel61 = new javax.swing.JLabel();
                jLabel62 = new javax.swing.JLabel();
                jTextField8 = new javax.swing.JTextField();
                jTextField9 = new javax.swing.JTextField();
                jPanel34 = new javax.swing.JPanel();
                jLabel64 = new javax.swing.JLabel();
                jPanel35 = new javax.swing.JPanel();
                jLabel65 = new javax.swing.JLabel();
                jLabel20 = new javax.swing.JLabel();
                jLabel22 = new javax.swing.JLabel();
                jTextField10 = new javax.swing.JTextField();
                jTextField11 = new javax.swing.JTextField();
                jComboBox1 = new javax.swing.JComboBox<>();
                jLabel27 = new javax.swing.JLabel();
                jLabel33 = new javax.swing.JLabel();
                dataPelanggan = new javax.swing.JPanel();
                jScrollPane2 = new javax.swing.JScrollPane();
                jTable2 = new javax.swing.JTable();
                jPanel33 = new javax.swing.JPanel();
                jLabel63 = new javax.swing.JLabel();
                jLabel66 = new javax.swing.JLabel();
                jPanel36 = new javax.swing.JPanel();
                jLabel67 = new javax.swing.JLabel();
                jLabel68 = new javax.swing.JLabel();
                jTextField12 = new javax.swing.JTextField();
                jTextField13 = new javax.swing.JTextField();
                jPanel37 = new javax.swing.JPanel();
                jLabel69 = new javax.swing.JLabel();
                jPanel38 = new javax.swing.JPanel();
                jLabel70 = new javax.swing.JLabel();
                jLabel37 = new javax.swing.JLabel();
                jLabel40 = new javax.swing.JLabel();
                jTextField14 = new javax.swing.JTextField();
                jTextField15 = new javax.swing.JTextField();
                jPanel43 = new javax.swing.JPanel();
                jLabel72 = new javax.swing.JLabel();
                jLabel81 = new javax.swing.JLabel();
                dataKaryawan = new javax.swing.JPanel();
                jLabel32 = new javax.swing.JLabel();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTable1 = new javax.swing.JTable();
                jPanel39 = new javax.swing.JPanel();
                jLabel71 = new javax.swing.JLabel();
                jPanel40 = new javax.swing.JPanel();
                jLabel74 = new javax.swing.JLabel();
                jLabel75 = new javax.swing.JLabel();
                jPanel41 = new javax.swing.JPanel();
                jLabel76 = new javax.swing.JLabel();
                jPanel42 = new javax.swing.JPanel();
                jLabel77 = new javax.swing.JLabel();
                jLabel78 = new javax.swing.JLabel();
                jLabel79 = new javax.swing.JLabel();
                jTextField18 = new javax.swing.JTextField();
                jTextField19 = new javax.swing.JTextField();
                jComboBox6 = new javax.swing.JComboBox<>();
                jPanel44 = new javax.swing.JPanel();
                jLabel73 = new javax.swing.JLabel();
                about = new javax.swing.JPanel();
                jLabel36 = new javax.swing.JLabel();
                jLabel30 = new javax.swing.JLabel();
                jLabel24 = new javax.swing.JLabel();
                jLabel29 = new javax.swing.JLabel();

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

                jLabel13.setBackground(new java.awt.Color(25, 26, 25));
                jLabel13.setFont(new java.awt.Font("Cascadia Mono", 1, 24)); // NOI18N
                jLabel13.setForeground(new java.awt.Color(25, 26, 25));
                jLabel13.setText("Toko Buah-Buahan");
                home.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 230, 60));

                jPanel12.setBackground(new java.awt.Color(78, 159, 61));

                jLabel8.setFont(new java.awt.Font("Cascadia Mono", 0, 24)); // NOI18N
                jLabel8.setForeground(new java.awt.Color(255, 255, 255));
                jLabel8.setText("bruh");

                jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-transaction-48.png"))); // NOI18N

                jPanel10.setBackground(new java.awt.Color(30, 81, 40));

                jLabel10.setFont(new java.awt.Font("Cascadia Mono", 0, 14)); // NOI18N
                jLabel10.setForeground(new java.awt.Color(255, 255, 255));
                jLabel10.setText("Transaksi Hari Ini");

                javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
                jPanel10.setLayout(jPanel10Layout);
                jPanel10Layout.setHorizontalGroup(
                        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(32, Short.MAX_VALUE))
                );
                jPanel10Layout.setVerticalGroup(
                        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                );

                javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
                jPanel12.setLayout(jPanel12Layout);
                jPanel12Layout.setHorizontalGroup(
                        jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                jPanel12Layout.setVerticalGroup(
                        jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                );

                home.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 210, 120));

                jPanel13.setBackground(new java.awt.Color(78, 159, 61));

                jLabel18.setFont(new java.awt.Font("Cascadia Mono", 0, 24)); // NOI18N
                jLabel18.setForeground(new java.awt.Color(255, 255, 255));
                jLabel18.setText("bruh");

                jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-bill-48.png"))); // NOI18N

                jPanel23.setBackground(new java.awt.Color(30, 81, 40));

                jLabel26.setFont(new java.awt.Font("Cascadia Mono", 0, 14)); // NOI18N
                jLabel26.setForeground(new java.awt.Color(255, 255, 255));
                jLabel26.setText("Total Transaksi");

                javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
                jPanel23.setLayout(jPanel23Layout);
                jPanel23Layout.setHorizontalGroup(
                        jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                                .addContainerGap(37, Short.MAX_VALUE)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27))
                );
                jPanel23Layout.setVerticalGroup(
                        jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                );

                javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
                jPanel13.setLayout(jPanel13Layout);
                jPanel13Layout.setHorizontalGroup(
                        jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel13Layout.setVerticalGroup(
                        jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                );

                home.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 110, 210, 120));

                jPanel14.setBackground(new java.awt.Color(78, 159, 61));

                jLabel34.setFont(new java.awt.Font("Cascadia Mono", 0, 24)); // NOI18N
                jLabel34.setForeground(new java.awt.Color(255, 255, 255));
                jLabel34.setText("bruh");

                jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-fruits-48.png"))); // NOI18N

                jPanel24.setBackground(new java.awt.Color(30, 81, 40));

                jLabel35.setFont(new java.awt.Font("Cascadia Mono", 0, 14)); // NOI18N
                jLabel35.setForeground(new java.awt.Color(255, 255, 255));
                jLabel35.setText("Stok Buah");

                javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
                jPanel24.setLayout(jPanel24Layout);
                jPanel24Layout.setHorizontalGroup(
                        jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel24Layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(jLabel35)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel24Layout.setVerticalGroup(
                        jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                );

                javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
                jPanel14.setLayout(jPanel14Layout);
                jPanel14Layout.setHorizontalGroup(
                        jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jLabel28)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(45, Short.MAX_VALUE))
                        .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                jPanel14Layout.setVerticalGroup(
                        jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(16, 16, 16)
                                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                );

                home.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 110, 210, 120));

                jPanel20.setBackground(new java.awt.Color(220, 237, 171));

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
                jLabel1.setText("<html>Stock Buah</html>");
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
                                .addGap(19, 19, 19)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(40, Short.MAX_VALUE))
                );
                jPanel4Layout.setVerticalGroup(
                        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                );

                jPanel5.setBackground(new java.awt.Color(220, 237, 171));
                jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(78, 159, 61), 2, true));

                jLabel3.setBackground(new java.awt.Color(25, 26, 25));
                jLabel3.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel3.setForeground(new java.awt.Color(25, 26, 25));
                jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-detail-30.png"))); // NOI18N
                jLabel3.setText("<html>Data Transaksi</html>");
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
                                .addContainerGap(20, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                );
                jPanel5Layout.setVerticalGroup(
                        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                );

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

                jPanel15.setBackground(new java.awt.Color(220, 237, 171));
                jPanel15.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(78, 159, 61), 2, true));
                jPanel15.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jPanel15MouseClicked(evt);
                        }
                });

                jLabel31.setBackground(new java.awt.Color(25, 26, 25));
                jLabel31.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel31.setForeground(new java.awt.Color(25, 26, 25));
                jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-buy-30.png"))); // NOI18N
                jLabel31.setText("<html>Buat Transaksi</html>");
                jLabel31.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel31MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
                jPanel15.setLayout(jPanel15Layout);
                jPanel15Layout.setHorizontalGroup(
                        jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(18, Short.MAX_VALUE))
                );
                jPanel15Layout.setVerticalGroup(
                        jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                );

                javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
                jPanel20.setLayout(jPanel20Layout);
                jPanel20Layout.setHorizontalGroup(
                        jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel20Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(145, Short.MAX_VALUE))
                );
                jPanel20Layout.setVerticalGroup(
                        jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel20Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
                );

                home.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 690, 250));

                jPanel6.add(home, "card4");

                cekStokBuah.setBackground(new java.awt.Color(216, 233, 168));
                cekStokBuah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jTable4.setBackground(new java.awt.Color(223, 232, 197));
                jTable4.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
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
                jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jTable4MouseClicked(evt);
                        }
                });
                jScrollPane4.setViewportView(jTable4);

                cekStokBuah.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 110, 360, 400));

                jSeparator2.setBackground(new java.awt.Color(30, 81, 40));
                jSeparator2.setForeground(new java.awt.Color(30, 81, 40));
                jSeparator2.setAlignmentX(1.0F);
                jSeparator2.setAlignmentY(1.0F);
                jSeparator2.setAutoscrolls(true);
                cekStokBuah.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 100, 210, 20));

                jTextField1.setBackground(new java.awt.Color(216, 233, 168));
                jTextField1.setBorder(null);
                jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyPressed(java.awt.event.KeyEvent evt) {
                                jTextField1KeyPressed(evt);
                        }
                });
                cekStokBuah.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 70, 210, 30));

                jLabel38.setForeground(new java.awt.Color(216, 233, 168));
                jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-search-30.png"))); // NOI18N
                jLabel38.setText("jLabel38");
                cekStokBuah.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, 30, 30));

                jLabel51.setBackground(new java.awt.Color(25, 26, 25));
                jLabel51.setFont(new java.awt.Font("Cascadia Mono", 1, 24)); // NOI18N
                jLabel51.setForeground(new java.awt.Color(25, 26, 25));
                jLabel51.setText("Stok Buah");
                cekStokBuah.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 260, 60));

                jPanel26.setBackground(new java.awt.Color(223, 232, 197));

                jLabel15.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel15.setForeground(new java.awt.Color(25, 26, 25));
                jLabel15.setText("Nama Buah");

                jLabel52.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel52.setForeground(new java.awt.Color(25, 26, 25));
                jLabel52.setText("Stok");

                jLabel53.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel53.setForeground(new java.awt.Color(25, 26, 25));
                jLabel53.setText("Harga");

                jPanel27.setBackground(new java.awt.Color(78, 159, 61));

                jLabel54.setBackground(new java.awt.Color(25, 26, 25));
                jLabel54.setFont(new java.awt.Font("Cascadia Mono", 1, 20)); // NOI18N
                jLabel54.setForeground(new java.awt.Color(255, 255, 255));
                jLabel54.setText("<html><center>Edit/Update Stok Buah-Buahan</center></html>");

                jLabel58.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-refresh-20.png"))); // NOI18N
                jLabel58.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel58MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
                jPanel27.setLayout(jPanel27Layout);
                jPanel27Layout.setHorizontalGroup(
                        jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel27Layout.createSequentialGroup()
                                .addContainerGap(28, Short.MAX_VALUE)
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                                                .addComponent(jLabel58)
                                                .addContainerGap())
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                                                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(23, 23, 23))))
                );
                jPanel27Layout.setVerticalGroup(
                        jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel27Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel58)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25))
                );

                jTextField3.setBackground(new java.awt.Color(223, 232, 197));
                jTextField3.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jTextField3ActionPerformed(evt);
                        }
                });

                jTextField4.setBackground(new java.awt.Color(223, 232, 197));
                jTextField4.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jTextField4ActionPerformed(evt);
                        }
                });

                jTextField5.setBackground(new java.awt.Color(223, 232, 197));
                jTextField5.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jTextField5ActionPerformed(evt);
                        }
                });

                jPanel28.setBackground(new java.awt.Color(78, 159, 61));

                jLabel55.setFont(new java.awt.Font("Cascadia Mono", 1, 18)); // NOI18N
                jLabel55.setForeground(new java.awt.Color(255, 255, 255));
                jLabel55.setText("<html><center>Tambah</center></html>");
                jLabel55.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel55MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
                jPanel28.setLayout(jPanel28Layout);
                jPanel28Layout.setHorizontalGroup(
                        jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel28Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel28Layout.setVerticalGroup(
                        jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                );

                jPanel29.setBackground(new java.awt.Color(253, 253, 150));

                jLabel56.setBackground(new java.awt.Color(25, 26, 25));
                jLabel56.setFont(new java.awt.Font("Cascadia Mono", 1, 18)); // NOI18N
                jLabel56.setForeground(new java.awt.Color(25, 26, 25));
                jLabel56.setText("<html><center>Update</center></html>");
                jLabel56.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel56MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
                jPanel29.setLayout(jPanel29Layout);
                jPanel29Layout.setHorizontalGroup(
                        jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel29Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel29Layout.setVerticalGroup(
                        jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                );

                jPanel30.setBackground(new java.awt.Color(255, 105, 97));

                jLabel57.setFont(new java.awt.Font("Cascadia Mono", 1, 18)); // NOI18N
                jLabel57.setForeground(new java.awt.Color(255, 255, 255));
                jLabel57.setText("<html><center>Delete</center></html>");
                jLabel57.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel57MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
                jPanel30.setLayout(jPanel30Layout);
                jPanel30Layout.setHorizontalGroup(
                        jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel30Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel30Layout.setVerticalGroup(
                        jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                );

                javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
                jPanel26.setLayout(jPanel26Layout);
                jPanel26Layout.setHorizontalGroup(
                        jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel26Layout.createSequentialGroup()
                                                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel15)
                                                        .addComponent(jLabel52)
                                                        .addComponent(jLabel53))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                                        .addComponent(jTextField4)
                                                        .addComponent(jTextField3))))
                                .addGap(22, 22, 22))
                );
                jPanel26Layout.setVerticalGroup(
                        jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel15)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel52)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel53)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(48, 48, 48))
                );

                cekStokBuah.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 250, 400));

                jTextField7.setBackground(new java.awt.Color(216, 233, 168));
                jTextField7.setForeground(new java.awt.Color(216, 233, 168));
                jTextField7.setBorder(null);
                jTextField7.setCaretColor(new java.awt.Color(216, 233, 168));
                jTextField7.setDisabledTextColor(new java.awt.Color(216, 233, 168));
                jTextField7.setSelectedTextColor(new java.awt.Color(216, 233, 168));
                jTextField7.setSelectionColor(new java.awt.Color(216, 233, 168));
                jTextField7.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jTextField7ActionPerformed(evt);
                        }
                });
                cekStokBuah.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

                jPanel46.setBackground(new java.awt.Color(78, 159, 61));

                jLabel17.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel17.setForeground(new java.awt.Color(255, 255, 255));
                jLabel17.setText("Cari");
                jLabel17.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel17MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
                jPanel46.setLayout(jPanel46Layout);
                jPanel46Layout.setHorizontalGroup(
                        jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel46Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel17)
                                .addContainerGap(35, Short.MAX_VALUE))
                );
                jPanel46Layout.setVerticalGroup(
                        jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel46Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                );

                cekStokBuah.add(jPanel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 70, 100, 30));

                jPanel6.add(cekStokBuah, "card2");

                buatTransaksi.setBackground(new java.awt.Color(216, 233, 168));
                buatTransaksi.setForeground(new java.awt.Color(255, 255, 255));

                jPanel45.setBackground(new java.awt.Color(78, 159, 61));

                jLabel14.setBackground(new java.awt.Color(255, 255, 255));
                jLabel14.setFont(new java.awt.Font("Cascadia Mono", 0, 24)); // NOI18N
                jLabel14.setForeground(new java.awt.Color(255, 255, 255));
                jLabel14.setText("Buat Transaksi");
                jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel14MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
                jPanel45.setLayout(jPanel45Layout);
                jPanel45Layout.setHorizontalGroup(
                        jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel45Layout.createSequentialGroup()
                                .addGap(268, 268, 268)
                                .addComponent(jLabel14)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel45Layout.setVerticalGroup(
                        jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                );

                jPanel17.setBackground(new java.awt.Color(223, 232, 197));

                jLabel2.setBackground(new java.awt.Color(25, 26, 25));
                jLabel2.setFont(new java.awt.Font("Cascadia Mono", 0, 14)); // NOI18N
                jLabel2.setForeground(new java.awt.Color(25, 26, 25));
                jLabel2.setText("No Faktur");

                jLabel39.setBackground(new java.awt.Color(25, 26, 25));
                jLabel39.setFont(new java.awt.Font("Cascadia Mono", 0, 14)); // NOI18N
                jLabel39.setForeground(new java.awt.Color(25, 26, 25));
                jLabel39.setText("Tanggal");

                jLabel41.setBackground(new java.awt.Color(25, 26, 25));
                jLabel41.setFont(new java.awt.Font("Cascadia Mono", 0, 14)); // NOI18N
                jLabel41.setForeground(new java.awt.Color(25, 26, 25));
                jLabel41.setText("Kasir");

                jLabel42.setBackground(new java.awt.Color(25, 26, 25));
                jLabel42.setFont(new java.awt.Font("Cascadia Mono", 0, 14)); // NOI18N
                jLabel42.setForeground(new java.awt.Color(25, 26, 25));
                jLabel42.setText("Pelanggan");

                jComboBox3.setBackground(new java.awt.Color(223, 232, 197));
                jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
                jComboBox3.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
                        public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                        }
                        public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                                jComboBox3PopupMenuWillBecomeInvisible(evt);
                        }
                        public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                        }
                });

                jLabel9.setText("jLabel9");

                jLabel11.setText("jLabel11");

                jLabel47.setText("jLabel47");

                javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
                jPanel17.setLayout(jPanel17Layout);
                jPanel17Layout.setHorizontalGroup(
                        jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel39)
                                        .addComponent(jLabel41)
                                        .addComponent(jLabel42)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(33, 33, 33)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                                .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap(23, Short.MAX_VALUE))
                );
                jPanel17Layout.setVerticalGroup(
                        jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel39)
                                        .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel41)
                                        .addComponent(jLabel11))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel42)
                                        .addComponent(jLabel47))
                                .addContainerGap(17, Short.MAX_VALUE))
                );

                jPanel18.setBackground(new java.awt.Color(223, 232, 197));

                jLabel43.setBackground(new java.awt.Color(25, 26, 25));
                jLabel43.setFont(new java.awt.Font("Cascadia Mono", 0, 14)); // NOI18N
                jLabel43.setForeground(new java.awt.Color(25, 26, 25));
                jLabel43.setText("Buah");

                jLabel44.setBackground(new java.awt.Color(25, 26, 25));
                jLabel44.setFont(new java.awt.Font("Cascadia Mono", 0, 14)); // NOI18N
                jLabel44.setForeground(new java.awt.Color(25, 26, 25));
                jLabel44.setText("Quantitas");

                jPanel22.setBackground(new java.awt.Color(78, 159, 61));
                jPanel22.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(78, 159, 61), 2, true));

                jLabel7.setBackground(new java.awt.Color(255, 255, 255));
                jLabel7.setFont(new java.awt.Font("Cascadia Mono", 0, 16)); // NOI18N
                jLabel7.setForeground(new java.awt.Color(255, 255, 255));
                jLabel7.setText("        Tambah");
                jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel7MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
                jPanel22.setLayout(jPanel22Layout);
                jPanel22Layout.setHorizontalGroup(
                        jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                );
                jPanel22Layout.setVerticalGroup(
                        jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                );

                jComboBox4.setBackground(new java.awt.Color(223, 232, 197));
                jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

                jTextField2.setBackground(new java.awt.Color(223, 232, 197));
                jTextField2.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseExited(java.awt.event.MouseEvent evt) {
                                jTextField2MouseExited(evt);
                        }
                });
                jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                jTextField2KeyReleased(evt);
                        }
                });

                javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
                jPanel18.setLayout(jPanel18Layout);
                jPanel18Layout.setHorizontalGroup(
                        jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel18Layout.createSequentialGroup()
                                                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(15, 15, 15))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createSequentialGroup()
                                                                .addComponent(jLabel44)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createSequentialGroup()
                                                                .addComponent(jLabel43)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(14, 14, 14))))
                );
                jPanel18Layout.setVerticalGroup(
                        jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                                .addContainerGap(15, Short.MAX_VALUE)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel43)
                                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel44)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12))
                );

                jTable5.setBackground(new java.awt.Color(223, 232, 197));
                jTable5.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jTable5.setForeground(new java.awt.Color(25, 26, 25));
                jTable5.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {
                                {null, null, null, null},
                                {null, null, null, null},
                                {null, null, null, null},
                                {null, null, null, null}
                        },
                        new String [] {
                                "Buah", "QTY", "Harga", "Kasir"
                        }
                ));
                jTable5.setGridColor(new java.awt.Color(78, 159, 61));
                jScrollPane5.setViewportView(jTable5);

                jPanel16.setBackground(new java.awt.Color(219, 232, 181));
                jPanel16.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(78, 159, 61), 2, true));

                jPanel3.setBackground(new java.awt.Color(78, 159, 61));

                jLabel45.setFont(new java.awt.Font("Cascadia Mono", 0, 24)); // NOI18N
                jLabel45.setForeground(new java.awt.Color(255, 255, 255));
                jLabel45.setText("TOTAL");

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel45)
                                .addContainerGap(15, Short.MAX_VALUE))
                );
                jPanel3Layout.setVerticalGroup(
                        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                );

                jLabel46.setBackground(new java.awt.Color(223, 232, 197));
                jLabel46.setFont(new java.awt.Font("Cascadia Mono", 0, 18)); // NOI18N
                jLabel46.setForeground(new java.awt.Color(25, 26, 25));
                jLabel46.setText("Rp. Harga Dirimu");

                javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
                jPanel16.setLayout(jPanel16Layout);
                jPanel16Layout.setHorizontalGroup(
                        jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel16Layout.setVerticalGroup(
                        jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );

                jPanel19.setBackground(new java.awt.Color(78, 159, 61));
                jPanel19.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(78, 159, 61), 2, true));

                jLabel49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-checkout-40.png"))); // NOI18N

                javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
                jPanel19.setLayout(jPanel19Layout);
                jPanel19Layout.setHorizontalGroup(
                        jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel19Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel49)
                                .addContainerGap(24, Short.MAX_VALUE))
                );
                jPanel19Layout.setVerticalGroup(
                        jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                );

                jPanel21.setBackground(new java.awt.Color(223, 232, 197));

                jLabel48.setBackground(new java.awt.Color(25, 26, 25));
                jLabel48.setFont(new java.awt.Font("Cascadia Mono", 0, 14)); // NOI18N
                jLabel48.setForeground(new java.awt.Color(25, 26, 25));
                jLabel48.setText("Transaksi Baru");

                jPanel25.setBackground(new java.awt.Color(78, 159, 61));
                jPanel25.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(78, 159, 61), 2, true));

                jLabel50.setBackground(new java.awt.Color(255, 255, 255));
                jLabel50.setFont(new java.awt.Font("Cascadia Mono", 0, 16)); // NOI18N
                jLabel50.setForeground(new java.awt.Color(255, 255, 255));
                jLabel50.setText("         Buat");
                jLabel50.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel50MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
                jPanel25.setLayout(jPanel25Layout);
                jPanel25Layout.setHorizontalGroup(
                        jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                );
                jPanel25Layout.setVerticalGroup(
                        jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel50, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                );

                jComboBox5.setBackground(new java.awt.Color(223, 232, 197));
                jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

                javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
                jPanel21.setLayout(jPanel21Layout);
                jPanel21Layout.setHorizontalGroup(
                        jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel21Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel21Layout.createSequentialGroup()
                                                .addComponent(jLabel48)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(15, 15, 15))
                );
                jPanel21Layout.setVerticalGroup(
                        jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel21Layout.createSequentialGroup()
                                .addContainerGap(30, Short.MAX_VALUE)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel48)
                                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                );

                javax.swing.GroupLayout buatTransaksiLayout = new javax.swing.GroupLayout(buatTransaksi);
                buatTransaksi.setLayout(buatTransaksiLayout);
                buatTransaksiLayout.setHorizontalGroup(
                        buatTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(buatTransaksiLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(buatTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(30, 30, 30)
                                .addGroup(buatTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(buatTransaksiLayout.createSequentialGroup()
                                                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(40, 40, 40))
                );
                buatTransaksiLayout.setVerticalGroup(
                        buatTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(buatTransaksiLayout.createSequentialGroup()
                                .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addGroup(buatTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(buatTransaksiLayout.createSequentialGroup()
                                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(buatTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(buatTransaksiLayout.createSequentialGroup()
                                                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(15, 15, 15)
                                                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(15, 15, 15)
                                                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(96, Short.MAX_VALUE))
                );

                jPanel6.add(buatTransaksi, "card5");

                dataPenjualan.setBackground(new java.awt.Color(216, 233, 168));
                dataPenjualan.setForeground(new java.awt.Color(255, 255, 255));

                jTable3.setBackground(new java.awt.Color(216, 233, 168));
                jTable3.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
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
                jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jTable3MouseClicked(evt);
                        }
                });
                jScrollPane3.setViewportView(jTable3);

                jPanel31.setBackground(new java.awt.Color(223, 232, 197));

                jLabel16.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel16.setForeground(new java.awt.Color(25, 26, 25));
                jLabel16.setText("Buah");

                jLabel59.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel59.setForeground(new java.awt.Color(25, 26, 25));
                jLabel59.setText("QTY");

                jLabel60.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel60.setForeground(new java.awt.Color(25, 26, 25));
                jLabel60.setText("Kasir");

                jPanel32.setBackground(new java.awt.Color(78, 159, 61));

                jLabel61.setBackground(new java.awt.Color(25, 26, 25));
                jLabel61.setFont(new java.awt.Font("Cascadia Mono", 1, 20)); // NOI18N
                jLabel61.setForeground(new java.awt.Color(255, 255, 255));
                jLabel61.setText("<html><center>Detail Penjualan</center></html>");

                jLabel62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-refresh-20.png"))); // NOI18N
                jLabel62.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel62MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
                jPanel32.setLayout(jPanel32Layout);
                jPanel32Layout.setHorizontalGroup(
                        jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel32Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel32Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel62)
                                .addContainerGap())
                );
                jPanel32Layout.setVerticalGroup(
                        jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel32Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel62)
                                .addGap(5, 5, 5)
                                .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25))
                );

                jTextField8.setBackground(new java.awt.Color(223, 232, 197));
                jTextField8.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jTextField8ActionPerformed(evt);
                        }
                });

                jTextField9.setBackground(new java.awt.Color(223, 232, 197));
                jTextField9.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jTextField9ActionPerformed(evt);
                        }
                });

                jPanel34.setBackground(new java.awt.Color(253, 253, 150));

                jLabel64.setBackground(new java.awt.Color(25, 26, 25));
                jLabel64.setFont(new java.awt.Font("Cascadia Mono", 1, 18)); // NOI18N
                jLabel64.setForeground(new java.awt.Color(25, 26, 25));
                jLabel64.setText("<html><center>Update</center></html>");
                jLabel64.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel64MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
                jPanel34.setLayout(jPanel34Layout);
                jPanel34Layout.setHorizontalGroup(
                        jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel34Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel34Layout.setVerticalGroup(
                        jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                );

                jPanel35.setBackground(new java.awt.Color(255, 105, 97));

                jLabel65.setFont(new java.awt.Font("Cascadia Mono", 1, 18)); // NOI18N
                jLabel65.setForeground(new java.awt.Color(255, 255, 255));
                jLabel65.setText("<html><center>Delete</center></html>");
                jLabel65.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel65MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
                jPanel35.setLayout(jPanel35Layout);
                jPanel35Layout.setHorizontalGroup(
                        jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel35Layout.setVerticalGroup(
                        jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                );

                jLabel20.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel20.setForeground(new java.awt.Color(25, 26, 25));
                jLabel20.setText("Pelanggan");

                jLabel22.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel22.setForeground(new java.awt.Color(25, 26, 25));
                jLabel22.setText("ID");

                jTextField10.setBackground(new java.awt.Color(223, 232, 197));

                jTextField11.setBackground(new java.awt.Color(223, 232, 197));

                jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
                jComboBox1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
                        public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                        }
                        public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                                jComboBox1PopupMenuWillBecomeInvisible(evt);
                        }
                        public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                        }
                });

                javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
                jPanel31.setLayout(jPanel31Layout);
                jPanel31Layout.setHorizontalGroup(
                        jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel31Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel31Layout.createSequentialGroup()
                                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel16)
                                                        .addComponent(jLabel59)
                                                        .addComponent(jLabel60)
                                                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel20))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jTextField9)
                                                        .addComponent(jTextField8)
                                                        .addComponent(jTextField10)
                                                        .addComponent(jTextField11)
                                                        .addComponent(jComboBox1, 0, 100, Short.MAX_VALUE))))
                                .addGap(22, 22, 22))
                );
                jPanel31Layout.setVerticalGroup(
                        jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel31Layout.createSequentialGroup()
                                .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel22))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel16)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel59)
                                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel60)
                                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15))
                );

                jLabel27.setForeground(new java.awt.Color(216, 233, 168));
                jLabel27.setText("jLabel27");

                jLabel33.setBackground(new java.awt.Color(25, 26, 25));
                jLabel33.setFont(new java.awt.Font("Cascadia Mono", 1, 24)); // NOI18N
                jLabel33.setForeground(new java.awt.Color(25, 26, 25));
                jLabel33.setText("Data Penjualan");

                javax.swing.GroupLayout dataPenjualanLayout = new javax.swing.GroupLayout(dataPenjualan);
                dataPenjualan.setLayout(dataPenjualanLayout);
                dataPenjualanLayout.setHorizontalGroup(
                        dataPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(dataPenjualanLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(dataPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(dataPenjualanLayout.createSequentialGroup()
                                                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(30, 30, 30)
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(40, 60, Short.MAX_VALUE))
                                        .addGroup(dataPenjualanLayout.createSequentialGroup()
                                                .addComponent(jLabel33)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel27)
                                                .addGap(14, 14, 14))))
                );
                dataPenjualanLayout.setVerticalGroup(
                        dataPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(dataPenjualanLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(dataPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel27))
                                .addGap(20, 20, 20)
                                .addGroup(dataPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30))
                );

                jPanel6.add(dataPenjualan, "card5");

                dataPelanggan.setBackground(new java.awt.Color(216, 233, 168));
                dataPelanggan.setForeground(new java.awt.Color(255, 255, 255));

                jTable2.setBackground(new java.awt.Color(216, 233, 168));
                jTable2.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
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
                jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jTable2MouseClicked(evt);
                        }
                });
                jScrollPane2.setViewportView(jTable2);

                jPanel33.setBackground(new java.awt.Color(223, 232, 197));

                jLabel63.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel63.setForeground(new java.awt.Color(25, 26, 25));
                jLabel63.setText("Alamat");

                jLabel66.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel66.setForeground(new java.awt.Color(25, 26, 25));
                jLabel66.setText("No HP");

                jPanel36.setBackground(new java.awt.Color(78, 159, 61));

                jLabel67.setBackground(new java.awt.Color(25, 26, 25));
                jLabel67.setFont(new java.awt.Font("Cascadia Mono", 1, 20)); // NOI18N
                jLabel67.setForeground(new java.awt.Color(255, 255, 255));
                jLabel67.setText("<html><center>Tambah/Edit Data Pelanggan</center></html>");

                jLabel68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-refresh-20.png"))); // NOI18N
                jLabel68.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel68MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
                jPanel36.setLayout(jPanel36Layout);
                jPanel36Layout.setHorizontalGroup(
                        jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel36Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel36Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel68)
                                .addContainerGap())
                );
                jPanel36Layout.setVerticalGroup(
                        jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel36Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel68)
                                .addGap(5, 5, 5)
                                .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25))
                );

                jTextField12.setBackground(new java.awt.Color(223, 232, 197));
                jTextField12.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jTextField12ActionPerformed(evt);
                        }
                });

                jTextField13.setBackground(new java.awt.Color(223, 232, 197));
                jTextField13.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jTextField13ActionPerformed(evt);
                        }
                });

                jPanel37.setBackground(new java.awt.Color(253, 253, 150));

                jLabel69.setBackground(new java.awt.Color(25, 26, 25));
                jLabel69.setFont(new java.awt.Font("Cascadia Mono", 1, 18)); // NOI18N
                jLabel69.setForeground(new java.awt.Color(25, 26, 25));
                jLabel69.setText("<html><center>Update</center></html>");
                jLabel69.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel69MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
                jPanel37.setLayout(jPanel37Layout);
                jPanel37Layout.setHorizontalGroup(
                        jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel37Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel37Layout.setVerticalGroup(
                        jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                );

                jPanel38.setBackground(new java.awt.Color(255, 105, 97));

                jLabel70.setFont(new java.awt.Font("Cascadia Mono", 1, 18)); // NOI18N
                jLabel70.setForeground(new java.awt.Color(255, 255, 255));
                jLabel70.setText("<html><center>Delete</center></html>");
                jLabel70.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel70MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
                jPanel38.setLayout(jPanel38Layout);
                jPanel38Layout.setHorizontalGroup(
                        jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel38Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel38Layout.setVerticalGroup(
                        jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel70, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                );

                jLabel37.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel37.setForeground(new java.awt.Color(25, 26, 25));
                jLabel37.setText("Nama");

                jLabel40.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel40.setForeground(new java.awt.Color(25, 26, 25));
                jLabel40.setText("ID");

                jTextField14.setBackground(new java.awt.Color(223, 232, 197));

                jTextField15.setBackground(new java.awt.Color(223, 232, 197));

                jPanel43.setBackground(new java.awt.Color(78, 159, 61));

                jLabel72.setFont(new java.awt.Font("Cascadia Mono", 1, 18)); // NOI18N
                jLabel72.setForeground(new java.awt.Color(255, 255, 255));
                jLabel72.setText("<html><center>Tambah</center></html>");
                jLabel72.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel72MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
                jPanel43.setLayout(jPanel43Layout);
                jPanel43Layout.setHorizontalGroup(
                        jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel43Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel43Layout.setVerticalGroup(
                        jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel72, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                );

                javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
                jPanel33.setLayout(jPanel33Layout);
                jPanel33Layout.setHorizontalGroup(
                        jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel33Layout.createSequentialGroup()
                                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel63)
                                                        .addComponent(jLabel66)
                                                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel37))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jTextField14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                                        .addComponent(jTextField15, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jTextField12, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jTextField13, javax.swing.GroupLayout.Alignment.TRAILING))))
                                .addGap(22, 22, 22))
                );
                jPanel33Layout.setVerticalGroup(
                        jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel33Layout.createSequentialGroup()
                                .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(15, 15, 15)
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel40))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel63)
                                        .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel66)
                                        .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(17, 17, 17)
                                .addComponent(jPanel43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15))
                );

                jLabel81.setBackground(new java.awt.Color(25, 26, 25));
                jLabel81.setFont(new java.awt.Font("Cascadia Mono", 1, 24)); // NOI18N
                jLabel81.setForeground(new java.awt.Color(25, 26, 25));
                jLabel81.setText("Data Pelanggan");

                javax.swing.GroupLayout dataPelangganLayout = new javax.swing.GroupLayout(dataPelanggan);
                dataPelanggan.setLayout(dataPelangganLayout);
                dataPelangganLayout.setHorizontalGroup(
                        dataPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(dataPelangganLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(dataPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel81)
                                        .addGroup(dataPelangganLayout.createSequentialGroup()
                                                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(30, 30, 30)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(46, Short.MAX_VALUE))
                );
                dataPelangganLayout.setVerticalGroup(
                        dataPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataPelangganLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addGroup(dataPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(30, 30, 30))
                );

                jPanel6.add(dataPelanggan, "card5");

                dataKaryawan.setBackground(new java.awt.Color(216, 233, 168));
                dataKaryawan.setForeground(new java.awt.Color(255, 255, 255));

                jLabel32.setBackground(new java.awt.Color(25, 26, 25));
                jLabel32.setFont(new java.awt.Font("Cascadia Mono", 1, 24)); // NOI18N
                jLabel32.setForeground(new java.awt.Color(25, 26, 25));
                jLabel32.setText("Data Karyawan");

                jTable1.setBackground(new java.awt.Color(216, 233, 168));
                jTable1.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
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

                jPanel39.setBackground(new java.awt.Color(223, 232, 197));

                jLabel71.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel71.setForeground(new java.awt.Color(25, 26, 25));
                jLabel71.setText("Bagian");

                jPanel40.setBackground(new java.awt.Color(78, 159, 61));

                jLabel74.setBackground(new java.awt.Color(25, 26, 25));
                jLabel74.setFont(new java.awt.Font("Cascadia Mono", 1, 20)); // NOI18N
                jLabel74.setForeground(new java.awt.Color(255, 255, 255));
                jLabel74.setText("<html><center>Update/Delete Data Karyawan</center></html>");

                jLabel75.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/icons8-refresh-20.png"))); // NOI18N
                jLabel75.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel75MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
                jPanel40.setLayout(jPanel40Layout);
                jPanel40Layout.setHorizontalGroup(
                        jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel40Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel40Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel75)
                                .addContainerGap())
                );
                jPanel40Layout.setVerticalGroup(
                        jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel40Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel75)
                                .addGap(5, 5, 5)
                                .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25))
                );

                jPanel41.setBackground(new java.awt.Color(253, 253, 150));

                jLabel76.setBackground(new java.awt.Color(25, 26, 25));
                jLabel76.setFont(new java.awt.Font("Cascadia Mono", 1, 18)); // NOI18N
                jLabel76.setForeground(new java.awt.Color(25, 26, 25));
                jLabel76.setText("<html><center>Update</center></html>");
                jLabel76.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel76MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
                jPanel41.setLayout(jPanel41Layout);
                jPanel41Layout.setHorizontalGroup(
                        jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel41Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel41Layout.setVerticalGroup(
                        jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel76, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                );

                jPanel42.setBackground(new java.awt.Color(255, 105, 97));

                jLabel77.setFont(new java.awt.Font("Cascadia Mono", 1, 18)); // NOI18N
                jLabel77.setForeground(new java.awt.Color(255, 255, 255));
                jLabel77.setText("<html><center>Delete</center></html>");
                jLabel77.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel77MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
                jPanel42.setLayout(jPanel42Layout);
                jPanel42Layout.setHorizontalGroup(
                        jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel42Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel42Layout.setVerticalGroup(
                        jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel77, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                );

                jLabel78.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel78.setForeground(new java.awt.Color(25, 26, 25));
                jLabel78.setText("Nama");

                jLabel79.setFont(new java.awt.Font("Cascadia Mono", 1, 14)); // NOI18N
                jLabel79.setForeground(new java.awt.Color(25, 26, 25));
                jLabel79.setText("ID");

                jTextField18.setBackground(new java.awt.Color(223, 232, 197));

                jTextField19.setBackground(new java.awt.Color(223, 232, 197));
                jTextField19.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jTextField19ActionPerformed(evt);
                        }
                });

                jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
                jComboBox6.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
                        public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                        }
                        public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                                jComboBox6PopupMenuWillBecomeInvisible(evt);
                        }
                        public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                        }
                });

                jPanel44.setBackground(new java.awt.Color(78, 159, 61));

                jLabel73.setFont(new java.awt.Font("Cascadia Mono", 1, 18)); // NOI18N
                jLabel73.setForeground(new java.awt.Color(255, 255, 255));
                jLabel73.setText("<html><center>Tambah</center></html>");
                jLabel73.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jLabel73MouseClicked(evt);
                        }
                });

                javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
                jPanel44.setLayout(jPanel44Layout);
                jPanel44Layout.setHorizontalGroup(
                        jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel44Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel44Layout.setVerticalGroup(
                        jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel73, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                );

                javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
                jPanel39.setLayout(jPanel39Layout);
                jPanel39Layout.setHorizontalGroup(
                        jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel39Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel39Layout.createSequentialGroup()
                                                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel71)
                                                        .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel78))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jTextField18)
                                                        .addComponent(jTextField19)
                                                        .addComponent(jComboBox6, 0, 100, Short.MAX_VALUE)))
                                        .addComponent(jPanel44, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(22, 22, 22))
                );
                jPanel39Layout.setVerticalGroup(
                        jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel39Layout.createSequentialGroup()
                                .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                .addGap(20, 20, 20)
                                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel79))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel71)
                                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(37, 37, 37)
                                .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15))
                );

                javax.swing.GroupLayout dataKaryawanLayout = new javax.swing.GroupLayout(dataKaryawan);
                dataKaryawan.setLayout(dataKaryawanLayout);
                dataKaryawanLayout.setHorizontalGroup(
                        dataKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(dataKaryawanLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(dataKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(dataKaryawanLayout.createSequentialGroup()
                                                .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(30, 30, 30)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(46, 46, 46))
                                        .addGroup(dataKaryawanLayout.createSequentialGroup()
                                                .addComponent(jLabel32)
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                );
                dataKaryawanLayout.setVerticalGroup(
                        dataKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataKaryawanLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addGroup(dataKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(jPanel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(75, 75, 75))
                );

                jPanel6.add(dataKaryawan, "card5");

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

                jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 790, 580));

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
      initThings();
    }//GEN-LAST:event_jLabel12MouseClicked

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

	jPanel6.add(dataPenjualan);
	jPanel6.repaint();
	jPanel6.revalidate();
	tabelPenjualan();
    }//GEN-LAST:event_jLabel3MouseClicked

        private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
	jPanel6.removeAll();
	jPanel6.repaint();
	jPanel6.revalidate();

	jPanel6.add(dataPelanggan);
	jPanel6.repaint();
	jPanel6.revalidate();

	tabelPelanggan();
        }//GEN-LAST:event_jLabel4MouseClicked

        private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
	jPanel6.removeAll();
	jPanel6.repaint();
	jPanel6.revalidate();

	jPanel6.add(dataKaryawan);
	jPanel6.repaint();
	jPanel6.revalidate();

	tabelKaryawan();
        }//GEN-LAST:event_jLabel5MouseClicked

        private void homeComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_homeComponentAdded

        }//GEN-LAST:event_homeComponentAdded

        private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
                // TODO add your handling code here:
		tabelBuah();
        }//GEN-LAST:event_jPanel4MouseClicked

        private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
                // TODO add your handling code here:
        }//GEN-LAST:event_jTextField1KeyPressed

        private void jLabel31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseClicked
                // TODO add your handling code here:
			jPanel6.removeAll();
			jPanel6.repaint();
			jPanel6.revalidate();

			jPanel6.add(buatTransaksi);
			jPanel6.repaint();
			jPanel6.revalidate();
        }//GEN-LAST:event_jLabel31MouseClicked

        private void jPanel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel15MouseClicked
                // TODO add your handling code here:
        }//GEN-LAST:event_jPanel15MouseClicked

        private void jComboBox3PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBox3PopupMenuWillBecomeInvisible
                // TODO add your handling code here:
		try{
			String sql = "select t.tanggal, p.nama from tb_transaksi as t join tb_pelanggan as p on t.id_pelanggan = p.id where t.no_faktur = " + jComboBox3.getSelectedItem();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while(rs.next()){
				jLabel9.setText(rs.getString(1));
				jLabel47.setText(rs.getString(2));
				tabelDetailPenjualan((String) jComboBox3.getSelectedItem());
			}
		}catch(SQLException ex){
			System.out.println("bruh");
		}

        }//GEN-LAST:event_jComboBox3PopupMenuWillBecomeInvisible

        private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
                // TODO add your handling code here:
		try {
			Integer.parseInt(jTextField2.getText());
		} catch (NumberFormatException e) {
			jTextField2.setText(null);
		}
        }//GEN-LAST:event_jTextField2KeyReleased

        private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
                // TODO add your handling code here:
		try{
			int um = Integer.valueOf(jTextField2.getText());
			System.out.println(um);
			if( um > 0 ){
				String sql = "select id from tb_buah where nama = '" + jComboBox4.getSelectedItem() + "'";
				pst = conn.prepareStatement(sql);
				rs = pst.executeQuery();
				while(rs.next()){
					idBuah = rs.getString(1);
				}
				sql = "insert into tb_detail_transaksi values(null, '" + jComboBox3.getSelectedItem() + "','" + idBuah + "','" + jTextField2.getText() + "','" + id + "')";
				pst = conn.prepareStatement(sql);
				rs = pst.executeQuery();
				tabelDetailPenjualan((String) jComboBox3.getSelectedItem());
			}
		}catch(SQLException ex){
			System.out.println("gabisa gan");
		}
        }//GEN-LAST:event_jLabel7MouseClicked

        private void jTextField2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField2MouseExited
                // TODO add your handling code here:
		if(jTextField2.getText().equals("")){
			jTextField2.setText("0");
		}
        }//GEN-LAST:event_jTextField2MouseExited

        private void jLabel50MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel50MouseClicked
		// TODO add your handling code here:
		String sql = "";
        }//GEN-LAST:event_jLabel50MouseClicked

        private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_jTextField4ActionPerformed

        private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_jTextField5ActionPerformed

        private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
                // TODO add your handling code here:
		int row = jTable4.rowAtPoint(evt.getPoint());
		String idBuah = jTable4.getValueAt(row,0).toString();
		String nama = jTable4.getValueAt(row,1).toString();
		String stok = jTable4.getValueAt(row,2).toString();
		String harga = jTable4.getValueAt(row,3).toString();
		if(jTable4.getValueAt(row, 0) == null){
			jTextField7.setText("");
		}else{
			jTextField7.setText(idBuah);
		}
		if(jTable4.getValueAt(row, 1) == null){
			jTextField3.setText("");
		}else{
			jTextField3.setText(nama);
			jTextField3.disable();
		}
		if(jTable4.getValueAt(row, 2) == null){
			jTextField4.setText("");
		}else{
			jTextField4.setText(stok);
		}
		if(jTable4.getValueAt(row, 3) == null){
			jTextField5.setText("");
		}else{
			jTextField5.setText(harga);
		}
        }//GEN-LAST:event_jTable4MouseClicked

        private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_jTextField3ActionPerformed

        private void jLabel55MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel55MouseClicked
                // TODO add your handling code here:
		String sql = "insert into tb_buah values(null, '" + jTextField3.getText() + "','"+ jTextField4.getText() + "','" + jTextField5.getText() + "')";
		System.out.println(sql);
		try{
		pst = conn.prepareStatement(sql);
		rs = pst.executeQuery();
           JOptionPane.showMessageDialog(null,"Data telah dimasukkan");
		}catch(SQLException ex){
           JOptionPane.showMessageDialog(null,"sike!!! gabisa");
		}
		tabelBuah();
		
        }//GEN-LAST:event_jLabel55MouseClicked

        private void jLabel58MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel58MouseClicked
                // TODO add your handling code here:
		clear();
        }//GEN-LAST:event_jLabel58MouseClicked

        private void jLabel56MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel56MouseClicked
                // TODO add your handling code here:
		String sql = "update tb_buah set nama = '" + jTextField3.getText() + "', stok = '"+ jTextField4.getText() + "', harga = '" + jTextField5.getText() + "' where id = '" + jTextField7.getText() + "'";
		System.out.println(sql);
		try{
		pst = conn.prepareStatement(sql);
		rs = pst.executeQuery();
           JOptionPane.showMessageDialog(null,"Data telah diupdate");
		}catch(SQLException ex){
           JOptionPane.showMessageDialog(null,"Gagal atau Masih ada data yang terrelasi");
		}
		tabelBuah();
        }//GEN-LAST:event_jLabel56MouseClicked

        private void jLabel57MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel57MouseClicked
                // TODO add your handling code here:
		String sql = "delete from tb_buah where id = '" + jTextField7.getText() + "'";
		System.out.println(sql);
		try{
		pst = conn.prepareStatement(sql);
		rs = pst.executeQuery();
           JOptionPane.showMessageDialog(null,"Data telah dihapus");
		}catch(SQLException ex){
           JOptionPane.showMessageDialog(null,"Gagal atau Masih ada data yang terrelasi");
		}
		tabelBuah();
        }//GEN-LAST:event_jLabel57MouseClicked

        private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_jTextField7ActionPerformed

        private void jLabel62MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel62MouseClicked
                // TODO add your handling code here:
		clear();
        }//GEN-LAST:event_jLabel62MouseClicked

        private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_jTextField8ActionPerformed

        private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_jTextField9ActionPerformed

        private void jLabel64MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel64MouseClicked
                // TODO add your handling code here:
		String sql = "update tb_detail_transaksi set id_buah = " + jLabel27.getText() + ", qty = '" + jTextField8.getText() + "' where id = " + jTextField11.getText();
		System.out.println(sql);
		try{
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			   JOptionPane.showMessageDialog(null,"Data telah diupdate");
			   tabelPenjualan();
		}catch(SQLException ex){
			   JOptionPane.showMessageDialog(null,"Data gagal diupdate");
		}
        }//GEN-LAST:event_jLabel64MouseClicked

        private void jLabel65MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel65MouseClicked
                // TODO add your handling code here:
		String sql = "delete from tb_detail_transaksi where id = '" + jTextField11.getText() + "'";
		try{
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			   JOptionPane.showMessageDialog(null,"Data telah dihapus");
			   tabelPenjualan();
		}catch(SQLException ex){
			   JOptionPane.showMessageDialog(null,"Data gagal dihapus");
		}
        }//GEN-LAST:event_jLabel65MouseClicked

        private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
                // TODO add your handling code here:
		int row = jTable3.rowAtPoint(evt.getPoint());
		String idTran = jTable3.getValueAt(row,0).toString();
		String pelanggan = jTable3.getValueAt(row,1).toString();
		String buah = jTable3.getValueAt(row,2).toString();
		String qty = jTable3.getValueAt(row,3).toString();
		String kasir = jTable3.getValueAt(row,4).toString();


		if(jTable3.getValueAt(row, 0) == null){
			jTextField11.setText("");
		}else{
			jTextField11.setText(idTran);
			jTextField11.disable();
		}
		if(jTable3.getValueAt(row, 1) == null){
			jTextField10.setText("");
		}else{
			jTextField10.setText(pelanggan);
			jTextField10.disable();
		}
		if(jTable3.getValueAt(row, 2) == null){
			jComboBox1.setSelectedItem(this);
		}else{
			jComboBox1.setSelectedItem(buah);
		String sql = "select id from tb_buah where nama = '" + jComboBox1.getSelectedItem() + "'";
		try{
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while(rs.next()){
				jLabel27.setText(rs.getString(1));
			}
		}catch(SQLException ex){
		}
		}
		if(jTable3.getValueAt(row, 3) == null){
			jTextField8.setText("");
		}else{
			jTextField8.setText(qty);
		}
		if(jTable3.getValueAt(row, 4) == null){
			jTextField9.setText("");
		}else{
			jTextField9.setText(kasir);
			jTextField9.disable();
		}
        }//GEN-LAST:event_jTable3MouseClicked

        private void jComboBox1PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBox1PopupMenuWillBecomeInvisible
                // TODO add your handling code here:
		// fetch id's
		String sql = "select id from tb_buah where nama = '" + jComboBox1.getSelectedItem() + "'";
		try{
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while(rs.next()){
				jLabel27.setText(rs.getString(1));
			}
		}catch(SQLException ex){
		}
        }//GEN-LAST:event_jComboBox1PopupMenuWillBecomeInvisible

        private void jLabel68MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel68MouseClicked
                // TODO add your handling code here:
		clear();
        }//GEN-LAST:event_jLabel68MouseClicked

        private void jTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField12ActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_jTextField12ActionPerformed

        private void jTextField13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField13ActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_jTextField13ActionPerformed

        private void jLabel69MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel69MouseClicked
                // TODO add your handling code here:
		// update
		String id = jTextField15.getText();
		String nama = jTextField14.getText();
		String alamat = jTextField12.getText();
		String noHp = jTextField13.getText();
			
		String sql = "update tb_pelanggan set nama = '" + nama + "', alamat = '" + alamat + "', no_hp = '"+ noHp + "' where id = " + id;
		try{
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			JOptionPane.showMessageDialog(null,"Data berhasil diupdate");
			tabelPelanggan();
		}catch(SQLException ex){
			JOptionPane.showMessageDialog(null,"Data gagal diupdate");
		}
        }//GEN-LAST:event_jLabel69MouseClicked

        private void jLabel70MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel70MouseClicked
                // TODO add your handling code here:
		// delete
		String id = jTextField15.getText();
		String nama = jTextField14.getText();
		String alamat = jTextField12.getText();
		String noHp = jTextField13.getText();
			
		String sql = "delete from tb_pelanggan where id = " + id;
		try{
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			JOptionPane.showMessageDialog(null,"Data berhasil dihapus");
			tabelPelanggan();
		}catch(SQLException ex){
			JOptionPane.showMessageDialog(null,"Data gagal dihapus");
		}
        }//GEN-LAST:event_jLabel70MouseClicked

        private void jLabel75MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel75MouseClicked
                // TODO add your handling code here:
        }//GEN-LAST:event_jLabel75MouseClicked

        private void jLabel76MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel76MouseClicked
                // TODO add your handling code here:
        }//GEN-LAST:event_jLabel76MouseClicked

        private void jLabel77MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel77MouseClicked
                // TODO add your handling code here:
        }//GEN-LAST:event_jLabel77MouseClicked

        private void jComboBox6PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBox6PopupMenuWillBecomeInvisible
                // TODO add your handling code here:
        }//GEN-LAST:event_jComboBox6PopupMenuWillBecomeInvisible

        private void jLabel72MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel72MouseClicked
                // TODO add your handling code here:
		// tambah
		String id = jTextField15.getText();
		String nama = jTextField14.getText();
		String alamat = jTextField12.getText();
		String noHp = jTextField13.getText();
		if(id.equals("")){
			String sql = "insert into tb_pelanggan values(null, '" + nama + "','" + alamat + "','" + noHp + "')";
			System.out.println(sql);
			try{
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			tabelPelanggan();
			}catch(SQLException ex){
			}
		}else{
			JOptionPane.showMessageDialog(null,"Data gagal dimasukkan");
		}
        }//GEN-LAST:event_jLabel72MouseClicked

        private void jLabel73MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel73MouseClicked
                // TODO add your handling code here:
        }//GEN-LAST:event_jLabel73MouseClicked

        private void jTextField19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField19ActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_jTextField19ActionPerformed

        private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
                // TODO add your handling code here:
		int row = jTable2.rowAtPoint(evt.getPoint());
		String idPe = jTable2.getValueAt(row,0).toString();
		String nama = jTable2.getValueAt(row,1).toString();
		String alamat = jTable2.getValueAt(row,2).toString();
		String noHp = jTable2.getValueAt(row,3).toString();


		if(jTable2.getValueAt(row, 0) == null){
			jTextField15.setText("");
		}else{
			jTextField15.setText(idPe);
			jTextField15.disable();
		}
		if(jTable2.getValueAt(row, 1) == null){
			jTextField14.setText("");
		}else{
			jTextField14.setText(nama);
		}
		if(jTable2.getValueAt(row, 2) == null){
			jTextField12.setText("");
		}else{
			jTextField12.setText(alamat);
		}
		if(jTable2.getValueAt(row, 3) == null){
			jTextField13.setText("");
		}else{
			jTextField13.setText(noHp);
		}
        }//GEN-LAST:event_jTable2MouseClicked

        private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
                // TODO add your handling code here:
        }//GEN-LAST:event_jLabel14MouseClicked

        private void jLabel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseClicked
                // TODO add your handling code here:
		    DefaultTableModel model = new DefaultTableModel();
		    model.addColumn("ID");
		    model.addColumn("Nama");
		    model.addColumn("Stok");
		    model.addColumn("Harga");
		    try{
			    String sql = "select * from tb_buah where nama like '%" + jTextField1.getText() + "%'";
			    pst=conn.prepareStatement(sql);
			    rs=pst.executeQuery();
			    while(rs.next()){
				    model.addRow(new Object[] {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
			    }
			    jTable4.setModel(model);
		    }catch(SQLException e){
		    }
        }//GEN-LAST:event_jLabel17MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        /* Create and display the form */
	System.setProperty("awt.useSystemAAFontSettings", "on");
        java.awt.EventQueue.invokeLater(() -> {
		try {
			new Dashboard().setVisible(true);
		} catch (SQLException | ClassNotFoundException ex) {
			Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
		}
	});
    }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JPanel about;
        private javax.swing.JPanel buatTransaksi;
        private javax.swing.JPanel cekStokBuah;
        private javax.swing.JPanel dataKaryawan;
        private javax.swing.JPanel dataPelanggan;
        private javax.swing.JPanel dataPenjualan;
        private javax.swing.JPanel home;
        private javax.swing.JComboBox<String> jComboBox1;
        private javax.swing.JComboBox<String> jComboBox3;
        private javax.swing.JComboBox<String> jComboBox4;
        private javax.swing.JComboBox<String> jComboBox5;
        private javax.swing.JComboBox<String> jComboBox6;
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
        private javax.swing.JLabel jLabel31;
        private javax.swing.JLabel jLabel32;
        private javax.swing.JLabel jLabel33;
        private javax.swing.JLabel jLabel34;
        private javax.swing.JLabel jLabel35;
        private javax.swing.JLabel jLabel36;
        private javax.swing.JLabel jLabel37;
        private javax.swing.JLabel jLabel38;
        private javax.swing.JLabel jLabel39;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel40;
        private javax.swing.JLabel jLabel41;
        private javax.swing.JLabel jLabel42;
        private javax.swing.JLabel jLabel43;
        private javax.swing.JLabel jLabel44;
        private javax.swing.JLabel jLabel45;
        private javax.swing.JLabel jLabel46;
        private javax.swing.JLabel jLabel47;
        private javax.swing.JLabel jLabel48;
        private javax.swing.JLabel jLabel49;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel50;
        private javax.swing.JLabel jLabel51;
        private javax.swing.JLabel jLabel52;
        private javax.swing.JLabel jLabel53;
        private javax.swing.JLabel jLabel54;
        private javax.swing.JLabel jLabel55;
        private javax.swing.JLabel jLabel56;
        private javax.swing.JLabel jLabel57;
        private javax.swing.JLabel jLabel58;
        private javax.swing.JLabel jLabel59;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JLabel jLabel60;
        private javax.swing.JLabel jLabel61;
        private javax.swing.JLabel jLabel62;
        private javax.swing.JLabel jLabel63;
        private javax.swing.JLabel jLabel64;
        private javax.swing.JLabel jLabel65;
        private javax.swing.JLabel jLabel66;
        private javax.swing.JLabel jLabel67;
        private javax.swing.JLabel jLabel68;
        private javax.swing.JLabel jLabel69;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JLabel jLabel70;
        private javax.swing.JLabel jLabel71;
        private javax.swing.JLabel jLabel72;
        private javax.swing.JLabel jLabel73;
        private javax.swing.JLabel jLabel74;
        private javax.swing.JLabel jLabel75;
        private javax.swing.JLabel jLabel76;
        private javax.swing.JLabel jLabel77;
        private javax.swing.JLabel jLabel78;
        private javax.swing.JLabel jLabel79;
        private javax.swing.JLabel jLabel8;
        private javax.swing.JLabel jLabel81;
        private javax.swing.JLabel jLabel9;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel10;
        private javax.swing.JPanel jPanel11;
        private javax.swing.JPanel jPanel12;
        private javax.swing.JPanel jPanel13;
        private javax.swing.JPanel jPanel14;
        private javax.swing.JPanel jPanel15;
        private javax.swing.JPanel jPanel16;
        private javax.swing.JPanel jPanel17;
        private javax.swing.JPanel jPanel18;
        private javax.swing.JPanel jPanel19;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel20;
        private javax.swing.JPanel jPanel21;
        private javax.swing.JPanel jPanel22;
        private javax.swing.JPanel jPanel23;
        private javax.swing.JPanel jPanel24;
        private javax.swing.JPanel jPanel25;
        private javax.swing.JPanel jPanel26;
        private javax.swing.JPanel jPanel27;
        private javax.swing.JPanel jPanel28;
        private javax.swing.JPanel jPanel29;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JPanel jPanel30;
        private javax.swing.JPanel jPanel31;
        private javax.swing.JPanel jPanel32;
        private javax.swing.JPanel jPanel33;
        private javax.swing.JPanel jPanel34;
        private javax.swing.JPanel jPanel35;
        private javax.swing.JPanel jPanel36;
        private javax.swing.JPanel jPanel37;
        private javax.swing.JPanel jPanel38;
        private javax.swing.JPanel jPanel39;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JPanel jPanel40;
        private javax.swing.JPanel jPanel41;
        private javax.swing.JPanel jPanel42;
        private javax.swing.JPanel jPanel43;
        private javax.swing.JPanel jPanel44;
        private javax.swing.JPanel jPanel45;
        private javax.swing.JPanel jPanel46;
        private javax.swing.JPanel jPanel5;
        private javax.swing.JPanel jPanel6;
        private javax.swing.JPanel jPanel7;
        private javax.swing.JPanel jPanel8;
        private javax.swing.JPanel jPanel9;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JScrollPane jScrollPane4;
        private javax.swing.JScrollPane jScrollPane5;
        private javax.swing.JSeparator jSeparator2;
        private javax.swing.JTable jTable1;
        private javax.swing.JTable jTable2;
        private javax.swing.JTable jTable3;
        private javax.swing.JTable jTable4;
        private javax.swing.JTable jTable5;
        private javax.swing.JTextField jTextField1;
        private javax.swing.JTextField jTextField10;
        private javax.swing.JTextField jTextField11;
        private javax.swing.JTextField jTextField12;
        private javax.swing.JTextField jTextField13;
        private javax.swing.JTextField jTextField14;
        private javax.swing.JTextField jTextField15;
        private javax.swing.JTextField jTextField18;
        private javax.swing.JTextField jTextField19;
        private javax.swing.JTextField jTextField2;
        private javax.swing.JTextField jTextField3;
        private javax.swing.JTextField jTextField4;
        private javax.swing.JTextField jTextField5;
        private javax.swing.JTextField jTextField7;
        private javax.swing.JTextField jTextField8;
        private javax.swing.JTextField jTextField9;
        // End of variables declaration//GEN-END:variables
}
