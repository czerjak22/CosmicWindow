package com.example.cosmicwindow.data.local.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.time.LocalDate

@Entity("saved_apods")
data class SavedItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "image_name") val imageName: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "saved_date") val savedDate: String = LocalDate.now().toString(),
    )


