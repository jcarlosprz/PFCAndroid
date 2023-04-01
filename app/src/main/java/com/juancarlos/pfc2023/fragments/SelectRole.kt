package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R


class SelectRole() : Fragment(R.layout.fragment_select_rol) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Ocultar el bottomNavigation
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation()

    }

}