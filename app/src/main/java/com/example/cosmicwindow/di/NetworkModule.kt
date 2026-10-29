package com.example.cosmicwindow.di

import android.util.Log
import com.example.cosmicwindow.data.network.ApodApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
//        val logging = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
        val requestLogger = Interceptor { chain ->
            val request = chain.request()
            Log.d("API_REQUEST", "${request.method} ${request.url}")
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(requestLogger)
            .build()
    }

    @Provides
    @Singleton
    fun provideApodApi(client: OkHttpClient): ApodApi =
        Retrofit.Builder()
            .baseUrl("https://science.nasa.gov/wp-json/wp/v2/apod-basic/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApodApi::class.java)
}