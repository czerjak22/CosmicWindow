package com.example.cosmicwindow.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.cosmicwindow.data.local.entity.SavedItemEntity

@Database(
    entities = [SavedItemEntity::class],
    version = 3,
)
abstract class CosmicWindowDatabase : RoomDatabase() {
    abstract fun savedItemDAO(): SavedItemDAO
}
