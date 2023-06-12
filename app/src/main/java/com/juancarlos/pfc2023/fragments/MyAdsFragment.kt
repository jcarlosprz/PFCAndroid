package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.adapters.MyAdsAdapter
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.data.UserAdsListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates


class MyAdsFragment() : Fragment(R.layout.fragment_myads) {
    lateinit var mainActivity: MainActivity
    var currentUser by Delegates.notNull<Int>()
    var myads: MutableList<UserAdsListResponse.Ad> = mutableListOf()
    private var currentOption: TextView? = null
    var isProfesor = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Mostrar el bottomNavigation
        mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation()
        currentUser = mainActivity.getCurrentUser()

        //Crear Adapter
        var rvSaved = view.findViewById<RecyclerView>(R.id.rvSaved)
        rvSaved.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvSaved.adapter = MyAdsAdapter(myads)

        view.findViewById<CardView>(R.id.addPost).setOnClickListener {
            mainActivity.goToFragment(SelectRole(), true)
        }
        var OptionProfesores = view.findViewById<TextView>(R.id.OptionProfesoresMyAds)
        var OptionAlumnos = view.findViewById<TextView>(R.id.OptionAlumnosMyAds)
        currentOption = if (isProfesor) {
            OptionProfesores
        } else {
            OptionAlumnos
        }
        getUserAds(currentUser.toString(), isProfesor)
        selectOption(currentOption!!) // Inicializar con la primera opción
        OptionProfesores.setOnClickListener {
            onOptionClicked(it)
            isProfesor = true
            getUserAds(currentUser.toString(), isProfesor)

        }

        OptionAlumnos.setOnClickListener {
            onOptionClicked(it)
            isProfesor = false
            getUserAds(currentUser.toString(), isProfesor)
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity.showBottomNavigation()
        mainActivity.setupKeyboardVisibilityListener(true)
    }

    fun onOptionClicked(view: View) {
        val option = view as TextView
        if (option != currentOption) {
            deselectOption(currentOption!!)
            selectOption(option)
            currentOption = option
        }
    }

    private fun selectOption(textView: TextView) {
        textView.setTextColor(resources.getColor(R.color.green))
        textView.setBackgroundColor(resources.getColor(R.color.background))
        val fadeInAnimation = AlphaAnimation(0f, 1f)
        fadeInAnimation.duration = 500
        textView.startAnimation(fadeInAnimation)
    }

    private fun deselectOption(textView: TextView) {
        textView.setTextColor(resources.getColor(R.color.white))
        textView.setBackgroundColor(resources.getColor(R.color.blue))

    }

    private fun getUserAds(id: String, adProfesor: Boolean) {
        val call = ApiRest.service.getUserAds(id)
        call.enqueue(object : Callback<UserAdsListResponse> {
            override fun onResponse(
                call: Call<UserAdsListResponse>,
                response: Response<UserAdsListResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    myads = body.ads
                    val filteredAdsList: MutableList<UserAdsListResponse.Ad> = myads.filter { ad -> ad.adProfesor == adProfesor }.toMutableList()
                    var rvSaved = view?.findViewById<RecyclerView>(R.id.rvSaved)
                    rvSaved?.adapter =
                        MyAdsAdapter(filteredAdsList)

                    Log.i("getUserAds", response.body()!!.ads.toString())


                } else {
                    Log.e("getUserAds", response.errorBody()?.string() ?: "Error getting user ads:")
                }
            }

            override fun onFailure(call: Call<UserAdsListResponse>, t: Throwable) {
                Log.e("getUser", "Error: ${t.message}")
            }
        })
    }

}