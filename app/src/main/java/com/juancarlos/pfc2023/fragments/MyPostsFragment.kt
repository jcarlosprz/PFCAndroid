package com.juancarlos.pfc2023.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.adapters.MyPostsAdapter


class MyPostsFragment() : Fragment(R.layout.fragment_myposts) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Mostrar el bottomNavigation
        val mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation()

        //Crear Adapter
        var rvSaved = view.findViewById<RecyclerView>(R.id.rvSaved)
        rvSaved.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvSaved.adapter = MyPostsAdapter()

        view.findViewById<Button>(R.id.addPost).setOnClickListener {

        }
    }

}