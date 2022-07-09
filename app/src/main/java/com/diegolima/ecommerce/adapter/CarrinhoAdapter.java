package com.diegolima.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diegolima.ecommerce.DAO.ItemPedidoDAO;
import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.model.ItemPedido;
import com.diegolima.ecommerce.model.Produto;
import com.diegolima.ecommerce.util.GetMask;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.MyViewHolder> {

	private final List<ItemPedido> itemPedidoList;
	private final ItemPedidoDAO itemPedidoDAO;
	private final Context context;
	private final OnClick onClick;

	public CarrinhoAdapter(List<ItemPedido> itemPedidoList, ItemPedidoDAO itemPedidoDAO, Context context, OnClick onClick) {
		this.itemPedidoList = itemPedidoList;
		this.itemPedidoDAO = itemPedidoDAO;
		this.context = context;
		this.onClick = onClick;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_carrinho, parent, false);
		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

		ItemPedido itemPedido = itemPedidoList.get(position);
		Produto produto = itemPedidoDAO.getProduto(itemPedido.getId());

		holder.textTitulo.setText(produto.getTitulo());
		holder.textQuantidade.setText(String.valueOf(itemPedido.getQuantidade()));
		holder.textValor.setText(context.getString(R.string.valor, GetMask.getValor(itemPedido.getValor() * itemPedido.getQuantidade())));
		Picasso.get().load(produto.getUrlsImagens().get(0).getCaminhoImagem()).into(holder.imgProduto);

		holder.itemView.setOnClickListener(v -> onClick.onClickLister(position, "detalhe"));
		holder.imgRemover.setOnClickListener(v -> onClick.onClickLister(position, "remover"));
		holder.ibMenos.setOnClickListener(v -> onClick.onClickLister(position, "menos"));
		holder.ibMais.setOnClickListener(v -> onClick.onClickLister(position, "mais"));

	}

	@Override
	public int getItemCount() {
		return itemPedidoList.size();
	}

	public interface OnClick {
		void onClickLister(int position, String operacao);
	}

	static class MyViewHolder extends RecyclerView.ViewHolder {

		ImageView imgProduto, imgRemover;
		ImageButton ibMenos, ibMais;
		TextView textTitulo, textValor, textQuantidade;

		public MyViewHolder(@NonNull View itemView) {
			super(itemView);

			imgProduto = itemView.findViewById(R.id.imgProduto);
			imgRemover = itemView.findViewById(R.id.imgRemover);

			ibMenos = itemView.findViewById(R.id.ibMenos);
			ibMais = itemView.findViewById(R.id.ibMais);

			textTitulo = itemView.findViewById(R.id.textTitulo);
			textValor = itemView.findViewById(R.id.textValor);
			textQuantidade = itemView.findViewById(R.id.textQuantidade);
		}
	}
}
