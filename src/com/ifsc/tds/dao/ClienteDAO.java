package com.ifsc.tds.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ifsc.tds.entity.Cliente;

public class ClienteDAO {
	
	private static Connection connection;

	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		ClienteDAO.connection = connection;
	}
	
	public static boolean inserir(Cliente cliente) {
		String sql = "INSERT INTO cliente (nome, telefone) VALUES (?, ?)";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, cliente.getNome());
			stmt.setString(2, cliente.getTelefone());
			stmt.execute();
			return true;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}
	
	public static Cliente buscar(Cliente cliente) {
		String sql = "SELECT * FROM cliente WHERE codCliente = ?";
		Cliente retorno = new Cliente();
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, cliente.getCodCliente());
			ResultSet resultado = stmt.executeQuery();
			if (resultado.next()) {
				cliente.setCodCliente(resultado.getInt("codCliente"));
				cliente.setNome(resultado.getString("nome"));
				cliente.setTelefone(resultado.getString("telefone"));
				retorno = cliente;
			}
		} catch (SQLException ex) {
			Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
		return retorno;
	}

	public static boolean alterar(Cliente cliente) {
		String sql = "UPDATE cliente SET nome = ?, telefone = ? WHERE codCliente = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, cliente.getNome());
			stmt.setString(2, cliente.getTelefone());
			stmt.setInt(3, cliente.getCodCliente());
			stmt.execute();
			return true;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}

	public static boolean remover(Cliente cliente) {
		String sql = "DELETE FROM cliente WHERE codCliente = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, cliente.getCodCliente());
			stmt.execute();
			return true;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}
	
	public static List<Cliente> listar() {
		String sql = "SELECT * FROM cliente";
		List<Cliente> retorno = new ArrayList<>();
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet resultado = stmt.executeQuery();
			while (resultado.next()) {
				Cliente cliente = new Cliente();
				cliente.setCodCliente(resultado.getInt("codCliente"));
				cliente.setNome(resultado.getString("nome"));
				cliente.setTelefone(resultado.getString("telefone"));
				retorno.add(cliente);
			}
		} catch (SQLException ex) {
			Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
		return retorno;
	}

}
