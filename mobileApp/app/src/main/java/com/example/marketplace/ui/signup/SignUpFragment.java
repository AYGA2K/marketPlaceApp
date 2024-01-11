package com.example.marketplace.ui.signup;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.marketplace.R;

import com.example.marketplace.databinding.FragmentSignUpBinding;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SignUpFragment extends Fragment {

	private FragmentSignUpBinding binding;

	private EditText editTextNameSignUp, editTextEmailSignUp, editTextPasswordSignUp;
	private Button buttonSignUp;

	public View onCreateView(@NonNull LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		SignUpViewModel signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

		binding = FragmentSignUpBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		// Add signup UI components
		editTextNameSignUp = root.findViewById(R.id.editTextNameSignUp);
		editTextEmailSignUp = root.findViewById(R.id.editTextEmailSignUp);
		editTextPasswordSignUp = root.findViewById(R.id.editTextPasswordSignUp);
		buttonSignUp = root.findViewById(R.id.buttonSignUp);

		buttonSignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Perform signup when the button is clicked
				performSignUp();
			}
		});

		return root;
	}

	private void performSignUp() {
		String name = editTextNameSignUp.getText().toString().trim();
		String email = editTextEmailSignUp.getText().toString().trim();
		String password = editTextPasswordSignUp.getText().toString().trim();

		// Validate input (you may add more validation as needed)

		// Create a JSON string with user data
		String jsonData = "{\"name\":\"" + name + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";

		// Perform signup in a background thread
		new SignUpTask().execute(jsonData);
	}

	private class SignUpTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				// Replace "your_server_ip_or_hostname" with the actual IP address or hostname
				// of your server
				URL url = new URL("http://192.168.11.128:8080/user/signup");
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
				// Signup successful
				Toast.makeText(requireContext(), "Signup successful", Toast.LENGTH_SHORT).show();
				// navigate to login screen
				Navigation.findNavController(requireView()).navigate(R.id.action_signupFragment_to_loginFragment);
			} else {
				// Signup failed
				Toast.makeText(requireContext(), "Signup failed", Toast.LENGTH_SHORT).show();
				Log.e("SignupTask", "Failed. Check server logs for details.");
			}
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}
