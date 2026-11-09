package com.example.cosmicwindow.data

import com.example.cosmicwindow.data.network.ExploreApi
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class ExploreRepository
    @Inject
    constructor(
        val exploreApi: ExploreApi,
    ) {
//    suspend fun getLatestImages(): Result<List<Explore>> {
//        try {
//            return Result.success(exploreApi.getLatestEarthImages().map { EarthMapper.map(it) })
//
//
//        } catch (ex: UnknownHostException) {
//            Log.e("Repository", ex.toString())
//            return Result.failure(RepositoryException("No internet connection, please retry when online"))
//        } catch (ex: CancellationException) {
//            Log.e("Repository", ex.toString())
//            throw ex
//        } catch (ex: Exception) {
//            Log.e("Repository", ex.toString())
//            return Result.failure(RepositoryException("Something Bad Happened"))
//        }
//    }
    }
