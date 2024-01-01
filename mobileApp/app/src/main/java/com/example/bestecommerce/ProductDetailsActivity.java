package com.example.bestecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.bestecommerce.databinding.ActivityProductDetailsBinding;
import com.example.bestecommerce.models.ProductModels;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailsActivity extends AppCompatActivity {
	ActivityProductDetailsBinding binding;
	ProductModels productModels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		double price = intent.getDoubleExtra("price", 0d);
		String image = intent.getStringExtra("image");
		int id = intent.getIntExtra("Id", 0);

		getSupportActionBar().setTitle(name);
		Glide.with(this).load(image).into(binding.pimages);


		Cart cart = TinyCartHelper.getCart();
		binding.addtocart.setOnClickListener(v -> {
			cart.addItem(productModels, 1);
			binding.addtocart.setEnabled(false);
			binding.addtocart.setText("Added in Cart");
			Toast.makeText(this, "Item is Added to Cart", Toast.LENGTH_SHORT).show();
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cart_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
          return  true;
	}
}
