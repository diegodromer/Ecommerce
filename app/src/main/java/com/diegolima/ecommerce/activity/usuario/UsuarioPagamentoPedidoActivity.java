package com.diegolima.ecommerce.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.diegolima.ecommerce.DAO.ItemDAO;
import com.diegolima.ecommerce.DAO.ItemPedidoDAO;
import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.diegolima.ecommerce.model.Endereco;
import com.diegolima.ecommerce.model.ItemPedido;
import com.diegolima.ecommerce.model.Loja;
import com.diegolima.ecommerce.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsuarioPagamentoPedidoActivity extends AppCompatActivity {

	private Endereco enderecoSelecionado;
	private Usuario usuario;
	private Loja loja;

	private ItemPedidoDAO itemPedidoDAO;
	private ItemDAO itemDAO;
	private List<ItemPedido> itemPedidoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usuario_pagamento_pedido);

		recuperaDados();
	}

	private void recuperaDados(){
		itemPedidoDAO = new ItemPedidoDAO(this);
		itemDAO = new ItemDAO(this);
		itemPedidoList = itemPedidoDAO.getList();

		recuperaUsuario();

		recuperaLoja();

		getExtra();
	}

	private void getExtra(){
		Bundle bundle = getIntent().getExtras();
		if (bundle != null){
			enderecoSelecionado = (Endereco) bundle.getSerializable("enderecoSelecionado");
		}
	}

	private void recuperaLoja(){
		DatabaseReference lojaRef = FirebaseHelper.getDatabaseReference()
				.child("loja");
		lojaRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				loja = snapshot.getValue(Loja.class);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	private void recuperaUsuario() {
		DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
				.child("usuarios")
				.child(FirebaseHelper.getIdFirebase());
		usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				usuario = snapshot.getValue(Usuario.class);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

}