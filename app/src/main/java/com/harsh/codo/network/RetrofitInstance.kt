package com.harsh.codo.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: Judge0ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://ce.judge0.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Judge0ApiService::class.java)
    }
} 