package com.diegolima.ecommerce.activity.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.adapter.DetalhesPedidoAdapter;
import com.diegolima.ecommerce.databinding.ActivityDetalhesPedidoBinding;
import com.diegolima.ecommerce.model.Endereco;
import com.diegolima.ecommerce.model.Pedido;
import com.diegolima.ecommerce.util.GetMask;

public class DetalhesPedidoActivity extends AppCompatActivity {

	private ActivityDetalhesPedidoBinding binding;

	private Pedido pedido;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityDetalhesPedidoBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		iniciaComponentes();
		getExtra();
		configClicks();
	}

	private void getExtra() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			pedido = (Pedido) bundle.getSerializable("pedidoSelecionado");
			configRv();
			configDados();
		}
	}

	private void configDados() {
		Endereco endereco = pedido.getEndereco();

		StringBuilder enderecoCompleto = new StringBuilder();
		enderecoCompleto.append(endereco.getLogradouro())
				.append(", ")
				.append(endereco.getNumero())
				.append("\n")
				.append(endereco.getBairro())
				.append(", ")
				.append(endereco.getLocalidade())
				.append("/")
				.append(endereco.getUf())
				.append("\n")
				.append("CEP: ")
				.append(endereco.getCep());

		binding.textEnderecoEntrega.setText(enderecoCompleto);
		binding.textNomePagamento.setText(pedido.getPagamento());

		double valorExtra = 0;
		double totalPedido = pedido.getTotal();

		if (pedido.getAcrescimo() > 0) {
			binding.textTipoPagamento.setText("Acréscimo");
			valorExtra = pedido.getAcrescimo();
			totalPedido += valorExtra;
		} else {
			binding.textTipoPagamento.setText("Desconto");
			valorExtra = pedido.getDesconto();
			totalPedido -= valorExtra;
		}

		binding.textValorTipoPagamento.setText(
				getString(R.string.valor, GetMask.getValor(valorExtra))
		);

		binding.textValorProdutos.setText(getString(R.string.valor, GetMask.getValor(pedido.getTotal())));

		binding.textValorFrete.setText("");

		binding.textValorTotal.setText(
				getString(R.string.valor, GetMask.getValor(totalPedido))
		);
	}

	private void configRv(){
		binding.rvProdutos.setLayoutManager(new LinearLayoutManager(this));
		binding.rvProdutos.setHasFixedSize(true);
		DetalhesPedidoAdapter detalhesPedidoAdapter = new DetalhesPedidoAdapter(pedido.getItemPedidoList(), this);
		binding.rvProdutos.setAdapter(detalhesPedidoAdapter);
	}

	private void configClicks(){
		binding.include.include.ibVoltar.setOnClickListener(v -> finish());
	}

	private void iniciaComponentes() {
		binding.include.textTitulo.setText("Detalhes pedido");
	}
}