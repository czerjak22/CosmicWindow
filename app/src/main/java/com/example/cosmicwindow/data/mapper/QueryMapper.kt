package com.example.cosmicwindow.data.mapper

import com.example.cosmicwindow.data.dto.explore.QueryRequestDto
import com.example.cosmicwindow.model.QueryExplore
import tech.mappie.api.ObjectMappie

object QueryMapper : ObjectMappie<QueryExplore, QueryRequestDto>() {
    override fun map(from: QueryExplore): QueryRequestDto =
        mapping {
            to::filters fromProperty from::filters transform { filterList ->
                val combinedFilter = StringBuilder(10)
                filterList.forEach { filter -> if (filter.isActive) combinedFilter.append(filter.value) }

                combinedFilter.toString().ifEmpty {
                    null
                }
            }
        }
}



