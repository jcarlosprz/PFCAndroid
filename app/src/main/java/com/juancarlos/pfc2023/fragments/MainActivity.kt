package com.juancarlos.pfc2023.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.juancarlos.pfc2023.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.container, WelcomeFragment())
            .commit()
        //Navegación BottonNavigationView
        findViewById<BottomNavigationView>(R.id.bottom_navigation_view).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_search -> goToFragment(SearchFragment())
                R.id.action_saved -> goToFragment(SavedFragment())
                R.id.action_profile -> goToFragment(ProfileFragment())
            }
            true
        }
    }
    //Navegacion BottonNavigationView entre fragments
    fun goToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
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