package com.team10nus.android.utility;

import android.content.Context;

import org.json.JSONObject;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiService {
    private OkHttpClient client;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public ApiService(Context context) {
        this.client = SSLHelper.getUnsafeOkHttpClient(context);
    }

    public void post(String url, JSONObject jsonBody, Callback callback) {
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(callback);
    }

    public void put(String url, JSONObject jsonBody, Callback callback) {
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder().url(url).put(body).build();
        client.newCall(request).enqueue(callback);
    }

    public void get(String url, Callback callback) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
