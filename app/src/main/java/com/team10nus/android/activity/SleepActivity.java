package com.team10nus.android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team10nus.android.R;
import com.team10nus.android.utility.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SleepActivity extends AppCompatActivity {
    private int sleepHours = 0;
    private int waterConsumption = 0;

    private double weight = 0;
    private double height = 0;

    private ApiService apiService; // Declare ApiService

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        apiService = new ApiService(this); // Initialize ApiService

        Button updateSleepButton = findViewById(R.id.updateSleepButton);
        ProgressBar progressBar = findViewById(R.id.circle_progress_bar);

        updateSleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSleepHours();
                progressBar.setVisibility(View.VISIBLE);
                fetchFitnessMetrics();
                progressBar.setVisibility(View.GONE);
            }
        });

        fetchFitnessMetrics();
    }

    private void fetchFitnessMetrics() {
        String url = "https://10.0.2.2:8080/api/fitness-metrics/show/1";

        apiService.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(result);

                        sleepHours = jsonObject.getInt("sleepHours");
                        waterConsumption = jsonObject.getInt("waterConsumption");
                        height = jsonObject.getDouble("height");
                        weight = jsonObject.getDouble("weight");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUI();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateSleepHours() {
        EditText editTextSleepHours = findViewById(R.id.editTextSleepHours);
        int sleepHoursInt = Integer.parseInt(editTextSleepHours.getText().toString());

        JSONObject json = new JSONObject();
        try {
            json.put("fitnessMetricsId", 1);
            json.put("sleepHours", sleepHoursInt);
            json.put("waterConsumption", waterConsumption);
            json.put("height", height);
            json.put("weight", weight);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://10.0.2.2:8080/api/fitness-metrics/update/1";

        apiService.put(url, json, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SleepActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    private void updateUI() {
        TextView sleepHoursTextView = findViewById(R.id.sleepHours);
        sleepHoursTextView.setText(sleepHours + " hours");

        EditText editTextSleepHours = findViewById(R.id.editTextSleepHours);
        editTextSleepHours.setText(String.valueOf(sleepHours));
    }
}
