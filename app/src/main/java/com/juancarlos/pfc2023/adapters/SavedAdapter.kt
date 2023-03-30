package com.juancarlos.pfc2023.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.juancarlos.pfc2023.R

class SavedAdapter : RecyclerView.Adapter<SavedAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_guardado, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //  holder.bind(data[position])
    }

    override fun getItemCount(): Int = 7

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //  fun bind(item:AgentsResponse.Agent) {

        //  itemView.setOnClickListener {
        //     Log.v("Pulso sobre", item.displayName.toString())

        //  }
    }


}

