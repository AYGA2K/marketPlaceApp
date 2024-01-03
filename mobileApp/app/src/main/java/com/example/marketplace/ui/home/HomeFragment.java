package com.example.marketplace.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.marketplace.R;

public class HomeFragment extends Fragment {

    private TextView textViewSignUpLink;
    private TextView textViewLoginLink;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the TextViews for sign-up and login links
        textViewSignUpLink = root.findViewById(R.id.textViewSignUpLink);
        textViewLoginLink = root.findViewById(R.id.textViewLoginLink);

        // Set up click listener for sign-up link
        textViewSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to SignUpFragment using the action ID
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_nav_home_to_signUpFragment);
            }
        });

        // Set up click listener for login link
        textViewLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to LoginFragment using the action ID
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_nav_home_to_loginFragment);
            }
        });

        return root;
    }
}
