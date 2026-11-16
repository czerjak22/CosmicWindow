package com.example.cosmicwindow.data

import android.content.Context
import android.util.Log
import com.example.cosmicwindow.data.local.SavedItemDAO
import com.example.cosmicwindow.data.local.entity.SavedItemEntity
import com.example.cosmicwindow.data.mapper.SavedItemEntityMapper
import com.example.cosmicwindow.data.mapper.SavedItemMapper
import com.example.cosmicwindow.model.SavedItem
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class SavedRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    val savedItemDAO: SavedItemDAO,
    val savedItemMapper: SavedItemMapper,
    val savedItemEntityMapper: SavedItemEntityMapper,
) {

    private val imagesDir = File(context.filesDir, "images")

    suspend fun addImage(
        imageBytes: ByteArray, fileName: String
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            runCatching {
                imagesDir.mkdirs()

                val imageFile = File(imagesDir, fileName)
                imageFile.writeBytes(imageBytes)

                imageFile.absolutePath
            }
        }
    }

    suspend fun getImage(fileName: String): Result<File?> {
        return runCatching {
            return Result.success(
                File(imagesDir, fileName).takeIf { it.exists() })
        }
    }

    suspend fun removeImage(
        fileUrl: String
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val imageFile = File(fileUrl)
                if (imageFile.exists() && !imageFile.delete()) {
                    error("Failed to delete image: $fileUrl")
                }
            }
        }
    }

    suspend fun loadItems(): Result<List<SavedItem>> {
        try {
            val items = savedItemDAO.getAll().map(savedItemEntityMapper::map)
            Log.w("Repository", "loadItems got items: ${items.size}")
            return Result.success(items)
        } catch (ex: UnknownHostException) {
            Log.e("Repository", "loadItems: $ex")
            return Result.failure(RepositoryException("No internet connection, please retry when online"))
        } catch (ex: CancellationException) {
            Log.e("Repository", "loadItems: $ex")
            throw ex
        } catch (ex: Exception) {
            Log.e("Repository", "loadItems: $ex")
            return Result.failure(RepositoryException("Something Bad Happened"))
        }
    }

    suspend fun findItemById(id: Int): Result<SavedItem?> {
        try {
            val item = savedItemDAO.getById(id)
            val savedItem = if (item != null) savedItemEntityMapper.map(item) else null
            Log.w("Repository", "loadItem got item: $item")
            return Result.success(savedItem)
        } catch (ex: UnknownHostException) {
            Log.e("Repository", "loadItems: $ex")
            return Result.failure(RepositoryException("No internet connection, please retry when online"))
        } catch (ex: CancellationException) {
            Log.e("Repository", "loadItems: $ex")
            throw ex
        } catch (ex: Exception) {
            Log.e("Repository", "loadItems: $ex")
            return Result.failure(RepositoryException("Something Bad Happened"))
        }
    }

    suspend fun deleteItem(item: SavedItem): Result<Unit> {
        try {
            Log.w("Repository", "loadItem got item: $item")
            return Result.success(savedItemDAO.deleteItem(savedItemMapper.map(item)))
        } catch (ex: UnknownHostException) {
            Log.e("Repository", "loadItems: $ex")
            return Result.failure(RepositoryException("No internet connection, please retry when online"))
        } catch (ex: CancellationException) {
            Log.e("Repository", "loadItems: $ex")
            throw ex
        } catch (ex: Exception) {
            Log.e("Repository", "loadItems: $ex")
            return Result.failure(RepositoryException("Something Bad Happened"))
        }
    }
}
