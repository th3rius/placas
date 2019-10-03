package com.ifsc.tds.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ifsc.tds.entity.Cliente;
import com.ifsc.tds.entity.Encomenda;

public class EncomendaDAO {

	private static Connection connection;

	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		EncomendaDAO.connection = connection;
	}

	public static boolean inserir(Encomenda encomenda) {
		String sql = "INSERT INTO encomenda (codCliente, altura, largura, corPlaca, frase, corFrase, entrega, sinal) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, encomenda.getCliente().getCodCliente());
			stmt.setDouble(2, encomenda.getAltura());
			stmt.setDouble(3, encomenda.getLargura());
			stmt.setString(4, encomenda.getCorPlaca());
			stmt.setString(5, encomenda.getFrase());
			stmt.setString(6, encomenda.getCorFrase());
			stmt.setDate(7, encomenda.getEntrega());
			stmt.setDouble(8, encomenda.getSinal());
			stmt.execute();
			return true;
		} catch (SQLException ex) {
			Logger.getLogger(EncomendaDAO.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}

	public static boolean remover(Encomenda encomenda) {
		String sql = "DELETE FROM encomenda WHERE codEncomenda = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, encomenda.getCodEncomenda());
			stmt.execute();
			return true;
		} catch (SQLException ex) {
			Logger.getLogger(EncomendaDAO.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}

	public static List<Encomenda> listar() {
		String sql = "SELECT * FROM encomenda";
		List<Encomenda> retorno = new ArrayList<>();
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet resultado = stmt.executeQuery();
			while (resultado.next()) {
				Encomenda encomenda = new Encomenda();
				Cliente cliente = new Cliente();
				encomenda.setCodEncomenda(resultado.getInt("codEncomenda"));
				encomenda.setAltura(resultado.getDouble("altura"));
				encomenda.setLargura(resultado.getDouble("largura"));
				encomenda.setCorPlaca(resultado.getString("corPlaca"));
				encomenda.setFrase(resultado.getString("frase"));
				encomenda.setCorFrase(resultado.getString("corFrase"));
				encomenda.setEntrega(resultado.getDate("entrega"));

				cliente.setCodCliente(resultado.getInt("codCliente"));
				ClienteDAO.setConnection(connection);
				cliente = ClienteDAO.buscar(cliente);
				encomenda.setCliente(cliente);

				double area = (encomenda.getAltura() * encomenda.getLargura());
				double preco = (area * 147.3);

				if (encomenda.getFrase() != null) {
					preco += encomenda.getFrase().length() * 0.32;
				}

				encomenda.setPreco(String.format("R$%.2f", preco));
				retorno.add(encomenda);
			}
		} catch (SQLException ex) {
			Logger.getLogger(EncomendaDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
		return retorno;
	}

	public static Date calcularEntrega() {
		Date entrega = new Date(Calendar.getInstance().getTimeInMillis());
		int entregasPorDia = 0;
		String sql = "SELECT Count(*) FROM encomenda WHERE entrega = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setDate(1, entrega);
			ResultSet resultado = stmt.executeQuery();
			if (resultado.next()) {
				entregasPorDia = resultado.getInt(1);
			}
		} catch (SQLException ex) {
			Logger.getLogger(EncomendaDAO.class.getName()).log(Level.SEVERE, null, ex);
		}

		while (entregasPorDia >= 6) {
			sql = "SELECT DATE_ADD(?, INTERVAL 1 DAY) AS ENTREGA";
			try {
				PreparedStatement stmt = connection.prepareStatement(sql);
				stmt.setDate(1, entrega);
				ResultSet resultado = stmt.executeQuery();
				if (resultado.next()) {
					entrega = resultado.getDate("ENTREGA");
				}
			} catch (SQLException ex) {
				Logger.getLogger(EncomendaDAO.class.getName()).log(Level.SEVERE, null, ex);
			}

			sql = "SELECT Count(*) FROM encomenda WHERE entrega = ?";
			try {
				PreparedStatement stmt = connection.prepareStatement(sql);
				stmt.setDate(1, entrega);
				ResultSet resultado = stmt.executeQuery();
				if (resultado.next()) {
					entregasPorDia = resultado.getInt("count(*)");
				}
			} catch (SQLException ex) {
				Logger.getLogger(EncomendaDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return entrega;
	}
}
