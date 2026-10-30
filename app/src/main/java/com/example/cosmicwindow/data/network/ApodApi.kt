package com.example.cosmicwindow.data.network

import com.example.cosmicwindow.data.dto.ApodDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApodApi {
    @GET("{date}")
    suspend fun getApodByDate(
        @Path("date") date: String,
    ): ApodDto


    @GET(".")
    suspend fun getApodRange(
        @Query("date_to") start: String,
        @Query("date_from") end: String,
    ): List<ApodDto>
}