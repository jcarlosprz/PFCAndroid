package com.juancarlos.pfc2023.api.data

data class AdsListResponse(
    val `data`: List<Data>,
) {
    data class Data(
        val attributes: Attributes,
        val id: Int
    ) {
        data class Attributes(
            val description: String,
            val modality: String,
            val price: Double,
            val subject: String,
            val ubication: String,
        )
    }
}