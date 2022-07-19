package com.diegolima.ecommerce.activity.loja;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.adapter.LojaPagamentoAdapter;
import com.diegolima.ecommerce.databinding.ActivityLojaPagamentosBinding;
import com.diegolima.ecommerce.databinding.DialogDeleteBinding;
import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.diegolima.ecommerce.model.Endereco;
import com.diegolima.ecommerce.model.FormaPagamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LojaPagamentosActivity extends AppCompatActivity implements LojaPagamentoAdapter.OnClick {

	private ActivityLojaPagamentosBinding binding;

	private LojaPagamentoAdapter lojaPagamentoAdapter;

	private final List<FormaPagamento> formaPagamentoList = new ArrayList<>();

	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityLojaPagamentosBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		iniciaComponentes();
		configClicks();
		configRv();
		recuperaFormaPagamento();
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
					binding.textInfo.setText("Nenhuma forma de pagamento cadastrada.");
				}

				binding.progressBar.setVisibility(View.GONE);
				Collections.reverse(formaPagamentoList);
				lojaPagamentoAdapter.notifyDataSetChanged();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
			}
		});
	}

	private void showDialogDelete(FormaPagamento formaPagamento) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog2);

		DialogDeleteBinding deleteBinding = DialogDeleteBinding
				.inflate(LayoutInflater.from(this));

		deleteBinding.btnFechar.setOnClickListener(v -> {
			lojaPagamentoAdapter.notifyDataSetChanged();
			dialog.dismiss();
		});

		deleteBinding.textTitulo.setText("Deseja remover este endereço?");

		deleteBinding.btnSim.setOnClickListener(v -> {
			formaPagamentoList.remove(formaPagamento);

			if (formaPagamentoList.isEmpty()){
				binding.textInfo.setText("Nenhum forma de pagamento cadastrada");
			}else{
				binding.textInfo.setText("");
			}

			formaPagamento.remover();

			lojaPagamentoAdapter.notifyDataSetChanged();

			dialog.dismiss();
		});

		builder.setView(deleteBinding.getRoot());

		dialog = builder.create();
		dialog.show();
	}

	private void configRv(){
		binding.rvPagamentos.setLayoutManager(new LinearLayoutManager(this));
		binding.rvPagamentos.setHasFixedSize(true);
		lojaPagamentoAdapter = new LojaPagamentoAdapter(formaPagamentoList, this, this);
		binding.rvPagamentos.setAdapter(lojaPagamentoAdapter);

		binding.rvPagamentos.setListener(new SwipeLeftRightCallback.Listener() {
			@Override
			public void onSwipedLeft(int position) {
			}

			@Override
			public void onSwipedRight(int position) {
				showDialogDelete(formaPagamentoList.get(position));
			}
		});

		binding.rvPagamentos.setLeftBg(R.color.color_laranja);
	}

	private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			result -> {
				if (result.getResultCode() == RESULT_OK) {
					FormaPagamento formaPagamento = (FormaPagamento) result.getData().getParcelableExtra("novoPagamento");
					formaPagamentoList.add(formaPagamento);
					lojaPagamentoAdapter.notifyItemInserted(formaPagamentoList.size());
					binding.textInfo.setText("");
				}
			}
	);


	private void configClicks(){
		binding.include.btnAdd.setOnClickListener(v -> resultLauncher.launch(new Intent(this, LojaFormPagamentoActivity.class)));
	}

	private void iniciaComponentes(){
		binding.include.textTitulo.setText("Formas de pagamento");
		binding.include.include.ibVoltar.setOnClickListener(v -> finish());
	}

	@Override
	public void onClickListener(FormaPagamento formaPagamento) {
		Intent intent = new Intent(this, LojaFormPagamentoActivity.class);
		intent.putExtra("formaPagamentoSelecionada", formaPagamento);
		startActivity(intent);
	}
}