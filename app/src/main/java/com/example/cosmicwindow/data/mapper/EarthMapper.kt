package com.example.cosmicwindow.data.mapper

import com.example.cosmicwindow.data.remote.dto.earth.EarthDto
import com.example.cosmicwindow.model.CentroidPosition
import com.example.cosmicwindow.model.Earth
import com.example.cosmicwindow.model.Position
import tech.mappie.api.ObjectMappie
import java.time.format.DateTimeFormatter

object EarthMapper : ObjectMappie<EarthDto, Earth>() {
    override fun map(from: EarthDto): Earth =
        mapping {
            to::imageUrl fromProperty from::imageName transform {
                val monthFormatter = DateTimeFormatter.ofPattern("MM")
                val dayFormatter = DateTimeFormatter.ofPattern("dd")
                "https://epic.gsfc.nasa.gov/archive/enhanced/${from.date.year}/${
                    from.date.format(
                        monthFormatter,
                    )
                }/${from.date.format(dayFormatter)}/jpg/${from.imageName}.jpg"
            }

            to::centroidCoordinates fromProperty from::centroidCoordinates transform {
                CentroidPosition(lat = from.centroidCoordinates.lat, lon = from.centroidCoordinates.lon)
            }
            to::telescopePosition fromProperty from::telescopePosition transform {
                Position(x = from.telescopePosition.x, y = from.telescopePosition.y, z = from.telescopePosition.z)
            }
        }
}
