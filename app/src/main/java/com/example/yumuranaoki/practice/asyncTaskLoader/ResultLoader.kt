package com.example.yumuranaoki.practice

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import okhttp3.OkHttpClient
import okhttp3.Request

class ResultLoader(context: Context) : AsyncTaskLoader<String>(context) {
    override fun loadInBackground(): String? {
        val result: String? = httpGet("http://10.0.2.2:3000")
        return result
    }

    override fun onStartLoading() {
        forceLoad()
    }

    override fun onStopLoading() {
        cancelLoad()
    }
}

fun httpGet(url: String): String? {
    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()
    val response = client.newCall(request).execute()
    val message = response.message()
    if (message == "OK") {
        val result = response.body()!!.string()
        return result
    }
    return null
}