package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.adapters.MyPostsAdapter
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.data.UserAdsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyPostsFragment() : Fragment(R.layout.fragment_myposts) {
    var myads= emptyList<UserAdsResponse.Ad>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Mostrar el bottomNavigation
        val mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation()

        //Crear Adapter
        var rvSaved = view.findViewById<RecyclerView>(R.id.rvSaved)
        rvSaved.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvSaved.adapter = MyPostsAdapter(myads)

        getUserAds("23")
        view.findViewById<Button>(R.id.addPost).setOnClickListener {

        }
    }
    private fun getUserAds(id: String) {
        val call = ApiRest.service.getUserAds(id)
        call.enqueue(object : Callback<UserAdsResponse> {
            override fun onResponse(call: Call<UserAdsResponse>, response: Response<UserAdsResponse>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    myads = body.ads
                    var rvSaved = view?.findViewById<RecyclerView>(R.id.rvSaved)
                    rvSaved?.adapter?.notifyDataSetChanged()
                    Log.i("getUserAds", response.body()!!.ads.toString())


                } else {
                    Log.e("getUserAds", response.errorBody()?.string() ?: "Error getting user ads:")
                }
            }
            override fun onFailure(call: Call<UserAdsResponse>, t: Throwable) {
                Log.e("getUser", "Error: ${t.message}")
            }
        })
    }

}