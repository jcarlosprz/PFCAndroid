package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.data.RegisterData
import com.juancarlos.pfc2023.api.data.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment() : Fragment(R.layout.fragment_register) {
    val TAG = "MainActivity"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ApiRest.initService()


        //Ocultar el bottomNavigation
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation()


        view.findViewById<EditText>(R.id.etRegisterDate).setOnClickListener() {
            showDatePickerDialog()
        }

        view.findViewById<Button>(R.id.btnRegister).setOnClickListener {
            var email = view.findViewById<EditText>(R.id.etRegisterEmail).text.toString()
            var username = view.findViewById<EditText>(R.id.etRegisterName).text.toString()
            var password = view.findViewById<EditText>(R.id.etRegisterPassword).text.toString()
            register(email, password, username)
            mainActivity.goToFragment(LoginFragment())
        }

        view.findViewById<Button>(R.id.btnLoginirRegistro).setOnClickListener() {
            mainActivity.goToFragment(LoginFragment(), true)
        }

    }

    private fun register(email: String, password: String, username: String) {
        val crearUser = RegisterData(email, password, username)
        val call = ApiRest.service.meterUser(crearUser)
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    var registroResponse = response.body()
                    print(registroResponse)
                } else {
                    Log.e(TAG, response.errorBody()?.string()?: "Error")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        getActivity()?.let { datePicker.show(it.getSupportFragmentManager(), "datePicker") }
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        var numberMonth = month + 1;
        view?.findViewById<EditText>(R.id.etRegisterDate)
            ?.setText("$day/$numberMonth/$year")
    }

}