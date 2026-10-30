package com.example.cosmicwindow.data

import android.util.Log
import com.example.cosmicwindow.data.mapper.ApodMapper
import com.example.cosmicwindow.data.network.ApodApi
import com.example.cosmicwindow.model.Apod
import java.net.UnknownHostException
import java.time.DateTimeException
import javax.inject.Inject
import javax.inject.Singleton
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Singleton
class ApodRepository @Inject constructor(
    val apodApi: ApodApi,
) {
    suspend fun getApodWeek(localDate: LocalDate = LocalDate.now()): Result<List<Apod>> {
        try {
            val startDate = localDate.format(DateTimeFormatter.ofPattern("yyMMdd"))
            val endDate = localDate.minusDays(7).format(DateTimeFormatter.ofPattern("yyMMdd"))
            return Result.success(
                apodApi.getApodRange(startDate, endDate).map { ApodMapper.map(it) })
        } catch (ex: UnknownHostException) {
            Log.e("Repository", ex.toString())
            return Result.failure(RepositoryException("No internet connection, please retry when online"));
        } catch (ex: Exception) {
            Log.e("Repository", ex.toString())
            return Result.failure(ex);
        }

    }

    suspend fun getApod(localDate: LocalDate = LocalDate.now()): Apod {
        val startDate = localDate.format(DateTimeFormatter.ofPattern("yyMMdd"))
        return ApodMapper.map(apodApi.getApodByDate(startDate))
    }

}