package com.example.deliverytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView userName;
    private TextView logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        userName = findViewById(R.id.userName);
        logoutButton = findViewById(R.id.logoutButton);

        if (currentUser != null) {
            // Try to get display name first
            String displayName = currentUser.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                userName.setText(displayName);
            } else {
                // If no display name, use email
                String email = currentUser.getEmail();
                // Remove everything after @ in email
                if (email != null) {
                    String name = email.split("@")[0];
                    // Capitalize first letter
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    userName.setText(name);
                } else {
                    userName.setText("User");
                }
            }
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, SignInActivity.class));
                finish();
            }
        });


        // Set click listeners for menu items
        // This is commented out for now as we don't have the layouts yet
        // setupMenuItems();
    }

    /*
    private void setupMenuItems() {
        // This method is commented out as we don't have these layouts yet
        // Uncomment and implement when you have the corresponding layouts

        // findViewById(R.id.myAccountLayout).setOnClickListener(v -> {
        //     // Handle My Account click
        // });

        // findViewById(R.id.addressLayout).setOnClickListener(v -> {
        //     // Handle Address click
        // });

        // findViewById(R.id.favoritesLayout).setOnClickListener(v -> {
        //     // Handle Favorites click
        // });

        // findViewById(R.id.orderHistoryLayout).setOnClickListener(v -> {
        //     // Handle Order History click
        // });

        // findViewById(R.id.settingsLayout).setOnClickListener(v -> {
        //     // Handle Settings click
        // });

        // findViewById(R.id.helpCenterLayout).setOnClickListener(v -> {
        //     // Handle Help Center click
        // });
    }
    */
}