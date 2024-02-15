package com.team10nus.android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;

import com.team10nus.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GymActivity extends AppCompatActivity {

    // A constant to track the request code of the image capture activity
    static final int REQUEST_IMAGE_CAPTURE = 1;

    TextView textViewCalories;

    String postUrl = "https://localhost:5000/api/gym-equipments";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        textViewCalories = findViewById(R.id.textViewCalories);

        CardView scanCardView = findViewById(R.id.scanCardView);

        scanCardView.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // Call the super class version

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // TODO: Send the image to your server
            sendImageToServer(imageBitmap);
        }
    }


    private void sendImageToServer(Bitmap image) {
        // Convert the Bitmap to a byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        RequestBody postBodyImage = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "androidFlask.jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
                .build();

        postRequest(postUrl, postBodyImage);
    }

    void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle the error
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseString = response.body().string();
                    runOnUiThread(() -> {
                        // Handle the response
                        handleServerResponse(responseString);
                    });
                } else {
                    // Handle the error
                }
            }
        });
    }

    private void handleServerResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            int estimatedCaloriesBurned = jsonResponse.getInt("estimated_calories_burned");
            String prediction = jsonResponse.getString("prediction");
            JSONObject probabilities = jsonResponse.getJSONObject("probabilities");

            // Update UI elements or internal state based on the response
            // For example:
//            textViewCalories.setText("Calories: " + estimatedCaloriesBurned);
            // textViewPrediction.setText("Prediction: " + prediction);

            // You might want to iterate over the probabilities JSONObject if needed
            // and update your UI or process data accordingly.

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}