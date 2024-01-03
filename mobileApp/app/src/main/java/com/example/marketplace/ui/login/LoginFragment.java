package com.example.marketplace.ui.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.marketplace.R;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private TextView textViewSignUpLink;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);

        View root = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize UI components
        editTextUsername = root.findViewById(R.id.editTextUsername);
        editTextPassword = root.findViewById(R.id.editTextPassword);
        buttonLogin = root.findViewById(R.id.buttonLogin);
        textViewSignUpLink = root.findViewById(R.id.textViewSignUpLink);

        // Set up click listener for the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform login when the button is clicked
                performLogin();
            }
        });

        // Set up click listener for the signup link
        textViewSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to SignUpFragment (you need to implement navigation logic)
                // For example:
                // Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment);
            }
        });

        return root;
    }

    private void performLogin() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validate input (you may add more validation as needed)

        // Create a JSON string with login data
        String jsonData = "{\"email\":\"" + username + "\",\"password\":\"" + password + "\"}";

        // Perform login in a background thread
        new LoginTask().execute(jsonData);
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                // Replace "your_server_ip_or_hostname" with the actual IP address or hostname of your server
                URL url = new URL("http://192.168.11.128:8080/user/login");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    // Set up the HTTP request
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);

                    // Write the JSON data to the output stream
                    try (OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream())) {
                        byte[] input = params[0].getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }

                    // Check if the response code is successful
                    return urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK;
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Login successful
                Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_productsListFragment);
            } else {
                // Login failed
                Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
