package com.ifsc.tds.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

import com.ifsc.tds.dao.ClienteDAO;
import com.ifsc.tds.db.Database;
import com.ifsc.tds.entity.Cliente;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class ClientesController implements Initializable {
	
	private final Connection connection = Database.conectar();
	private List<Cliente> listCliente;
	private ObservableList<Cliente> obsListCliente;
    @FXML private TableView<Cliente> tblClientes;
    @FXML private TableColumn<Cliente, String> clnNome;
    @FXML private TableColumn<Cliente, String> clnTelefone;
    @FXML private Button btnAdicionarCliente;
    @FXML private Button btnExcluirCliente;
    @FXML private TextField txtNome;
    @FXML private TextField txtTelefone;
    
	@Override public void initialize(URL location, ResourceBundle resources) {
		ClienteDAO.setConnection(connection);
		carregarClientes();
		configurarBindings();
	}
	
	void carregarClientes() {
		clnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		clnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		listCliente = ClienteDAO.listar();
		obsListCliente = FXCollections.observableArrayList(listCliente);
		tblClientes.setItems(obsListCliente);
		clnNome.setCellFactory(TextFieldTableCell.forTableColumn());
		clnTelefone.setCellFactory(TextFieldTableCell.forTableColumn());
	}
	
	void configurarBindings() {
		btnAdicionarCliente.disableProperty().bind(txtNome.textProperty().isEmpty().or(txtTelefone.textProperty().isEmpty()));
		btnExcluirCliente.disableProperty().bind(tblClientes.getSelectionModel().selectedItemProperty().isNull());
	}

    @FXML void adicionarCliente(ActionEvent event) {
    	Cliente cliente = new Cliente();
    	cliente.setNome(txtNome.getText());
    	cliente.setTelefone(txtTelefone.getText());
    	ClienteDAO.inserir(cliente);
    	carregarClientes();
    }

    @FXML void editarNome(CellEditEvent<Cliente, String> event) {
    	Cliente cliente = tblClientes.getSelectionModel().getSelectedItem();
    	cliente.setNome(event.getNewValue().toString());
    	ClienteDAO.alterar(cliente);
    	carregarClientes();
    }
    
    @FXML void editarTelefone(CellEditEvent<Cliente, String> event) {
    	Cliente cliente = tblClientes.getSelectionModel().getSelectedItem();
    	cliente.setTelefone(event.getNewValue().toString());
    	ClienteDAO.alterar(cliente);
    	carregarClientes();
    }

    @FXML void excluirCliente(ActionEvent event) {
    	ClienteDAO.remover(tblClientes.getSelectionModel().getSelectedItem());
    	carregarClientes();
    }
}
