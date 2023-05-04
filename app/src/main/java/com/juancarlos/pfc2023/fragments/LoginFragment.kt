package com.juancarlos.pfc2023.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.data.LoginCredentials
import com.juancarlos.pfc2023.api.data.LoginResponse
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.JWTParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException


class LoginFragment() : Fragment(R.layout.fragment_login) {
    val TAG = "MainActivity"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Ocultar el bottomnavigation
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation()

        //Radio Button Recuerdame
        val radioButton = view.findViewById<RadioButton>(R.id.rbRecordar)
        var isChecked = false
        radioButton.setOnClickListener {
            isChecked = !isChecked
            radioButton.isChecked = isChecked
        }

        //Iniciamos el APIRest
        ApiRest.initService()

        view.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            var email = view.findViewById<EditText>(R.id.etLoginEmail).text.toString()
            var password = view.findViewById<EditText>(R.id.etLoginPassword).text.toString()
           // login(email, password)
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, SearchFragment())?.addToBackStack(null)
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
                    val tokenString = response.body()?.jwt
                    //Sacar id del usuario loggeado con el token
                    try {
                        val jwt: JWT = JWTParser.parse(tokenString)
                        val claimsSet: JWTClaimsSet = jwt.jwtClaimsSet
                        val userId: Int = claimsSet.getIntegerClaim("id")

                        //Guardamos el id y el token en local
                        val sharedPreferences =
                            requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("token", tokenString)
                        editor.putInt("userID", userId)
                        editor.apply()

                        /**
                        //Obtenemos los datos del local
                        val sharedPreferencesGet = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
                        val getToken = sharedPreferencesGet.getString("token", "")
                        val getID = sharedPreferencesGet.getInt("userID", 0)
                        Log.i(TAG, "$getToken $getID")
                         */
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.container, SearchFragment())?.addToBackStack(null)
                            ?.commit()
                    } catch (e: ParseException) {
                        Log.e(TAG, "Failed to parse JWT token: ${e.message}")
                    }


                } else {
                    Log.e(TAG, response.errorBody()?.string() ?: "Error")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "Error en la solicitud de login: ${t.message}")
            }

        })
    }
}