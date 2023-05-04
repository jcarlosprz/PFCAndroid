package com.juancarlos.pfc2023.api

import com.juancarlos.pfc2023.api.data.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    //Registrar Usuario
    @POST("auth/local/register")
    fun meterUser(
        @Body request: RegisterData
    ): Call<RegisterResponse>

    //Logear Usuario
    @POST("auth/local")
    fun login(
        @Body credentials: LoginCredentials
    ): Call<LoginResponse>

    //Lista Ads
    /**
    @GET("/api/ads") //
    fun getAds(): Call<AdsResponse>
*/
    @PUT("users/{id}") //
    fun updateUser(
        @Body updatedUser: UserData,
        @Path("id") id: String
    ): Call<UserData>

    @GET("users/{id}")
    fun getUserById(@Path("id") id: String): Call<UserData>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id: String): Call<Unit>
}