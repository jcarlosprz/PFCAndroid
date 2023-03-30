package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.LoginCredentials
import com.juancarlos.pfc2023.api.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment() : Fragment(R.layout.fragment_login) {
    val TAG = "MainActivity"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Ocultar el bottomnavigation
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation()
        ApiRest.initService()


        view.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            login("correo@gmail.com", "contraseaaaA1")
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, SearchFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    private fun login(usernameOrEmail: String, password: String) {

        val credentials = LoginCredentials(usernameOrEmail, password)
        val call = ApiRest.service.login(credentials)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    var registroResponse = response.body()
                    Log.i(TAG, "${registroResponse.toString()}")
                } else {
                    Log.e(TAG, response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
        //------------------------------------------------------------


    }
}