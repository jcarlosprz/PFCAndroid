package com.juancarlos.pfc2023.api.data

data class AdsListResponse(
    val `data`: List<Data>,
    val meta: Meta
) {
    data class Data(
        val attributes: Attributes,
        val id: Int
    ) {
        data class Attributes(
            val createdAt: String,
            val description: String,
            val modality: String,
            val price: Double,
            val subject: String,
            val ubication: String,
            val updatedAt: String
        )
    }

    data class Meta(
        val pagination: Pagination
    ) {
        data class Pagination(
            val page: Int,
            val pageCount: Int,
            val pageSize: Int,
            val total: Int
        )
    }
}