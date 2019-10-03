package com.ifsc.tds.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.ifsc.tds.dao.EncomendaDAO;
import com.ifsc.tds.db.Database;
import com.ifsc.tds.entity.Cliente;
import com.ifsc.tds.entity.Encomenda;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EncomendasController implements Initializable {

	private final Connection connection = Database.conectar();
	private List<Encomenda> listEncomenda;
	private ObservableList<Encomenda> obsListEncomenda;
    @FXML private TableView<Encomenda> tblEncomendas;
    @FXML private TableColumn<Cliente, Cliente> clnCliente;
    @FXML private TableColumn<Encomenda, Double> clnAltura;
    @FXML private TableColumn<Encomenda, String> clnCor;
    @FXML private TableColumn<Encomenda, Double> clnLargura;
    @FXML private TableColumn<Encomenda, String> clnFrase;
    @FXML private TableColumn<Encomenda, String> clnCorFrase;
    @FXML private TableColumn<Encomenda, String> clnPreco;
    @FXML private TableColumn<Encomenda, Date> clnEntrega;
    @FXML private Button btnExcluirEncomenda;
    
	@Override public void initialize(URL arg0, ResourceBundle arg1) {
		EncomendaDAO.setConnection(connection);
		btnExcluirEncomenda.disableProperty().bind(tblEncomendas.getSelectionModel().selectedItemProperty().isNull());
		carregarEncomendas();
	}
	
	void carregarEncomendas() {
		clnCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
		clnAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
		clnLargura.setCellValueFactory(new PropertyValueFactory<>("largura"));
		clnCor.setCellValueFactory(new PropertyValueFactory<>("corPlaca"));
		clnFrase.setCellValueFactory(new PropertyValueFactory<>("frase"));
		clnCorFrase.setCellValueFactory(new PropertyValueFactory<>("corFrase"));
		clnPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
		clnEntrega.setCellValueFactory(new PropertyValueFactory<>("entrega"));
		listEncomenda = EncomendaDAO.listar();
		obsListEncomenda = FXCollections.observableArrayList(listEncomenda);
		tblEncomendas.setItems(obsListEncomenda);
	}
  
    @FXML void adicionarEncomenda(ActionEvent event) throws IOException {
		AnchorPane a = FXMLLoader.load(getClass().getResource("/com/ifsc/tds/view/PlacaDialog.fxml"));
		Stage stage = new Stage();
		Scene scene = new Scene(a);
		stage.setTitle("Encomenda");
		stage.getIcons().add(new Image("file:resources/images/stop.png"));
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.showAndWait();
		carregarEncomendas();
    }

    @FXML void excluirEncomenda(ActionEvent event) {
    	EncomendaDAO.remover(tblEncomendas.getSelectionModel().getSelectedItem());
    	carregarEncomendas();
    }
}
