package com.example.cosmicwindow.data.mapper

import com.example.cosmicwindow.data.dto.EarthDto
import com.example.cosmicwindow.model.Earth
import tech.mappie.api.ObjectMappie
import java.time.format.DateTimeFormatter

object EarthMapper : ObjectMappie<EarthDto, Earth>() {
    override fun map(from: EarthDto): Earth =
        mapping {
            to::imageUrl fromProperty from::imageName transform {
                val monthFormatter = DateTimeFormatter.ofPattern("MM")
                val dayFormatter = DateTimeFormatter.ofPattern("dd")
                "https://epic.gsfc.nasa.gov/archive/enhanced/${from.date.year}/${from.date.format(
                    monthFormatter,
                )}/${from.date.format(dayFormatter)}/jpg/${from.imageName}.jpg"
            }
        }
}
