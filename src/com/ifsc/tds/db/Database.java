package com.ifsc.tds.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
	private static Connection connection;
	private static String usuario = "root";
	private static String senha = "";

	public static Connection conectar() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/placas", usuario, senha);
			return connection;
		} catch (SQLException | ClassNotFoundException ex) {
			Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public static void desconectar(Connection connection) {
		try {
			connection.close();
		} catch (SQLException ex) {
			Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}