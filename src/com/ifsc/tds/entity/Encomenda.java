package com.ifsc.tds.entity;

import java.sql.Date;

public class Encomenda {

	private Integer codEncomenda;
	private Double altura;
	private Double largura;
	private String frase;
	private String corPlaca;
	private String corFrase;
	private String preco;
	private Date entrega;
	private Cliente cliente;
	private Double sinal;

	public Integer getCodEncomenda() {
		return codEncomenda;
	}

	public void setCodEncomenda(Integer codEncomenda) {
		this.codEncomenda = codEncomenda;
	}

	public Double getAltura() {
		return altura;
	}

	public void setAltura(Double altura) {
		this.altura = altura;
	}

	public Double getLargura() {
		return largura;
	}

	public void setLargura(Double largura) {
		this.largura = largura;
	}

	public String getFrase() {
		return frase;
	}

	public void setFrase(String frase) {
		this.frase = frase;
	}

	public String getCorPlaca() {
		return corPlaca;
	}

	public void setCorPlaca(String corPlaca) {
		this.corPlaca = corPlaca;
	}

	public String getCorFrase() {
		return corFrase;
	}

	public void setCorFrase(String corFrase) {
		this.corFrase = corFrase;
	}

	public String getPreco() {
		return preco;
	}

	public void setPreco(String preco) {
		this.preco = preco;
	}

	public Date getEntrega() {
		return entrega;
	}

	public void setEntrega(Date entrega) {
		this.entrega = entrega;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Double getSinal() {
		return sinal;
	}

	public void setSinal(Double sinal) {
		this.sinal = sinal;
	}
}
