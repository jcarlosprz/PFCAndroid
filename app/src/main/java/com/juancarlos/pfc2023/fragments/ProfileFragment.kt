package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.data.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment() : Fragment(R.layout.fragment_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Mostrar el bottomNavigation
        val mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation()

        view.findViewById<Button>(R.id.btnEditar)
            .setOnClickListener { mainActivity.goToFragment(EditProfileFragment()) }


    }
    private fun getUserById(id: String) {
        val call = ApiRest.service.getUserById(id)
        call.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                // manejar la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    // procesar la respuesta aquí
                } else {
                    Log.e("TAG", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("TAG", "Error: ${t.message}")
            }
        })
    }

}