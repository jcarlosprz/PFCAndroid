package com.juancarlos.pfc2023.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.data.AdByIdResponse
import com.juancarlos.pfc2023.api.data.AdData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditAdFragment() : Fragment(R.layout.fragment_editad) {
    lateinit var mainActivity: MainActivity
    lateinit var currentAd: AdByIdResponse
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity

        //Ocultar el bottomnavigation
        mainActivity.hideBottomNavigation()
        mainActivity.setupKeyboardVisibilityListener(false)

        val adId =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                arguments?.getInt("adId")
            } else {
                arguments?.getInt("adId")
            }
        Log.i("getAD", adId.toString())
        var currentUser = mainActivity.getCurrentUser()
        getAd(adId.toString())

        val btnActualizar = view.findViewById<Button>(R.id.btnEditAd)
        btnActualizar.setOnClickListener {
            var etSubject = view.findViewById<TextView>(R.id.etEditAdSubject)!!
            var etUbication = view.findViewById<TextView>(R.id.etEditAdUbication)!!
            var etModality = view.findViewById<TextView>(R.id.etEditAdModality)!!
            var etDescription = view.findViewById<TextView>(R.id.etEditAdDescription)!!
            var etPrice = view.findViewById<TextView>(R.id.etEditAdPrice)!!

            val ad = AdData(
                data = AdData.Data(
                    creator = currentUser,
                    description = etDescription.text.toString(),
                    modality = etModality.text.toString(),
                    price = etPrice.text.toString().toDouble(),
                    subject = etSubject.text.toString(),
                    ubication = etUbication.text.toString(),
                    adProfesor = currentAd.data.attributes.adProfesor
                )
            )
            updateAd(adId.toString(), ad)

        }
        //Alert Eliminar
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Eliminar Anuncio")
            .setMessage("¿Estás seguro/a?\nEsta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { dialog, which ->
                deleteAd(adId.toString())
            }
            .setNegativeButton("Cancelar") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "No"
            }
        val alerta = builder.create()
        //Acción botón
        val btnEliminar = view.findViewById<Button>(R.id.btnDeleteAd)
        btnEliminar.setOnClickListener {
            alerta.show()
        }

    }

    private fun getAd(id: String) {
        val call = ApiRest.service.getAd(id)
        call.enqueue(object : Callback<AdByIdResponse> {
            override fun onResponse(
                call: Call<AdByIdResponse>,
                response: Response<AdByIdResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    currentAd = body
                    val etSubject = view?.findViewById<TextView>(R.id.etEditAdSubject)!!
                    val etUbication = view?.findViewById<TextView>(R.id.etEditAdUbication)!!
                    val etModality = view?.findViewById<TextView>(R.id.etEditAdModality)!!
                    val etDescription = view?.findViewById<TextView>(R.id.etEditAdDescription)!!
                    val etPrice = view?.findViewById<TextView>(R.id.etEditAdPrice)!!

                    etSubject.text = body.data.attributes.subject
                    etUbication.text = body.data.attributes.ubication
                    etModality.text = body.data.attributes.modality
                    etDescription.text = body.data.attributes.description
                    etPrice.text = body.data.attributes.price.toString()
                    Log.i("getAD", body.toString())
                } else {
                }
            }

            override fun onFailure(call: Call<AdByIdResponse>, t: Throwable) {
                Log.e("getUser", "Error: ${t.message}")
            }
        })
    }

    fun updateAd(id: String, updatedAd: AdData) {
        val call = ApiRest.service.updateAd(updatedAd, id)
        call.enqueue(object : Callback<AdData> {
            override fun onResponse(
                call: Call<AdData>,
                response: Response<AdData>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Toast.makeText(
                        mainActivity.applicationContext,
                        "Usuario actualizado",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    mainActivity.goToFragment(MyAdsFragment())
                } else {
                    Log.e("updateUser", response.errorBody()?.string() ?: "Error updating user")
                }
            }

            override fun onFailure(call: Call<AdData>, t: Throwable) {
                Log.e("updateUser", "Error: ${t.message}")
            }
        })
    }

    private fun deleteAd(adId: String) {
        val call = ApiRest.service.deleteAd(adId)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    mainActivity.goToFragment(MyAdsFragment(), false)
                } else {
                    Log.e("deleteUser", response.errorBody()?.string() ?: "Error deleting user")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.e("deleteUser", "Error: ${t.message}")
            }
        })
    }
}


