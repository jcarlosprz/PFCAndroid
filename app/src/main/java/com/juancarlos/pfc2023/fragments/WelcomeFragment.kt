package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import com.juancarlos.pfc2023.R


class WelcomeFragment() : Fragment(R.layout.fragment_welcome) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Ocultar el bottomNavigation
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation()
        mainActivity.setStatusBarColor("#214F95")


        var botonRegistro = view.findViewById<Button>(R.id.irRegistro)
        var botonLogin = view.findViewById<Button>(R.id.irLogin)

        botonRegistro.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, RegisterFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
        botonLogin.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, LoginFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

    }

}