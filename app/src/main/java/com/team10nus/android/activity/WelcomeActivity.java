package com.team10nus.android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.team10nus.android.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is already logged in
        if (isUserLoggedIn()) {
            // User is already logged in, redirect to MainActivity
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finish LoginActivity so user can't go back to it with the back button
            return; // Don't proceed to setContentView, as we're redirecting
        }

        setContentView(R.layout.activity_welcome);

        Button login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}