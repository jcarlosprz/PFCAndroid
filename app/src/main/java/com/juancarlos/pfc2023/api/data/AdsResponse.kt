package com.juancarlos.pfc2023.api.data

data class AdsResponse(
    val `data`: List<Data>,
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

}