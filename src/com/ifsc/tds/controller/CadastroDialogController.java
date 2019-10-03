package com.ifsc.tds.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import com.ifsc.tds.application.Main;
import com.ifsc.tds.dao.AdminDAO;
import com.ifsc.tds.db.Database;
import com.ifsc.tds.entity.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CadastroDialogController implements Initializable {
	
	private final Connection connection = Database.conectar();
	private Stage prevStage = new Stage();
    @FXML private AnchorPane ap;
    @FXML private TextField txtNome;
    @FXML private PasswordField txtSenha;
    @FXML private Button btnCriarConta;
    
	@Override public void initialize(URL arg0, ResourceBundle arg1) {
		AdminDAO.setConnection(connection);
		btnCriarConta.disableProperty().bind(txtNome.textProperty().isEmpty().or
				(txtSenha.textProperty().isEmpty()));
	}

    @FXML void criarConta(ActionEvent event) throws IOException {
    	prevStage.close();
    	Admin admin = new Admin();
    	admin.setNome(txtNome.getText());
    	admin.setSenha(txtSenha.getText());
    	AdminDAO.inserir(admin);
    	Stage st = (Stage) ap.getScene().getWindow();
    	st.close();
    	AnchorPane a = FXMLLoader.load(getClass().getResource("/com/ifsc/tds/view/RootSistema.fxml"));
    	Stage stage = new Stage();
		Scene scene = new Scene(a);
		stage.setTitle("Placas");
		stage.getIcons().add(new Image("file:resources/images/stop.png"));
		stage.setOnCloseRequest(e -> {
			if (Main.onCloseQuery()) {
				System.exit(0);
			} else {
				e.consume();
			}
		});
		stage.setScene(scene);
		stage.show();
    }
    
    void setStage(Stage prevStage) {
    	this.prevStage = prevStage;
    }
}
