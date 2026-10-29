package com.example.cosmicwindow.data

import com.example.cosmicwindow.data.mapper.ApodMapper
import com.example.cosmicwindow.data.network.ApodApi
import com.example.cosmicwindow.model.Apod
import javax.inject.Inject
import javax.inject.Singleton
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Singleton
class ApodRepository @Inject constructor(
    val apodApi: ApodApi,
) {
    suspend fun getApodWeek(localDate: LocalDate = LocalDate.now()): List<Apod> {
        val startDate = localDate.format(DateTimeFormatter.ofPattern("yyMMdd"))
        val endDate = localDate.minusDays(7).format(DateTimeFormatter.ofPattern("yyMMdd"))
        return apodApi.getApodRange(startDate, endDate).map { ApodMapper.map(it) }
    }

    suspend fun getApod(localDate: LocalDate = LocalDate.now()): Apod {
        val startDate = localDate.format(DateTimeFormatter.ofPattern("yyMMdd"))
        return ApodMapper.map(apodApi.getApodByDate(startDate))
    }

}