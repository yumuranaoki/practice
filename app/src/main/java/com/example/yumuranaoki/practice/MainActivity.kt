package com.example.yumuranaoki.practice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String> {
    var disposable: Disposable? = null
    // dagger2
    @Inject lateinit var newWallet: NewWallet

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<String> = ResultLoader(this)

    override fun onLoadFinished(loader: Loader<String>, data: String?) {
        val resultText = findViewById<TextView>(R.id.result)
        if (data != null) {
            resultText.text = data
        } else {
            resultText.text = "network error"
        }
        supportLoaderManager.destroyLoader(0)
    }

    override fun onLoaderReset(loader: Loader<String>) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportLoaderManager.initLoader(0, null, this)
        val privateKeyText = findViewById<TextView>(R.id.privateKey)
        DaggerNewWalletComponent.create().inject(this)
        val information: String = newWallet.information
        privateKeyText.text = information

        // retrofit
        val walletApi = walletApi.create()

        // rxjava
        disposable = walletApi.getWallet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ result ->
                    val privateKey = result.privateKey
                    privateKeyText.text = privateKey
                }

        // rxBinding
        val button = findViewById<Button>(R.id.button)
        val buttonObservable = RxView.clicks(button)
        buttonObservable
            .observeOn(Schedulers.io())
            .map {
                val result = httpGet("http://10.0.2.2:3000/")
                result
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
                if (it != null) {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    // okHttp
    private fun httpGet(url: String): String? {
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
}
