package com.example.marketplace.ui.add_products;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.marketplace.R;
import com.example.marketplace.databinding.FragmentAddProductsBinding;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class AddProductsFragment extends Fragment {

    private FragmentAddProductsBinding binding;
    private EditText editTextProductName;
    private EditText editTextProductDescription;
    private EditText editTextProductPrice;
    private Button buttonChooseImage;
    private ImageView imageViewSelected;
    private Button buttonSubmitProduct;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

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
        buttonChooseImage = root.findViewById(R.id.buttonChooseImage);
        imageViewSelected = root.findViewById(R.id.imageViewSelected);
        buttonSubmitProduct = root.findViewById(R.id.buttonSubmitProduct);

        // Set up click listener for the choose image button
        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        // Set up click listener for the submit button
        buttonSubmitProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event, e.g., retrieve data and upload image
                String productName = editTextProductName.getText().toString();
                String productDescription = editTextProductDescription.getText().toString();
                String productPrice = editTextProductPrice.getText().toString();
                // Call your method to upload the image along with other data
                // uploadProduct(productName, productDescription, productPrice, imageUri);
            }
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
}
