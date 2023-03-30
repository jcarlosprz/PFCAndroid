package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.juancarlos.pfc2023.R

import com.juancarlos.pfc2023.adapters.SavedAdapter


class ProfileFragment() : Fragment(R.layout.fragment_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Mostrar el bottomNavigation
        val mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation()



    }

}