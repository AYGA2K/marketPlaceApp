package com.example.marketplace.ui.add_products;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.marketplace.R;
import com.example.marketplace.databinding.FragmentAddProductsBinding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class AddProductsFragment extends Fragment {

    private FragmentAddProductsBinding binding;
    private EditText editTextProductName;
    private EditText editTextProductDescription;
    private EditText editTextProductPrice;
    private EditText editTextProductPhone;
    private Button buttonChooseImage;
    private ImageView imageViewSelected;
    private Button buttonSubmitProduct;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private ProductApi productApi;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddProductsViewModel addProductsViewModel =
                new ViewModelProvider(this).get(AddProductsViewModel.class);

        binding = FragmentAddProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize UI components
        editTextProductName = root.findViewById(R.id.editTextProductName);
        editTextProductDescription = root.findViewById(R.id.editTextProductDescription);
        editTextProductPrice = root.findViewById(R.id.editTextProductPrice);
        editTextProductPhone = root.findViewById(R.id.editTextProductPhone);
        buttonChooseImage = root.findViewById(R.id.buttonChooseImage);
        imageViewSelected = root.findViewById(R.id.imageViewSelected);
        buttonSubmitProduct = root.findViewById(R.id.buttonSubmitProduct);

        // Set up Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.11.128:8080/")  // Change this to your server base URL
                .client(new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        productApi = retrofit.create(ProductApi.class);

        // Set up click listener for the choose image button
        buttonChooseImage.setOnClickListener(view -> openImagePicker());

        // Set up click listener for the submit button
        buttonSubmitProduct.setOnClickListener(view -> {
            // Handle the click event, e.g., retrieve data
            String productName = editTextProductName.getText().toString();
            String productDescription = editTextProductDescription.getText().toString();
            String phone = editTextProductPhone.getText().toString();
            double productPrice = Double.parseDouble(editTextProductPrice.getText().toString());
            int sellerId = 1;
            int categoryId = 1;

            // Validate input (you may add more validation as needed)

            // Create a JSON string with product data
            ProductRequestBody requestBody = new ProductRequestBody(productName, productDescription,
                    phone, productPrice, sellerId, categoryId);
            Log.d("json:", new Gson().toJson(requestBody));

            // Perform product creation using Retrofit
            createProduct(requestBody);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Open image picker
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // Handle result from image picker
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Log.d("image:", String.valueOf(imageUri));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                imageViewSelected.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Retrofit interface for product API
    interface ProductApi {
        @retrofit2.http.POST("product/create")
        Call<JsonObject> createProduct(@retrofit2.http.Body RequestBody body);
    }

    // Request body for product creation
    static class ProductRequestBody {
        private final String name;
        private final String desc;
        private final String phone;
        private String image;
        private final double price;
        private final int seller_id;
        private final int category_id;

        ProductRequestBody(String name, String description, String phone, double price, int seller_id, int category_id) {
            this.name = name;
            this.desc = description;
            this.phone = phone;
            this.price = price;
            this.seller_id = seller_id;
            this.category_id = category_id;
            this.image="product_7_images.jpeg";
        }
    }

    private void createProduct(ProductRequestBody requestBody) {
        // Create product
        Call<JsonObject> createProductCall = productApi.createProduct(RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(requestBody)));
        createProductCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Product created successfully", Toast.LENGTH_SHORT).show();
                    // Clear the input fields or perform any other necessary actions
                } else {
                    Toast.makeText(requireContext(), "Product creation failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(requireContext(), "Product creation failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
