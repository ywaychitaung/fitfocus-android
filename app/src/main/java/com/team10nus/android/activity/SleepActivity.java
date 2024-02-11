package com.team10nus.android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class SleepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        Button updateSleepButton = findViewById(R.id.updateSleepButton);

        updateSleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSleepHours();

                ProgressBar progressBar = findViewById(R.id.circle_progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                fetchFitnessMetrics();
                progressBar.setVisibility(View.GONE);
            }
        });

        fetchFitnessMetrics();
    }

    private void fetchFitnessMetrics() {
        OkHttpClient client = SSLHelper.getUnsafeOkHttpClient(this); // Consider reviewing this for production use

        String url = "https://10.0.2.2:8080/api/fitness-metrics/show/1"; // Ensure this URL is reachable from your device

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
                    Log.d("SleepActivity", "Response data: " + result);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        // Parsing JSON response
                        final int sleepHours = jsonObject.getInt("sleepHours");
                        // Other variables...

                        // UI update must run on the UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView sleepHoursTextView = findViewById(R.id.sleepHours);
                                sleepHoursTextView.setText(sleepHours + " hours");

                                TextView editTextSleepHours = findViewById(R.id.editTextSleepHours);
                                editTextSleepHours.setText(String.valueOf(sleepHours));
                                // Update other views...
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
        String sleepHours = editTextSleepHours.getText().toString();

        int sleepHoursInt = Integer.parseInt(sleepHours);

        OkHttpClient client = SSLHelper.getUnsafeOkHttpClient(getApplicationContext());

        // Modify this if your API requires a different format
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("fitnessMetricsId", 1);
            json.put("sleepHours", sleepHoursInt);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://10.0.2.2:8080/api/fitness-metrics/update/1";
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
                    Intent intent = new Intent(SleepActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
