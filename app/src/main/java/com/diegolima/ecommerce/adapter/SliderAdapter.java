package com.diegolima.ecommerce.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.diegolima.ecommerce.R;
import com.diegolima.ecommerce.model.ImagemUpload;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.MyViewHolder> {

	List<ImagemUpload> urlsImagens;

	public SliderAdapter(List<ImagemUpload> urlsImagens) {
		this.urlsImagens = urlsImagens;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide_imagem, parent, false);
		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(MyViewHolder viewHolder, int position) {
		ImagemUpload imagemUpload = urlsImagens.get(position);

		Picasso.get().load(imagemUpload.getCaminhoImagem()).into(viewHolder.imgSlider);
	}

	@Override
	public int getCount() {
		return urlsImagens.size();
	}

	static class MyViewHolder extends SliderViewAdapter.ViewHolder{

		ImageView imgSlider;

		public MyViewHolder(View itemView) {
			super(itemView);

			imgSlider = itemView.findViewById(R.id.sliderView);
		}
	}
}