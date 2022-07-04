package com.diegolima.ecommerce.model;

import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Loja {
	private String id;
	private String nome;
	private String email;
	private String senha;
	private double pedidoMinimo;
	private double freteGratis;
	private String publicKey;
	private String urlLogo;
	private String accessToken;
	private String CNPJ;
	private int parcelas;

	public Loja() {
	}

	public void salvar() {
		DatabaseReference lojaRef = FirebaseHelper.getDatabaseReference()
				.child("loja");
		lojaRef.setValue(this);
	}

	public double getPedidoMinimo() {
		return pedidoMinimo;
	}

	public void setPedidoMinimo(double pedidoMinimo) {
		this.pedidoMinimo = pedidoMinimo;
	}

	public double getFreteGratis() {
		return freteGratis;
	}

	public void setFreteGratis(double freteGratis) {
		this.freteGratis = freteGratis;
	}

	public String getUrlLogo() {
		return urlLogo;
	}

	public void setUrlLogo(String urlLogo) {
		this.urlLogo = urlLogo;
	}

	public String getCNPJ() {
		return CNPJ;
	}

	public void setCNPJ(String CNPJ) {
		this.CNPJ = CNPJ;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Exclude
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getParcelas() {
		return parcelas;
	}

	public void setParcelas(int parcelas) {
		this.parcelas = parcelas;
	}
}
