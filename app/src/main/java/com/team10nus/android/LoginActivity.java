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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
        OkHttpClient client = SSLHelper.getUnsafeOkHttpClient(LoginActivity.this);

        // Building the request body
        String formBody = "email=" + encodeURIComponent(email) +
                "&password=" + encodeURIComponent(password);

        RequestBody body = RequestBody.create(formBody, MediaType.parse("application/x-www-form-urlencoded"));

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

    // Helper method to URL-encode strings
    private String encodeURIComponent(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            Log.e("Encoding Error", "Error encoding parameter", e);
            return null;
        }
    }
}