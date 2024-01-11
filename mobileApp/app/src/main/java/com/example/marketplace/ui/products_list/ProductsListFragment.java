package com.example.marketplace.ui.products_list;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketplace.R;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class ProductsListFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Product> productList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_products_list, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Fetch data from the API
        new FetchProductsTask().execute();

        return root;
    }

    private class FetchProductsTask extends AsyncTask<Void, Void, List<Product>> {
        @Override
        protected List<Product> doInBackground(Void... voids) {
            return fetchDataFromApi();
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            // Update UI with the fetched data
            productList = products;
            ProductAdapter productAdapter = new ProductAdapter(productList);
            recyclerView.setAdapter(productAdapter);
        }
    }

    private List<Product> fetchDataFromApi() {
        List<Product> productList = new ArrayList<>();

        try {
            // Replace "your_api_endpoint" with the actual endpoint of your API
            URL url = new URL("http://192.168.11.128:8080/product/list");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                // Set up the HTTP request
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");

                // Check if the response code is successful
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // Read the response data
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(in);

                    // Parse the JSON response and populate the productList
                    productList = parseJsonResponse(response);
                } else {
                    // Handle the error, e.g., show an error message
                }
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return productList;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        inputStream.close();
        return sb.toString();
    }

    private List<Product> parseJsonResponse(String jsonResponse) {
        List<Product> productList = new ArrayList<>();


        try {
            Gson gson = new Gson();
            Type productListType = new TypeToken<List<Product>>() {}.getType();
            productList = gson.fromJson(jsonResponse, productListType);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        return productList;
    }
}
