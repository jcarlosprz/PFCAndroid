package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R


class SelectRole() : Fragment(R.layout.fragment_select_rol) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Ocultar el bottomNavigation
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation()

        val rolProfesor = view.findViewById<CardView>(R.id.cvRolProfesor)
        val rolAlumno = view.findViewById<CardView>(R.id.cvRolAlumno)
        rolProfesor.setOnClickListener {
            activity?.let {
                val fragment = CreateAdFragment()
                fragment.arguments = Bundle()
                fragment.arguments?.putBoolean("isProfesor", true)
                mainActivity.goToFragment(fragment)
            }
        }
        rolAlumno.setOnClickListener {
            activity?.let {
                val fragment = CreateAdFragment()
                fragment.arguments = Bundle()
                fragment.arguments?.putBoolean("isProfesor", false)
                mainActivity.goToFragment(fragment)
            }
        }
    }

}