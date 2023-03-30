package com.juancarlos.pfc2023.api

data class RegisterResponse(
    val jwt: String,
    val user: User
) {
    data class User(
        val blocked: Boolean,
        val confirmed: Boolean,
        val createdAt: String,
        val email: String,
        val id: Int,
        val provider: String,
        val updatedAt: String,
        val username: String
    )
}