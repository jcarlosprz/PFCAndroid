package com.juancarlos.pfc2023

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.juancarlos.pfc2023.fragments.MyPostsFragment
import com.juancarlos.pfc2023.fragments.ProfileFragment
import com.juancarlos.pfc2023.fragments.SearchFragment
import com.juancarlos.pfc2023.fragments.WelcomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.container, WelcomeFragment())
            .commit()
        //Navegación BottonNavigationView
        setupKeyboardVisibilityListener()
        findViewById<BottomNavigationView>(R.id.bottom_navigation_view).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_search -> goToFragment(SearchFragment())
                R.id.action_saved -> goToFragment(MyPostsFragment())
                R.id.action_profile -> goToFragment(ProfileFragment())
            }
            true
        }

    }

    //Navegacion BottonNavigationView entre fragments
    fun goToFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    //Obtener el id del usuario loggeado (del local)
    fun getCurrentUser(): Int {
        val sharedPreferencesGet = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        //val getToken = sharedPreferencesGet.getString("token", "")
        val getID = sharedPreferencesGet.getInt("userID", 0)
        return getID
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
//Ocultar bottomnavigation al abrir el teclado
    private fun setupKeyboardVisibilityListener() {
        val rootView = findViewById<View>(android.R.id.content)
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            private val windowVisibleDisplayFrame = Rect()
            private var isKeyboardOpen = false

            override fun onGlobalLayout() {
                rootView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame)
                val screenHeight = rootView.rootView.height

                val heightDiff = screenHeight - windowVisibleDisplayFrame.bottom
                val isOpen =
                    heightDiff > screenHeight * 0.15 // Se considera que el teclado está abierto si la diferencia de altura es superior al 15% de la altura de la pantalla

                if (isOpen != isKeyboardOpen) {
                    isKeyboardOpen = isOpen
                    if (isKeyboardOpen) {
                        // El teclado está abierto, ocultar el BottomNavigationView
                        bottomNavigationView.visibility = View.GONE
                    } else {
                        // El teclado está cerrado, mostrar el BottomNavigationView
                        bottomNavigationView.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}