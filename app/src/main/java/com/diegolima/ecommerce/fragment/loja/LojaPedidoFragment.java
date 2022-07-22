package com.diegolima.ecommerce.fragment.loja;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.activity.app.DetalhesPedidoActivity;
import com.diegolima.ecommerce.adapter.LojaPedidosAdapter;
import com.diegolima.ecommerce.adapter.UsuarioPedidosAdapter;
import com.diegolima.ecommerce.databinding.DialogDeleteBinding;
import com.diegolima.ecommerce.databinding.FragmentLojaPedidoBinding;
import com.diegolima.ecommerce.databinding.LayoutDialogStatusPedidoBinding;
import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.diegolima.ecommerce.model.Categoria;
import com.diegolima.ecommerce.model.Pedido;
import com.diegolima.ecommerce.model.StatusPedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LojaPedidoFragment extends Fragment implements LojaPedidosAdapter.OnClickListener {

	private FragmentLojaPedidoBinding binding;

	private LojaPedidosAdapter lojaPedidosAdapter;
	private final List<Pedido> pedidoList = new ArrayList<>();
	private AlertDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = FragmentLojaPedidoBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		configRv();

		recuperaPedidos();
	}

	private void configRv() {
		binding.rvPedidos.setLayoutManager(new LinearLayoutManager(requireContext()));
		binding.rvPedidos.setHasFixedSize(true);
		lojaPedidosAdapter = new LojaPedidosAdapter(pedidoList, requireContext(), this);
		binding.rvPedidos.setAdapter(lojaPedidosAdapter);
	}

	private void recuperaPedidos() {
		DatabaseReference pedidosRef = FirebaseHelper.getDatabaseReference()
				.child("lojaPedidos");
		pedidosRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if (snapshot.exists()) {
					pedidoList.clear();
					for (DataSnapshot ds : snapshot.getChildren()) {
						Pedido pedido = ds.getValue(Pedido.class);
						pedidoList.add(pedido);
					}
					binding.textInfo.setText("");
				} else {
					binding.textInfo.setText("Nenhum pedido recebido.");
				}

				binding.progressBar.setVisibility(View.GONE);
				Collections.reverse(pedidoList);
				lojaPedidosAdapter.notifyDataSetChanged();

			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	private void showDialogStatus(Pedido pedido) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog2);

		LayoutDialogStatusPedidoBinding statusBinding = LayoutDialogStatusPedidoBinding
				.inflate(LayoutInflater.from(getContext()));

		RadioGroup rgStatus = statusBinding.rgStatus;
		RadioButton rbPendente = statusBinding.rbPendente;
		RadioButton rbAprovado = statusBinding.rbAprovado;
		RadioButton rbCancelado = statusBinding.rbCancelado;

		switch (pedido.getStatusPedido()){
			case PENDENTE:
				rgStatus.check(R.id.rbPendente);
				rbAprovado.setEnabled(true);
				rbCancelado.setEnabled(true);
				break;
			case APROVADO:
				rbPendente.setEnabled(false);
				rbCancelado.setEnabled(false);
				rgStatus.check(R.id.rbAprovado);
				break;
			default:
				rbPendente.setEnabled(false);
				rbAprovado.setEnabled(false);
				rgStatus.check(R.id.rbCancelado);
				break;
		}

		statusBinding.btnFechar.setOnClickListener(v -> dialog.dismiss());

		rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
			if (checkedId == R.id.rbPendente){
				pedido.setStatusPedido(StatusPedido.PENDENTE);
			}else if (checkedId == R.id.rbAprovado){
				pedido.setStatusPedido(StatusPedido.APROVADO);
			}else {
				pedido.setStatusPedido(StatusPedido.CANCELADO);
			}
		});

		statusBinding.btnConfirmar.setOnClickListener(v -> {
			pedido.salvar(false);
			dialog.dismiss();
		});

		builder.setView(statusBinding.getRoot());

		dialog = builder.create();
		if (!requireActivity().isFinishing()){
			dialog.show();
		}
	}

	@Override
	public void onClick(Pedido pedido, String operacao) {
		switch (operacao){
			case "detalhes":
				Intent intent = new Intent(requireContext(), DetalhesPedidoActivity.class);
				intent.putExtra("pedidoSelecionado", pedido);
				startActivity(intent);
				break;
			case "status":
				showDialogStatus(pedido);
				break;
			default:
				Toast.makeText(requireContext(), "Operação inválida, favor verifique.", Toast.LENGTH_SHORT).show();
				break;
		}
	}
}