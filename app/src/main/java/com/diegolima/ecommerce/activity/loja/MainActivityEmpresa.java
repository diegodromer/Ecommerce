package com.diegolima.ecommerce.activity.loja;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.diegolima.ecommerce.R;

public class MainActivityEmpresa extends AppCompatActivity {

	private com.diegolima.ecommerce.databinding.ActivityMainEmpresaBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = com.diegolima.ecommerce.databinding.ActivityMainEmpresaBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
		NavController navController = navHostFragment.getNavController();
		NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
	}
}