package com.diegolima.ecommerce.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.databinding.ActivityUsuarioPerfilBinding;
import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.diegolima.ecommerce.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UsuarioPerfilActivity extends AppCompatActivity {

	private ActivityUsuarioPerfilBinding binding;

	private Usuario usuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityUsuarioPerfilBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		iniciaComponentes();
		configClicks();
		recuperaUsuario();
	}

	private void validaDados() {
		String nome = binding.edtNome.getText().toString().trim();
		String telefone = binding.edtTelefone.getMasked()
				.replace("(", "")
				.replace(")", "")
				.replace(" ", "");

		if (!nome.isEmpty()) {
			if (!telefone.isEmpty()) {
				if (telefone.length() == 12) {
					if (usuario != null) {
						usuario.setNome(nome);
						usuario.setTelefone(telefone);
						usuario.salvar();
					}else{
						Toast.makeText(this, "Aguarde, ainda estamos recuperando as informações.", Toast.LENGTH_SHORT).show();
					}
					binding.progressBar.setVisibility(View.GONE);
				} else {
					binding.edtTelefone.requestFocus();
					binding.edtTelefone.setError("Formato do telefone inválido.");
				}
			} else {
				binding.edtTelefone.requestFocus();
				binding.edtTelefone.setError("Informação obrigatória.");
			}
		} else {
			binding.edtNome.requestFocus();
			binding.edtNome.setError("Informação obrigatória.");
		}
	}

	private void configDados() {
		binding.edtNome.setText(usuario.getNome());
		binding.edtTelefone.setText(usuario.getTelefone());
		binding.edtEmail.setText(usuario.getEmail());
		binding.progressBar.setVisibility(View.GONE);
	}

	private void recuperaUsuario() {
		DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
				.child("usuarios")
				.child(FirebaseHelper.getIdFirebase());
		usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				usuario = snapshot.getValue(Usuario.class);
				configDados();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	private void configClicks() {
		binding.include.include.ibVoltar.setOnClickListener(v -> finish());
		binding.include.btnSalvar.setOnClickListener(v -> validaDados());
	}

	private void iniciaComponentes() {
		binding.include.textTitulo.setText("Meus dados");
	}
}