package com.example.cosmicwindow.data.network

import com.example.cosmicwindow.data.dto.EarthDto
import retrofit2.http.GET

interface EarthApi {
    @GET(".")
    suspend fun getLatestEarthImages(): List<EarthDto>
}
