package com.example.bestecommerce.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bestecommerce.ProductDetailsActivity;
import com.example.bestecommerce.R;
import com.example.bestecommerce.databinding.SampleProductBinding;
import com.example.bestecommerce.models.ProductModels;

import java.util.List;

public class ProductAdapters extends RecyclerView.Adapter<ProductAdapters.ProductViewHolder> {
	private List<ProductModels> productList;
	private Context context;

	public ProductAdapters(List<ProductModels> productList, Context context) {
		this.productList = productList;
		this.context = context;
	}

	@NonNull
	@Override
	public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_product, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
		final ProductModels productModels = productList.get(position);
		Glide.with(context).load(productModels.getImage()).into(holder.binding.pimage);
		holder.binding.pprice.setText("PRK " + productModels.getPrice());
		holder.binding.ptitle.setText(productModels.getName());

		holder.itemView.setOnClickListener(v -> {
			Intent intent = new Intent(context.getApplicationContext(), ProductDetailsActivity.class);
			intent.putExtra("name", productModels.getName());
			intent.putExtra("image", productModels.getImage());
			intent.putExtra("price", productModels.getPrice());
			intent.putExtra("id", productModels.getId());
			context.startActivity(intent);
		});
	}

	@Override
	public int getItemCount() {
		return productList.size();
	}

	public class ProductViewHolder extends RecyclerView.ViewHolder {
		SampleProductBinding binding;

		public ProductViewHolder(@NonNull View itemView) {
			super(itemView);
			binding = SampleProductBinding.bind(itemView);
		}
	}
}
