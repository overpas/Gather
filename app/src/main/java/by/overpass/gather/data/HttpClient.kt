package by.overpass.gather.data

import okhttp3.*
import javax.inject.Inject

class HttpClient @Inject constructor(private val client: OkHttpClient) {

    fun get(url: String,
            callback: Callback,
            requestBody: RequestBody?,
            vararg paramsNamesAndValues: String) {
        val urlBuilder = HttpUrl.parse(url)!!.newBuilder()
        for (i in 0 until paramsNamesAndValues.size - 1) {
            urlBuilder.addQueryParameter(paramsNamesAndValues[i], paramsNamesAndValues[i + 1])
        }
        val finalUrl = urlBuilder.build().toString()
        request(GET, finalUrl, requestBody, callback)
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

        private const val GET = "GET"
    }
}
