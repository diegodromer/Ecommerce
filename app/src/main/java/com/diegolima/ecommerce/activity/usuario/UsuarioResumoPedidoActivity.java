package com.diegolima.ecommerce.activity.usuario;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.diegolima.ecommerce.DAO.ItemPedidoDAO;
import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.databinding.ActivityUsuarioResumoPedidoBinding;
import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.diegolima.ecommerce.model.Endereco;
import com.diegolima.ecommerce.util.GetMask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioResumoPedidoActivity extends AppCompatActivity {

	private ActivityUsuarioResumoPedidoBinding binding;

	private final List<Endereco> enderecoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityUsuarioResumoPedidoBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		recuperaEnderecos();
		configClick();
	}

	private void configClick(){
		binding.btnAlterarEndereco.setOnClickListener(v -> {
			resultLauncher.launch(new Intent(this, UsuarioSelecionaEnderecoActivity.class));
		});
	}

	private void configDados() {
		ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO(this);

		binding.include.textTitulo.setText("Resumo pedido");
		binding.include.include.ibVoltar.setOnClickListener(v -> finish());

		if (!enderecoList.isEmpty()) {
			Endereco endereco = enderecoList.get(0);

			StringBuilder enderecoCompleto = new StringBuilder();
			enderecoCompleto.append(endereco.getLogradouro())
					.append(", ")
					.append(endereco.getNumero())
					.append("\n")
					.append(endereco.getBairro())
					.append(", ")
					.append(endereco.getLocalidade())
					.append("/")
					.append(endereco.getUf())
					.append("\n")
					.append("CEP: ")
					.append(endereco.getCep());

			binding.textEnderecoEntrega.setText(enderecoCompleto);
			binding.btnAlterarEndereco.setText("Alterar endereço de entrega");
		} else {
			binding.textEnderecoEntrega.setText("Nenhum endereço cadastrado");
			binding.btnAlterarEndereco.setText("Cadastrar endereço");
		}
		binding.textValorTotal.setText(getString(R.string.valor, GetMask.getValor(itemPedidoDAO.getTotalPedido())));
		binding.textValor.setText(getString(R.string.valor_total_carrinho, GetMask.getValor(itemPedidoDAO.getTotalPedido())));
	}

	private void recuperaEnderecos() {
		DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
				.child("enderecos")
				.child(FirebaseHelper.getIdFirebase());
		enderecoRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				for (DataSnapshot ds : snapshot.getChildren()) {
					Endereco endereco = ds.getValue(Endereco.class);
					enderecoList.add(endereco);
				}
				binding.progressBar.setVisibility(View.GONE);
				Collections.reverse(enderecoList);
				configDados();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			result -> {
				if (result.getResultCode() == RESULT_OK) {
					Endereco endereco = (Endereco) result.getData().getSerializableExtra("enderecoSelecionado");
					enderecoList.add(0, endereco);
					configDados();
				}
			}
	);
}