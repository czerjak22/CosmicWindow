package com.example.cosmicwindow.data.network

import com.example.cosmicwindow.data.dto.explore.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ExploreApi {
    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("media_type") mediaType: String = "image",
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("keywords") keywords: String?,
    ): SearchResponseDto


}
