package com.temp.randomfact.data.repository.remote

import com.temp.randomfact.data.model.FactInfo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("random")
    suspend fun getRandomFact(@Query("language") language: String): FactInfo

    @GET("today")
    suspend fun getTodayFact(@Query("language") language: String = "en"): FactInfo

    @GET("/{id}")
    suspend fun getFactBasedId(@Path("id") factId: String): FactInfo
}