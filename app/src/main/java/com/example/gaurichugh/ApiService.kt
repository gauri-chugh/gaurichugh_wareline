package com.example.gaurichugh

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("curated")
    suspend fun getCuratedPhotos(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): PhotoResponse
}
