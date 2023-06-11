package com.juancarlos.pfc2023.api.data

data class AdData(
    val `data`: Data
) {
    data class Data(
        val creator: Int,
        val description: String,
        val modality: String,
        val price: Double,
        val subject: String,
        val ubication: String,
        val adProfesor: Boolean
    )
}