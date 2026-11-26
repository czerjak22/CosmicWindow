package com.example.cosmicwindow.data.mapper

import com.example.cosmicwindow.core.utils.extractVideoLink
import com.example.cosmicwindow.data.remote.dto.ApodDto
import com.example.cosmicwindow.model.Apod
import com.example.cosmicwindow.model.MediaType
import tech.mappie.api.ObjectMappie

object ApodMapper : ObjectMappie<ApodDto, Apod>() {
    override fun map(from: ApodDto) =
        mapping {
            to::mediaType fromProperty from::mediaType transform {
                when (it) {
                    "video" -> MediaType.VIDEO
                    else -> MediaType.PICTURE
                }
            }
            to::videoUrl fromProperty from::basicHtml transform {
                it.extractVideoLink()
            }
        }
}
