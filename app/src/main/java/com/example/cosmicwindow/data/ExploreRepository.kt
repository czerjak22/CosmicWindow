package com.example.cosmicwindow.data

import android.util.Log
import com.example.cosmicwindow.data.mapper.QueryMapper
import com.example.cosmicwindow.data.mapper.ResponseMapper
import com.example.cosmicwindow.data.network.ExploreApi
import com.example.cosmicwindow.model.QueryExplore
import com.example.cosmicwindow.model.SearchResult
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
    suspend fun getSearchByQuery(query: QueryExplore): Result<SearchResult> {
        try {
            val queryRequest = QueryMapper.map(query)
            Log.d("Repository","newSearch with params: $query")
            return Result.success(
                ResponseMapper.map(
                    exploreApi.search(
                        queryRequest.query,
                        page = queryRequest.page,
                        pageSize = queryRequest.pageSize,
                        keywords = queryRequest.filters,
                    )
                )
            )

        } catch (ex: UnknownHostException) {
            Log.e("Repository", ex.toString())
            return Result.failure(RepositoryException("No internet connection, please retry when online"))
        } catch (ex: CancellationException) {
            Log.e("Repository", ex.toString())
            throw ex
        } catch (ex: Exception) {
            Log.e("Repository", ex.toString())
            return Result.failure(RepositoryException("Something Bad Happened"))
        }
    }
}
