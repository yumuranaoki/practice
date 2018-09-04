package com.example.yumuranaoki.practice

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface walletApi {
    @GET("wallet")
    fun getWallet(): Observable<Wallet>

    companion object {
        fun create(): walletApi {
            val retrofit =  Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            val walletApi = retrofit.create(walletApi::class.java)
            return walletApi
        }
    }
}