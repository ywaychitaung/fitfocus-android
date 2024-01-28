package com.team10nus.android;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;

    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);

        OkHttpClient client = SSLHelper.getUnsafeOkHttpClient(LoginActivity.this);

        JSONObject loginForm = new JSONObject();
        try {
            loginForm.put("email", email);
            loginForm.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(loginForm.toString(),
                MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url("https://10.0.2.2:8080/api/auth/login") // Use 10.0.2.2 for Android emulator
                .post(body)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        // Handle the response on the UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Update your UI here based on responseData
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        // Handle the error on the UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Update your UI here to show error
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    // Optionally handle the exception on the UI thread
                }
            }
        }).start();
    }

}