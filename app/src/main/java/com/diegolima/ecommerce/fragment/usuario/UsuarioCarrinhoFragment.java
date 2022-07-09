package com.diegolima.ecommerce.fragment.usuario;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diegolima.ecommerce.DAO.ItemDAO;
import com.diegolima.ecommerce.DAO.ItemPedidoDAO;
import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.adapter.CarrinhoAdapter;
import com.diegolima.ecommerce.databinding.FragmentUsuarioCarrinhoBinding;
import com.diegolima.ecommerce.model.ItemPedido;
import com.diegolima.ecommerce.util.GetMask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioCarrinhoFragment extends Fragment implements CarrinhoAdapter.OnClick {

	private FragmentUsuarioCarrinhoBinding binding;

	private final List<ItemPedido> itemPedidoList = new ArrayList<>();
	private final List<String> idsFavoritos = new ArrayList<>();

	private CarrinhoAdapter carrinhoAdapter;

	private ItemDAO itemDAO;
	private ItemPedidoDAO itemPedidoDAO;

	private AlertDialog dialog;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = FragmentUsuarioCarrinhoBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		itemDAO = new ItemDAO(requireContext());
		itemPedidoDAO = new ItemPedidoDAO(requireContext());
		itemPedidoList.addAll(itemPedidoDAO.getList());

		configRv();
	}

	private void configSaldoCarrinho(){
		binding.textValor.setText(getString(R.string.valor_total_carrinho, GetMask.getValor(itemPedidoDAO.getTotalCarrinho())));
	}

	private void configRv(){
		Collections.reverse(itemPedidoList);
		binding.rvProdutos.setLayoutManager(new LinearLayoutManager(requireContext()));
		binding.rvProdutos.setHasFixedSize(true);
		carrinhoAdapter = new CarrinhoAdapter(itemPedidoList, itemPedidoDAO, requireContext(), this);
		binding.rvProdutos.setAdapter(carrinhoAdapter);

		configSaldoCarrinho();
	}

	@Override
	public void onClickLister(int position, String operacao) {

	}
}