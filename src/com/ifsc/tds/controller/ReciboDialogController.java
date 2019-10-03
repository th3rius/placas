package com.ifsc.tds.controller;

import com.ifsc.tds.entity.Encomenda;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ReciboDialogController  {
	
    @FXML private TextArea txtRecibo;
    
	public void setEncomenda(Encomenda encomenda) {
		txtRecibo.setText("RECIBO"
				+ "\n\nAltura: " + encomenda.getAltura()
				+ "\nLargura: " + encomenda.getLargura()
				+ "\nCor da Placa: " + encomenda.getCorPlaca()
				+ "\nFrase: " + encomenda.getFrase()
				+ "\nCor da Frase: " + encomenda.getCorFrase()
				+ "\n\n----------------------------------------------------"
				+ "\n\nNome do Cliente: " + encomenda.getCliente()
				+ "\tTelefone: " + encomenda.getCliente().getTelefone()
				+ "\nPreço: " + encomenda.getPreco()
				+ "\tPrevisao de Entrega: " + encomenda.getEntrega()
				+ "\nValor do Sinal: " + String.format("R$%.2f", encomenda.getSinal())
		);
	}
}
