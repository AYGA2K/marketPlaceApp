import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bestecommerce.Adapters.CategoriesAdapters;
import com.example.bestecommerce.Adapters.ProductAdapters;
import com.example.bestecommerce.Constants;
import com.example.bestecommerce.SearchActivity;
import com.example.bestecommerce.databinding.ActivityMainBinding;
import com.example.bestecommerce.models.CategoriesModels;
import com.example.bestecommerce.models.ProductModels;
import com.mancj.materialsearchbar.SimpleOnSearchActionListener;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	private ArrayList<CategoriesModels> clist;
	private CategoriesAdapters cadapters;
	private RequestQueue queue;
	private ArrayList<ProductModels> productlist;
	private ProductAdapters productAdapters;
	private ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		LoadCategory();

		LoadProducts();

		binding.searchBar.setOnSearchActionListener(new SimpleOnSearchActionListener() {
			@Override
			public void onSearchStateChanged(boolean enabled) {
				super.onSearchStateChanged(enabled);
			}

			@Override
			public void onSearchConfirmed(CharSequence text) {
				Intent sintent = new Intent(MainActivity.this, SearchActivity.class);
				sintent.putExtra("searchtext", text.toString());
				startActivity(sintent);
				super.onSearchConfirmed(text);
			}

			@Override
			public void onButtonClicked(int buttonCode) {
				super.onButtonClicked(buttonCode);
			}
		});
	}

	private void fetchcategory() {
		// Instantiate the RequestQueue.
		queue = Volley.newRequestQueue(this);
		String url = Constants.GET_CATEGORIES_URL;

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							if (jsonObject.get("status").equals("success")) {
								JSONArray data = jsonObject.getJSONArray("categories");
								for (int i = 0; i < data.length(); i++) {
									JSONObject d1 = data.getJSONObject(i);

									List<ProductModels> productsList = new ArrayList<>(); // Assuming you have a list of products for each
																																				// category

									CategoriesModels category = new CategoriesModels(
											d1.getInt("id"),
											d1.getString("name"));
									clist.add(category);
									cadapters.notifyDataSetChanged();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(MainActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show();
					}
				});

		// Add the request to the RequestQueue.
		queue.add(stringRequest);
	}

	private void fetchProduct() {
		// Instantiate the RequestQueue.
		RequestQueue queue = Volley.newRequestQueue(this);

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.GET_PRODUCTS_URL + "?count=6",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject mainobj = new JSONObject(response);
							String status = mainobj.getString("status");
							if (status.equals("success")) {
								JSONArray pdata = mainobj.getJSONArray("products");
								for (int i = 0; i < pdata.length(); i++) {
									JSONObject d1 = pdata.getJSONObject(i);

									ProductModels productdata = new ProductModels(
											d1.getString("name"),
											d1.getString("desc"),
											d1.getString("image"),
											d1.getDouble("price"),
											d1.getBoolean("isAvailable"),
											d1.getInt("sellerId"),
											d1.getInt("id"));
									productlist.add(productdata);
									productAdapters.notifyDataSetChanged();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(MainActivity.this, "Error while loading products", Toast.LENGTH_SHORT).show();
					}
				});

		// Add the request to the RequestQueue.
		queue.add(stringRequest);
	}

	private void LoadProducts() {
		productlist = new ArrayList<>();
		fetchProduct();

		productAdapters = new ProductAdapters(productlist, this);
		binding.productrecyclerview.setAdapter(productAdapters);

		GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
		binding.productrecyclerview.setLayoutManager(gridLayoutManager);
	}

	private void LoadCategory() {
		clist = new ArrayList<>();
		fetchcategory();
		cadapters = new CategoriesAdapters(clist, this);
		binding.categoriesrecyclerview.setAdapter(cadapters);

		GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
		binding.categoriesrecyclerview.setLayoutManager(gridLayoutManager);
	}
}
