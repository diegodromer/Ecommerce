package com.diegolima.ecommerce.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.adapter.SliderAdapter;
import com.diegolima.ecommerce.databinding.ActivityDetalhesProdutosBinding;
import com.diegolima.ecommerce.model.Produto;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

public class DetalhesProdutosActivity extends AppCompatActivity {

	private ActivityDetalhesProdutosBinding binding;

	private Produto produto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityDetalhesProdutosBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		getExtra();
	}

	private void getExtra(){
		produto = (Produto) getIntent().getSerializableExtra("produtoSelecionado");
		configDados();
	}

	private void configDados(){
		binding.sliderView.setSliderAdapter(new SliderAdapter(produto.getUrlsImagens()));
		binding.sliderView.startAutoCycle();
		binding.sliderView.setScrollTimeInSec(4);
		binding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
		binding.sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
	}
}