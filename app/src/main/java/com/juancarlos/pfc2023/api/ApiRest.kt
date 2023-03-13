package com.juancarlos.pfc2023.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiRest {
    lateinit var service: ApiService
    val URL = "http://10.1.204.75:1337/api/"
    //val URL_IMAGES = "https://image.tmdb.org/t/p/w500"
    val apiKey =
        "1afd7493dbf1c8a2c8ac92f4e7ff1050d138ebff40daa616ae5e7b0bc308328c6088867227f2a5f3778f1f3563bbdcd247fb26a45cc516cc09b72c67312e4ba902dd2cacf59d866d0d4445ab267e0783ed42f8d87d1f50d90de46678bbc7e668c9f83ff0192e8d02a10a2d1e0e0035d2bce13afe034ed40e970229b7cd6deafc"

    fun initService() {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(ApiService::class.java)
    }
}
