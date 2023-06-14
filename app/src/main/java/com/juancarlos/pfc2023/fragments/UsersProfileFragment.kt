package com.juancarlos.pfc2023.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.adapters.UserAdsAdapter
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.data.UserAdsListResponse
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UsersProfileFragment() : Fragment(R.layout.fragment_profile_user) {
    var adProfesor = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Mostrar el bottomNavigation
        val mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation()

        val llDescription: LinearLayout = view.findViewById(R.id.llProfileDescription)
        val scrollView: ScrollView = view.findViewById(R.id.svProfile)
        setupSwipeAnimation(llDescription, scrollView)

        val userId =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                arguments?.getInt("userId")
            } else {
                arguments?.getInt("userId")
            }
        adProfesor =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                arguments?.getBoolean("adProfesor")!!
            } else {
                arguments?.getBoolean("adProfesor")!!
            }
        ApiRest.initService()
        getUserById(userId.toString())


    }

    fun setupSwipeAnimation(llDescription: LinearLayout, scrollView: ScrollView) {
        var initialY = 0f
        val icon = view?.findViewById<ImageView>(R.id.icSwipe)
        scrollView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialY = event.y
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val deltaY = event.y - initialY
                    if (deltaY > 0) {
                        llDescription.animate()
                            .alpha(1f)
                            .setDuration(200)
                            .withStartAction {
                                llDescription.visibility = View.VISIBLE
                            }
                            .start()
                        icon?.setBackgroundResource(R.drawable.ic_swipe_up)
                    } else if (deltaY < 0) {
                        llDescription.animate()
                            .alpha(0f)
                            .setDuration(200)
                            .withEndAction {
                                llDescription.visibility = View.GONE
                            }
                            .start()
                        icon?.setBackgroundResource(R.drawable.ic_swipe_down)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun getUserById(id: String) {
        val call = ApiRest.service.getUserAds(id)
        call.enqueue(object : Callback<UserAdsListResponse> {
            override fun onResponse(
                call: Call<UserAdsListResponse>,
                response: Response<UserAdsListResponse>
            ) {
                // manejar la respuesta exitosa aquí
                val user = response.body()
                if (response.isSuccessful && user != null) {
                    val name = view?.findViewById<TextView>(R.id.tvProfileName)
                    val username = view?.findViewById<TextView>(R.id.tvProfileUsername)
                    val phone = view?.findViewById<TextView>(R.id.tvProfilePhone)
                    val mail = view?.findViewById<TextView>(R.id.tvProfileEmail)
                    val description = view?.findViewById<TextView>(R.id.tvProfileDescription)
                    val imgProfile = view?.findViewById<CircleImageView>(R.id.imgProfilePicture)
                    name?.text = user.name
                    username?.text = "@" + user.username
                    phone?.text = user.contactPhone
                    mail?.text = user.contactEmail
                    description?.text = user.description
                    Glide.with(view!!)
                        .load(user.imgURL)
                        .into(imgProfile!!)
                    val btnPhone = view!!.findViewById<CardView>(R.id.btnProfilePhone)
                    btnPhone.setOnClickListener {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${user.contactPhone}")
                        }
                        view!!.context.startActivity(intent)
                    }
                    val btnEmail = view!!.findViewById<CardView>(R.id.btnProfileEmail)
                    btnEmail.setOnClickListener {
                        val subject =
                            "Clases" // Reemplaza esto con el asunto deseado
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "message/rfc822"
                            putExtra(Intent.EXTRA_EMAIL, arrayOf(user.contactEmail))
                            putExtra(Intent.EXTRA_SUBJECT, subject)
                        }
                        val chooser =
                            Intent.createChooser(intent, "Seleccionar aplicación de correo")
                        if (intent.resolveActivity(view!!.context.packageManager) != null) {
                            view!!.context.startActivity(chooser)
                        } else {
                            Toast.makeText(
                                view!!.context,
                                "No se encontró una aplicación de correo.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    if (user.ads != null) {
                        val myads: MutableList<UserAdsListResponse.Ad> = user.ads.toMutableList()
                        val filteredAdsList: MutableList<UserAdsListResponse.Ad> =
                            myads.filter { ad -> ad.adProfesor == adProfesor }.toMutableList()

                        val rvUserInfo = view?.findViewById<RecyclerView>(R.id.rvUserAds)
                        rvUserInfo?.layoutManager = LinearLayoutManager(context)
                        rvUserInfo?.adapter = UserAdsAdapter(filteredAdsList)
                    } else {
                        Log.e("TAG", "User ads is null")
                    }
                } else {
                    Log.e("TAG", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<UserAdsListResponse>, t: Throwable) {
                Log.e("TAG", "Error: ${t.message}")
            }
        })
    }

}