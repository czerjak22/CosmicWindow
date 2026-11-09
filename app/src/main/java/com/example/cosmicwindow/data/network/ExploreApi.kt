package com.example.cosmicwindow.data.network

import com.example.cosmicwindow.data.dto.AssetResponseDto
import com.example.cosmicwindow.data.dto.SearchResponseDto
import kotlinx.serialization.json.JsonObject
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExploreApi {
    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("media_type") mediaType: String = "image",
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
    ): SearchResponseDto

    @GET("asset/{nasaId}")
    suspend fun getAssets(
        @Path("nasaId") nasaId: String,
    ): AssetResponseDto

    @GET("metadata/{nasaId}")
    suspend fun getMetadata(
        @Path("nasaId") nasaId: String,
    ): JsonObject
}
