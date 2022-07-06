package com.diegolima.ecommerce.fragment.usuario;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.adapter.CategoriaAdapter;
import com.diegolima.ecommerce.adapter.LojaProdutoAdapter;
import com.diegolima.ecommerce.databinding.FragmentUsuarioHomeBinding;
import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.diegolima.ecommerce.model.Categoria;
import com.diegolima.ecommerce.model.Favorito;
import com.diegolima.ecommerce.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioHomeFragment extends Fragment implements CategoriaAdapter.OnClick, LojaProdutoAdapter.OnClickListener, LojaProdutoAdapter.OnClickFavorito {

	private FragmentUsuarioHomeBinding binding;

	private CategoriaAdapter categoriaAdapter;
	private LojaProdutoAdapter lojaProdutoAdapter;

	private final List<Categoria> categoriaList = new ArrayList<>();
	private final List<Produto> produtoList = new ArrayList<>();
	private final List<String> idsFavoritos = new ArrayList<>();

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = FragmentUsuarioHomeBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		configRvCategorias();
		configRvProdutos();

		recuperaCategorias();
		recuperaProdutos();
		recuperaFavoritos();
	}

	private void recuperaFavoritos() {
		if (FirebaseHelper.getAutenticado()){
			DatabaseReference favoritoRef = FirebaseHelper.getDatabaseReference()
					.child("favoritos")
					.child(FirebaseHelper.getIdFirebase());
			favoritoRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot snapshot) {
					idsFavoritos.clear();
					for (DataSnapshot ds : snapshot.getChildren()){
						String idFavorito = ds.getValue(String.class);
						idsFavoritos.add(idFavorito);
					}
					categoriaAdapter.notifyDataSetChanged();
				}

				@Override
				public void onCancelled(@NonNull DatabaseError error) {

				}
			});
		}
	}


	private void configRvCategorias() {
		binding.rvCategorias.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
		binding.rvCategorias.setHasFixedSize(true);
		categoriaAdapter = new CategoriaAdapter(R.layout.item_categoria_horizontal, true, categoriaList, this);
		binding.rvCategorias.setAdapter(categoriaAdapter);
	}

	private void configRvProdutos() {
		binding.rvProdutos.setLayoutManager(new GridLayoutManager(requireContext(), 2));
		binding.rvProdutos.setHasFixedSize(true);
		lojaProdutoAdapter = new LojaProdutoAdapter(produtoList, requireContext(), true,idsFavoritos, this, this);
		binding.rvProdutos.setAdapter(lojaProdutoAdapter);
	}

	private void recuperaProdutos() {
		DatabaseReference produtoRef = FirebaseHelper.getDatabaseReference()
				.child("produtos");
		produtoRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {

				produtoList.clear();
				for (DataSnapshot ds : snapshot.getChildren()) {
					Produto produto = ds.getValue(Produto.class);
					produtoList.add(produto);
				}
				listEmpty();

				binding.progressBar.setVisibility(View.GONE);
				Collections.reverse(produtoList);
				lojaProdutoAdapter.notifyDataSetChanged();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	private void listEmpty() {
		if (produtoList.isEmpty()) {
			binding.textInfo.setText("Nenhum produto cadastrado");
		} else {
			binding.textInfo.setText("");
		}
	}

	private void recuperaCategorias() {
		DatabaseReference categoriasRef = FirebaseHelper.getDatabaseReference()
				.child("categorias");
		categoriasRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {

				categoriaList.clear();
				for (DataSnapshot ds : snapshot.getChildren()){
					Categoria categoria = ds.getValue(Categoria.class);
					categoriaList.add(categoria);
				}

				Collections.reverse(categoriaList);
				categoriaAdapter.notifyDataSetChanged();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		binding = null;
	}

	@Override
	public void onClickListener(Categoria categoria) {
		Toast.makeText(requireContext(), categoria.getNome(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnClick(Produto produto) {

	}

	@Override
	public void OnClickFavorito(String idProduto) {
		if (!idsFavoritos.contains(idProduto)){
			idsFavoritos.add(idProduto);
		}else {
			idsFavoritos.remove(idProduto);
		}
		Favorito.salvar(idsFavoritos);
	}
}