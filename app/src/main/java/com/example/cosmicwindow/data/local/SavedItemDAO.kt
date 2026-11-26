package com.example.cosmicwindow.data.local

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Upsert
import com.example.cosmicwindow.data.local.entity.SavedItemEntity

@Dao
interface SavedItemDAO {
    @Query("SELECT * from saved_apods")
    suspend fun getAll(): List<SavedItemEntity>

    @Query("SELECT * FROM saved_apods WHERE date_created=:dateCreated")
    suspend fun getByDateCreated(dateCreated: String): SavedItemEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM saved_apods WHERE post_id = :postId)")
    suspend fun isSaved(postId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: SavedItemEntity): Long

    @Upsert
    suspend fun updateItem(item: SavedItemEntity): Long

    @Delete
    suspend fun deleteItem(item: SavedItemEntity): Int

    @Query("DELETE FROM saved_apods WHERE post_id= :postId")
    suspend fun deleteItemByPostId(postId: Long): Int
}
