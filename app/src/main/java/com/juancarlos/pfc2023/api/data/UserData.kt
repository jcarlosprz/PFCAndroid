package com.juancarlos.pfc2023.api.data

data class UserData(
    var blocked: Boolean,
    var confirmed: Boolean,
    var contactEmail: String,
    var contactPhone: String,
    var createdAt: String,
    var description: String,
    var email: String,
    var id: Int,
    var imgURL: String,
    var name: String,
    var provider: String,
    var updatedAt: String,
    var username: String
)