package com.example.cosmicwindow.data

import android.util.Log
import com.example.cosmicwindow.data.mapper.EarthMapper
import com.example.cosmicwindow.data.network.EarthApi
import com.example.cosmicwindow.model.Earth
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.net.UnknownHostException
import kotlinx.coroutines.CancellationException

@Singleton
class EarthRepository
@Inject
constructor(
    val earthApi: EarthApi,
) {
    suspend fun getLatestEarthPictures(): Result<List<Earth>> {
        try {
            return Result.success(earthApi.getLatestEarthImages().map { EarthMapper.map(it) })
        } catch (ex: UnknownHostException) {
            Log.e("Repository", ex.toString())
            return Result.failure(RepositoryException("No internet connection, please retry when online"))
        } catch (ex: CancellationException) {
            Log.e("Repository", ex.toString())
            throw ex
        } catch (ex: Exception) {
            Log.e("Repository", ex.toString())
            return Result.failure(RepositoryException("Something Bad Happened"))
        }
    }
}
