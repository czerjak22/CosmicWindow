package com.example.cosmicwindow.model

data class QueryExplore(
    val filters: List<Filter> = emptyList(),
    val query: String = "",
    val pageSize: Int = 10,
    val page: Int = 1,
    val mediaType: String = "image",
) {
    override fun toString(): String {
        return "query:$query, page:$page,filters:${
            filters.filter { it.isActive }.joinToString(" ") { it.value }
        }"
    }
}

