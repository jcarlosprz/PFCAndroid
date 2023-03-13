package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.juancarlos.pfc2023.R


class LoginFragment() : Fragment(R.layout.fragment_login) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Ocultar el bottomnavigation
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation()
        mainActivity.setStatusBarColor("#214F95")


        view.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, SearchFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
    }

}