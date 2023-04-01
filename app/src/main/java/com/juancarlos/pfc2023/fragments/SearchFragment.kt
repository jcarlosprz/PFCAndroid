package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.adapters.AnunciosAdapter


class SearchFragment() : Fragment(R.layout.fragment_search) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Mostrar el bottoNavigation
        val mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation()


        var rvUserInfo = view.findViewById<RecyclerView>(R.id.rvAnuncios)
        rvUserInfo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvUserInfo.adapter = AnunciosAdapter()
    }

}