package com.team10nus.android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.team10nus.android.R;
import com.team10nus.android.utility.SSLHelper;

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

public class GoalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitGoals();
            }
        });

        fetchGoals();
    }

    private void fetchGoals() {
        // Use SSLHelper to get a custom OkHttpClient instance
        OkHttpClient client = SSLHelper.getUnsafeOkHttpClient(getApplicationContext());

        String url = "https://10.0.2.2:8080/api/users/goals/show/1"; // Ensure this URL is reachable from your device

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();

                    try {
                        // Parse the JSON response
                        JSONObject jsonObject = new JSONObject(result);
                        double startingWeight = jsonObject.getDouble("startingWeight");
                        double goalWeight = jsonObject.getDouble("goalWeight");

                        // Update the UI on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EditText goalWeightEditText = findViewById(R.id.goalWeightEditText);
                                goalWeightEditText.setText(String.valueOf(goalWeight));
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    private void submitGoals() {
        EditText goalWeightEditText = findViewById(R.id.goalWeightEditText); // Replace with your EditText's ID
        String newGoalWeight = goalWeightEditText.getText().toString();

        if (!newGoalWeight.isEmpty()) {
            double goalWeightValue = Double.parseDouble(newGoalWeight);

            OkHttpClient client = SSLHelper.getUnsafeOkHttpClient(getApplicationContext());

            // Modify this if your API requires a different format
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject json = new JSONObject();
            try {
                json.put("id", 1);
                json.put("goalWeight", goalWeightValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = "https://10.0.2.2:8080/api/users/goals/update/1";
            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    // Log the error or handle the failure
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    // Handle the successful response
                    if (response.isSuccessful()) {
                        // Existing code to handle the response
                        Intent intent = new Intent(GoalsActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } else {
            // Handle case where no new goal weight is entered
        }
    }
}