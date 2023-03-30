package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.adapters.SavedAdapter


class SavedFragment() : Fragment(R.layout.fragment_saved) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Mostrar el bottomNavigation
        val mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation()


        var rvSaved = view.findViewById<RecyclerView>(R.id.rvSaved)
        rvSaved.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvSaved.adapter = SavedAdapter()
    }

}