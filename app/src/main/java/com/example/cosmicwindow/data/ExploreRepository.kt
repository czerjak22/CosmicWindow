package com.example.cosmicwindow.data

import android.util.Log
import android.util.LruCache
import com.example.cosmicwindow.data.mapper.QueryMapper
import com.example.cosmicwindow.data.mapper.ResponseMapper
import com.example.cosmicwindow.data.network.ExploreApi
import com.example.cosmicwindow.model.QueryExplore
import com.example.cosmicwindow.model.SearchResult
import com.example.cosmicwindow.model.SearchResultItem
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class ExploreRepository
@Inject
constructor(
    private val exploreApi: ExploreApi,
) {

    private val cache = LruCache<String, SearchResultItem>(200)
    suspend fun getSearchByQuery(query: QueryExplore): Result<SearchResult> {
        try {
            val queryRequest = QueryMapper.map(query)
            Log.d("Repository", "newSearch with params: $query")

            val response = exploreApi.search(
                queryRequest.query,
                page = queryRequest.page,
                pageSize = queryRequest.pageSize,
                keywords = queryRequest.filters,
            )
            val resultSearch = ResponseMapper.map(response)
            resultSearch.searchItems.forEach { cache.put(it.nasaId, it) }
            return Result.success(resultSearch)

        } catch (ex: UnknownHostException) {
            Log.e("Repository", "getSearchByQuery: $ex")
            return Result.failure(RepositoryException("No internet connection, please retry when online"))
        } catch (ex: CancellationException) {
            Log.e("Repository", "getSearchByQuery: $ex")
            throw ex
        } catch (ex: Exception) {
            Log.e("Repository", "getSearchByQuery: $ex")
            return Result.failure(RepositoryException("Something Bad Happened"))
        }
    }

    suspend fun getItemDetailsById(id: String): Result<SearchResultItem> {
        try {

            val find = cache.get(id)
            if (find != null) {
                Log.d("Repository", "Got element nasaId:$id from cache")
                return Result.success(find)
            }

            val response = exploreApi.searchById(
                nasaId = id
            )
            val resultSearch = ResponseMapper.map(response)
            resultSearch.searchItems.forEach { cache.put(it.nasaId, it) }
            if (resultSearch.totalFinds > 1) {
                return Result.failure(RepositoryException("The given nasaId has multiple items!"))
            }
            return Result.success(resultSearch.searchItems.first())

        } catch (ex: UnknownHostException) {
            Log.e("Repository", "getItemDetailsById: $ex")
            return Result.failure(RepositoryException("No internet connection, please retry when online"))
        } catch (ex: CancellationException) {
            Log.e("Repository", "getItemDetailsById: $ex")
            throw ex
        } catch (ex: Exception) {
            Log.e("Repository", "getItemDetailsById: $ex")
            return Result.failure(RepositoryException("Something Bad Happened"))
        }
    }
}
