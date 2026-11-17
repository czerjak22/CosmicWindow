package com.example.cosmicwindow.data.local.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.time.LocalDate

@Entity("saved_apods")
data class SavedItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "image_name") val imageName: String,
    @ColumnInfo(name = "date_saved") val dateSaved: String = LocalDate.now().toString(),
    @ColumnInfo(name = "post_id") val postId: Long,
    @ColumnInfo(name = "date_created") val dateCreated: String,
    @ColumnInfo(name = "picture_link") val pictureLink: String? = null,
    val title: String,
    val explanation: String,
    val permalink: String,
    val credit: String,
    val copyright: String?,
    val alt: String,
)
