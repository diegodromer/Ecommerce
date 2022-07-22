package com.diegolima.ecommerce.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.adapter.UsuarioPagamentoAdapter;
import com.diegolima.ecommerce.databinding.ActivityUsuarioSelecionaPagamentoBinding;
import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.diegolima.ecommerce.model.FormaPagamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioSelecionaPagamentoActivity extends AppCompatActivity implements UsuarioPagamentoAdapter.OnClick {

	private ActivityUsuarioSelecionaPagamentoBinding binding;

	private UsuarioPagamentoAdapter usuarioPagamentoAdapter;

	private List<FormaPagamento> formaPagamentoList = new ArrayList<>();

	private FormaPagamento formaPagamento = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityUsuarioSelecionaPagamentoBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		iniciaComponente();
		configClicks();
		configRv();
		recuperaFormaPagamento();
	}

	private void configClicks(){
		binding.include.include.ibVoltar.setOnClickListener(v -> finish());
		binding.btnContinuar.setOnClickListener(v -> {
			Intent intent = new Intent(this, UsuarioResumoPedidoActivity.class);
			intent.putExtra("pagamentoSelecionado", formaPagamento);
			startActivity(intent);
		});
	}

	private void recuperaFormaPagamento(){
		DatabaseReference pagamentoRef = FirebaseHelper.getDatabaseReference()
				.child("formapagamento");
		pagamentoRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if (snapshot.exists()){
					formaPagamentoList.clear();
					for (DataSnapshot ds : snapshot.getChildren()){
						FormaPagamento formaPagamento = ds.getValue(FormaPagamento.class);
						formaPagamentoList.add(formaPagamento);
					}
					binding.textInfo.setText("");
				}else{
					binding.textInfo.setText("Nenhuma forma de pagamento criada.");
				}

				binding.progressBar.setVisibility(View.GONE);
				Collections.reverse(formaPagamentoList);
				usuarioPagamentoAdapter.notifyDataSetChanged();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
			}
		});
	}

	private void configRv(){
		binding.rvPagamentos.setLayoutManager(new LinearLayoutManager(this));
		binding.rvPagamentos.setHasFixedSize(true);
		usuarioPagamentoAdapter = new UsuarioPagamentoAdapter(formaPagamentoList, this);
		binding.rvPagamentos.setAdapter(usuarioPagamentoAdapter);
	}

	private void iniciaComponente(){
		binding.include.textTitulo.setText("Forma de pagamento");
	}

	@Override
	public void onClickListener(FormaPagamento pagamento) {
		this.formaPagamento = pagamento;

	}
}