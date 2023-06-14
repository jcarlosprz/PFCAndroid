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
import com.juancarlos.pfc2023.api.data.AdData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates


class CreateAdFragment() : Fragment(R.layout.fragment_createad) {
    lateinit var mainActivity: MainActivity
    var currentUser by Delegates.notNull<Int>()
    var isProfesor by Delegates.notNull<Boolean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity

        //Ocultar el bottomnavigation
        mainActivity.hideBottomNavigation()
        mainActivity.setupKeyboardVisibilityListener(false)

        currentUser = mainActivity.getCurrentUser()
        isProfesor =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                arguments?.getBoolean("isProfesor")!!
            } else {
                arguments?.getBoolean("isProfesor")!!
            }

        val btnCreateAd = view.findViewById<Button>(R.id.btnCreateAd)
        btnCreateAd.setOnClickListener {
            var description =
                view.findViewById<EditText>(R.id.etCreateAdDescription).text.toString()
            var modality = view.findViewById<EditText>(R.id.etCreateAdModality).text.toString()
            var price =
                view.findViewById<EditText>(R.id.etCreateAdPrice).text.toString().toDoubleOrNull()
            var subject = view.findViewById<EditText>(R.id.etCreateAdSubject).text.toString()
            var ubication = view.findViewById<EditText>(R.id.etCreateAdUbication).text.toString()
            if (currentUser != null && description != null && modality != null && price != null && subject != null && ubication != null && isProfesor != null) {
                var adData = AdData(
                    data = AdData.Data(
                        creator = currentUser,
                        description = description,
                        modality = modality,
                        price = price,
                        subject = subject,
                        ubication = ubication,
                        adProfesor = isProfesor
                    )
                )
                createAd(adData)
            }


        }
    }

    private fun createAd(ad: AdData) {
        val call = ApiRest.service.createAd(ad)
        call.enqueue(object : Callback<AdData> {
            override fun onResponse(call: Call<AdData>, response: Response<AdData>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.i("CREATEAD", body.toString())
                    mainActivity.goToFragment(MyAdsFragment(), false)
                } else {
                    // Hubo un error en la solicitud, puedes manejarlo aquí

                    val errorBody = response.errorBody()?.string()
                    Log.e(
                        "CreateAdFragment",
                        "$errorBody"
                    )
                }
            }

            override fun onFailure(call: Call<AdData>, t: Throwable) {
                // Error en la solicitud, puedes manejarlo aquí
                Log.e(
                    "CreateAdFragment",
                    "Error en la solicitud de creación de anuncio: ${t.message}"
                )
            }
        })
    }

}