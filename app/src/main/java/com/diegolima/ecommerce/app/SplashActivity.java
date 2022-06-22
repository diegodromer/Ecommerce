package com.diegolima.ecommerce.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.diegolima.ecommerce.activity.usuario.MainActivityUsuario;
import com.diegolima.ecommerce.R;

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		new Handler(getMainLooper()).postDelayed(() -> {
			finish();
			startActivity(new Intent(this, MainActivityUsuario.class));
		}, 500);
	}
}