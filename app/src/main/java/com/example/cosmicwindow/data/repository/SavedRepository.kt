package com.example.cosmicwindow.data.repository

import android.content.Context
import android.util.Log
import com.example.cosmicwindow.data.local.SavedItemDAO
import com.example.cosmicwindow.data.mapper.ApodToSavedItemEntityMapper
import com.example.cosmicwindow.data.mapper.SavedItemEntityMapper
import com.example.cosmicwindow.data.mapper.SavedItemMapper
import com.example.cosmicwindow.data.remote.api.ImageApi
import com.example.cosmicwindow.model.Apod
import com.example.cosmicwindow.model.SavedItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class SavedRepository
@Inject
constructor(
    @ApplicationContext private val context: Context,
    val savedItemDAO: SavedItemDAO,
    val imageApi: ImageApi,
) {
    private val imagesDir = File(context.filesDir, "images")
    private val writeMutex = Mutex()

    suspend fun addImage(imageBytes: ByteArray, fileName: String): Result<String> =
        withContext(Dispatchers.IO) {
            runCatching {
                imagesDir.mkdirs()

                val imageFile = File(imagesDir, fileName)
                imageFile.writeBytes(imageBytes)

                imageFile.absolutePath
            }
        }

    suspend fun getImageFolderSizeMb(): Float =
        withContext(Dispatchers.IO) {
            val bytes = imagesDir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
            bytes / (1024.0 * 1024.0).toFloat()
        }

    fun getImage(fileName: String): Result<String?> {
        return runCatching {
            return Result.success(
                File(imagesDir, fileName).takeIf { it.exists() }?.absolutePath,
            )
        }
    }

    suspend fun removeImage(fileName: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val imageFile = File(imagesDir, fileName)
                if (imageFile.exists() && !imageFile.delete()) {
                    error("Failed to delete image: $fileName")
                }
            }
        }

    suspend fun saveItem(apod: Apod): Result<Unit> {
        writeMutex.withLock {
            try {
                withContext(Dispatchers.IO) {
                    val savedItemEntity = ApodToSavedItemEntityMapper.map(apod)
                    if (apod.hdUrl != null) {
                        val imageBytes = imageApi.download(apod.hdUrl).use { it.bytes() }
                        addImage(
                            imageBytes = imageBytes,
                            fileName = savedItemEntity.imageName,
                        )
                    }
                    val items = savedItemDAO.updateItem(savedItemEntity)
                    Log.w("Repository", "saved item: $items")
                }
            } catch (ex: CancellationException) {
                Log.e("Repository", "addItem: $ex")
                throw ex
            } catch (ex: Exception) {
                Log.e("Repository", "addItem: $ex")
                return Result.failure(RepositoryException("Something Bad Happened"))
            }
            return Result.success(Unit)
        }
    }

    suspend fun loadItems(): Result<List<SavedItem>> {
        try {
            return withContext(Dispatchers.IO) {
                val items =
                    savedItemDAO
                        .getAll()
                        .map(SavedItemEntityMapper::map)
                        .map { it.copy(imagePath = getImage(it.imageName).getOrNull()) }

                Log.w("Repository", "loadItems got items: ${items.size}")
                Result.success(items)
            }
        } catch (ex: CancellationException) {
            Log.e("Repository", "loadItems: $ex")
            throw ex
        } catch (ex: Exception) {
            Log.e("Repository", "loadItems: $ex")
            return Result.failure(RepositoryException("Something Bad Happened"))
        }
    }

    suspend fun isSaved(postId: Long): Result<Boolean> =
        try {
            Result.success(
                withContext(Dispatchers.IO) {
                    savedItemDAO.isSaved(postId)
                },
            )
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Exception) {
            Result.failure(RepositoryException("Could not read saved items: ${ex.message}"))
        }

    suspend fun findItemByCreatedDate(createdDate: LocalDate): Result<SavedItem?> {
        try {
            return withContext(Dispatchers.IO) {
                val item = savedItemDAO.getByDateCreated(createdDate.toString())
                val savedItem = if (item != null) SavedItemEntityMapper.map(item) else null
                Log.w("Repository", "findItemById got item: $item")
                Result.success(savedItem)
            }
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Exception) {
            Log.e("Repository", "findItemById: $ex")
            return Result.failure(RepositoryException("Something Bad Happened"))
        }
    }

    suspend fun deleteItem(item: SavedItem): Result<Unit> {
        try {
            return withContext(Dispatchers.IO) {
                Log.w("Repository", "deleteItem got item: $item")
                removeImage(item.imageName)
                Result.success(savedItemDAO.deleteItem(SavedItemMapper.map(item)))
            }
        } catch (ex: CancellationException) {
            Log.e("Repository", "deleteItem: $ex")
            throw ex
        } catch (ex: Exception) {
            Log.e("Repository", "deleteItem: $ex")
            return Result.failure(RepositoryException("Something Bad Happened"))
        }
    }

    suspend fun deleteItem(item: Apod): Result<Unit> {
        writeMutex.withLock {
            try {
                return withContext(Dispatchers.IO) {
                    val savedItem = ApodToSavedItemEntityMapper.map(item)
                    removeImage(savedItem.imageName)
                    Log.w("Repository", "deleteItem got item: $item")
                    Result.success(savedItemDAO.deleteItemByPostId(savedItem.postId))
                }
            } catch (ex: CancellationException) {
                Log.e("Repository", "deleteItem: $ex")
                throw ex
            } catch (ex: Exception) {
                Log.e("Repository", "deleteItem: $ex")
                return Result.failure(RepositoryException("Something Bad Happened"))
            }
        }
    }
}
