package com.example.cloudraya.API

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService (baseUrl : String) {
//    private const val BASE_URL ="https://api.cloudraya.com/"

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Set the desired log level
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create()) // Optional: Gson converter for JSON serialization/deserialization
        .client(client)
        .build()

    val instanceRetrofit = retrofit.create(UserApi::class.java)

//    val instanceRetrofit : UserApi
//        get() = client.create(UserApi::class.java)

}