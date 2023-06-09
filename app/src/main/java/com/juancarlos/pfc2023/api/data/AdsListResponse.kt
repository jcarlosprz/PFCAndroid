package com.juancarlos.pfc2023.api.data

data class AdsListResponse(
    val `data`: List<Data>
) {
    data class Data(
        val attributes: Attributes,
        val id: Int
    ) {
        data class Attributes(
            val createdAt: String,
            val creator: Creator,
            val description: String,
            val modality: String,
            val price: Double,
            val subject: String,
            val ubication: String,
            val adProfesor: Boolean,
            val updatedAt: String
        ) {
            data class Creator(
                val `data`: CreatorData
            ) {
                data class CreatorData(
                    val attributes: CreatorAttributes,
                    val id: Int
                ) {
                    data class CreatorAttributes(
                        val blocked: Boolean,
                        val confirmed: Boolean,
                        val contactEmail: String,
                        val contactPhone: String,
                        val createdAt: String,
                        val description: String,
                        val email: String,
                        val imgURL: String,
                        val name: String,
                        val provider: String,
                        val updatedAt: String,
                        val username: String
                    )
                }
            }
        }
    }
}