package com.juancarlos.pfc2023.api

import com.juancarlos.pfc2023.api.data.*
import retrofit2.Call
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

    //Actualizar Usuario
    @PUT("users/{id}") //
    fun updateUser(
        @Body updatedUser: UserData,
        @Path("id") id: String
    ): Call<UserData>

    //Get del Usuario
    @GET("users/{id}")
    fun getUserById(@Path("id") id: String): Call<UserData>

    //Borrar Usuario
    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id: String): Call<Unit>

    //Get Lista Ads
    @GET("ads?populate=*") //
    fun getAds(): Call<AdsListResponse>

    @GET("users/{id}?populate=ads")
    fun getUserAds(@Path("id") id: String): Call<UserAdsResponse>

}