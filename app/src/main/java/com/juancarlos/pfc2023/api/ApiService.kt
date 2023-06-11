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
    @GET("ads?")
    fun getAdsFiltered(
        @Query("filters[\$or][0][subject][\$containsi]") subject: String,
        @Query("filters[\$or][1][description][\$containsi]") description: String,
        @Query("filters[\$or][2][modality][\$containsi]") modality: String,
        @Query("filters[\$or][3][price][\$containsi]") price: String,
        @Query("filters[\$or][4][ubication][\$containsi]") ubication: String,
        @Query("filters[adProfesor]") adProfesor: Boolean,
        @Query("populate") populate: String
    ): Call<AdsListResponse>

    @GET("users/{id}?")
    fun getUserAds(
        @Path("id") id: String,
        @Query("populate") populate: String = "ads",
    ): Call<UserAdsListResponse>

    @POST("ads")
    fun createAd(
        @Body AdData: AdData
    ): Call<AdData>
}