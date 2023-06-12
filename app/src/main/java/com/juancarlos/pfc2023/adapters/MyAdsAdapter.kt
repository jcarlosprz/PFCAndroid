package com.juancarlos.pfc2023.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.api.ApiRest
import com.juancarlos.pfc2023.api.data.UserAdsListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.floor

class MyAdsAdapter(private val myads: MutableList<UserAdsListResponse.Ad>) :
    RecyclerView.Adapter<MyAdsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_myads, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(myads[position])
    }

    override fun getItemCount(): Int {
        return myads.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(ad: UserAdsListResponse.Ad) {
            val subject = itemView.findViewById<TextView>(R.id.tvMyAdSubject)
            val modality = itemView.findViewById<TextView>(R.id.tvMyAdModality)
            val description = itemView.findViewById<TextView>(R.id.tvMyAdDescription)
            val ubication = itemView.findViewById<TextView>(R.id.tvMyAdUbication)
            val price = itemView.findViewById<TextView>(R.id.tvMyAdPrice)
            subject.text = ad.subject
            modality.text = ad.modality
            description.text = ad.description
            ubication.text = ad.ubication
            if (ad.price == floor(ad.price)) {
                price.text = ad.price.toInt().toString() + "€"
            } else {
                price.text = ad.price.toString() + "€"
            }
            val btnEliminar = itemView.findViewById<CardView>(R.id.btnEliminarItem)
            btnEliminar.setOnClickListener {
                Log.i("ELIMINAR", ad.id.toString())
                deleteAd(ad)
            }
            //  itemView.setOnClickListener {
            //     Log.v("Pulso sobre", item.displayName.toString())

            //  }
        }


    }

    private fun deleteAd(ad: UserAdsListResponse.Ad) {
        val call = ApiRest.service.deleteAd(ad.id.toString())
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    myads.remove(ad)
                    notifyDataSetChanged()
                } else {
                    Log.e("deleteUser", response.errorBody()?.string() ?: "Error deleting user")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.e("deleteUser", "Error: ${t.message}")
            }
        })
    }
}
