package com.example.cosmicwindow.data.local

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Upsert
import com.example.cosmicwindow.data.local.entity.SavedItemEntity

@Dao
interface SavedItemDAO {
    @Query("SELECT * from saved_apods")
    suspend fun getAll(): List<SavedItemEntity>

    @Query("SELECT * from saved_apods WHERE id =:id")
    suspend fun getById(id: Int): SavedItemEntity?

    @Insert
    suspend fun insertItem(item: SavedItemEntity): Long

    @Upsert
    suspend fun updateItem(item: SavedItemEntity): Long

    @Delete
    suspend fun deleteItem(item: SavedItemEntity)


}
