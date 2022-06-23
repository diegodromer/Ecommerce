package com.diegolima.ecommerce.fragment.loja;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.databinding.FragmentLojaCategoriaBinding;

public class LojaCategoriaFragment extends Fragment {

	private FragmentLojaCategoriaBinding binding;
	private AlertDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = FragmentLojaCategoriaBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		configClicks();
	}

	private void configClicks(){
		binding.btnAddCategoria.setOnClickListener(v -> showDialog());
	}

	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				getContext(),
				R.style.CustomAlertDialog
		);

		View view = getLayoutInflater().inflate(R.layout.dialog_form_categoria, null);

		builder.setView(view);

		dialog = builder.create();
		dialog.show();
	}
}