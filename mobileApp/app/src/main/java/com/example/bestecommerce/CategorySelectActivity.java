
package com.example.bestecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bestecommerce.Adapters.ProductAdapters;
import com.example.bestecommerce.databinding.ActivityCategorySelectBinding;
import com.example.bestecommerce.models.ProductModels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategorySelectActivity extends AppCompatActivity {
    ActivityCategorySelectBinding binding;
    private ArrayList<ProductModels> productlist;
    private ProductAdapters productAdapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategorySelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int categoryId = intent.getIntExtra("cid", 0);

        getProductList(categoryId);

        productlist = new ArrayList<>();

        productAdapters = new ProductAdapters(productlist, this);
        binding.categoryselectitemrecyclerview.setAdapter(productAdapters);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.categoryselectitemrecyclerview.setLayoutManager(gridLayoutManager);
    }

    private void getProductList(int categoryId) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://tutorials.mianasad.com/ecommerce/services/listProduct?category_id=" + categoryId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject mainobj = new JSONObject(response);
                            String status = mainobj.getString("status");
                            if (status.equals("success")) {
                                JSONArray productsData = mainobj.getJSONArray("products");
                                for (int i = 0; i < productsData.length(); i++) {
                                    JSONObject productData = productsData.getJSONObject(i);

                                    ProductModels product = new ProductModels(
                                            productData.getString("name"),
                                            Constants.PRODUCTS_IMAGE_URL + productData.getString("image"),
                                            productData.getString("description"),
                                            productData.getDouble("price"),
                                            productData.getBoolean("isAvailable"),
                                            productData.getInt("sellerId"),
                                            productData.getInt("id")
                                    );

                                    productlist.add(product);
                                    productAdapters.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(CategorySelectActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CategorySelectActivity.this, "Error while loading products", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
