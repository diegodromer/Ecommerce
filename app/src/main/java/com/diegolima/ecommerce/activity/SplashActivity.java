package com.diegolima.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.diegolima.ecommerce.MainActivity;
import com.diegolima.ecommerce.R;

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		new Handler(getMainLooper()).postDelayed(() -> {
			finish();
			startActivity(new Intent(this, MainActivity.class));
		}, 500);
	}
}