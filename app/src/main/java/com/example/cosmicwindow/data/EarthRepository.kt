package com.example.cosmicwindow.data

import com.example.cosmicwindow.data.mapper.EarthMapper
import com.example.cosmicwindow.data.network.EarthApi
import com.example.cosmicwindow.model.Earth
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class EarthRepository
    @Inject
    constructor(
        val earthApi: EarthApi,
    ) {
        suspend fun getLatestEarthPictures(): Result<List<Earth>> {
            try {
                return Result.success(earthApi.getLatestEarthImages().map { EarthMapper.map(it) })
            } catch (ex: Exception) {
                return Result.failure(RepositoryException("Something Bad Happened"))
            }
        }
    }
