package com.team10nus.android;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

    public DashboardFragment() {
        // Required empty public constructor
    }

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
        fetchFitnessMetrics();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    private void fetchFitnessMetrics() {
        // Use SSLHelper to get a custom OkHttpClient instance
        OkHttpClient client = SSLHelper.getUnsafeOkHttpClient(getContext()); // Use getContext() to get the Context

        String url = "https://10.0.2.2:8080/api/fitness-metrics/1"; // Ensure this URL is reachable from your device

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

                                        sleepHoursTextView.setText(getString(R.string.sleep_hours, sleepHours));
                                        waterConsumptionTextView.setText(getString(R.string.water_consumption, waterConsumption));

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
            TextView sleepHoursTextView = view.findViewById(R.id.sleepHoursTextView);
            sleepHoursTextView.setText("Hours"); // Modify this line as needed to display your data
        }
    }
}
