package com.github.overpass.gather.model.data;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpClient {

    private static volatile HttpClient instance;
    private static final Object lock = new Object();

    private final OkHttpClient client;

    private HttpClient(OkHttpClient client) {
        this.client = client;
    }

    public static HttpClient getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new HttpClient(new OkHttpClient.Builder().build());
                }
            }
        }
        return instance;
    }

    public void get(String url, Callback callback, String... paramsNamesAndValues) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        for (int i = 0; i < paramsNamesAndValues.length - 1; i++) {
            urlBuilder.addQueryParameter(paramsNamesAndValues[i], paramsNamesAndValues[i + 1]);
        }
        String finalUrl = urlBuilder.build().toString();
        request("GET", finalUrl, null, callback);
    }

    public void request(String method, String url, RequestBody requestBody, Callback callback) {
        Request request = new Request.Builder()
                .method(method, requestBody)
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
