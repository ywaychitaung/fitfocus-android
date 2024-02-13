package com.team10nus.android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;

import com.team10nus.android.R;

import java.io.ByteArrayOutputStream;

public class FoodActivity extends AppCompatActivity {
    // A constant to track the request code of the image capture activity
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

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
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // TODO: Implement the API call to your server with the imageBytes array
        // This typically involves setting up using a library OkHttp to make the request
    }

    // TODO: Implement the response handler for the server's response
    private void handleServerResponse(String response) {
        // Process the response from the server after sending the image
    }
}