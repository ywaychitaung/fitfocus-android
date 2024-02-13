package com.team10nus.android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.team10nus.android.R;
import com.team10nus.android.utility.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private ApiService apiService;

    EditText email;
    EditText password;

    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is already logged in
        if (isUserLoggedIn()) {
            // User is already logged in, redirect to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finish LoginActivity so user can't go back to it with the back button
            return; // Don't proceed to setContentView, as we're redirecting
        }

        setContentView(R.layout.activity_login);

        apiService = new ApiService(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();
                userLogin(emailStr, passwordStr);
            }
        });
    }

    private void userLogin(String email, String password) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        apiService.post("https://10.0.2.2:8080/api/auth/login", jsonBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Handle failure
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Handle response
                if (response.isSuccessful()) {
                    // get response body
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String userId = jsonObject.getString("userId");

                        saveLoginInformation(userId, true);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void saveLoginInformation(String userId, boolean isLoggedIn) {
        // Get SharedPreferences object
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
        // Get an Editor object
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Put key-value pairs
        editor.putString("userId", userId); // Example: save user ID
        editor.putBoolean("isLoggedIn", isLoggedIn); // Example: save login state

        // Commit changes
        editor.apply(); // or editor.commit() for synchronous save
    }

    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}