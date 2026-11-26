package com.example.cosmicwindow.data.remote.api

import com.example.cosmicwindow.data.remote.dto.earth.EarthDto
import retrofit2.http.GET

interface EarthApi {
    @GET(".")
    suspend fun getLatestEarthImages(): List<EarthDto>
}
