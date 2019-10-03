package com.ifsc.tds.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import com.ifsc.tds.application.Main;
import com.ifsc.tds.dao.AdminDAO;
import com.ifsc.tds.db.Database;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	
	private final Connection connection = Database.conectar();
    @FXML private AnchorPane ap;
    @FXML private TextField txtNome;
    @FXML private PasswordField txtSenha;
    @FXML private Button btnEntrar;
    
	@Override public void initialize(URL location, ResourceBundle resources) {
		AdminDAO.setConnection(connection);
		btnEntrar.disableProperty().bind(txtNome.textProperty().isEmpty().or(
				txtSenha.textProperty().isEmpty()));
	}

    @FXML void criarConta(ActionEvent event) throws IOException {
       	FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ifsc/tds/view/CadastroDialog.fxml"));
    	AnchorPane a = loader.load();
       	CadastroDialogController controller = loader.getController();
       	Stage st = (Stage) ap.getScene().getWindow();
       	controller.setStage(st);
    	Stage stage = new Stage();
		Scene scene = new Scene(a);
		stage.setTitle("Placas");
		stage.getIcons().add(new Image("file:resources/images/stop.png"));
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.showAndWait();
    }

    @FXML void entrar(ActionEvent event) throws IOException {
    	if (AdminDAO.validacao(txtNome.getText(), txtSenha.getText())) {
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
    		Stage st = (Stage) ap.getScene().getWindow();
    		st.close();
    		stage.show();
    	} else {
    		Alert alert = new Alert(Alert.AlertType.WARNING);
    		alert.setHeaderText("Usuário e/ou senha inválidos.");
    		alert.showAndWait();
    	}
    }
}
