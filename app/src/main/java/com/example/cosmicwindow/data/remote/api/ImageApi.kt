package com.example.cosmicwindow.data.remote.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface ImageApi {
    @Streaming
    @GET
    suspend fun download(
        @Url url: String,
    ): ResponseBody
}
