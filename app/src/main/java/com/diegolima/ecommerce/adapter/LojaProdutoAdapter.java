package com.diegolima.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.helper.FirebaseHelper;
import com.diegolima.ecommerce.model.Produto;
import com.diegolima.ecommerce.util.GetMask;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

public class LojaProdutoAdapter extends RecyclerView.Adapter<LojaProdutoAdapter.MyViewHolder> {

	private int layout;
	private final List<Produto> produtoList;
	private final Context context;
	private final boolean favorito;
	private final List<String> idsFavoritos;
	private final OnClickListener onClickListener;
	private final OnClickFavorito onClickFavorito;

	public LojaProdutoAdapter(int layout, List<Produto> produtoList, Context context, boolean favorito, List<String> idsFavoritos, OnClickListener onClickListener, OnClickFavorito onClickFavorito) {
		this.layout = layout;
		this.produtoList = produtoList;
		this.context = context;
		this.favorito = favorito;
		this.idsFavoritos = idsFavoritos;
		this.onClickListener = onClickListener;
		this.onClickFavorito = onClickFavorito;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		Produto produto = produtoList.get(position);

		holder.txtNomeProduto.setText(produto.getTitulo());

		if (favorito) {
			holder.likeButton.setLiked(idsFavoritos.contains(produto.getId()));
		}else{
			holder.likeButton.setVisibility(View.GONE);
		}

		if (produto.getValorAntigo() > 0) {

			double resto = produto.getValorAntigo() - produto.getValorAtual();
			int porcentagem = (int) (resto / produto.getValorAntigo() * 100);

			if (porcentagem >= 10) {
				holder.txtDescontoProduto.setText(context.getString(R.string.valor_off, porcentagem, "%"));
			} else { //0,09
				String porcent = String.valueOf(porcentagem).replace("0", ""); //9
				holder.txtDescontoProduto.setText(context.getString(R.string.valor_off, Integer.parseInt(porcent), "%"));
			}
		} else {
			holder.txtDescontoProduto.setVisibility(View.GONE);
		}

		holder.likeButton.setOnLikeListener(new OnLikeListener() {
			@Override
			public void liked(LikeButton likeButton) {
				if (FirebaseHelper.getAutenticado()) {
					onClickFavorito.OnClickFavorito(produto);
				} else {
					Toast.makeText(context, "Voc?? n??o est?? autenticado no app.", Toast.LENGTH_SHORT).show();
					holder.likeButton.setLiked(false);
				}
			}

			@Override
			public void unLiked(LikeButton likeButton) {
				onClickFavorito.OnClickFavorito(produto);
			}
		});

		for (int i = 0; i < produto.getUrlsImagens().size(); i++) {
			if (produto.getUrlsImagens().get(i).getIndex() == 0) {
				Glide
						.with(context)
						.load(produto.getUrlsImagens().get(i).getCaminhoImagem())
						.centerCrop()
						.into(holder.imagemProduto);
			}
		}

		holder.txtValorProduto.setText(String.valueOf(context.getString(R.string.valor, GetMask.getValor(produto.getValorAtual()))));

		holder.itemView.setOnClickListener(v -> onClickListener.OnClick(produto));
	}

	@Override
	public int getItemCount() {
		return produtoList.size();
	}

	public interface OnClickListener {
		void OnClick(Produto produto);
	}

	public interface OnClickFavorito {
		void OnClickFavorito(Produto produto);
	}

	static class MyViewHolder extends RecyclerView.ViewHolder {

		ImageView imagemProduto;
		TextView txtNomeProduto, txtValorProduto, txtDescontoProduto;
		LikeButton likeButton;

		public MyViewHolder(@NonNull View itemView) {
			super(itemView);

			imagemProduto = itemView.findViewById(R.id.cardView);
			txtNomeProduto = itemView.findViewById(R.id.txtNomeProduto);
			txtValorProduto = itemView.findViewById(R.id.txtValorProduto);
			txtDescontoProduto = itemView.findViewById(R.id.txtDescontoProduto);
			likeButton = itemView.findViewById(R.id.likeButton);
		}
	}
}
