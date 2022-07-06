package com.diegolima.ecommerce.fragment.loja;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.activity.loja.LojaConfigActivity;
import com.diegolima.ecommerce.activity.loja.LojaRecebimentosActivity;
import com.diegolima.ecommerce.activity.loja.MainActivityEmpresa;
import com.diegolima.ecommerce.activity.usuario.MainActivityUsuario;
import com.diegolima.ecommerce.databinding.FragmentLojaConfigBinding;
import com.diegolima.ecommerce.helper.FirebaseHelper;

public class LojaConfigFragment extends Fragment {

	private FragmentLojaConfigBinding binding;

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		binding = FragmentLojaConfigBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		configClicks();
	}

	private void configClicks(){
		binding.btnConfigLoja.setOnClickListener(v -> startActivity(LojaConfigActivity.class));

		binding.btnPagamentos.setOnClickListener(v -> startActivity(LojaRecebimentosActivity.class));

		binding.btnDeslogar.setOnClickListener(v -> {
			FirebaseHelper.getAuth().signOut();
			requireActivity().finish();
			startActivity(MainActivityUsuario.class);
		});
	}

	private void startActivity(Class<?> clazz){
		startActivity(new Intent(requireContext(), clazz));
	}
}