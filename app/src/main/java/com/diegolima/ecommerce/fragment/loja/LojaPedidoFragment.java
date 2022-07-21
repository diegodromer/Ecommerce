package com.diegolima.ecommerce.fragment.loja;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.adapter.LojaPedidosAdapter;
import com.diegolima.ecommerce.adapter.UsuarioPedidosAdapter;
import com.diegolima.ecommerce.databinding.FragmentLojaPedidoBinding;
import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.diegolima.ecommerce.model.Pedido;
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

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	@Override
	public void onClick(Pedido pedido, String operacao) {
		switch (operacao){
			case "detalhes":
				Toast.makeText(requireContext(), "Detalhes do pedido.", Toast.LENGTH_SHORT).show();
				break;
			case "status":
				Toast.makeText(requireContext(), "Status do pedido.", Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(requireContext(), "Operação inválida, favor verifique.", Toast.LENGTH_SHORT).show();
				break;
		}
	}
}