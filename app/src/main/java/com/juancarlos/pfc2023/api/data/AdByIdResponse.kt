package com.juancarlos.pfc2023.api.data

data class AdByIdResponse(
    val `data`: Data,
) {
    data class Data(
        var attributes: Attributes,
        var id: Int
    ) {
        data class Attributes(
            var adProfesor: Boolean,
            var createdAt: String,
            var description: String,
            var modality: String,
            var price: Double,
            var subject: String,
            var ubication: String,
            var updatedAt: String
        )
    }
}