/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
import java.sql.*;
/**
 *
 * @author Raihan
 */
public class Koneksi {
	private static Connection config;
	public static Connection getKoneksi() throws SQLException, ClassNotFoundException {
		try{
		    Class.forName("org.mariadb.jdbc.Driver");
		    config = DriverManager.getConnection("jdbc:mysql://localhost/db_tokobuah", "root", "");
		    System.out.println("sukses gan");
		}catch(ClassNotFoundException | SQLException e){
			System.out.println("Koneksi gagal");
		}
		return config;
  }

}