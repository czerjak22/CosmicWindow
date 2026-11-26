package com.example.cosmicwindow.data.mapper

import com.example.cosmicwindow.data.local.entity.SavedItemEntity
import com.example.cosmicwindow.model.Apod
import com.example.cosmicwindow.model.SavedItem
import java.time.LocalDate

object SavedItemEntityMapper {
    fun map(from: SavedItemEntity): SavedItem =
        SavedItem(
            id = from.id,
            imageName = from.imageName,
            savedDate = LocalDate.parse(from.dateSaved),
            apod =
                Apod(
                    date = LocalDate.parse(from.dateCreated),
                    title = from.title,
                    permalink = from.permalink,
                    explanation = from.explanation,
                    credit = from.credit,
                    copyright = from.copyright,
                    alt = from.alt,
                    url = from.permalink,
                    hdUrl = from.pictureLink,
                    mediaType = from.mediaType,
                    videoUrl = from.videoUrl,
                    postId = from.postId,
                ),
        )
}

object ApodToSavedItemEntityMapper {
    fun map(from: Apod): SavedItemEntity =
        SavedItemEntity(
            id = 0,
            imageName = "${from.date}.jpg",
            dateSaved = LocalDate.now().toString(),
            postId = from.postId,
            dateCreated = from.date.toString(),
            pictureLink = from.hdUrl,
            title = from.title,
            explanation = from.explanation,
            permalink = from.permalink,
            credit = from.credit,
            copyright = from.copyright,
            alt = from.alt,
            mediaType = from.mediaType,
            videoUrl = from.videoUrl,
        )
}

object SavedItemMapper {
    fun map(from: SavedItem): SavedItemEntity =
        SavedItemEntity(
            id = from.id,
            imageName = from.imageName,
            dateSaved = from.savedDate.toString(),
            postId = from.apod.postId,
            dateCreated = from.apod.date.toString(),
            pictureLink = from.apod.hdUrl,
            title = from.apod.title,
            explanation = from.apod.explanation,
            permalink = from.apod.permalink,
            credit = from.apod.credit,
            copyright = from.apod.copyright,
            alt = from.apod.alt,
            mediaType = from.apod.mediaType,
            videoUrl = from.apod.videoUrl,
        )
}
