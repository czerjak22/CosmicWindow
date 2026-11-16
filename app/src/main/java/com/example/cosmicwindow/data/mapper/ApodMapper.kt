package com.example.cosmicwindow.data.mapper

import com.example.cosmicwindow.core.utils.extractVideoLink
import com.example.cosmicwindow.data.dto.ApodDto
import com.example.cosmicwindow.model.Apod
import com.example.cosmicwindow.model.MediaType
import tech.mappie.api.ObjectMappie

object ApodMapper : ObjectMappie<ApodDto, Apod>() {
    override fun map(from: ApodDto) =
        mapping {
            // to::credit fromProperty from::credit transform { it.stripHtml() }
            // to::copyright fromProperty from::copyright transform { it.stripHtml() }
            // to::explanation fromProperty from::explanation transform { it.stripHtml() }
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
