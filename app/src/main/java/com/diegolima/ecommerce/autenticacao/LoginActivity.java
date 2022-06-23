package com.diegolima.ecommerce.autenticacao;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.diegolima.ecommerce.activity.loja.MainActivityEmpresa;
import com.diegolima.ecommerce.activity.usuario.MainActivityUsuario;
import com.diegolima.ecommerce.databinding.ActivityLoginBinding;
import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

	private ActivityLoginBinding binding;

	private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			result -> {
				if (result.getResultCode() == RESULT_OK) {
					String email = result.getData().getStringExtra("email");
					String senha = result.getData().getStringExtra("senha");

					binding.edtEmail.setText(email);
					binding.edtSenha.setText(senha);
				}
			}
	);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityLoginBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		configClicks();
	}

	private void recuperaUsuario(String id) {
		DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
				.child("usuarios")
				.child(id);
		usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if (snapshot.exists()){
					startActivity(new Intent(getBaseContext(), MainActivityUsuario.class));
				}else{
					startActivity(new Intent(getBaseContext(), MainActivityEmpresa.class));
				}
				finish();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	private void login(String email, String senha) {
		FirebaseHelper.getAuth().signInWithEmailAndPassword(email, senha)
				.addOnCompleteListener(task -> {
					if (task.isSuccessful()) {
						recuperaUsuario(task.getResult().getUser().getUid());
						finish();
					} else {
						Toast.makeText(this, FirebaseHelper.validaErros(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
					}
				});

		binding.progressBar.setVisibility(View.GONE);
	}

	public void validaDados(View view) {
		String email = binding.edtEmail.getText().toString().trim();
		String senha = binding.edtSenha.getText().toString().trim();

		if (!email.isEmpty()) {
			if (!senha.isEmpty()) {
				binding.progressBar.setVisibility(View.VISIBLE);
				login(email, senha);
			} else {
				binding.edtSenha.requestFocus();
				binding.edtSenha.setError("Informe uma senha.");
			}
		} else {
			binding.edtEmail.requestFocus();
			binding.edtEmail.setError("Informe seu email.");
		}
	}

	private void configClicks() {
		binding.btnRecuperaSenha.setOnClickListener(view ->
				startActivity(new Intent(this, RecuperaContaActivity.class)));

		binding.include.ibVoltar.setOnClickListener(view ->
				startActivity(new Intent(this, MainActivityUsuario.class)));

		binding.btnCadastro.setOnClickListener(view -> {
			Intent intent = new Intent(this, CadastroActivity.class);
			resultLauncher.launch(intent);
		});
	}
}