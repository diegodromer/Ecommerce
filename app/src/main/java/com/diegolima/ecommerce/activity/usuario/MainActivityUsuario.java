package com.diegolima.ecommerce.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavigatorState;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.autenticacao.LoginActivity;
import com.diegolima.ecommerce.databinding.ActivityMainUsuarioBinding;
import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivityUsuario extends AppCompatActivity {

	private ActivityMainUsuarioBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMainUsuarioBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		NavHostFragment navHostFragment =
				(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
		NavController navController = navHostFragment.getNavController();
		NavigationUI.setupWithNavController(binding.bottomNavigationView ,navController);

		int id = getIntent().getIntExtra("id", 0);

		if(id == 2){
			binding.bottomNavigationView.setSelectedItemId(R.id.menu_carrinho);
		}

	}
}