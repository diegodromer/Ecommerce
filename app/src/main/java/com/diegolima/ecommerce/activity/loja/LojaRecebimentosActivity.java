package com.diegolima.ecommerce.activity.loja;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.databinding.ActivityLojaRecebimentosBinding;

public class LojaRecebimentosActivity extends AppCompatActivity {

	private ActivityLojaRecebimentosBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityLojaRecebimentosBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
	}

	private void validaDados(){
		String publicKey = binding.edtPublicKey.getText().toString().trim();
		String acessToken = binding.edtAcessToken.getText().toString().trim();
		int parcelas = Integer.parseInt(binding.edtParcelas.getText().toString().trim());

		if ()
	}
}