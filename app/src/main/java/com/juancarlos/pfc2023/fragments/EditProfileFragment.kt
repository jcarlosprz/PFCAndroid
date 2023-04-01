package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R


class EditProfileFragment() : Fragment(R.layout.fragment_profile_edit) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Mostrar el bottomNavigation
        val mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation()



    }

}