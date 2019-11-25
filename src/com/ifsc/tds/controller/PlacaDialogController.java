package com.ifsc.tds.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.ifsc.tds.dao.ClienteDAO;
import com.ifsc.tds.dao.EncomendaDAO;
import com.ifsc.tds.db.Database;
import com.ifsc.tds.entity.Cliente;
import com.ifsc.tds.entity.Encomenda;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlacaDialogController implements Initializable {

	private final Connection connection = Database.conectar();
	private List<Cliente> listCliente;
	private ObservableList<Cliente> obsListCliente;
	private Date entrega;
    @FXML private AnchorPane ap;
    @FXML private TextField txtAltura;
    @FXML private TextField txtLargura;
    @FXML private TextField txtFrase;
    @FXML private ChoiceBox<String> cbxCorPlaca;
    @FXML private ChoiceBox<String> cbxCorFrase;
    @FXML private Rectangle placa;
    @FXML private Label lblFrasePlaca;
    @FXML private Label lblPreço;
    @FXML private TextField txtSinal;
    @FXML private ChoiceBox<Cliente> cbxCliente;
    @FXML private Label lblEntrega;
    @FXML private Button btnConfirmar;
    
	@Override public void initialize(URL location, ResourceBundle resources) {
		EncomendaDAO.setConnection(connection);
		ClienteDAO.setConnection(connection);
		entrega = EncomendaDAO.calcularEntrega();
		carregarItens();
		configurarBindings();
	}
	
	void carregarItens() {
		cbxCorPlaca.getItems().addAll("Branca", "Cinza");
		cbxCorPlaca.setValue("Branca");
		cbxCorFrase.getItems().addAll("Azul", "Vermelho", "Amarelo", "Preto", "Verde");
		cbxCorFrase.setValue("Preto");
		listCliente = ClienteDAO.listar();
		obsListCliente = FXCollections.observableArrayList(listCliente);
		cbxCliente.setItems(obsListCliente);
		cbxCliente.setValue(obsListCliente.iterator().next());
		lblEntrega.setText("Entrega: " + entrega);
	}
	
	void configurarBindings() {
		lblFrasePlaca.textProperty().bind(txtFrase.textProperty());;
		lblFrasePlaca.textProperty().addListener((ObservableValue<? extends String> obs, String ov, String nv) -> {
	    	calcularPreco();
	    });
		cbxCorPlaca.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> obs, String ov, String nv) -> {
	    	corPlaca(nv);
	    });
		cbxCorFrase.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> obs, String ov, String nv) -> {
	    	corFrase(nv);
	    });
		txtAltura.textProperty().addListener((ObservableValue<? extends String> obs, String ov, String nv) -> {
	    	calcularPreco();
	    });
		txtLargura.textProperty().addListener((ObservableValue<? extends String> obs, String ov, String nv) -> {
	    	calcularPreco();
	    });
		btnConfirmar.disableProperty().bind(txtAltura.textProperty().isEmpty().or(
				txtLargura.textProperty().isEmpty().or(
						txtSinal.textProperty().isEmpty().or(
								txtFrase.textProperty().isEmpty()))));
	}
    
    void corPlaca(String cor) {
    	switch(cor) {
    	case "Branca":
    		placa.setFill(Color.WHITE);
    		break;
    	case "Cinza":
    		placa.setFill(Color.GRAY);
    	}
    }
    
    void corFrase(String cor) {
    	switch(cor) {
    	case "Azul":
    		lblFrasePlaca.setTextFill(Color.DEEPSKYBLUE);
    		break;
    	case "Vermelho":
    		lblFrasePlaca.setTextFill(Color.RED);
    		break;
    	case "Amarelo":
    		lblFrasePlaca.setTextFill(Color.YELLOW);
    		break;
    	case "Preto":
    		lblFrasePlaca.setTextFill(Color.BLACK);
    		break;
    	case "Verde":
    		lblFrasePlaca.setTextFill(Color.GREEN);
    		break;
    	}
    }
    
    double calcularPreco() {
    	double area = (Double.parseDouble(txtAltura.getText()) * Double.parseDouble(txtLargura.getText()));
    	double preco = (area * 147.3) + (txtFrase.getText().length() * 0.32);
    	lblPreço.setText("Preço: " + String.format("R$%.2f", preco));
    	return preco;
    }
    
    void imprimirRecibo(Encomenda encomenda) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ifsc/tds/view/ReciboDialog.fxml"));
    	AnchorPane a = loader.load();
    	ReciboDialogController controller = loader.getController();
    	controller.setEncomenda(encomenda);
		Stage stage = new Stage();
		Scene scene = new Scene(a);
		stage.setTitle("Encomenda");
		stage.getIcons().add(new Image("file:resources/images/stop.png"));
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.showAndWait();
    }
    
    @FXML void confirmar(ActionEvent event) throws IOException {
     	if(Double.parseDouble(txtSinal.getText()) < calcularPreco()/2) {
    		Alert alert = new Alert(Alert.AlertType.WARNING);
    		alert.setHeaderText("O valor do sinal precisa ser no mínimo 50% do preço.");
    		alert.showAndWait();
    		return;
    	}
     	if(Double.parseDouble(txtSinal.getText()) > calcularPreco()) {
    		Alert alert = new Alert(Alert.AlertType.WARNING);
    		alert.setHeaderText("O valor do sinal excede o preço da placa.");
    		alert.showAndWait();
    		return;
    	}
    	
    	Encomenda encomenda = new Encomenda();
    	encomenda.setCliente(cbxCliente.getValue());
    	encomenda.setAltura(Double.parseDouble(txtAltura.getText()));
    	encomenda.setLargura(Double.parseDouble(txtLargura.getText()));
    	encomenda.setCorPlaca(cbxCorPlaca.getValue());
    	encomenda.setFrase(txtFrase.getText());
        encomenda.setCorFrase(cbxCorFrase.getValue());
    	encomenda.setEntrega(entrega);
    	encomenda.setPreco(String.format("R$%.2f", calcularPreco()));
    	encomenda.setSinal(Double.parseDouble(txtSinal.getText()));
    	EncomendaDAO.inserir(encomenda);
    	Stage st = (Stage) ap.getScene().getWindow();
    	st.close();
    	imprimirRecibo(encomenda);
    }
}
