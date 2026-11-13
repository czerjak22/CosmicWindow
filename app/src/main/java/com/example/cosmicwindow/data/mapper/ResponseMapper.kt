package com.example.cosmicwindow.data.mapper


import com.example.cosmicwindow.data.dto.explore.SearchResponseDto
import com.example.cosmicwindow.model.SearchResult
import com.example.cosmicwindow.model.SearchResultItem
import java.time.Instant
import java.time.ZoneOffset


object ResponseMapper {
    fun map(from: SearchResponseDto): SearchResult {

        val items = from.items.filter { item -> item.data.isNotEmpty() }.map { item ->
            val data = item.data.first();
            val thumbnailLink = item.links.firstOrNull { item -> item.rel == "preview" }
            var originalLink = item.links.firstOrNull { item -> item.rel == "canonical" }

            if (originalLink == null || originalLink.href.contains(regex = Regex("\\.tif$"))) {
                originalLink =
                    item.links.firstOrNull { item -> item.rel == "alternate" && item.href.contains("~large") }
            }

            SearchResultItem(
                nasaId = data.nasaId,
                title = data.title,
                dateCreated = Instant.parse(data.dateCreated).atZone(ZoneOffset.UTC).toLocalDate(),
                thumbnailUrl = thumbnailLink?.href,
                originalUrl = originalLink?.href,
                description = data.description ?: data.description508 ,
                center = data.center,
            )
        }
        return SearchResult(searchItems = items, totalFinds = from.totalHits)
    }

}



