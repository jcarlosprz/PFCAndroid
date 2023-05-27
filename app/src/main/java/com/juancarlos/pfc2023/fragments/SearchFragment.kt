package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.adapters.AnunciosAdapter
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.data.AdsListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment() : Fragment(R.layout.fragment_search) {
    lateinit var mainActivity: MainActivity
    lateinit var adsList: List<AdsListResponse.Data>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        //Mostrar el bottoNavigation
        mainActivity.showBottomNavigation()
        ApiRest.initService()
        getAds()
    }

    private fun getAds() {
        val call = ApiRest.service.getAds()
        call.enqueue(object : Callback<AdsListResponse> {
            override fun onResponse(
                call: Call<AdsListResponse>,
                response: Response<AdsListResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    adsList = body.data
                    var rvUserInfo = view?.findViewById<RecyclerView>(R.id.rvAnuncios)
                    rvUserInfo?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    rvUserInfo?.adapter = AnunciosAdapter(adsList)
                    Log.i("getAds", adsList.toString())
                } else {
                    Log.e("getAds", response.errorBody()?.string() ?: "Error getting user:")
                }
            }

            override fun onFailure(call: Call<AdsListResponse>, t: Throwable) {
                Log.e("getAdsOnFailure", "Error: ${t.message}")
            }
        })
    }

}