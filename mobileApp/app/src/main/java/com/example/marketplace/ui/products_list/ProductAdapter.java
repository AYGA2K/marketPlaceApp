package com.example.marketplace.ui.products_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketplace.R;

import java.util.List;

// ProductAdapter.java
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageProduct;
        private TextView textProductName;
        private TextView textProductDescription;
        private TextView textProductPrice;
        private TextView textSellerPhoneNumber;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            textProductName = itemView.findViewById(R.id.textProductName);
            textProductDescription = itemView.findViewById(R.id.textProductDescription);
            textProductPrice = itemView.findViewById(R.id.textProductPrice);
            textSellerPhoneNumber = itemView.findViewById(R.id.textSellerPhoneNumber);
        }

        public void bind(Product product) {
            // Load product data into the views
            textProductName.setText(product.getName());
            textProductDescription.setText(product.getDescription());
            textProductPrice.setText(String.valueOf(product.getPrice())); // Convert int to String
            textSellerPhoneNumber.setText(product.getSellerPhoneNumber());  // Load image using your preferred image loading library (e.g., Glide, Picasso)
            // Example: Glide.with(itemView).load(product.getImageUrl()).into(imageProduct);
        }
    }
}
