package com.diegolima.ecommerce.model;

import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.logging.FileHandler;

public class Usuario {
	private String id;
	private String nome;
	private String email;
	private String telefone;
	private String senha;

	public Usuario() {
	}

	public void salvar(){
		DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
				.child("usuarios")
				.child(this.getId());
		usuarioRef.setValue(this);
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
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
}
