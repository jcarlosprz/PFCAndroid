package com.juancarlos.pfc2023.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.juancarlos.pfc2023.R
import com.juancarlos.pfc2023.api.data.AdsListResponse
import de.hdodenhof.circleimageview.CircleImageView

class AnunciosAdapter(private val adsList: List<AdsListResponse.Data>) :
    RecyclerView.Adapter<AnunciosAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_teacher_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(adsList[position])

    }

    override fun getItemCount(): Int {
        // Devuelve la cantidad de elementos en la lista itemList
        return adsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(addata: AdsListResponse.Data) {
            var imgProfile = itemView.findViewById<CircleImageView>(R.id.imgProfileItemPost)
            var name = itemView.findViewById<TextView>(R.id.tvItemName)
            var username = itemView.findViewById<TextView>(R.id.tvItemUsername)

            Glide.with(itemView)
                .load(addata.attributes.creator.data.attributes.imgURL)
                .into(imgProfile)
            name.text = addata.attributes.creator.data.attributes.name
            username.text = addata.attributes.creator.data.attributes.username
        }


    }
}

