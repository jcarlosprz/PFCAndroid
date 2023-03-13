package com.juancarlos.pfc2023.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.juancarlos.pfc2023.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.container, WelcomeFragment())
            .commit()
    }

    //Cambiar color del status bar
    fun setStatusBarColor(color: String) {
        val colorInt: Int = Color.parseColor(color)
        window.statusBarColor = colorInt
    }

    //Ocultar el bottom navigation
    fun hideBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.visibility = View.INVISIBLE
    }

    //Mostrar el bottom navigation
    fun showBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.visibility = View.VISIBLE
    }
}