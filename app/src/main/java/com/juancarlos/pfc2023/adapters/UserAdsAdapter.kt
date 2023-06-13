package com.juancarlos.pfc2023.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.api.data.UserAdsListResponse
import kotlin.math.floor

class UserAdsAdapter(private val myads: MutableList<UserAdsListResponse.Ad>) :
    RecyclerView.Adapter<UserAdsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_userads, parent, false)
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
            val subject = itemView.findViewById<TextView>(R.id.tvUserAdSubject)
            val modality = itemView.findViewById<TextView>(R.id.tvUserAdModality)
            val description = itemView.findViewById<TextView>(R.id.tvUserAdDescription)
            val ubication = itemView.findViewById<TextView>(R.id.tvUserAdUbication)
            val price = itemView.findViewById<TextView>(R.id.tvUserAdPrice)
            subject.text = ad.subject
            modality.text = ad.modality
            description.text = ad.description
            ubication.text = ad.ubication
            if (ad.price == floor(ad.price)) {
                price.text = ad.price.toInt().toString() + "€"
            } else {
                price.text = ad.price.toString() + "€"
            }
        }


    }

}
