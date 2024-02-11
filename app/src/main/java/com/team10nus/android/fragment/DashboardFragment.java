package com.team10nus.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.team10nus.android.R;
import com.team10nus.android.activity.SleepActivity;
import com.team10nus.android.utility.SSLHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DashboardFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DashboardFragment() {}

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        fetchUserDetails();
        fetchFitnessMetrics();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        CardView sleepCardView = view.findViewById(R.id.sleepCardView);
        sleepCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event
                Intent intent = new Intent(getContext(), SleepActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void fetchUserDetails() {
        // Use SSLHelper to get a custom OkHttpClient instance
        OkHttpClient client = SSLHelper.getUnsafeOkHttpClient(getContext()); // Use getContext() to get the Context

        String url = "https://10.0.2.2:8080/api/users/show/1";

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
                        final String firstName = jsonObject.getString("firstName");
                        final String lastName = jsonObject.getString("lastName");

                        // Update UI on the main thread
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Make sure getView() is not null
                                    View view = getView();
                                    if (view != null) {
                                        TextView nameTextView = view.findViewById(R.id.nameTextView);
                                        nameTextView.setText(firstName + " " + lastName);
                                    }
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void fetchFitnessMetrics() {
        // Use SSLHelper to get a custom OkHttpClient instance
        OkHttpClient client = SSLHelper.getUnsafeOkHttpClient(getContext()); // Use getContext() to get the Context

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

                    // Log the response data
                    Log.d("DashboardFragment", "Response data: " + result);

                    try {
                        // Parse the JSON response
                        JSONObject jsonObject = new JSONObject(result);
                        final double weight = jsonObject.getDouble("weight");
                        final double height = jsonObject.getDouble("height");
                        final int sleepHours = jsonObject.getInt("sleepHours");
                        final int waterConsumption = jsonObject.getInt("waterConsumption");
                        final int meditationDuration = jsonObject.getInt("meditationDuration");
                        final double bmi = jsonObject.getDouble("bmi");
                        final double foodCaloriesConsumed = jsonObject.getDouble("foodCaloriesConsumed");
                        final double exerciseCaloriesBurned = jsonObject.getDouble("exerciseCaloriesBurned");

                        // Update UI on the main thread
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Make sure getView() is not null
                                    View view = getView();
                                    if (view != null) {
                                        TextView sleepHoursTextView = view.findViewById(R.id.sleepHoursTextView);
                                        TextView waterConsumptionTextView = view.findViewById(R.id.waterConsumptionTextView);
                                        TextView bmiTextView = view.findViewById(R.id.bmiTextView);
                                        TextView foodCaloriesConsumedTextView = view.findViewById(R.id.foodCaloriesConsumedTextView);
                                        TextView exerciseCaloriesBurnedTextView = view.findViewById(R.id.exerciseCaloriesBurnedTextView);
                                        ProgressBar caloriesProgressBar = view.findViewById(R.id.caloriesProgressBar);
                                        TextView caloriesProgressBarTextView = view.findViewById(R.id.caloriesProgressTextView);

                                        sleepHoursTextView.setText(String.valueOf(sleepHours + " hours"));
                                        waterConsumptionTextView.setText(String.valueOf(waterConsumption + " liters"));
                                        bmiTextView.setText(String.valueOf(bmi));
                                        foodCaloriesConsumedTextView.setText(String.valueOf(foodCaloriesConsumed));
                                        exerciseCaloriesBurnedTextView.setText(String.valueOf(exerciseCaloriesBurned));

                                        caloriesProgressBar.setProgress((int) (exerciseCaloriesBurned / foodCaloriesConsumed * 100));
                                        caloriesProgressBarTextView.setText(exerciseCaloriesBurned + "/" + foodCaloriesConsumed);
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    private void updateUI(String data) {
        View view = getView();
        if (view != null) {
//            TextView sleepHoursTextView = view.findViewById(R.id.sleepHoursTextView);
//            sleepHoursTextView.setText("Hours"); // Modify this line as needed to display your data
        }
    }
}
