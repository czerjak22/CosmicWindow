package com.example.cosmicwindow.data.mapper

import com.example.cosmicwindow.data.local.entity.SavedItemEntity
import com.example.cosmicwindow.model.Apod
import com.example.cosmicwindow.model.SavedItem
import java.time.LocalDate
import tech.mappie.api.ObjectMappie

object SavedItemEntityMapper : ObjectMappie<SavedItemEntity, SavedItem>() {
    override fun map(from: SavedItemEntity): SavedItem =
        mapping {
            to::savedDate fromProperty from::savedDate transform {
                LocalDate.parse(from.savedDate)
            }
        }
}

object SavedItemMapper : ObjectMappie<SavedItem, SavedItemEntity>() {


}

object ApodTOSavedItemMapper : ObjectMappie<Apod, SavedItem>() {
    override fun map(from: Apod): SavedItem =
        mapping {
            to::imageName fromProperty from::date transform {
                "${from.date}.jpg"
            }
        }
}
