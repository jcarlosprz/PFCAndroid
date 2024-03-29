package com.juancarlos.pfc2023.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.data.LoginCredentials
import com.juancarlos.pfc2023.api.data.LoginResponse
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.JWTParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import kotlin.properties.Delegates


class LoginFragment : Fragment(R.layout.fragment_login) {
    lateinit var mainActivity: MainActivity

    //Boton recuerdame
    var isChecked by Delegates.notNull<Boolean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        //Configurar bottomNavigation al abrir el teclado
        mainActivity.setupKeyboardVisibilityListener(false)
        //Ocultar el bottomnavigation
        mainActivity.hideBottomNavigation()

        //Comprobar si hay una sesión iniciada
        var currentUserId = mainActivity.getCurrentUser()
        if (currentUserId > 0) {
            mainActivity.goToFragment(SearchFragment())
        }

        //Hacer Login al pulsar el botón
        ApiRest.initService()
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.etLoginEmail).text.toString()
            val password = view.findViewById<EditText>(R.id.etLoginPassword).text.toString()
            login(email, password)
        }

        //Radio Button Recuerdame
        val radioButton = view.findViewById<RadioButton>(R.id.rbRecordar)
        loadRememberMe(radioButton)
        radioButton.setOnClickListener {
            isChecked = !isChecked
            // Establecer el estado del RadioButton según el nuevo valor de isChecked
            radioButton.isChecked = isChecked
            val sharedPreferences =
                requireContext().getSharedPreferences("remember", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            // Guardar el nuevo valor de isChecked en SharedPreferences
            editor.putBoolean("isChecked", isChecked)
            editor.apply()
        }


        val btnirRegistro = view.findViewById<Button>(R.id.btnLoginirRegistro)
        btnirRegistro.setOnClickListener {
            mainActivity.goToFragment(RegisterFragment(), true)
        }

    }

    //Consulta para el Login
    private fun login(usernameOrEmail: String, password: String) {
        val credentials = LoginCredentials(usernameOrEmail, password)
        val call = ApiRest.service.login(credentials)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    val tokenString = response.body()?.jwt
                    try {
                        saveLogin(tokenString!!)
                        saveRememberMe(isChecked)
                        mainActivity.goToFragment(SearchFragment(), false)
                    } catch (e: ParseException) {
                        Log.e("LoginFragment", "Failed to parse JWT token: ${e.message}")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody)
                    val errorObject = errorJson.getJSONObject("error")
                    val errorMessage = errorObject.getString("message")
                    var tvError = view?.findViewById<TextView>(R.id.tvLoginError)
                    if (errorMessage == "identifier is a required field" || errorMessage == "password is a required field") {
                        tvError?.text = "Usuario o contraseña incorrectos"
                    } else if (errorMessage == "Invalid identifier or password") {
                        tvError?.text = "Usuario o contraseña incorrectos"
                    }

                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginFragment", "Error en la solicitud de login: ${t.message}")
            }

        })
    }

    //Guardamos el id y el token en local
    private fun saveLogin(JWTtoken: String) {
        //Sacar id del usuario loggeado con el token
        val jwt: JWT = JWTParser.parse(JWTtoken)
        val claimsSet: JWTClaimsSet = jwt.jwtClaimsSet
        val userId: Int = claimsSet.getIntegerClaim("id")

        val sharedPreferences = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", JWTtoken)
        editor.putInt("userID", userId)
        editor.apply()
    }

    //Carga del SharedPreferences los datos del Remember me
    private fun loadRememberMe(radioButton: RadioButton) {
        val sharedPreferences =
            requireContext().getSharedPreferences("remember", Context.MODE_PRIVATE)
        isChecked = sharedPreferences.getBoolean("isChecked", false)
        radioButton?.isChecked = isChecked

        if (isChecked) {
            val user = sharedPreferences.getString("user", "")
            val password = sharedPreferences.getString("password", "")
            view?.findViewById<TextView>(R.id.etLoginEmail)?.text = user
            view?.findViewById<TextView>(R.id.etLoginPassword)?.text = password
        }
    }

    private fun saveRememberMe(isChecked: Boolean) {
        val sharedPreferences =
            requireContext().getSharedPreferences("remember", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Guardar el nuevo valor de isChecked en SharedPreferences
        editor.putBoolean("isChecked", isChecked)

        // Si isChecked es true, guardar el usuario y la contraseña en SharedPreferences
        if (isChecked) {
            val user = view?.findViewById<TextView>(R.id.etLoginEmail)?.text.toString()
            val password = view?.findViewById<TextView>(R.id.etLoginPassword)?.text.toString()

            editor.putString("user", user)
            editor.putString("password", password)
        } else {
            // Si isChecked es false, eliminar el usuario y la contraseña de SharedPreferences
            editor.remove("user")
            editor.remove("password")
        }
        editor.apply()
    }
}