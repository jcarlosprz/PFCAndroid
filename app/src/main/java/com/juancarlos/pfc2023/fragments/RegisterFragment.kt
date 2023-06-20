package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.data.RegisterData
import com.juancarlos.pfc2023.api.data.RegisterResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment() : Fragment(R.layout.fragment_register) {
    lateinit var mainActivity: MainActivity
    val TAG = "MainActivity"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ApiRest.initService()


        //Ocultar el bottomNavigation
        mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation()


        view.findViewById<EditText>(R.id.etRegisterDate).setOnClickListener() {
            showDatePickerDialog()
        }

        view.findViewById<Button>(R.id.btnRegister).setOnClickListener {
            var name = view.findViewById<EditText>(R.id.etRegisterName).text.toString()
            var username = view.findViewById<EditText>(R.id.etRegisterUsername).text.toString()
            var email = view.findViewById<EditText>(R.id.etRegisterEmail).text.toString()
            var phone = view.findViewById<EditText>(R.id.etRegisterPhone).text.toString()
            var birthday = view.findViewById<EditText>(R.id.etRegisterDate).text.toString()
            var password = view.findViewById<EditText>(R.id.etRegisterPassword).text.toString()
            val txtError = view?.findViewById<TextView>(R.id.tvRegisterError)!!

            if (name == "" || username == "" || email == "" || phone == "" || birthday == "" || password == "") {
                txtError.text = "Rellene todos los campos"
            } else if (!validarNumeroTelefono(phone)) {
                txtError.text = "Número de telefono no valido"
            } else {
                register(name, username, email, phone, birthday, password)
            }
        }

        view.findViewById<Button>(R.id.btnLoginirRegistro).setOnClickListener() {
            mainActivity.goToFragment(LoginFragment(), true)
        }

    }

    private fun register(
        name: String,
        username: String,
        email: String,
        phone: String,
        birthday: String,
        password: String
    ) {
        val crearUser = RegisterData(name, username, email, email, phone, birthday, password)
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
                    mainActivity.goToFragment(LoginFragment())
                } else {
                    val etRegisterName = view?.findViewById<TextView>(R.id.etRegisterName)!!
                    val etRegisterUsername = view?.findViewById<TextView>(R.id.etRegisterUsername)!!
                    val etRegisterEmail = view?.findViewById<TextView>(R.id.etRegisterEmail)!!
                    val etRegisterPhone = view?.findViewById<TextView>(R.id.etRegisterPhone)!!
                    val etRegisterDate = view?.findViewById<TextView>(R.id.etRegisterDate)!!
                    val etRegisterPassword = view?.findViewById<TextView>(R.id.etRegisterPassword)!!
                    val txtError = view?.findViewById<TextView>(R.id.tvRegisterError)!!
                    if (!etRegisterName.text.isEmpty() || !etRegisterUsername.text.isEmpty() || !etRegisterEmail.text.isEmpty() || !etRegisterPhone.text.isEmpty() || !etRegisterDate.text.isEmpty() || !etRegisterPassword.text.isEmpty()) {
                        val errorBody = response.errorBody()?.string()
                        val errorJson = JSONObject(errorBody)
                        val errorObject = errorJson.getJSONObject("error")
                        val errorMessage = errorObject.getString("message")
                        Log.e("hola", errorMessage)
                        if (errorMessage == "email must be a valid email") {
                            txtError.text = "Email no válido"
                        } else if (errorMessage == "Email or Username are already taken") {
                            txtError.text = "Email/Nombre de usuario ya está en uso"
                        } else if (errorMessage == "password must be at least 6 characters") {
                            txtError.text = "Contraseña no válida. Debe poseer 6 carácteres mínimo"

                        }
                    }


                }


            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun showDatePickerDialog() {
        val datePicker =
            DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        getActivity()?.let { datePicker.show(it.getSupportFragmentManager(), "datePicker") }
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        var numberMonth = month + 1;
        view?.findViewById<EditText>(R.id.etRegisterDate)
            ?.setText("$day/$numberMonth/$year")
    }

    fun validarNumeroTelefono(numero: String): Boolean {
        val patron = Regex("^[0-9]{9}\$")
        return patron.matches(numero)
    }


}