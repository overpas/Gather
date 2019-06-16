package com.github.overpass.gather.model.data

import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class HttpClient private constructor(private val client: OkHttpClient) {

    fun get(url: String,
            callback: Callback,
            requestBody: RequestBody?,
            vararg paramsNamesAndValues: String) {
        val urlBuilder = HttpUrl.parse(url)!!.newBuilder()
        for (i in 0 until paramsNamesAndValues.size - 1) {
            urlBuilder.addQueryParameter(paramsNamesAndValues[i], paramsNamesAndValues[i + 1])
        }
        val finalUrl = urlBuilder.build().toString()
        request("GET", finalUrl, requestBody, callback)
    }

    private fun request(method: String,
                        url: String,
                        requestBody: RequestBody?,
                        callback: Callback) {
        val request = Request.Builder()
                .method(method, requestBody)
                .url(url)
                .build()
        client.newCall(request).enqueue(callback)
    }

    companion object {

        @Volatile
        private var instance: HttpClient? = null
        private val lock = Any()

        @JvmStatic
        fun getInstance(): HttpClient = instance ?: synchronized(lock) {
            instance ?: HttpClient(OkHttpClient.Builder().build()).also {
                instance = it
            }
        }
    }
}
