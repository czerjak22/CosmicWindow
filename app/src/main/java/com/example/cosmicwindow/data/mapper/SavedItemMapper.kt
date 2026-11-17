package com.example.cosmicwindow.data.mapper

import com.example.cosmicwindow.data.local.entity.SavedItemEntity
import com.example.cosmicwindow.model.Apod
import com.example.cosmicwindow.model.MediaType
import com.example.cosmicwindow.model.SavedItem
import java.time.LocalDate
import kotlin.text.Typography.copyright

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
                    hdurl = from.pictureLink,
                    mediaType = MediaType.PICTURE,
                    videoUrl = null,
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
            pictureLink = from.hdurl,
            title = from.title,
            explanation = from.explanation,
            permalink = from.permalink,
            credit = from.credit,
            copyright = from.copyright,
            alt = from.alt,
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
            pictureLink = from.apod.hdurl,
            title = from.apod.title,
            explanation = from.apod.explanation,
            permalink = from.apod.permalink,
            credit = from.apod.credit,
            copyright = from.apod.copyright,
            alt = from.apod.alt,
        )
}
