package com.juancarlos.pfc2023.api.data

data class UserAdsListResponse(
    val ads: MutableList<Ad>,
    val blocked: Boolean,
    val confirmed: Boolean,
    val contactEmail: String,
    val contactPhone: String,
    val createdAt: String,
    val description: String,
    val email: String,
    val id: Int,
    val imgURL: String,
    val name: String,
    val provider: String,
    val updatedAt: String,
    val username: String
) {
    data class Ad(
        val createdAt: String,
        val description: String,
        val id: Int,
        val modality: String,
        val price: Double,
        val subject: String,
        val ubication: String,
        val adProfesor: Boolean,
        val updatedAt: String
    )
}