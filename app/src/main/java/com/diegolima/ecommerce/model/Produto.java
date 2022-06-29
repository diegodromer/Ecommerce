package com.diegolima.ecommerce.model;

import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Produto {
	private String id; // Id Firebase
	private int idLocal; // Id Local
	private String titulo;
	private String descricao;
	private double valorAntigo;
	private double valorAtual;
	private boolean rascunho = false;
	private List<String> idsCategorias = new ArrayList<>();
	private List<ImagemUpload> urlsImagens = new ArrayList<>();

	public Produto() {
		DatabaseReference produtoRef = FirebaseHelper.getDatabaseReference();
		this.setId(produtoRef.push().getKey());
	}

	public void salvar(boolean novoProduto){
		DatabaseReference produtoRef = FirebaseHelper.getDatabaseReference()
				.child("produtos")
				.child(this.getId());
		produtoRef.setValue(this);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Exclude
	public int getIdLocal() {
		return idLocal;
	}

	public void setIdLocal(int idLocal) {
		this.idLocal = idLocal;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getValorAntigo() {
		return valorAntigo;
	}

	public void setValorAntigo(double valorAntigo) {
		this.valorAntigo = valorAntigo;
	}

	public double getValorAtual() {
		return valorAtual;
	}

	public void setValorAtual(double valorAtual) {
		this.valorAtual = valorAtual;
	}

	public boolean isRascunho() {
		return rascunho;
	}

	public void setRascunho(boolean rascunho) {
		this.rascunho = rascunho;
	}

	public List<String> getIdsCategorias() {
		return idsCategorias;
	}

	public void setIdsCategorias(List<String> idsCategorias) {
		this.idsCategorias = idsCategorias;
	}

	public List<ImagemUpload> getUrlsImagens() {
		return urlsImagens;
	}

	public void setUrlsImagens(List<ImagemUpload> urlsImagens) {
		this.urlsImagens = urlsImagens;
	}
}
