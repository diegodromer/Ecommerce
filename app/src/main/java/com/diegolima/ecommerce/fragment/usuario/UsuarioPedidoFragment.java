package com.diegolima.ecommerce.fragment.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.diegolima.ecommerce.activity.app.DetalhesPedidoActivity;
import com.diegolima.ecommerce.adapter.UsuarioPedidosAdapter;
import com.diegolima.ecommerce.autenticacao.LoginActivity;
import com.diegolima.ecommerce.databinding.FragmentUsuarioPedidoBinding;
import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.diegolima.ecommerce.model.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioPedidoFragment extends Fragment implements UsuarioPedidosAdapter.OnClickListener {

	private FragmentUsuarioPedidoBinding binding;

	private UsuarioPedidosAdapter usuarioPedidosAdapter;

	private final List<Pedido> pedidoList = new ArrayList<>();

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = FragmentUsuarioPedidoBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		configClicks();
	}

	private void recuperaPedidos(){
		DatabaseReference pedidosRef = FirebaseHelper.getDatabaseReference()
				.child("usuarioPedidos")
				.child(FirebaseHelper.getIdFirebase());
		pedidosRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if (snapshot.exists()){
					pedidoList.clear();
					for (DataSnapshot ds : snapshot.getChildren()){
						Pedido pedido = ds.getValue(Pedido.class);
						pedidoList.add(pedido);
					}
					binding.textInfo.setText("");
				}else{
					binding.textInfo.setText("Nenhum pedido encontrado.");
				}
				Collections.reverse(pedidoList);
				binding.progressBar.setVisibility(View.GONE);
				usuarioPedidosAdapter.notifyDataSetChanged();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	private void configRv(){
		binding.rvPedidos.setLayoutManager(new LinearLayoutManager(requireContext()));
		binding.rvPedidos.setHasFixedSize(true);
		usuarioPedidosAdapter = new UsuarioPedidosAdapter(pedidoList, requireContext(), this);
		binding.rvPedidos.setAdapter(usuarioPedidosAdapter);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (FirebaseHelper.getAutenticado()) {
			configRv();
			recuperaPedidos();
			binding.btnLogin.setVisibility(View.GONE);
		} else {
			binding.btnLogin.setVisibility(View.VISIBLE);
			binding.progressBar.setVisibility(View.GONE);
			binding.textInfo.setText("Você não está autenticado no app.");
		}
	}

	private void configClicks() {
		binding.btnLogin.setOnClickListener(v -> startActivity(new Intent(requireContext(), LoginActivity.class)));
	}

	@Override
	public void onClick(Pedido pedido) {
		Intent intent = new Intent(requireContext(), DetalhesPedidoActivity.class);
		intent.putExtra("pedidoSelecionado", pedido);
		startActivity(intent);
	}
}