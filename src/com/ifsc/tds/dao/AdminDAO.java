package com.ifsc.tds.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ifsc.tds.entity.Admin;

public class AdminDAO {
	private static Connection connection;

	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		AdminDAO.connection = connection;
	}
	
	public static boolean inserir(Admin admin) {
		String sql = "INSERT INTO admin (nome, senha) VALUES (?, ?)";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, admin.getNome());
			stmt.setString(2, admin.getSenha());
			stmt.execute();
			return true;
		} catch (SQLException ex) {
			Logger.getLogger(AdminDAO.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}
	
	public static boolean validacao(String nome, String senha) {
		String sql = "SELECT * FROM admin WHERE nome = ? AND senha = ?";
		Boolean acesso = false;
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, nome);
			stmt.setString(2, senha);
			ResultSet resultado = stmt.executeQuery();
			if (resultado.next()) {
				acesso = true;
			}
		} catch (SQLException ex) {
			Logger.getLogger(AdminDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
		return acesso;
	}
}
