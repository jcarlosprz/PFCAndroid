package com.juancarlos.pfc2023.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("auth/local/register")
    fun meterUser(
        @Body request: RegisterData
    ): Call<RegisterResponse>

    @POST("auth/local")
    fun login(
        @Body credentials: LoginCredentials
    ): Call<LoginResponse>
}