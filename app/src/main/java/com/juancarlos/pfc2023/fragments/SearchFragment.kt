package com.juancarlos.pfc2023.fragments
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.juancarlos.pfc2023.MainActivity
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.adapters.AdsAdapter
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.data.AdsListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment() : Fragment(R.layout.fragment_search) {
    lateinit var mainActivity: MainActivity
    lateinit var adsList: List<AdsListResponse.Data>
    private var currentOption: TextView? = null
    var isProfesor = true
    var filtro = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity

        //Establecer colores del SearchView
        var searchView = view.findViewById<SearchView>(R.id.svSearch)
        changeSearchViewColors(searchView, android.R.color.white)

        //Mostrar el bottoNavigation
        mainActivity.showBottomNavigation()
        ApiRest.initService()
        getAds(filtro)

        var rvUserInfo = view?.findViewById<RecyclerView>(R.id.rvAnuncios)
        rvUserInfo?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvUserInfo?.adapter = AdsAdapter(adsList = emptyList())


        var svSearchView = view.findViewById<SearchView>(R.id.svSearch)
        // Configurar el listener de búsqueda
        svSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Realizar la búsqueda aquí
                if (!query.isNullOrEmpty()) {
                    filtro = query
                    getAds(filtro)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Actualizar la búsqueda mientras se escribe
                filtro = newText!!
                getAds(filtro)
                return false
            }
        })
        var textOption1 = view.findViewById<TextView>(R.id.textOption1)
        var textOption2 = view.findViewById<TextView>(R.id.textOption2)
        currentOption = if (isProfesor) {
            textOption1

        } else {
            textOption2
        }
        selectOption(currentOption!!) // Inicializar con la primera opción
        textOption1.setOnClickListener {
            onOptionClicked(it)
            isProfesor = true
            getAds(filtro)
            // Lógica para el caso del profesor
            println("Eres un profesor.")
        }

        textOption2.setOnClickListener {
            onOptionClicked(it)
            isProfesor = false
            getAds(filtro)
            // Lógica para el caso del alumno
            println("Eres un alumno.")
        }
    }

    fun onOptionClicked(view: View) {
        val option = view as TextView
        if (option != currentOption) {
            deselectOption(currentOption!!)
            selectOption(option)
            currentOption = option


        }
    }

    private fun selectOption(textView: TextView) {
        textView.setTextColor(resources.getColor(R.color.green))
        textView.setBackgroundColor(resources.getColor(R.color.blue_white))
        val fadeInAnimation = AlphaAnimation(0f, 1f)
        fadeInAnimation.duration = 500
        textView.startAnimation(fadeInAnimation)
    }

    private fun deselectOption(textView: TextView) {
        textView.setTextColor(resources.getColor(R.color.white))
        textView.setBackgroundColor(resources.getColor(R.color.blue))

    }

    private fun getAds(filtro: String) {
        val call = ApiRest.service.getAdsFiltered(
            filtro,
            filtro,
            filtro,
            filtro,
            filtro,
            isProfesor,
            "*"
        )
        call.enqueue(object : Callback<AdsListResponse> {
            override fun onResponse(
                call: Call<AdsListResponse>,
                response: Response<AdsListResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    adsList = body.data
                    var rvUserInfo = view?.findViewById<RecyclerView>(R.id.rvAnuncios)
                    rvUserInfo?.adapter = AdsAdapter(adsList)
                    Log.i("getAds", adsList.toString())
                } else {
                    Log.e("getAds", response.errorBody()?.string() ?: "Error getting user:")
                }
            }

            override fun onFailure(call: Call<AdsListResponse>, t: Throwable) {
                Log.e("getAdsOnFailure", "Error: ${t.message}")
            }
        })
    }

    private fun changeSearchViewColors(searchView: SearchView, ColorResId: Int) {
        val searchEditText =
            searchView.findViewById<SearchView.SearchAutoComplete>(androidx.appcompat.R.id.search_src_text)
        val searchIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        val closeIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)

        searchEditText.setTextColor(resources.getColor(ColorResId))
        searchIcon.setColorFilter(resources.getColor(ColorResId), PorterDuff.Mode.SRC_IN)
        closeIcon.setColorFilter(resources.getColor(ColorResId), PorterDuff.Mode.SRC_IN)
    }

}