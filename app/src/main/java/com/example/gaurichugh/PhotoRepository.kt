package com.example.gaurichugh

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class PhotoRepository {
    private val apiService: ApiService

    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()

        apiService = retrofit.create()
    }

    suspend fun getCuratedPhotos(perPage: Int, page: Int) = apiService.getCuratedPhotos(perPage, page)
}
