package com.diegolima.ecommerce.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.model.Categoria;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriaDialogAdapter extends RecyclerView.Adapter<CategoriaDialogAdapter.MyViewHolder> {

	private final List<String> idsCategoriasSelecionadas;
	private final List<Categoria> categoriaList;

	private OnClick onClick;

	public CategoriaDialogAdapter(List<String> idsCategoriasSelecionadas, List<Categoria> categoriaList, OnClick onClick) {
		this.idsCategoriasSelecionadas = idsCategoriasSelecionadas;
		this.categoriaList = categoriaList;
		this.onClick = onClick;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria_dialog, parent, false);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		Categoria categoria = categoriaList.get(position);

		Picasso.get().load(categoria.getUrlImagem()).into(holder.imagemCategoria);
		holder.nomeCategoria.setText(categoria.getNome());
		if (idsCategoriasSelecionadas.contains(categoria.getId())){
			holder.checkBox.setChecked(true);
		}
		holder.itemView.setOnClickListener(v -> {
			onClick.onClickListener(categoria);

			holder.checkBox.setChecked(!holder.checkBox.isChecked());
		});
	}

	@Override
	public int getItemCount() {
		return categoriaList.size();
	}

	public interface OnClick{
		public void onClickListener(Categoria categoria);
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder {

		ImageView imagemCategoria;
		TextView nomeCategoria;
		CheckBox checkBox;

		public MyViewHolder(@NonNull View itemView) {
			super(itemView);
			imagemCategoria = itemView.findViewById(R.id.imagemCategoria);
			nomeCategoria = itemView.findViewById(R.id.nomeCategoria);
			checkBox = itemView.findViewById(R.id.checkBox);
		}
	}
}
