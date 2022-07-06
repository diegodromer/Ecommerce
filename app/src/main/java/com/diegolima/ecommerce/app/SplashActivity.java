package com.diegolima.ecommerce.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.diegolima.ecommerce.activity.loja.MainActivityEmpresa;
import com.diegolima.ecommerce.activity.usuario.MainActivityUsuario;
import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		new Handler(getMainLooper()).postDelayed(this::verificaAcesso, 500);
	}

	private void verificaAcesso(){
		if (FirebaseHelper.getAutenticado()){
			recuperaAcesso();
		}else{
			startActivity(new Intent(this, MainActivityUsuario.class));
			finish();
		}
	}

	private void recuperaAcesso() {
		DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
				.child("usuarios")
				.child(FirebaseHelper.getIdFirebase());
		usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if (snapshot.exists()){
					startActivity(new Intent(getBaseContext(), MainActivityUsuario.class));
				}else{
					startActivity(new Intent(getBaseContext(), MainActivityUsuario.class));
				}
				finish();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

}