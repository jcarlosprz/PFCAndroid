package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.ApiService
import com.juancarlos.pfc2023.api.data.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditProfileFragment() : Fragment(R.layout.fragment_profile_edit) {
    lateinit var user: UserData
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Mostrar el bottomNavigation
        val mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation()
        ApiRest.initService()
        getUser("23", view)
        view.findViewById<Button>(R.id.btnPEGuardar).setOnClickListener {
            user.description = view.findViewById<TextView>(R.id.etPEDescription).text.toString()
            user.contactEmail = view.findViewById<TextView>(R.id.etPEEmail).text.toString()
            user.contactPhone = view.findViewById < TextView >(R.id.etPEPhone).text.toString()
            updateUser("23", user)
            mainActivity.goToFragment(ProfileFragment())
        }
        view.findViewById<Button>(R.id.btnPEDelete).setOnClickListener {

            deleteUser("19")
        }

    }


    private fun getUser(id: String, view: View) {
        val call = ApiRest.service.getUserById(id)
        call.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    user = body
                    view.findViewById<TextView>(R.id.tvPEName).text = user.name
                    view.findViewById<TextView>(R.id.tvPEUsername).text = "@" + user.username
                    view.findViewById<TextView>(R.id.etPEDescription).text = user.description
                    view.findViewById<TextView>(R.id.etPEEmail).text = user.contactEmail
                    view.findViewById<TextView>(R.id.etPEPhone).text = user.contactPhone
                } else {
                    Log.e("EditProfileFragment", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("EditProfileFragment", "Error: ${t.message}")
            }
        })
    }


    private fun updateUser(id: String, updatedUser: UserData) {
        val call = ApiRest.service.updateUser(updatedUser, id)
        call.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    // procesa la respuesta aquí
                } else {
                    Log.e("EditProfileFragment", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("EditProfileFragment", "Error: ${t.message}")
            }
        })
    }
    private fun deleteUser(id: String) {
        val call = ApiRest.service.deleteUser(id)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    // El usuario ha sido eliminado con éxito
                } else {
                    // Ha habido un error en la petición DELETE
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // Ha habido un error en la comunicación con el servidor
            }
        })
    }
}