package com.example.cosmicwindow.data.remote.dto.earth

import com.example.cosmicwindow.data.remote.serializer.LocaleDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class EarthDto(
    @SerialName("image") val imageName: String,
    @Serializable(LocaleDateTimeSerializer::class)
    val date: LocalDateTime,
    val caption: String,
    @SerialName("centroid_coordinates") val centroidCoordinates: CentroidCoordinates,
    @SerialName("dscovr_j2000_position") val telescopePosition: TelescopePosition,
)
