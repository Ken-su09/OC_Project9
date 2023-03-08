package com.suonk.oc_project9.api

import androidx.annotation.VisibleForTesting
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PlacesApiHolder {
    private const val BASE_URL = "https://maps.googleapis.com/"

    fun getInstance(): PlacesApiService {
        return getInstance(BASE_URL.toHttpUrl())
    }

    @VisibleForTesting
    fun getInstance(baseUrl: HttpUrl): PlacesApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlacesApiService::class.java)
    }
}